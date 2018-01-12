package com.eloancn.architect.demo.redis;

import com.eloancn.architect.dao.NameDao;
import com.eloancn.architect.model.Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * redis使用模板
 * Created by qinxf on 2017/11/02.
 */

@Service
public class RedisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisService.class);

    //string操作类
    @Resource(name = "redisTemplate")
    private ValueOperations valueOperations;

    @Autowired
    private NameDao nameDao;

    /**
     * redis保存key
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     */
    public void setValue(String key, String value, long timeout, TimeUnit timeUnit){
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=RedisService.setValue=>key[{}],value[{}],timeout[{}],timeUnit[{}] start!", key, value, timeout, timeUnit);
        }
        if(StringUtils.isEmpty(key) || StringUtils.isEmpty(value)){
            LOGGER.error("=RedisService.setValue=>error: key or value is null!");
        }else {
            valueOperations.set(key, value, timeout, timeUnit);
        }
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=RedisService.setValue=>key[{}],value[{}],timeout[{}],timeUnit[{}] end!", key, value, timeout, timeUnit);
        }
    }

    /**
     * 从redis获取key的值
     * @param key
     * @return
     */
    public String getValue(String key){
        String result;
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=RedisService.getValue=>key[{}] start!", key);
        }
        if(StringUtils.isEmpty(key)){
            LOGGER.error("=RedisService.getValue=>error: key is null!");
            result = null;
        }else {
            result = valueOperations.get(key).toString();
        }
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=RedisService.getValue=>key[{}] end!", key);
        }
        return result;
    }

    /**
     * 删除redis的key
     * @param key
     */
    public void delValue(String key){
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=RedisService.delValue=>key[{}] start!", key);
        }
        if(StringUtils.isEmpty(key)){
            LOGGER.error("=RedisService.delValue=>error: key is null!");
        }else {
            valueOperations.getOperations().delete(key);
        }
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("=RedisService.delValue=>key[{}] end!", key);
        }
    }

    //根据方法的请求参数对其结果进行缓存
    @Cacheable(value = "Name", key = "'id_'+#id")
    public Name get(Integer id){
        LOGGER.info("=RedisService.get=>get Name id[{}] one time!", id);
        return nameDao.get(id);
    }

    //根据方法的请求参数对其结果进行缓存，和 @Cacheable 不同的是，它每次都会触发真实方法的调用
    @CachePut(value = "Name",key = "'id_'+#name.id")
    public Name insert(Name name){
        int flag = nameDao.insert(name);
        if(flag == 1){
            return name;
        }
        return null;
    }

    //根据一定的条件对缓存进行清空
    @CacheEvict(value = "Name", key = "'id_'+#id")
    public int delete(Integer id){
        return nameDao.delete(id);
    }

}
