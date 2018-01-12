/*
 * Powered By [crazy-framework]
 * Web Site: http://www.eloan.com
 * Since 2015 - 2017
 */

package com.eloancn.architect.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageBounds;
import com.eloancn.framework.utils.ObjectUtils;

import com.eloancn.architect.model.Company;
import com.eloancn.architect.dao.CompanyDao;
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
public class CompanyServiceBean{
	
	private static final Logger logger = LoggerFactory.getLogger(CompanyServiceBean.class);
	
	private CompanyDao companyDao;
	
	@Autowired
	public void setCompanyDao(CompanyDao companyDao) {
		this.companyDao = companyDao;
	}
	/**  
     *   
     * <save one>  
     * @param company  
     * @return Integer
     */ 
	public void insert(Company company){
		if(logger.isDebugEnabled()){
			logger.debug("CompanyServiceBean.insert(Company company) start-->", company);
		}
		if(ObjectUtils.isNullOrEmpty(company)){
			
		}
		
		int i=companyDao.insert(company);
		if(logger.isDebugEnabled()){
			logger.debug("CompanyServiceBean.insert(Company company) end-->", i);
		}
	}
	 /**  
     *   
     * <update one>  
     * @param company  
     * @throws DAOException  
     */ 
	public int update(Company company){
		if(logger.isDebugEnabled()){
			logger.debug("CompanyServiceBean.update(Company company) start-->", company);
		}
		if(ObjectUtils.isNullOrEmpty(company)){
			
		}
		
		int u=companyDao.update(company);
		if(logger.isDebugEnabled()){
			logger.debug("CompanyServiceBean.update(Company company) end-->", u);
		}
		return u;
	}
	 /**  
     *   
     * <find one by id>  
     * @param id  
     * @return Company
     */ 
	public Company get(Integer id){
		if(logger.isDebugEnabled()){
			logger.debug("CompanyServiceBean.get(Integer id) start-->", id);
		}
		if(ObjectUtils.isNullOrEmpty(id)){
			
		}
		
		Company company=companyDao.get(id);
		if(logger.isDebugEnabled()){
			logger.debug("CompanyServiceBean.get(Integer id) end-->", company);
		}
		return company;
	}
	/**  
     * <query all>  
     * @param paramMap  
     * @param pageBounds
     * @return List<Company>
     */ 
	public List<Company> search(Map paramMap ,PageBounds pageBounds){
		if(logger.isDebugEnabled()){
			logger.debug("CompanyServiceBean.search(Map paramMap ,PageBounds pageBounds) start-->", paramMap);
		}
		if(ObjectUtils.isNullOrEmpty(paramMap)){
			
		}
		
		List<Company> companyList=companyDao.search(paramMap,pageBounds);
		if(logger.isDebugEnabled()){
			logger.debug("CompanyServiceBean.search(Map paramMap ,PageBounds pageBounds) end-->", companyList);
		}
		return companyList;
	}
	
}