/*
 * Powered By [eloancn-generator]
 * Author:qinxf
 * Since 2017 - 2018
 */
 
package com.eloancn.architect.dao;

import com.eloancn.architect.model.Name;
import com.eloancn.framework.orm.base.dao.IBaseDao;
import com.eloancn.framework.orm.mybatis.MyBatisRepository;

import java.util.Map;

/**
 * <>
 * @author qinxf
 * @version 1.0
 * @Time 2018-01-18 15:49:21
 */

@MyBatisRepository
public interface NameDao extends IBaseDao<Name>{

    /**
     * <query all>
     * @param paramMap
     * @return List<Name>
     */
    int searchcount(Map paramMap );
}
