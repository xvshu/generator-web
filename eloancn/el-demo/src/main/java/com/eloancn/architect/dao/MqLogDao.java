/*
 * Powered By [eloancn-generator]
 * Author:qinxf
 * Since 2017 - 2018
 */
 
package com.eloancn.architect.dao;

import com.eloancn.framework.orm.base.dao.IBaseDao;
import com.eloancn.framework.orm.mybatis.MyBatisRepository;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageBounds;

import com.eloancn.architect.model.MqLog;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageList;
import java.util.List;
import java.util.Map;
import java.util.Date;
/**
 * <>
 * @author qinxf
 * @version 1.0
 * @Time 2018-01-18 15:49:20
 */

@MyBatisRepository
public interface MqLogDao extends IBaseDao<MqLog>{

}
