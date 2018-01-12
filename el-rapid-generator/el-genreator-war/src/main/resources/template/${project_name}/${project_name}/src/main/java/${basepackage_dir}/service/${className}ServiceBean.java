<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageBounds;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageList;
import com.eloancn.framework.utils.ObjectUtils;

import ${basepackage}.model.${className};
import ${basepackage}.dao.${className}Dao;
<#include "/java_imports.include">
@Service
public class ${className}ServiceBean{
	
	private static final Logger logger = LoggerFactory.getLogger(${className}ServiceBean.class);
	
	private ${className}Dao ${classNameLower}Dao;
	
	@Autowired
	public void set${className}Dao(${className}Dao ${classNameLower}Dao) {
		this.${classNameLower}Dao = ${classNameLower}Dao;
	}
	/**  
     *   
     * <save one>  
     * @param ${classNameLower}  
     * @return ${table.pkColumn.simpleJavaType}
     */ 
	public void insert(${className} ${classNameLower}){
		if(logger.isDebugEnabled()){
			logger.debug("${className}ServiceBean.insert(${className} ${classNameLower}) start-->", ${classNameLower});
		}
		if(ObjectUtils.isNullOrEmpty(${classNameLower})){
			
		}
		
		int i=${classNameLower}Dao.insert(${classNameLower});
		if(logger.isDebugEnabled()){
			logger.debug("${className}ServiceBean.insert(${className} ${classNameLower}) end-->", i);
		}
	}
	 /**  
     *   
     * <update one>  
     * @param ${classNameLower}  
     */
	public int update(${className} ${classNameLower}){
		if(logger.isDebugEnabled()){
			logger.debug("${className}ServiceBean.update(${className} ${classNameLower}) start-->", ${classNameLower});
		}
		if(ObjectUtils.isNullOrEmpty(${classNameLower})){
			
		}
		
		int u=${classNameLower}Dao.update(${classNameLower});
		if(logger.isDebugEnabled()){
			logger.debug("${className}ServiceBean.update(${className} ${classNameLower}) end-->", u);
		}
		return u;
	}
	 /**  
     *   
     * <find one by id>  
     * @param id  
     * @return ${className}
     */ 
	public ${className} get(${table.idColumn.simpleJavaType} id){
		if(logger.isDebugEnabled()){
			logger.debug("${className}ServiceBean.get(${table.idColumn.simpleJavaType} id) start-->", id);
		}
		if(ObjectUtils.isNullOrEmpty(id)){
			
		}
		
		${className} ${classNameLower}=${classNameLower}Dao.get(id);
		if(logger.isDebugEnabled()){
			logger.debug("${className}ServiceBean.get(${table.idColumn.simpleJavaType} id) end-->", ${classNameLower});
		}
		return ${classNameLower};
	}
	/**  
     * <query all>  
     * @param paramMap  
     * @param pageBounds
     * @return PageList<${className}>
     */ 
	public PageList<${className}> search(Map paramMap ,PageBounds pageBounds){
		if(logger.isDebugEnabled()){
			logger.debug("${className}ServiceBean.search(Map paramMap ,PageBounds pageBounds) start-->", paramMap);
		}
		if(ObjectUtils.isNullOrEmpty(paramMap)){
			
		}

		PageList<${className}> ${classNameLower}List=${classNameLower}Dao.search(paramMap,pageBounds);
		if(logger.isDebugEnabled()){
			logger.debug("${className}ServiceBean.search(Map paramMap ,PageBounds pageBounds) end-->", ${classNameLower}List);
		}
		return ${classNameLower}List;
	}

	/**
	 * <query List>
	 * @param paramMap
	 * @return List<${className}>
	 */
	public List<${className}> search(Map paramMap){
		if(logger.isDebugEnabled()){
			logger.debug("${className}ServiceBean.search(Map paramMap) start-->", paramMap);
		}
		if(ObjectUtils.isNullOrEmpty(paramMap)){

		}

		List<${className}> ${classNameLower}List=${classNameLower}Dao.search(paramMap);
		if(logger.isDebugEnabled()){
		logger.debug("${className}ServiceBean.search(Map paramMap) end-->", ${classNameLower}List);
		}
		return ${classNameLower}List;
	}
	
	<#list table.columns as column>
	<#if column.unique && !column.pk>
	/**  
     *   
     * <find one by ${column.columnName}>  
     * @param id  
     * @return ${className}
     */ 
	public ${className} getBy${column.columnName}(${column.simpleJavaType} ${column.columnNameFirstLower}){
		if(logger.isDebugEnabled()){
			logger.debug("${className}ServiceBean.getBy${column.columnName}(${column.simpleJavaType} ${column.columnNameFirstLower} start-->", ${column.columnNameFirstLower});
		}
		if(ObjectUtils.isNullOrEmpty(${column.columnNameFirstLower})){
			
		}
		
		${className} ${classNameLower}=${classNameLower}Dao.getBy${column.columnName}(${column.columnNameFirstLower});
		if(logger.isDebugEnabled()){
			logger.debug("${className}ServiceBean.getBy${column.columnName}(${column.simpleJavaType} ${column.columnNameFirstLower} end-->", ${classNameLower});
		}
		return ${classNameLower};
	 }
	</#if>
	</#list>
}