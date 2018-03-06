/*
 * Powered By [eloancn-generator]
 * Author:qinxf
 * Since 2017 - 2018
 */
 
package com.eloancn.architect.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageBounds;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageList;
import com.eloancn.framework.utils.ObjectUtils;

import com.eloancn.framework.orm.base.service.BaseService;
import com.eloancn.architect.model.Name;
import com.eloancn.architect.dao.NameDao;
import java.util.List;
import java.util.Map;
import java.util.Date;
/**
 * <>
 * @author qinxf
 * @version 1.0
 * @Time 2018-01-18 15:49:21
 */

@Service
public class NameServiceBean extends BaseService<Name>{
	
	private static final Logger logger = LoggerFactory.getLogger(NameServiceBean.class);
	
	private NameDao nameDao;
	
	@Autowired
	public void setNameDao(NameDao nameDao) {
		this.nameDao = nameDao;
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
}