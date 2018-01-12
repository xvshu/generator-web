/*
 * Powered By [crazy-framework]
 * Web Site: http://www.eloan.com
 * Since 2015 - 2017
 */

package com.eloancn.architect.dao;

import com.eloancn.framework.orm.mybatis.MyBatisRepository;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageBounds;

import com.eloancn.architect.model.Name;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageList;

import java.util.List;
import java.util.Map;
import java.util.Date;
/**
 * <>
 * @author crazy
 * @version 1.0
 * @Time 2017-11-03 17:48:52
 */

@MyBatisRepository
public interface NameDao{
	/**  
     *   
     * <save one>  
     * @param name  
     * @return Integer
     */ 
	int insert(Name name);
	 /**  
     *   
     * <update one>  
     * @param name  
     * @throws DAOException  
     */ 
	int update(Name name);
	 /**  
     *   
     * <find one by id>  
     * @param id  
     * @return Name
     */ 
	Name get(Integer id);
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
     * @return List<Name>
     */ 
	PageList<Name> search(Map paramMap , PageBounds pageBounds);

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
	List<Name> list(Map paramMap);
}
