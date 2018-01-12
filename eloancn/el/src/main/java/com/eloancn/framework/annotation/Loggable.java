package com.eloancn.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Loggable {
	
	public final static int DEBUG=1;
	
	public final static int INFO=2;
	
	public final static int ERROR=3;
	
	int value() default 1;

}
