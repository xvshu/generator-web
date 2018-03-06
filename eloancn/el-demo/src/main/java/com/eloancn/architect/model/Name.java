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
 * @Time 2018-01-18 15:49:21
 */

public class Name implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	/**  
     * 主键  
     */ 
	//
	private Integer id;
	/**  
     * 姓名  
     */ 
	//@Length(max=255)
	private String name;
	/**  
     *   
     */ 
	//
	private Double sex;
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

	public Name(){
	}

	public Name(
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
			.append("Name",getName())
			.append("Sex",getSex())
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
		if(obj instanceof Name == false) return false;
		if(this == obj) return true;
		Name other = (Name)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
}

