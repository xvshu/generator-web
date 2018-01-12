//
// Powered By [rapid-generator-framework]
// (By:qinxf)
//

package com.eloancn.architect.base;

import com.alibaba.druid.pool.DruidDataSource;
import com.eloancn.architect.base.InitDbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {"classpath:spring/applicationContext*.xml"})
public class SpringBaseTest extends AbstractJUnit4SpringContextTests {

    private static final String init_test_data = "/hsqldb/init-test-data.sql";
    private static final String del_test_data = "/hsqldb/del-test-data.sql";

    @Autowired
    private DruidDataSource dataSource;

    @org.junit.Before
    public void setUp() throws Exception {
        InitDbUtils.execute(dataSource,del_test_data);
        InitDbUtils.execute(dataSource,init_test_data);
    }

    @org.junit.After
    public void tearDown() throws Exception {
        InitDbUtils.execute(dataSource,del_test_data);
    }

}
