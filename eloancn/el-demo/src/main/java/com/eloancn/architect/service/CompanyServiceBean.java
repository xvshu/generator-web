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
import com.eloancn.architect.model.Company;
import com.eloancn.architect.dao.CompanyDao;
import java.util.List;
import java.util.Map;
import java.util.Date;
/**
 * <>
 * @author qinxf
 * @version 1.0
 * @Time 2018-01-18 15:49:19
 */

@Service
public class CompanyServiceBean extends BaseService<Company>{
	
	private static final Logger logger = LoggerFactory.getLogger(CompanyServiceBean.class);
	
	private CompanyDao companyDao;
	
	@Autowired
	public void setCompanyDao(CompanyDao companyDao) {
		this.companyDao = companyDao;
	}

}