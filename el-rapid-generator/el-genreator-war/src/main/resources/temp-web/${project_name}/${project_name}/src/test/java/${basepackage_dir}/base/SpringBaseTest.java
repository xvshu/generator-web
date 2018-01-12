//
// Powered By [rapid-generator-framework]
// (By:qinxf)
//

package ${basepackage}.base;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {"classpath:spring/applicationContext*.xml"})
public class SpringBaseTest extends AbstractJUnit4SpringContextTests {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBaseTest.class);

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

	@org.junit.Test
    public void test() throws Exception {
        LOGGER.info("=SpringBaseTest.test=>test!");

    }
	
}
