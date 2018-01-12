package com.eloancn.framework.transaction;

public class CurrentTransactionThreadlLocal {

	private static ThreadLocal<Boolean> threadLocal = new ThreadLocal<Boolean>();  

	public static Boolean get(){
		return threadLocal.get();
	}
	public static void set(Boolean mid){
		 threadLocal.set(mid);
	}
	public static void remove(){
		 threadLocal.remove();
	}
}
