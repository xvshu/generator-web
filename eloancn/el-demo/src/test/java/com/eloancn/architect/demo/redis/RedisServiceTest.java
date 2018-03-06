package com.eloancn.architect.demo.redis;

import com.eloancn.architect.base.SpringBaseTest;
import com.eloancn.architect.model.Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * RedisServiceTest测试
 * Created by qinxf on 2017/11/2.
 */
public class RedisServiceTest extends SpringBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisServiceTest.class);

    @Autowired
    private RedisService redisService;

    @org.junit.Test
    public void testValueOperations() throws Exception {
        LOGGER.info("=RedisServiceTest.testValueOperations=>start at time[{}]", System.currentTimeMillis());
        //保存key-value
        redisService.setValue("key", "value", 100, TimeUnit.SECONDS);
        //获取key
        String value = redisService.getValue("key");
        Assert.isTrue(value != null);
        Assert.isTrue("value".equals(value));
        //删除key
        redisService.delValue("key");
        LOGGER.info("=RedisServiceTest.testValueOperations=>end at time[{}]", System.currentTimeMillis());
    }

    @org.junit.Test
    public void testCacheableInsertAndDelete() throws Exception {
        LOGGER.info("=RedisServiceTest.testCacheableInsertAndDelete=>start at time[{}]", System.currentTimeMillis());
        //insert
        Name name = this.getName();
        redisService.insert(name);
        LOGGER.info("=RedisServiceTest.testCacheableInsertAndDelete=>insert Name id[{}]", name.getId());
        Assert.isTrue(name.getId() == 12);

        //delete
        int deleteFlag = redisService.delete(Long.valueOf(name.getId()));
        LOGGER.info("=RedisServiceTest.testCacheableInsertAndDelete=>delete Name flag[{}]", deleteFlag);
        Assert.isTrue(deleteFlag == 1);

        LOGGER.info("=RedisServiceTest.testCacheableInsertAndDelete=>end at time[{}]", System.currentTimeMillis());
    }

    @org.junit.Test
    public void testCacheableGet() throws Exception {
        LOGGER.info("=RedisServiceTest.testCacheableGet=>start at time[{}]", System.currentTimeMillis());

        //get
        Name cacheName = redisService.get(1L);
        Assert.isTrue(cacheName != null);
        Assert.isTrue(cacheName.getId() == 1);

        //cache get
        LOGGER.info("=RedisServiceTest.testCacheableGet=>cache get Name start time[{}]", System.currentTimeMillis());
        redisService.get(1L);
        LOGGER.info("=RedisServiceTest.testCacheableGet=>cache get Name end time[{}]", System.currentTimeMillis());

        LOGGER.info("=RedisServiceTest.testCacheableGet=>end at time[{}]", System.currentTimeMillis());
    }

    /** 创建一个新的Name */
    private Name getName(){
        Name name = new Name();
        name.setId(12);
        name.setName("liushier");
        return name;
    }


}
