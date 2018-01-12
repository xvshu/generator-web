package com.eloancn.framework.ump.annotation;

import com.baidu.disconf.client.common.annotations.DisconfItem;
import com.dianping.cat.message.Transaction;
import com.eloancn.framework.ump.Profiler;
import com.site.lookup.util.StringUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/4/7.
 */
public class PackageProxyFactoryBean extends AbstractAutoProxyCreator implements MethodInterceptor {

    private List<String> packages;
    //    private Set<Class> proxySet = new HashSet<Class>();
    private Map<Class, String> JDKclassMap = new HashMap<Class, String>();

    private Pattern includePackageRegex;//包含的正则表达式
    private Pattern excludePackageRegex;//不包含的正则表达式


    /**
     * split by domain name .
     */
    private String umpOpenFlag = "true";
    /**
     * Match a String against the given pattern, supporting the following simple
     * pattern styles: "xxx*", "*xxx", "*xxx*" and "xxx*yyy" matches (with an
     * arbitrary number of pattern parts), as well as direct equality.
     *
     * @return whether the String matches the given pattern
     */
    private List<String> excludePackages;


    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        if (!CollectionUtils.isEmpty(packages)) {
            if (Proxy.isProxyClass(beanClass)) {
                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class interClzz : interfaces) {
                    if(interClzz.getPackage()==null){
                        continue;
                    }
                    String className = interClzz.getName();

                    if(regexMatch(className,includePackageRegex)
                            && !regexMatch(className,excludePackageRegex)){
                        JDKclassMap.put(beanClass, className);
//                        proxySet.add(beanClass);
                        return new Object[]{this};
                    }
                }
            } else {
                if (null != beanClass.getPackage()
                        && regexMatch(beanClass.getPackage().getName(),includePackageRegex)
                        && !regexMatch(beanClass.getPackage().getName(),excludePackageRegex)) {
                    /*Class<?> clazz=beanClass;
                    if(ClassUtils.isCglibProxyClass(beanClass)){
                        clazz = beanClass.getSuperclass();
                    }*/
//                    proxySet.add(clazz);

                    return new Object[]{this};
                }
            }
        }
        return DO_NOT_PROXY;
    }

    public List<String> getPackages() {
        return packages;
    }

    public void setPackages(List<String> packages) {
        this.packages = packages;
        includePackageRegex = createRegex(packages);
    }

    private Pattern createRegex(List<String> packages) {
        if (CollectionUtils.isEmpty(packages)) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (String str : packages) {
            sb.append("(^").append(str).append(")*");
        }
        sb.append("([.][a-zA-Z]+)*");
        return Pattern.compile(sb.toString());
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(invocation.getThis());
        if ("true".equals(umpOpenFlag.trim()) //&& proxySet.contains(targetClass)
                ) {
            //方法注解
            UmpLog log = targetClass.getMethod(invocation.getMethod().getName(), invocation.getMethod().getParameterTypes()).getAnnotation(UmpLog.class);
            if (null == log) {
                //类注解
                log = targetClass.getAnnotation(UmpLog.class);
            }
            if (log == null) {
                return monitorLog(null, invocation);
            } else if (log.log()) {
                return monitorLog(log, invocation);
            }
        }
        return invocation.proceed();
    }

    private Object monitorLog(UmpLog umpLog, MethodInvocation invocation) throws Throwable {
        Transaction tt;
        Object result;


        if (null != umpLog && StringUtils.isNotEmpty(umpLog.type()) && StringUtils.isNotEmpty(umpLog.name())) {
            tt = Profiler.registerInfo(umpLog.type(), umpLog.name());
        } else {
            Class<?> targetClass = AopProxyUtils.ultimateTargetClass(invocation.getThis());

            String ttName ="";
            if(isJdkDynamicProxy(targetClass)){
                ttName=JDKclassMap.get(targetClass);
                if(ttName==null){//
                    Class<?>[] interfaces = targetClass.getInterfaces();
                    for (Class interClzz : interfaces) {
                        if(interClzz.getPackage()==null){
                            continue;
                        }
                        String className = interClzz.getName();

                        if(regexMatch(className,includePackageRegex)
                                && !regexMatch(className,excludePackageRegex)){
                            JDKclassMap.put(targetClass, className);
                        }
                    }
                    ttName=JDKclassMap.get(targetClass);
                    if(ttName==null){
                        return invocation.proceed();
                    }
                }
            }else{
                ttName = targetClass.getName();
            }


            String[] type = getTrancationTypeAndName(ttName, invocation.getMethod().getName());

            tt = Profiler.registerInfo(type[0], type[1]);
        }

        try {
            result = invocation.proceed();
        } catch (Throwable e) {
            result = null;
            Profiler.functionError(tt, e);
            throw e;
        } finally {
            Profiler.registerEnd(tt);
        }
        return result;
    }

    private String[] getTrancationTypeAndName(String ttName, String methodName) {
        String[] type = new String[2];
        int i = ttName.lastIndexOf(".");
        String className=ttName;
        if(i>0){
            className= ttName.substring(i + 1);
        }
        if(className.length()>2){
            className=className.substring(0,1).toLowerCase()+className.substring(1);
        }
        if ( ttName.contains(".logic")) {
            type[0] = "app.logic";
        } else if ( ttName.contains(".dao") || ttName.contains(".mapper")) {
            type[0] = "app.dao";
        } else {
            type[0] = "app.service";
        }
        type[1] = className + "." + methodName;

        return type;
    }


    @DisconfItem(key = "umpOpenFlag")
    public String getUmpOpenFlag() {
        return umpOpenFlag;
    }

    public void setUmpOpenFlag(String umpOpenFlag) {
        this.umpOpenFlag = umpOpenFlag;
    }


    public List<String> getExcludePackages() {
        return excludePackages;
    }

    public void setExcludePackages(List<String> excludePackages) {
        this.excludePackages = excludePackages;
        excludePackageRegex = createRegex(excludePackages);
    }

    public boolean isJdkDynamicProxy(Class clazz) {
        return Proxy.isProxyClass(clazz);
    }

    private boolean isAopProxy(Class proxy) {
        return Proxy.isProxyClass(proxy) || ClassUtils.isCglibProxyClass(proxy);
    }

    private boolean regexMatch(String pkName, Pattern checkRegex) {
        if (checkRegex == null) {//如果没有设置包含的话，则默认为所有的
            return false;
        }
        return checkRegex.matcher(pkName).matches();
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException{
        PackageProxyFactoryBean bean = new PackageProxyFactoryBean();
        System.out.println(bean.getPath());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("classpath:spring/applicationContext.xml"));
        Element root = document.getDocumentElement();

        System.out.println(System.getProperty("user.dir"));
        System.out.println(root);
    }

    public String getPath(){
        return this.getClass().getClassLoader().getResource("").getHost();
    }
}
