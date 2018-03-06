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

import com.eloancn.framework.orm.base.service.BaseService;
import ${basepackage}.model.${className};
import ${basepackage}.dao.${className}Dao;
<#include "/java_imports.include">
@Service
public class ${className}ServiceBean extends BaseService<${className}>{
	
	private static final Logger logger = LoggerFactory.getLogger(${className}ServiceBean.class);
	
	private ${className}Dao ${classNameLower}Dao;
	
	@Autowired
	public void set${className}Dao(${className}Dao ${classNameLower}Dao) {
		this.${classNameLower}Dao = ${classNameLower}Dao;
	}

}