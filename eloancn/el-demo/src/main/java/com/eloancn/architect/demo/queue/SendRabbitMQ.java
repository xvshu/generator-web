package com.eloancn.architect.demo.queue;

import com.eloancn.architect.constant.Constants;
import com.eloancn.architect.dao.NameDao;
import com.eloancn.architect.model.Name;
import com.eloancn.framework.queue.AmqMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * mq生产者
 * Created by qinxf on 2017/11/1.
 */

@Service
public class SendRabbitMQ {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendRabbitMQ.class);

    @Autowired
    private NameDao nameDao;

    @Autowired
    private AmqMessageService messageService;

    /**
     * 根据nameId查询Name对象
     * 并将Name对象发送到MQ对列
     * @param nameId
     */
    public void send(Integer nameId){
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=SendRabbitMQ.send=>nameId[{}], start!", nameId);
        }

        if(nameId == null || nameId == 0){
            LOGGER.error("=SendRabbitMQ.send=>error, name id is null!");
        }
        Name name = nameDao.get(nameId);
        messageService.send(Constants.RABBITMQ_EXCHANGE, Constants.RABBITMQ_ROUTEKEY, name);

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=SendRabbitMQ.send=>nameId[{}], end!", nameId);
        }
    }
}
