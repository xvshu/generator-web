<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dao;

import com.eloancn.framework.orm.base.dao.IBaseDao;
import com.eloancn.framework.orm.mybatis.MyBatisRepository;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageBounds;

import ${basepackage}.model.${className};
import com.eloancn.framework.orm.mybatis.paginator.domain.PageList;
<#include "/java_imports.include">
@MyBatisRepository
public interface ${className}Dao extends IBaseDao<${className}>{

}
