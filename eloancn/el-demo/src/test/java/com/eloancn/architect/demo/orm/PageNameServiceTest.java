package com.eloancn.architect.demo.orm;

import com.eloancn.architect.base.SpringBaseTest;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageList;
import com.eloancn.framework.orm.mybatis.paginator.domain.Paginator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PageNameServiceTest
 * Created by qinxf on 2017/11/6.
 */
public class PageNameServiceTest extends SpringBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageNameServiceTest.class);

    @Autowired
    private PageNameService pageService;

    @org.junit.Test
    public void testPageService() throws Exception {
        LOGGER.info("=PageNameServiceTest.testPageService=>start at time[{}]", System.currentTimeMillis());
        //查询参数
        Map param = new HashMap();
        param.put("createDateBegin", "2017-11-03 18:00:00");
        PageList pageList = pageService.searchPage(param, 1, 10);
        Paginator paginator = pageList.getPaginator();
        Assert.isTrue(pageList != null);
        Assert.isTrue(pageList.getPaginator().getPage() == 1);
        Assert.isTrue(pageList.getPaginator().getLimit() == 10);
        Assert.isTrue(pageList.getPaginator().getTotalCount() == 11);
        Assert.isTrue(pageList.getPaginator().getTotalPages() == 2);
        LOGGER.info("=PageNameServiceTest.testPageService=>end at time[{}]", System.currentTimeMillis());
    }

    @org.junit.Test
    public void testSearchNoPage() throws Exception {
        LOGGER.info("=PageNameServiceTest.testSearchNoPage=>start at time[{}]", System.currentTimeMillis());
        //查询参数
        Map param = new HashMap();
        param.put("createDateBegin", "2017-11-03 18:00:00");
        PageList pageList = pageService.searchNoPage(param);
        Assert.isTrue(pageList != null);
        LOGGER.info("=PageNameServiceTest.testSearchNoPage=>end at time[{}]", System.currentTimeMillis());
    }

    @org.junit.Test
    public void testList() throws Exception {
        LOGGER.info("=PageNameServiceTest.testList=>start at time[{}]", System.currentTimeMillis());
        //查询参数
        Map param = new HashMap();
        param.put("createDateBegin", "2017-11-03 18:00:00");
        List list = pageService.list(param);
        Assert.isTrue(list != null);
        LOGGER.info("=PageNameServiceTest.testList=>end at time[{}]", System.currentTimeMillis());
    }
}
