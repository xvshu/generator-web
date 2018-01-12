/*
 * Powered By [crazy-framework]
 * Web Site: http://www.eloan.com
 * Since 2015 - 2017
 */

package com.eloancn.architect.service;

import com.eloancn.architect.dao.NameDao;
import com.eloancn.architect.model.Name;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageBounds;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageList;
import com.eloancn.framework.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
/**
 * <>
 * @author crazy
 * @version 1.0
 * @Time 2017-11-03 17:48:52
 */

@Service
public class NameServiceBean{
	
	private static final Logger logger = LoggerFactory.getLogger(NameServiceBean.class);
	
	private NameDao nameDao;
	
	@Autowired
	public void setNameDao(NameDao nameDao) {
		this.nameDao = nameDao;
	}
	/**  
     *   
     * <save one>  
     * @param name  
     * @return Integer
     */ 
	public void insert(Name name){
		if(logger.isDebugEnabled()){
			logger.debug("NameServiceBean.insert(Name name) start-->", name);
		}
		if(ObjectUtils.isNullOrEmpty(name)){
			
		}
		
		int i=nameDao.insert(name);
		if(logger.isDebugEnabled()){
			logger.debug("NameServiceBean.insert(Name name) end-->", i);
		}
	}
	 /**  
     *   
     * <update one>  
     * @param name  
     * @throws DAOException  
     */ 
	public int update(Name name){
		if(logger.isDebugEnabled()){
			logger.debug("NameServiceBean.update(Name name) start-->", name);
		}
		if(ObjectUtils.isNullOrEmpty(name)){
			
		}
		
		int u=nameDao.update(name);
		if(logger.isDebugEnabled()){
			logger.debug("NameServiceBean.update(Name name) end-->", u);
		}
		return u;
	}
	 /**  
     *   
     * <find one by id>  
     * @param id  
     * @return Name
     */ 
	public Name get(Integer id){
		if(logger.isDebugEnabled()){
			logger.debug("NameServiceBean.get(Integer id) start-->", id);
		}
		if(ObjectUtils.isNullOrEmpty(id)){
			
		}
		
		Name name=nameDao.get(id);
		if(logger.isDebugEnabled()){
			logger.debug("NameServiceBean.get(Integer id) end-->", name);
		}
		return name;
	}
	/**  
     * <query all>  
     * @param paramMap  
     * @param pageBounds
     * @return List<Name>
     */ 
	public PageList<Name> search(Map paramMap , PageBounds pageBounds){
		if(logger.isDebugEnabled()){
			logger.debug("NameServiceBean.search(Map paramMap ,PageBounds pageBounds) start-->", paramMap);
		}
		if(ObjectUtils.isNullOrEmpty(paramMap)){
			
		}

		PageList<Name> nameList=nameDao.search(paramMap,pageBounds);
		if(logger.isDebugEnabled()){
			logger.debug("NameServiceBean.search(Map paramMap ,PageBounds pageBounds) end-->", nameList);
		}
		return nameList;
	}

	/**
	 * <query all>
	 * @param paramMap
	 * @return List<Name>
	 */
	public int searchCount(Map paramMap ){
		if(logger.isDebugEnabled()){
			logger.debug("NameServiceBean.searchCount(Map paramMap ) start-->", paramMap);
		}
		if(ObjectUtils.isNullOrEmpty(paramMap)){

		}

		int result =nameDao.searchcount(paramMap);
		if(logger.isDebugEnabled()){
			logger.debug("NameServiceBean.searchCount(Map paramMap ) end-->", result);
		}
		return result;
	}

	/**
	 * <query all>
	 * @param id
	 * @return List<Name>
	 */
	public int delete(int id){
		if(logger.isDebugEnabled()){
			logger.debug("NameServiceBean.delete(int id ) start-->id:{}", id);
		}
		if(ObjectUtils.isNullOrEmpty(id)){

		}

		int result =nameDao.delete(id);
		if(logger.isDebugEnabled()){
			logger.debug("NameServiceBean.delete(int id ) end-->result:{}", result);
		}
		return result;
	}
	
}