/*
 * Powered By [eloancn-generator]
 * Author:qinxf
 * Since 2017 - 2018
 */
 
package com.eloancn.architect.model;

import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;
import java.util.Map;
import java.util.Date;
/**
 * <>
 * @author qinxf
 * @version 1.0
 * @Time 2018-01-18 15:49:20
 */

public class MqLog implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	/**  
     *   
     */ 
	//
	private Integer id;
	/**  
     *   
     */ 
	//@Length(max=128)
	private String messageId;
	/**  
     *   
     */ 
	//@Length(max=128)
	private String exchange;
	/**  
     *   
     */ 
	//@Length(max=128)
	private String routingKey;
	/**  
     *   
     */ 
	//@Length(max=65535)
	private String error;
	/**  
     * 消息体  
     */ 
	//@Length(max=65535)
	private String messageBody;
	/**  
     *   
     */ 
	//@NotNull 
	private Date updateTime;

	public MqLog(){
	}

	public MqLog(
		Integer id
	){
		this.id = id;
	}

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
	public void setUpdateTime(Date value) {
		this.updateTime = value;
	}
	
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("Id",getId())
			.append("MessageId",getMessageId())
			.append("Exchange",getExchange())
			.append("RoutingKey",getRoutingKey())
			.append("Error",getError())
			.append("MessageBody",getMessageBody())
			.append("UpdateTime",getUpdateTime())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof MqLog == false) return false;
		if(this == obj) return true;
		MqLog other = (MqLog)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
}

