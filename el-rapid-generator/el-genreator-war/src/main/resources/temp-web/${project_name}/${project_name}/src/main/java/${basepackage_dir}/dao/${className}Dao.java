<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dao;

import com.eloancn.framework.orm.mybatis.MyBatisRepository;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageBounds;

import ${basepackage}.model.${className};
import com.eloancn.framework.orm.mybatis.paginator.domain.PageList;
<#include "/java_imports.include">
@MyBatisRepository
public interface ${className}Dao{
	/**  
     *   
     * <save one>  
     * @param ${classNameLower}  
     * @return ${table.pkColumn.simpleJavaType}
     */ 
	int insert(${className} ${classNameLower});
	 /**  
     *   
     * <update one>  
     * @param ${classNameLower}  
     */
	int update(${className} ${classNameLower});
	 /**  
     *   
     * <find one by id>  
     * @param id  
     * @return ${className}
     */ 
	${className} get(${table.idColumn.simpleJavaType} id);
	/**  
     *   
     * <delete one by id>  
     * @param id  
     * @return int
     */ 
	int delete(${table.idColumn.simpleJavaType} id);
	/**  
     * <query page>  
     * @param paramMap  
     * @param pageBounds
     * @return List<${className}>
     */ 
	PageList<${className}> search(Map paramMap ,PageBounds pageBounds);


	/**
	 * <query all>
	 * @param paramMap
	 * @return List<Name>
	 */
	int searchcount(Map paramMap );


	/**
	 * <query list>
	 * @param paramMap
	 * @return
     */
	List<${className}> search(Map paramMap);
	
	<#list table.columns as column>
	<#if column.unique && !column.pk>
	/**  
     *   
     * <find one by ${column.columnName}>  
     * @param id  
     * @return ${className}
     */ 
	 ${className} getBy${column.columnName}(${column.simpleJavaType} ${column.columnNameFirstLower});
	</#if>
	</#list>



}
