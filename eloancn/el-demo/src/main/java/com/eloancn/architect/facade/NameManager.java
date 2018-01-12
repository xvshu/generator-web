/*
 * Powered By [crazy-framework]
 * Web Site: http://www.eloan.com
 * Since 2015 - 2017
 */

package com.eloancn.architect.facade;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eloancn.framework.utils.BeanMapper;
import com.eloancn.framework.orm.mybatis.paginator.domain.Order;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageBounds;
import com.eloancn.framework.utils.ObjectUtils;

import com.eloancn.architect.api.NameService;
import com.eloancn.architect.model.Name;
import com.eloancn.architect.service.NameServiceBean;
import com.eloancn.architect.dto.NameDto;
import com.eloancn.framework.sevice.api.MessageCode;
import com.eloancn.framework.sevice.api.MessageStatus;
import com.eloancn.framework.sevice.api.ResultDTO;
import com.eloancn.framework.sevice.api.PageParsDTO;
import com.eloancn.framework.sevice.api.PageResultDTO;
import com.eloancn.framework.orm.mybatis.paginator.PaginatorUtil;
import com.eloancn.framework.annotation.Validated;
import java.util.List;
import java.util.Map;
import java.util.Date;
/**
 * <>
 * @author crazy
 * @version 1.0
 * @Time 2017-11-03 17:48:52
 */

@Service
public class NameManager implements NameService{
	
	private static final Logger logger = LoggerFactory.getLogger(NameManager.class);
	
	private NameServiceBean nameServiceBean;
	
	@Autowired
	public void setNameServiceBean(NameServiceBean nameServiceBean) {
		this.nameServiceBean = nameServiceBean;
	}
	/**  
     *   
     * <save one>  
     * @param name  
     * @return ResultDTO<Integer>
     */ 
	@Validated
	public ResultDTO<Integer> insertName(NameDto nameDto){
		if(logger.isDebugEnabled()){
			logger.debug("NameManager.insertName(NameDto nameDto) start-->", nameDto);
		}		
		ResultDTO<Integer> result= new ResultDTO<Integer>();
		Name name = null;
		try{
			name=BeanMapper.map(nameDto,Name.class);
			nameServiceBean.insert(name);
			result.setData(name.getId());
		}catch (Exception e) {
			result.setStatus(MessageStatus.SUCCESS.getValue());
			//result.setMessage(MessageCode.SUCCESS.getMessage());
			//result.setCode(MessageCode.SUCCESS.getCode());
			if(logger.isErrorEnabled()){
				logger.error("NameManager.insertName(NameDto nameDto) error\n", e);
			}
		}	
		if(logger.isDebugEnabled()){
			logger.debug("NameManager.insertName(NameDto nameDto) end-->", name);
		}
		return result;
	}
	 /**  
     *   
     * <update one>  
     * @param name  
     * @return ResultDTO<Integer>  
     */ 
	@Validated
	public ResultDTO<Integer> updateName(NameDto nameDto){
		if(logger.isDebugEnabled()){
			logger.debug("NameManager.updateName(NameDto nameDto) start-->", nameDto);
		}		
		ResultDTO<Integer> result= new ResultDTO<Integer>();
		int u=0;
		try{
			Name name=BeanMapper.map(nameDto,Name.class);
			u=nameServiceBean.update(name);
			result.setData(u);
		}catch (Exception e) {
			result.setStatus(MessageStatus.SUCCESS.getValue());
			//result.setMessage(MessageCode.SUCCESS.getMessage());
			//result.setCode(MessageCode.SUCCESS.getCode());
			if(logger.isErrorEnabled()){
				logger.error("NameManager.updateName(NameDto nameDto) error\n", e);
			}
		}	
		if(logger.isDebugEnabled()){
			logger.debug("NameManager.updateName(NameDto nameDto) end-->", u);
		}
		return result;
	}
	 /**  
     *   
     * <find one by id>  
     * @param id  
     * @return ResultDTO<NameDto>
     */ 
	public ResultDTO<NameDto> getNameById(Integer id){
		if(logger.isDebugEnabled()){
			logger.debug("NameManager.getNameById(Integer id) start-->", id);
		}		
		ResultDTO<NameDto> result= new ResultDTO<NameDto>();
		Name name=null;
		try{			
			name=nameServiceBean.get(id);
			NameDto nameDto=BeanMapper.map(name,NameDto.class);
			result.setData(nameDto);
		}catch (Exception e) {
			result.setStatus(MessageStatus.SUCCESS.getValue());
			//result.setMessage(MessageCode.SUCCESS.getMessage());
			//result.setCode(MessageCode.SUCCESS.getCode());
			if(logger.isErrorEnabled()){
				logger.error("NameManager.getNameById(Integer id) error\n", e);
			}
		}	
		if(logger.isDebugEnabled()){
			logger.debug("NameManager.getNameById(Integer id) end-->", name);
		}
		return result;
	}
	 /**  
     *   
     * <query all>  
     * @param pageParsDTO  
     * @return ResultDTO<List<NameDto>>
     */ 
	public ResultDTO<PageResultDTO<NameDto>> searchName(PageParsDTO<Map> pageParsDTO){
		if(logger.isDebugEnabled()){
			logger.debug("NameManager.searchName(int page,int limit,String sort,String dir,Map paramMap) start-->", pageParsDTO);
		}		
		ResultDTO<PageResultDTO<NameDto>> result= new ResultDTO<PageResultDTO<NameDto>>();
		List<Name> nameList = null;
		try{
			nameList=nameServiceBean.search(pageParsDTO.getParam(),new PageBounds(pageParsDTO.getPage(), pageParsDTO.getLimit(),  Order.formString(pageParsDTO.getSort())));
			PageResultDTO<NameDto> pageResultDTO=PaginatorUtil.mapList(nameList,NameDto.class);
			result.setData(pageResultDTO);
		}catch (Exception e) {
			result.setStatus(MessageStatus.SUCCESS.getValue());
			//result.setMessage(MessageCode.SUCCESS.getMessage());
			//result.setCode(MessageCode.SUCCESS.getCode());
			if(logger.isErrorEnabled()){
				logger.error("NameManager.searchName(int page,int limit,String sort,String dir,Map paramMap) error\n", e);
			}
		}	
		if(logger.isDebugEnabled()){
			logger.debug("NameManager.searchName(int page,int limit,String sort,String dir,Map paramMap) end-->", nameList);
		}
		return result;
	}
}