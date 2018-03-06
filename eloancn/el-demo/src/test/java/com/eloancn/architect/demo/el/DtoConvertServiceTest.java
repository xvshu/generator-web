package com.eloancn.architect.demo.el;

import com.eloancn.architect.base.SpringBaseTest;
import com.eloancn.architect.dao.NameDao;
import com.eloancn.architect.demo.orm.PageNameService;
import com.eloancn.architect.dto.NameDto;
import com.eloancn.architect.model.Name;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DtoConvertServiceTest测试
 * Created by qinxf on 2017/11/6.
 */
public class DtoConvertServiceTest extends SpringBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DtoConvertServiceTest.class);

    @Autowired
    private NameDao nameDao;
    @Autowired
    private PageNameService pageService;
    @Autowired
    private DtoConvertService dtoConvert;

    @org.junit.Test
    public void testConvertDto() throws Exception {
        LOGGER.info("=DtoConvertServiceTest.testConvertDto=>start at time[{}]", System.currentTimeMillis());
        Name name = nameDao.get(1L);
        NameDto dto = dtoConvert.convert(name);
        Assert.isTrue(dto != null);
        Assert.isTrue(dto.getId() == 1);
        LOGGER.info("=DtoConvertServiceTest.testConvertDto=>end at time[{}]", System.currentTimeMillis());
    }

    @org.junit.Test
    public void testConvertList() throws Exception {
        LOGGER.info("=DtoConvertServiceTest.testConvertList=>start at time[{}]", System.currentTimeMillis());
        //查询参数
        Map param = new HashMap();
        param.put("createDateBegin", "2017-11-03 18:00:00");
        PageList pageList = pageService.searchPage(param, 1, 10);
        List<NameDto> list = dtoConvert.convert(pageList);
        Assert.isTrue(list != null);
        Assert.isTrue(list.size() == 10);
        Assert.isTrue(list.get(0).getId() > 0);
        LOGGER.info("=DtoConvertServiceTest.testConvertList=>end at time[{}]", System.currentTimeMillis());
    }
}
