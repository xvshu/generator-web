package com.eloancn.framework.log;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eloancn.framework.annotation.Loggable;

@Aspect
public class MethodLogger {

	private static final Logger logger = LoggerFactory
			.getLogger(MethodLogger.class);

	@Around("execution(* *(..)) && @annotation(com.eloancn.framework.annotation.Loggable)")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		Object result=null;
		long start = System.currentTimeMillis();
		result = point.proceed();
		long end = System.currentTimeMillis();
		try {
			final Method method = MethodSignature.class.cast(point.getSignature())
					.getMethod();
			final Loggable log = method.getAnnotation(Loggable.class);
			String [] print=new String[point.getArgs().length+3];
			String methodName=MethodSignature.class.cast(point.getSignature()).getMethod().getName();
			StringBuffer sb=new StringBuffer("#{} pars:(");
			print[0]=methodName;
			int i=1;
			for(Object o:point.getArgs()){
				if(o!=null)
					print[i]=o.toString();
				else
					print[i]="";
				sb.append("{},");
				i++;
			}
			sb.append(") return val:{} in {}");
			print[i]=result!=null?result.toString():"";
			i++;
			print[i]=""+(end - start);
			switch (log.value()) {
			case Loggable.INFO:
				logger.info(sb.toString(),print);
				break;
			case Loggable.ERROR:
				logger.error(sb.toString(),print);
				break;
			default:
				logger.debug(sb.toString(),print);
				break;
			}
			return result;
		} catch (Exception e) {
			logger.error("log error!", e);
		}
		return result;
		
	}
}