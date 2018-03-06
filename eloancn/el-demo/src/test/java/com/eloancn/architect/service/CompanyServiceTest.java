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
import com.eloancn.architect.service.CompanyServiceBean;
import org.springframework.util.Assert;

public class CompanyServiceTest extends SpringBaseTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceTest.class);


    @Autowired
    private CompanyServiceBean companyService;


    @org.junit.Test
    public void testCompanyService() throws Exception {
        Assert.notNull(companyService);
        Assert.isTrue(companyService instanceof CompanyServiceBean);
		LOGGER.info("=CompanyServiceTest.testCompanyService=>companyService Autowired",CompanyServiceBean.class);

    }

}
