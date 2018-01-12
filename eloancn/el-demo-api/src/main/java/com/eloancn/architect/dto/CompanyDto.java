/*
 * Powered By [crazy-framework]
 * Web Site: http://www.eloan.com
 * Since 2015 - 2017
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
 * @author crazy
 * @version 1.0
 * @Time 2017-11-03 17:48:53
 */

public class CompanyDto implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	/**  
     * 主键  
     */ 
	@Max(9999999999L)
	private Integer id;
	/**  
     *   
     */ 
	@Max(9999999999L)
	private Integer nameId;
	/**  
     * 姓名  
     */ 
	@Length(max=255)
	private String company;
	/**  
     * 创建时间  
     */ 
	
	private Date createDate;
	/**  
     * 更新时间  
     */ 
	
	private Date updateDate;


	public void setId(Integer value) {
		this.id = value;
	}
	
	public Integer getId() {
		return this.id;
	}
	public void setNameId(Integer value) {
		this.nameId = value;
	}
	
	public Integer getNameId() {
		return this.nameId;
	}
	public void setCompany(String value) {
		this.company = value;
	}
	
	public String getCompany() {
		return this.company;
	}
	public void setCreateDate(Date value) {
		this.createDate = value;
	}
	
	public Date getCreateDate() {
		return this.createDate;
	}
	public void setUpdateDate(Date value) {
		this.updateDate = value;
	}
	
	public Date getUpdateDate() {
		return this.updateDate;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
