<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>

package ${basepackage}.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ${basepackage}.base.SpringBaseTest;
import ${basepackage}.dao.${className}Dao;
import org.springframework.util.Assert;

public class ${className}DaoTest extends SpringBaseTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(${className}DaoTest.class);

    @Autowired
    private ${className}Dao ${classNameLower}Dao;


    @org.junit.Test
    public void test${className}Dao() throws Exception {
        Assert.notNull(${classNameLower}Dao);
        Assert.isTrue(${classNameLower}Dao instanceof ${className}Dao);
		LOGGER.info("=${className}DaoTest.test${className}Dao=>${classNameLower}Dao Autowired",${className}Dao.class);

    }

}
