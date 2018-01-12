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

import com.eloancn.architect.api.CompanyService;
import com.eloancn.architect.model.Company;
import com.eloancn.architect.service.CompanyServiceBean;
import com.eloancn.architect.dto.CompanyDto;
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
 * @Time 2017-11-03 17:48:53
 */

@Service
public class CompanyManager implements CompanyService{
	
	private static final Logger logger = LoggerFactory.getLogger(CompanyManager.class);
	
	private CompanyServiceBean companyServiceBean;
	
	@Autowired
	public void setCompanyServiceBean(CompanyServiceBean companyServiceBean) {
		this.companyServiceBean = companyServiceBean;
	}
	/**  
     *   
     * <save one>  
     * @param company  
     * @return ResultDTO<Integer>
     */ 
	@Validated
	public ResultDTO<Integer> insertCompany(CompanyDto companyDto){
		if(logger.isDebugEnabled()){
			logger.debug("CompanyManager.insertCompany(CompanyDto companyDto) start-->", companyDto);
		}		
		ResultDTO<Integer> result= new ResultDTO<Integer>();
		Company company = null;
		try{
			company=BeanMapper.map(companyDto,Company.class);
			companyServiceBean.insert(company);
			result.setData(company.getId());
		}catch (Exception e) {
			result.setStatus(MessageStatus.SUCCESS.getValue());
			//result.setMessage(MessageCode.SUCCESS.getMessage());
			//result.setCode(MessageCode.SUCCESS.getCode());
			if(logger.isErrorEnabled()){
				logger.error("CompanyManager.insertCompany(CompanyDto companyDto) error\n", e);
			}
		}	
		if(logger.isDebugEnabled()){
			logger.debug("CompanyManager.insertCompany(CompanyDto companyDto) end-->", company);
		}
		return result;
	}
	 /**  
     *   
     * <update one>  
     * @param company  
     * @return ResultDTO<Integer>  
     */ 
	@Validated
	public ResultDTO<Integer> updateCompany(CompanyDto companyDto){
		if(logger.isDebugEnabled()){
			logger.debug("CompanyManager.updateCompany(CompanyDto companyDto) start-->", companyDto);
		}		
		ResultDTO<Integer> result= new ResultDTO<Integer>();
		int u=0;
		try{
			Company company=BeanMapper.map(companyDto,Company.class);
			u=companyServiceBean.update(company);
			result.setData(u);
		}catch (Exception e) {
			result.setStatus(MessageStatus.SUCCESS.getValue());
			//result.setMessage(MessageCode.SUCCESS.getMessage());
			//result.setCode(MessageCode.SUCCESS.getCode());
			if(logger.isErrorEnabled()){
				logger.error("CompanyManager.updateCompany(CompanyDto companyDto) error\n", e);
			}
		}	
		if(logger.isDebugEnabled()){
			logger.debug("CompanyManager.updateCompany(CompanyDto companyDto) end-->", u);
		}
		return result;
	}
	 /**  
     *   
     * <find one by id>  
     * @param id  
     * @return ResultDTO<CompanyDto>
     */ 
	public ResultDTO<CompanyDto> getCompanyById(Integer id){
		if(logger.isDebugEnabled()){
			logger.debug("CompanyManager.getCompanyById(Integer id) start-->", id);
		}		
		ResultDTO<CompanyDto> result= new ResultDTO<CompanyDto>();
		Company company=null;
		try{			
			company=companyServiceBean.get(id);
			CompanyDto companyDto=BeanMapper.map(company,CompanyDto.class);
			result.setData(companyDto);
		}catch (Exception e) {
			result.setStatus(MessageStatus.SUCCESS.getValue());
			//result.setMessage(MessageCode.SUCCESS.getMessage());
			//result.setCode(MessageCode.SUCCESS.getCode());
			if(logger.isErrorEnabled()){
				logger.error("CompanyManager.getCompanyById(Integer id) error\n", e);
			}
		}	
		if(logger.isDebugEnabled()){
			logger.debug("CompanyManager.getCompanyById(Integer id) end-->", company);
		}
		return result;
	}
	 /**  
     *   
     * <query all>  
     * @param pageParsDTO  
     * @return ResultDTO<List<CompanyDto>>
     */ 
	public ResultDTO<PageResultDTO<CompanyDto>> searchCompany(PageParsDTO<Map> pageParsDTO){
		if(logger.isDebugEnabled()){
			logger.debug("CompanyManager.searchCompany(int page,int limit,String sort,String dir,Map paramMap) start-->", pageParsDTO);
		}		
		ResultDTO<PageResultDTO<CompanyDto>> result= new ResultDTO<PageResultDTO<CompanyDto>>();
		List<Company> companyList = null;
		try{
			companyList=companyServiceBean.search(pageParsDTO.getParam(),new PageBounds(pageParsDTO.getPage(), pageParsDTO.getLimit(),  Order.formString(pageParsDTO.getSort())));
			PageResultDTO<CompanyDto> pageResultDTO=PaginatorUtil.mapList(companyList,CompanyDto.class);
			result.setData(pageResultDTO);
		}catch (Exception e) {
			result.setStatus(MessageStatus.SUCCESS.getValue());
			//result.setMessage(MessageCode.SUCCESS.getMessage());
			//result.setCode(MessageCode.SUCCESS.getCode());
			if(logger.isErrorEnabled()){
				logger.error("CompanyManager.searchCompany(int page,int limit,String sort,String dir,Map paramMap) error\n", e);
			}
		}	
		if(logger.isDebugEnabled()){
			logger.debug("CompanyManager.searchCompany(int page,int limit,String sort,String dir,Map paramMap) end-->", companyList);
		}
		return result;
	}
}