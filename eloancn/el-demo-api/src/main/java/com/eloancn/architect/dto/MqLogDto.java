/*
 * Powered By [eloancn-generator]
 * Author:qinxf
 * Since 2017 - 2018
 */
 
package com.eloancn.architect.dto;

import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;
import java.util.Map;
import java.util.Date;
/**
 * <>
 * @author qinxf
 * @version 1.0
 * @Time 2018-01-18 15:49:20
 */

public class MqLogDto implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	/**  
     *   
     */ 
	
	private Integer id;
	/**  
     *   
     */ 
	@Length(max=128)
	private String messageId;
	/**  
     *   
     */ 
	@Length(max=128)
	private String exchange;
	/**  
     *   
     */ 
	@Length(max=128)
	private String routingKey;
	/**  
     *   
     */ 
	@Length(max=65535)
	private String error;
	/**  
     * 消息体  
     */ 
	@Length(max=65535)
	private String messageBody;
	/**  
     *   
     */ 
	@NotNull 
	private java.util.Date updateTime;


	public void setId(Integer value) {
		this.id = value;
	}
	
	public Integer getId() {
		return this.id;
	}
	public void setMessageId(String value) {
		this.messageId = value;
	}
	
	public String getMessageId() {
		return this.messageId;
	}
	public void setExchange(String value) {
		this.exchange = value;
	}
	
	public String getExchange() {
		return this.exchange;
	}
	public void setRoutingKey(String value) {
		this.routingKey = value;
	}
	
	public String getRoutingKey() {
		return this.routingKey;
	}
	public void setError(String value) {
		this.error = value;
	}
	
	public String getError() {
		return this.error;
	}
	public void setMessageBody(String value) {
		this.messageBody = value;
	}
	
	public String getMessageBody() {
		return this.messageBody;
	}
	public void setUpdateTime(java.util.Date value) {
		this.updateTime = value;
	}
	
	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
