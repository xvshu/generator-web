package com.eloancn.framework.orm;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import com.eloancn.framework.annotation.DataSource;
@Aspect
public class DataSourceAspect {

    /**
     * 拦截目标方法，获取由@DataSource指定的数据源标识，设置到线程存储中以便切换数据源
     * 
     * @param point
     * @throws Exception
     */
	@Around("execution(* *(..)) && @annotation(com.eloancn.framework.annotation.DataSource)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Class<?> target = point.getTarget().getClass();
    	Method method = MethodSignature.class.cast(point.getSignature())
				.getMethod();
    	Object result=null;
    	DataSource ds = method.getAnnotation(DataSource.class);
    	if(ds==null){
    		ds=target.getAnnotation(DataSource.class);
    	}
    	if(ds!=null){
    		DynamicDataSourceHolder.setDataSource(ds.value());
    	}
    	try {
			result = point.proceed();
		} catch (Exception e) {
			throw e;
		}finally{
			DynamicDataSourceHolder.clearDataSource();
		}
        return result;
    }


}