package com.eloancn.architect.demo.queue;

import com.eloancn.architect.base.SpringBaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * SendRabbitMQTest测试
 * Created by qinxf on 2017/11/1.
 */
public class SendRabbitMQTest extends SpringBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendRabbitMQTest.class);

    @Autowired
    private SendRabbitMQ sendRabbitMQ;


    @org.junit.Test
    public void testSendRabbitMQ() throws Exception {
        LOGGER.info("=SendRabbitMQTest.testSendRabbitMQ=>start at time[{}]", System.currentTimeMillis());
        sendRabbitMQ.send(1);
        //沉睡一会，等待消费
        Thread.sleep(5000);
        LOGGER.info("=SendRabbitMQTest.testSendRabbitMQ=>end at time[{}]", System.currentTimeMillis());
    }
}
