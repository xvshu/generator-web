package com.eloancn.framework.transaction;

import java.io.Serializable;
import java.util.Date;

public class MessageBody implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date creatTime;
	private String exchange;
	private String routingKey;
	private String errorService;
	private String virtualHost;
	private String tId;
	private String messageId;
	private Object body;
	
	public MessageBody(){
		creatTime = new Date();
	}
	
	public String gettId() {
		return tId;
	}
	public void settId(String tId) {
		this.tId = tId;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public String getRoutingKey() {
		return routingKey;
	}
	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}
	
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getErrorService() {
		return errorService;
	}
	public void setErrorService(String errorService) {
		this.errorService = errorService;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	public String getVirtualHost() {
		return virtualHost;
	}
	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}
	public Date getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	@Override
	public String toString() {
		return "MessageBody [exchange=" + exchange + ", routingKey="
				+ routingKey + ", errorService=" + errorService
				+ ", virtualHost=" + virtualHost + ", tId=" + tId
				+ ", messageId=" + messageId + ", body=" + body + "]";
	}

}
