package com.eloancn.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Security {

	public final static int FORM=0;
	
	public final static int ATTRIBUTE=1;
	
	public final static int PARAMETER=2;
	
	int value();
	
	String encode() default "utf-8";
}