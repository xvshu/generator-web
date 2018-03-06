/*
 * Powered By [eloancn-generator]
 * Author:qinxf
 * Since 2017 - 2018
 */
 

package com.eloancn.architect.service;

import com.eloancn.architect.model.Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.eloancn.architect.base.SpringBaseTest;
import com.eloancn.architect.service.NameServiceBean;
import org.springframework.util.Assert;

import java.util.*;

public class NameServiceTest extends SpringBaseTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NameServiceTest.class);


    @Autowired
    private NameServiceBean nameService;



    @org.junit.Test
    public void testNameService() throws Exception {
        Assert.notNull(nameService);
        Assert.isTrue(nameService instanceof NameServiceBean);
		LOGGER.info("=NameServiceTest.testNameService=>nameService Autowired",NameServiceBean.class);

    }

    @org.junit.Test
    public void testInsertBatch() throws Exception {
        Name n1 = new Name();
        n1.setName("zhangsan");
        Name n2 = new Name();
        n2.setName("lisi");

        List<Name> list = new ArrayList<>();
        list.add(n1);
        list.add(n2);
        int i = nameService.insertBatch(list);
        System.out.println(i);
    }



    @org.junit.Test
    public void testUpdateBatch() throws Exception {
        Map map = new HashMap();
        map.put("name","zhangsan");
        List<Name> names = nameService.list(map);
        for (Name name :names) {
            name.setSex(Double.valueOf(1));
            name.setCreateDate(new Date());
            name.setUpdateDate(new Date());
        }

        int i = nameService.updateBatch(names);
        System.out.println(i);
    }
}
