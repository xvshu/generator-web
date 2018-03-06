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
 * @Time 2018-01-18 15:49:21
 */

public class NameDto implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	/**  
     * 主键  
     */ 
	
	private Integer id;
	/**  
     * 姓名  
     */ 
	@Length(max=255)
	private String name;
	/**  
     *   
     */ 
	
	private Double sex;
	/**  
     * 创建时间  
     */ 
	
	private java.util.Date createDate;
	/**  
     * 更新时间  
     */ 
	
	private java.util.Date updateDate;


	public void setId(Integer value) {
		this.id = value;
	}
	
	public Integer getId() {
		return this.id;
	}
	public void setName(String value) {
		this.name = value;
	}
	
	public String getName() {
		return this.name;
	}
	public void setSex(Double value) {
		this.sex = value;
	}
	
	public Double getSex() {
		return this.sex;
	}
	public void setCreateDate(java.util.Date value) {
		this.createDate = value;
	}
	
	public java.util.Date getCreateDate() {
		return this.createDate;
	}
	public void setUpdateDate(java.util.Date value) {
		this.updateDate = value;
	}
	
	public java.util.Date getUpdateDate() {
		return this.updateDate;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
