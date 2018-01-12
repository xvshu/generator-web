<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>

package ${basepackage}.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ${basepackage}.base.SpringBaseTest;
import ${basepackage}.service.${className}ServiceBean;
import org.springframework.util.Assert;

public class ${className}ServiceTest extends SpringBaseTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(${className}ServiceTest.class);


    @Autowired
    private ${className}ServiceBean ${classNameLower}Service;


    @org.junit.Test
    public void test${className}Service() throws Exception {
        Assert.notNull(${classNameLower}Service);
        Assert.isTrue(${classNameLower}Service instanceof ${className}ServiceBean);
		LOGGER.info("=${className}ServiceTest.test${className}Service=>${classNameLower}Service Autowired",${className}ServiceBean.class);

    }

}
