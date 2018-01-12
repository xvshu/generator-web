package com.eloancn.framework.transaction;

import java.util.ArrayList;

public class CurrentPublishThreadlLocal {

	private static ThreadLocal<ArrayList<MessageBody>> threadLocal = new ThreadLocal<ArrayList<MessageBody>>();  

	public static ArrayList<MessageBody> get(){
		return threadLocal.get();
	}
	public static void set(MessageBody mid){
		ArrayList<MessageBody> list= get();
		if(list==null){
			list= new ArrayList<MessageBody>();
		}
		list.add(mid);
		threadLocal.set(list);
	}
	public static void remove(){
		 threadLocal.remove();
	}
}
