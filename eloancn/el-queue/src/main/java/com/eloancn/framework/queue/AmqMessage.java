package com.eloancn.framework.queue;

public class AmqMessage {

	private String appID;
	private String messageId;
	private String messageListenerService;
	private String methodName;
	private String bodyType;
	private String body;
	private String exchange;
	private String routingKey;
	
	public String getAppID() {
		return appID;
	}
	public void setAppID(String appID) {
		this.appID = appID;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getMessageListenerService() {
		return messageListenerService;
	}
	public void setMessageListenerService(String messageListenerService) {
		this.messageListenerService = messageListenerService;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getBodyType() {
		return bodyType;
	}
	public void setBodyType(String bodyType) {
		this.bodyType = bodyType;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
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
	@Override
	public String toString() {
		return "AmqMessage [appID=" + appID + ", messageId=" + messageId
				+ ", messageListenerService=" + messageListenerService
				+ ", methodName=" + methodName + ", bodyType=" + bodyType
				+ ", body=" + body + ", exchange=" + exchange + ", routingKey="
				+ routingKey + "]";
	}
	
}
