package com.eloancn.framework.transaction;

import java.util.ArrayList;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.support.DefaultTransactionStatus;

public class DataSourceTransactionManager extends org.springframework.jdbc.datasource.DataSourceTransactionManager implements ApplicationContextAware{

    private ApplicationContext applicationContext;
    
	private RedisTemplate<String,Object> redisTemplate;
	
	
	public void setRedisTemplate(RedisTemplate<String,Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String PUBLISH_COMMIT = "DataSourceTransactionManager_publish_commit";
	public static final String PUBLISH_COMMITED = "DataSourceTransactionManager_publish_commited";
	
	@Override
	protected Object doGetTransaction() {
		CurrentTransactionThreadlLocal.set(true);
		return super.doGetTransaction();
	}

	@Override
	protected void doCleanupAfterCompletion(Object transaction) {
		super.doCleanupAfterCompletion(transaction);
		ArrayList<MessageBody> list = CurrentPublishThreadlLocal.get();
		if(null!=list&&list.size()>0){
			for(MessageBody body:list){
				redisTemplate.boundHashOps(PUBLISH_COMMIT).put(body.getMessageId(), body);
				this.applicationContext.publishEvent(new MessageBodyEvent(body));
			}
			CurrentPublishThreadlLocal.remove();
		}
		CurrentTransactionThreadlLocal.remove();
	}

	@Override
	protected void doRollback(DefaultTransactionStatus status) {
		super.doRollback(status);
		ArrayList<MessageBody> list = CurrentPublishThreadlLocal.get();
		if(null!=list&&list.size()>0){
			CurrentPublishThreadlLocal.remove();
		}
		CurrentTransactionThreadlLocal.remove();
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	
}
