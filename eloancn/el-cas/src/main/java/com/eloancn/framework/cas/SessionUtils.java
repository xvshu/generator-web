package com.eloancn.framework.cas;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

public class SessionUtils {

	public static String getUerName() {
		return ((UserInfo) SecurityUtils.getSubject().getPrincipal()).getUserName();
	}
	public static Subject getSubject(){
		return SecurityUtils.getSubject();
	}
	public static boolean isLogin(){
		Subject subject=SecurityUtils.getSubject();
		if(null==subject)
			return false;
		return subject.isAuthenticated();
	}
	public static String getUserId() {
		return ((UserInfo) SecurityUtils.getSubject().getPrincipal()).getUserId();
	}
	public static UserInfo getUserInfo(){
		return (UserInfo) SecurityUtils.getSubject().getPrincipal();
	}
	public static Session getSession(){
		return SecurityUtils.getSubject().getSession();
	}
	public static Session getSession(boolean create){
		return SecurityUtils.getSubject().getSession(create);
	}
}
