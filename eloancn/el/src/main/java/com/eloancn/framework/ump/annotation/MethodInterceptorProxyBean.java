package com.eloancn.framework.ump.annotation;

import com.baidu.disconf.client.common.annotations.DisconfItem;
import com.dianping.cat.message.Transaction;
import com.eloancn.framework.ump.Profiler;
import com.site.lookup.util.StringUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;

import java.lang.reflect.Proxy;

/**
 * 　　　　　　　　┏┓　　　┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 *
 * @User : Hapic
 * @Date : 2016/12/5 15:41
 * 新增CAT基于方法的代理
 */
public class MethodInterceptorProxyBean implements MethodInterceptor {

    private String umpOpenFlag = "true";


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
                Class<?>[] interfaces = targetClass.getInterfaces();
                for (Class interClzz : interfaces) {
                    if(interClzz.getPackage()==null){
                        continue;
                    }
                    ttName = interClzz.getName();
                    break;
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


    public boolean isJdkDynamicProxy(Class clazz) {
        return Proxy.isProxyClass(clazz);
    }

    @DisconfItem(key = "umpOpenFlag")
    public String getUmpOpenFlag() {
        return umpOpenFlag;
    }

    public void setUmpOpenFlag(String umpOpenFlag) {
        this.umpOpenFlag = umpOpenFlag;
    }

}
