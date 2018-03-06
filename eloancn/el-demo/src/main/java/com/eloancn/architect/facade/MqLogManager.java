/*
 * Powered By [eloancn-generator]
 * Author:qinxf
 * Since 2017 - 2018
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

import com.eloancn.architect.api.MqLogService;
import com.eloancn.architect.model.MqLog;
import com.eloancn.architect.service.MqLogServiceBean;
import com.eloancn.architect.dto.MqLogDto;
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
 * @author qinxf
 * @version 1.0
 * @Time 2018-01-18 15:49:20
 */

@Service
public class MqLogManager implements MqLogService{
	
	private static final Logger logger = LoggerFactory.getLogger(MqLogManager.class);
	
	private MqLogServiceBean mqLogServiceBean;
	
	@Autowired
	public void setMqLogServiceBean(MqLogServiceBean mqLogServiceBean) {
		this.mqLogServiceBean = mqLogServiceBean;
	}
	/**  
     *   
     * <save one>  
     * @return ResultDTO<Integer>
     */ 
	@Validated
	public ResultDTO<Integer> insertMqLog(MqLogDto mqLogDto){
		if(logger.isDebugEnabled()){
			logger.debug("MqLogManager.insertMqLog(MqLogDto mqLogDto) start-->", mqLogDto);
		}		
		ResultDTO<Integer> result= new ResultDTO<Integer>();
		MqLog mqLog = null;
		try{
			mqLog=BeanMapper.map(mqLogDto,MqLog.class);
			mqLogServiceBean.insert(mqLog);
			result.setData(mqLog.getId());
		}catch (Exception e) {
			result.setStatus(MessageStatus.SUCCESS.getValue());
			//result.setMessage(MessageCode.SUCCESS.getMessage());
			//result.setCode(MessageCode.SUCCESS.getCode());
			if(logger.isErrorEnabled()){
				logger.error("MqLogManager.insertMqLog(MqLogDto mqLogDto) error\n", e);
			}
		}	
		if(logger.isDebugEnabled()){
			logger.debug("MqLogManager.insertMqLog(MqLogDto mqLogDto) end-->", mqLog);
		}
		return result;
	}
	 /**  
     *   
     * <update one>  
     * @return ResultDTO<Integer>
     */ 
	@Validated
	public ResultDTO<Integer> updateMqLog(MqLogDto mqLogDto){
		if(logger.isDebugEnabled()){
			logger.debug("MqLogManager.updateMqLog(MqLogDto mqLogDto) start-->", mqLogDto);
		}		
		ResultDTO<Integer> result= new ResultDTO<Integer>();
		int u=0;
		try{
			MqLog mqLog=BeanMapper.map(mqLogDto,MqLog.class);
			u=mqLogServiceBean.update(mqLog);
			result.setData(u);
		}catch (Exception e) {
			result.setStatus(MessageStatus.SUCCESS.getValue());
			//result.setMessage(MessageCode.SUCCESS.getMessage());
			//result.setCode(MessageCode.SUCCESS.getCode());
			if(logger.isErrorEnabled()){
				logger.error("MqLogManager.updateMqLog(MqLogDto mqLogDto) error\n", e);
			}
		}	
		if(logger.isDebugEnabled()){
			logger.debug("MqLogManager.updateMqLog(MqLogDto mqLogDto) end-->", u);
		}
		return result;
	}
	 /**  
     *   
     * <find one by id>  
     * @return ResultDTO<MqLogDto>
     */ 
	public ResultDTO<MqLogDto> getMqLogById(Long id){
		if(logger.isDebugEnabled()){
			logger.debug("MqLogManager.getMqLogById(Long id) start-->", id);
		}		
		ResultDTO<MqLogDto> result= new ResultDTO<MqLogDto>();
		MqLog mqLog=null;
		try{			
			mqLog=mqLogServiceBean.get(id);
			MqLogDto mqLogDto=BeanMapper.map(mqLog,MqLogDto.class);
			result.setData(mqLogDto);
		}catch (Exception e) {
			result.setStatus(MessageStatus.SUCCESS.getValue());
			//result.setMessage(MessageCode.SUCCESS.getMessage());
			//result.setCode(MessageCode.SUCCESS.getCode());
			if(logger.isErrorEnabled()){
				logger.error("MqLogManager.getMqLogById(Long id) error\n", e);
			}
		}	
		if(logger.isDebugEnabled()){
			logger.debug("MqLogManager.getMqLogById(Long id) end-->", mqLog);
		}
		return result;
	}
	 /**  
     *   
     * <query all>  
     * @return ResultDTO<List<MqLogDto>>
     */ 
	public ResultDTO<PageResultDTO<MqLogDto>> searchMqLog(PageParsDTO<Map> pageParsDTO){
		if(logger.isDebugEnabled()){
			logger.debug("MqLogManager.searchMqLog(int page,int limit,String sort,String dir,Map paramMap) start-->", pageParsDTO);
		}		
		ResultDTO<PageResultDTO<MqLogDto>> result= new ResultDTO<PageResultDTO<MqLogDto>>();
		List<MqLog> mqLogList = null;
		try{
			mqLogList=mqLogServiceBean.search(pageParsDTO.getParam(),new PageBounds(pageParsDTO.getPage(), pageParsDTO.getLimit(),  Order.formString(pageParsDTO.getSort())));
			PageResultDTO<MqLogDto> pageResultDTO=PaginatorUtil.mapList(mqLogList,MqLogDto.class);
			result.setData(pageResultDTO);
		}catch (Exception e) {
			result.setStatus(MessageStatus.SUCCESS.getValue());
			//result.setMessage(MessageCode.SUCCESS.getMessage());
			//result.setCode(MessageCode.SUCCESS.getCode());
			if(logger.isErrorEnabled()){
				logger.error("MqLogManager.searchMqLog(int page,int limit,String sort,String dir,Map paramMap) error\n", e);
			}
		}	
		if(logger.isDebugEnabled()){
			logger.debug("MqLogManager.searchMqLog(int page,int limit,String sort,String dir,Map paramMap) end-->", mqLogList);
		}
		return result;
	}
}