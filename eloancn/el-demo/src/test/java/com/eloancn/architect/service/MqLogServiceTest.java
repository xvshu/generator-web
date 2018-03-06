/*
 * Powered By [eloancn-generator]
 * Author:qinxf
 * Since 2017 - 2018
 */
 

package com.eloancn.architect.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.eloancn.architect.base.SpringBaseTest;
import com.eloancn.architect.service.MqLogServiceBean;
import org.springframework.util.Assert;

public class MqLogServiceTest extends SpringBaseTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MqLogServiceTest.class);


    @Autowired
    private MqLogServiceBean mqLogService;


    @org.junit.Test
    public void testMqLogService() throws Exception {
        Assert.notNull(mqLogService);
        Assert.isTrue(mqLogService instanceof MqLogServiceBean);
		LOGGER.info("=MqLogServiceTest.testMqLogService=>mqLogService Autowired",MqLogServiceBean.class);

    }

}
