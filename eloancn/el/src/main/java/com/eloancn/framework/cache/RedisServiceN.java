package com.eloancn.framework.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 封装redis 缓存服务器服务接口  
 * 
 * @author jia
 * 
 * 2012-12-16 上午3:09:18
 */
@Slf4j
public class RedisServiceN {

    private RedisNameSpace redisNameSpace;

    private RedisTemplate<String,Object> redisTemplate;

	public RedisServiceN(  RedisTemplate<String, Object> redisTemplate) {

		this.redisTemplate = redisTemplate;
	}


	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public Boolean expire(String key, final long timeout, final TimeUnit unit) {
		return this.redisTemplate.expire(nameSpace()+key, timeout, unit);
	}
	public Boolean expireAt(String key, final Date date) {
		key=nameSpace()+key;
		log.info("key:{} date:{}",key,date);
		return this.redisTemplate.expireAt(key, date);
	}
    public void setObject(String key,Object value){
		key=nameSpace()+key;
		log.info("key:{} value:{}",key,value);
    	this.redisTemplate.boundValueOps(key).set(value);
    }
    public void setObject(String key,Object value,long timeout, TimeUnit unit){
		key=nameSpace()+key;
		log.info("key:{} value:{} timeout:{},T:{}",key,value,timeout,unit);
    	this.redisTemplate.boundValueOps(key).set(value, timeout, unit);
    }
    public Object getObject(String key){
		key=nameSpace()+key;
    	return this.redisTemplate.boundValueOps(key).get();
    }

    public Long increment(String key,long delta){
		key=nameSpace()+key;
    	log.info("key:{},delta:{}",key,delta);
    	return this.redisTemplate.boundValueOps(key).increment(delta);
    }
    public Double increment(String key,double delta){
		key=nameSpace()+key;
		log.info("key:{},delta:{}",key,delta);
    	return this.redisTemplate.boundValueOps(key).increment(delta);
    }
    public void setMap(String key, Map value){
		key=nameSpace()+key;
		log.info("key:{},mapSize:{}",key,value!=null?value.size():null);
    	this.redisTemplate.boundHashOps(key).putAll(value);
    }
    public void setMap(String key, Object field,Object value){
        key=nameSpace()+key;
        log.info("key:{},field:{},value:{}",key,field,value);
        this.redisTemplate.boundHashOps(key).put(field,value);
    }
    public void setMap(String key, Map value,long timeout, TimeUnit unit){
		key=nameSpace()+key;
		log.info("key:{},mapSize:{},timeout:{},t:{}",key,value!=null?value.size():null,timeout,unit);
    	this.redisTemplate.boundHashOps(key).putAll(value);
    	this.redisTemplate.boundHashOps(key).expire(timeout, unit);
    }
    public Map getMap(String key){
		key=nameSpace()+key;
    	return this.redisTemplate.boundHashOps(key).entries();
    }
    public void setSet(String key,Set value){
		key=nameSpace()+key;
		log.info("key:{},setSize:{}",key,value!=null?value.size():null);
    	this.redisTemplate.boundSetOps(key).add(value);
    }
    public void setSet(String key,Set value,long timeout, TimeUnit unit){
		key=nameSpace()+key;
		log.info("key:{},mapSize:{},timeout:{},t:{}",key,value!=null?value.size():null,timeout,unit);
    	this.redisTemplate.boundSetOps(key).add(value);
    	this.redisTemplate.boundSetOps(key).expire(timeout, unit);
    }
    public Set getSet(String key){
		key=nameSpace()+key;
    	return this.redisTemplate.boundSetOps(key).members();
    }
    public void setList(String key,List value){
		key=nameSpace()+key;
		log.info("key:{},listSize:{}",key,value!=null?value.size():null);
    	this.redisTemplate.boundListOps(key).leftPushAll(value);
    }
    public void setList(String key,List value,long timeout, TimeUnit unit){
		key=nameSpace()+key;
		log.info("key:{},listSize:{},timeout:{},t:{}",key,value!=null?value.size():null,timeout,unit);
    	this.redisTemplate.boundListOps(key).leftPushAll(value);
    	this.redisTemplate.boundListOps(key).expire(timeout, unit);
    }
    public List getList(String key){
		key=nameSpace()+key;
    	return this.redisTemplate.boundListOps(key).range(0, this.redisTemplate.boundListOps(key).size());
    }
    public void del(String key){
		key=nameSpace()+key;
		log.info("key:{}",key);
    	this.redisTemplate.delete(key);
    }
    public boolean exists(String key){
		key=nameSpace()+key;
    	final byte[] keys =this.redisTemplate.getStringSerializer().serialize(key);
    	return this.redisTemplate.execute(new RedisCallback<Boolean>() {

			public Boolean doInRedis(RedisConnection connection) {				
				return connection.exists(keys);
			}
		}, true);
    }

    private String nameSpace(){
    	return "";
//		return redisNameSpace==null?"":redisNameSpace.toString();
	}


}