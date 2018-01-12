/*
 * Powered By [crazy-framework]
 * Web Site: http://www.eloan.com
 * Since 2015 - 2017
 */

package com.eloancn.architect.dao;

import com.eloancn.framework.orm.mybatis.MyBatisRepository;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageBounds;

import com.eloancn.architect.model.Company;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageList;

import java.util.List;
import java.util.Map;
import java.util.Date;
/**
 * <>
 * @author crazy
 * @version 1.0
 * @Time 2017-11-03 17:48:53
 */

@MyBatisRepository
public interface CompanyDao{
	/**  
     *   
     * <save one>  
     * @param company  
     * @return Integer
     */ 
	int insert(Company company);
	 /**  
     *   
     * <update one>  
     * @param company  
     * @throws DAOException  
     */ 
	int update(Company company);
	 /**  
     *   
     * <find one by id>  
     * @param id  
     * @return Company
     */ 
	Company get(Integer id);
	/**  
     *   
     * <delete one by id>  
     * @param id  
     * @return int
     */ 
	int delete(Integer id);
	/**  
     * <query all>  
     * @param paramMap  
     * @param pageBounds
     * @return List<Company>
     */ 
	PageList<Company> search(Map paramMap , PageBounds pageBounds);
	

}
