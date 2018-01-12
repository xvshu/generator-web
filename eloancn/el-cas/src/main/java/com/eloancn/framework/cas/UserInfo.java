package com.eloancn.framework.cas;

import java.io.Serializable;

import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserInfo implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private static Logger log = LoggerFactory.getLogger(UserInfo.class);
	 
	private String userName;
	
	private String userRealName;
	
	private String userId;
	
	private String email;
	
	private String mobile;
	
	private String nickname;
	
	private String idCard;
	
	private String roles;
	
	private String permissions;
	
	private Assertion assertion;
	
	private String paypassword;
	
	private String status;
	
	private String photo;
	
	private String accountNum;
	
	private String userType;
	
	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Assertion getAssertion() {
		return assertion;
	}

	public void setAssertion(Assertion assertion) {
		this.assertion = assertion;
	}
	
	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getPaypassword() {
		return paypassword;
	}

	public void setPaypassword(String paypassword) {
		this.paypassword = paypassword;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getService(String serviceUrl){
		 if(null!=this.assertion){
			 String proxyTicket=this.assertion.getPrincipal().getProxyTicketFor(serviceUrl);
			 if(serviceUrl.indexOf("?")>0){
				 serviceUrl="&ticket="+proxyTicket;
			 }
			 if(log.isInfoEnabled()){
				 log.info("UserInfo getService url:"+serviceUrl);
			 }
			 return CommonUtils.getResponseFromServer(serviceUrl, null) ;
		 }
		return null;
	}
	public String getService(String serviceUrl,String encoding){
		 if(null!=this.assertion){
			 String proxyTicket=this.assertion.getPrincipal().getProxyTicketFor(serviceUrl);
			 if(serviceUrl.indexOf("?")>0){
				 serviceUrl="&ticket="+proxyTicket;
			 }
			 if(log.isInfoEnabled()){
				 log.info("UserInfo getService url:"+serviceUrl);
			 }
			 return CommonUtils.getResponseFromServer(serviceUrl, null) ;
		 }
		return null;
	}

	@Override
	public String toString() {
		return "UserInfo [userName=" + userName + ", userRealName="
				+ userRealName + ", userId=" + userId + ", email=" + email
				+ ", mobile=" + mobile + ", nickname=" + nickname + ", idCard="
				+ idCard + ", roles=" + roles + ", permissions=" + permissions
				+ ", assertion=" + assertion + "]";
	}

}
