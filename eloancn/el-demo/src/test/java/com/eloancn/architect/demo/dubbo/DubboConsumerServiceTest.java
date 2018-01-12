package com.eloancn.architect.demo.dubbo;

import com.eloancn.architect.base.SpringBaseTest;
import com.eloancn.architect.dto.NameDto;
import com.eloancn.framework.sevice.api.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * DubboConsumerServiceTest测试
 * Created by qinxf on 2017/11/6.
 */
public class DubboConsumerServiceTest extends SpringBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DubboConsumerServiceTest.class);

    @Autowired
    private DubboConsumerService dubboConsumerService;

    /** 后面报错，是因为dubbo服务关闭和zk客户端关闭
     *  对于junit来说算是个bug，关闭顺序错误导致的异常情况 */
    @org.junit.Test
    public void testDubboService() throws Exception {
        LOGGER.info("=DubboConsumerServiceTest.testLoginController=>start at time[{}]", System.currentTimeMillis());
        ResultDTO<NameDto> result = dubboConsumerService.getNameById(1);
        Assert.isTrue(result != null);
        Assert.isTrue(result.getData() != null);
        Assert.isTrue(result.getData().getId() == 1);
        LOGGER.info("=DubboConsumerServiceTest.testLoginController=>end at time[{}]", System.currentTimeMillis());
    }
}
