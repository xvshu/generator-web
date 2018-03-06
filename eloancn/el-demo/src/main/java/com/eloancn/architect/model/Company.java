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
 * @Time 2018-01-18 15:49:19
 */

public class Company implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	/**  
     * 主键  
     */ 
	//
	private Integer id;
	/**  
     *   
     */ 
	//
	private Integer nameId;
	/**  
     * 姓名  
     */ 
	//@Length(max=255)
	private String company;
	/**  
     * 创建时间  
     */ 
	//
	private Date createDate;
	/**  
     * 更新时间  
     */ 
	//
	private Date updateDate;

	public Company(){
	}

	public Company(
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

	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("Id",getId())
			.append("NameId",getNameId())
			.append("Company",getCompany())
			.append("CreateDate",getCreateDate())
			.append("UpdateDate",getUpdateDate())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Company == false) return false;
		if(this == obj) return true;
		Company other = (Company)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
}

