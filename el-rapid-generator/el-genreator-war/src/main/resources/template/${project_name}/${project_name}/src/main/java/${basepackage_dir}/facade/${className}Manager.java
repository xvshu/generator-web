<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.facade;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eloancn.framework.utils.BeanMapper;
import com.eloancn.framework.orm.mybatis.paginator.domain.Order;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageBounds;
import com.eloancn.framework.utils.ObjectUtils;

import ${basepackage}.api.${className}Service;
import ${basepackage}.model.${className};
import ${basepackage}.service.${className}ServiceBean;
import ${basepackage}.dto.${className}Dto;
import com.eloancn.framework.sevice.api.MessageCode;
import com.eloancn.framework.sevice.api.MessageStatus;
import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.framework.sevice.api.PageParsDTO;
import com.eloancn.framework.sevice.api.PageResultDTO;
import com.eloancn.framework.orm.mybatis.paginator.PaginatorUtil;
import com.eloancn.framework.annotation.Validated;
<#include "/java_imports.include">
@Service
public class ${className}Manager implements ${className}Service{
	
	private static final Logger logger = LoggerFactory.getLogger(${className}Manager.class);
	
	private ${className}ServiceBean ${classNameLower}ServiceBean;
	
	@Autowired
	public void set${className}ServiceBean(${className}ServiceBean ${classNameLower}ServiceBean) {
		this.${classNameLower}ServiceBean = ${classNameLower}ServiceBean;
	}
	/**  
     *   
     * <save one>  
     * @return ResultDTO<Integer>
     */ 
	@Validated
	public ResultDTO<Long> insert${className}(${className}Dto ${classNameLower}Dto){
		if(logger.isDebugEnabled()){
			logger.debug("${className}Manager.insert${className}(${className}Dto ${classNameLower}Dto) start-->", ${classNameLower}Dto);
		}		
		ResultDTO<Long> result= new ResultDTO<Long>();
		${className} ${classNameLower} = null;
		try{
			${classNameLower}=BeanMapper.map(${classNameLower}Dto,${className}.class);
			${classNameLower}ServiceBean.insert(${classNameLower});
			result.setData(${classNameLower}.getId());
		}catch (Exception e) {
			result.setStatus(MessageStatus.SUCCESS.getValue());
			//result.setMessage(MessageCode.SUCCESS.getMessage());
			//result.setCode(MessageCode.SUCCESS.getCode());
			if(logger.isErrorEnabled()){
				logger.error("${className}Manager.insert${className}(${className}Dto ${classNameLower}Dto) error\n", e);
			}
		}	
		if(logger.isDebugEnabled()){
			logger.debug("${className}Manager.insert${className}(${className}Dto ${classNameLower}Dto) end-->", ${classNameLower});
		}
		return result;
	}
	 /**  
     *   
     * <update one>  
     * @return ResultDTO<Integer>
     */ 
	@Validated
	public ResultDTO<Integer> update${className}(${className}Dto ${classNameLower}Dto){
		if(logger.isDebugEnabled()){
			logger.debug("${className}Manager.update${className}(${className}Dto ${classNameLower}Dto) start-->", ${classNameLower}Dto);
		}		
		ResultDTO<Integer> result= new ResultDTO<Integer>();
		int u=0;
		try{
			${className} ${classNameLower}=BeanMapper.map(${classNameLower}Dto,${className}.class);
			u=${classNameLower}ServiceBean.update(${classNameLower});
			result.setData(u);
		}catch (Exception e) {
			result.setStatus(MessageStatus.SUCCESS.getValue());
			//result.setMessage(MessageCode.SUCCESS.getMessage());
			//result.setCode(MessageCode.SUCCESS.getCode());
			if(logger.isErrorEnabled()){
				logger.error("${className}Manager.update${className}(${className}Dto ${classNameLower}Dto) error\n", e);
			}
		}	
		if(logger.isDebugEnabled()){
			logger.debug("${className}Manager.update${className}(${className}Dto ${classNameLower}Dto) end-->", u);
		}
		return result;
	}
	 /**  
     *   
     * <find one by id>  
     * @return ResultDTO<${className}Dto>
     */ 
	public ResultDTO<${className}Dto> get${className}ById(${table.idColumn.simpleJavaType} id){
		if(logger.isDebugEnabled()){
			logger.debug("${className}Manager.get${className}ById(${table.idColumn.simpleJavaType} id) start-->", id);
		}		
		ResultDTO<${className}Dto> result= new ResultDTO<${className}Dto>();
		${className} ${classNameLower}=null;
		try{			
			${classNameLower}=${classNameLower}ServiceBean.get(id);
			${className}Dto ${classNameLower}Dto=BeanMapper.map(${classNameLower},${className}Dto.class);
			result.setData(${classNameLower}Dto);
		}catch (Exception e) {
			result.setStatus(MessageStatus.SUCCESS.getValue());
			//result.setMessage(MessageCode.SUCCESS.getMessage());
			//result.setCode(MessageCode.SUCCESS.getCode());
			if(logger.isErrorEnabled()){
				logger.error("${className}Manager.get${className}ById(${table.idColumn.simpleJavaType} id) error\n", e);
			}
		}	
		if(logger.isDebugEnabled()){
			logger.debug("${className}Manager.get${className}ById(${table.idColumn.simpleJavaType} id) end-->", ${classNameLower});
		}
		return result;
	}
	 /**  
     *   
     * <query all>  
     * @return ResultDTO<List<${className}Dto>>
     */ 
	public ResultDTO<PageResultDTO<${className}Dto>> search${className}(PageParsDTO<Map> pageParsDTO){
		if(logger.isDebugEnabled()){
			logger.debug("${className}Manager.search${className}(int page,int limit,String sort,String dir,Map paramMap) start-->", pageParsDTO);
		}		
		ResultDTO<PageResultDTO<${className}Dto>> result= new ResultDTO<PageResultDTO<${className}Dto>>();
		List<${className}> ${classNameLower}List = null;
		try{
			${classNameLower}List=${classNameLower}ServiceBean.search(pageParsDTO.getParam(),new PageBounds(pageParsDTO.getPage(), pageParsDTO.getLimit(),  Order.formString(pageParsDTO.getSort())));
			PageResultDTO<${className}Dto> pageResultDTO=PaginatorUtil.mapList(${classNameLower}List,${className}Dto.class);
			result.setData(pageResultDTO);
		}catch (Exception e) {
			result.setStatus(MessageStatus.SUCCESS.getValue());
			//result.setMessage(MessageCode.SUCCESS.getMessage());
			//result.setCode(MessageCode.SUCCESS.getCode());
			if(logger.isErrorEnabled()){
				logger.error("${className}Manager.search${className}(int page,int limit,String sort,String dir,Map paramMap) error\n", e);
			}
		}	
		if(logger.isDebugEnabled()){
			logger.debug("${className}Manager.search${className}(int page,int limit,String sort,String dir,Map paramMap) end-->", ${classNameLower}List);
		}
		return result;
	}
}