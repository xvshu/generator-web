package com.eloancn.architect.demo.queue;

import com.eloancn.architect.model.Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * RabbitMQ消费者
 * Created by qinxf on 2017/11/1.
 */

@Service
public class RabbitMQListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQListener.class);

    /**
     * RabbitMQ消费者从队列获取Name对象
     * @param nameOutput
     */
    public void listener(Name nameOutput){
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=RabbitMQListener.listener=>Name[{}], start!", nameOutput.toString());
        }
        if(nameOutput == null){
            LOGGER.error("=RabbitMQListener.listener=>name is null!");
        }else {
            LOGGER.info("=RabbitMQListener.listener=>id[{}],name[{}]", nameOutput.getId(), nameOutput.getName());

        }
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=RabbitMQListener.listener=>Name[{}], end!", nameOutput.toString());
        }
    }
}
