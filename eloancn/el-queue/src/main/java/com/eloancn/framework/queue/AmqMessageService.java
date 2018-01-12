package com.eloancn.framework.queue;

import java.lang.reflect.Field;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.data.redis.core.RedisTemplate;

import com.eloancn.framework.annotation.Encryption;
import com.eloancn.framework.cipher.DESUtils;
import com.eloancn.framework.transaction.CurrentPublishThreadlLocal;
import com.eloancn.framework.transaction.CurrentTransactionThreadlLocal;
import com.eloancn.framework.transaction.DataSourceTransactionManager;
import com.eloancn.framework.transaction.MessageBody;

public class AmqMessageService {

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());
	
	private String serialNoFieldName;
	
	private String appId;
	
	public final static String MQ_PREFIX = "mq:";

	private RabbitTemplate rabbitTemplate;
	
	private RedisTemplate<String,Object> redisTemplate;
	
	private AmpConnectionFactory ampConnectionFactory;
	
	private String virtualHost = "/";
	

	public void setRedisTemplate(RedisTemplate<String,Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	public void setSerialNoFieldName(String serialNoFieldName) {
		this.serialNoFieldName = serialNoFieldName;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}

	public void setAmpConnectionFactory(AmpConnectionFactory ampConnectionFactory) {
		this.ampConnectionFactory = ampConnectionFactory;
	}

	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	public void send(Object object) {
		send(object, null,null);
	}
	public void send(Object object,String virtualHost) {
		send(object, virtualHost,null);
	}
	public void send(Object object,String virtualHost,String errorService) {
		encryption(object); 
		try {
			final MessageBody body=new MessageBody();
			body.setErrorService(errorService);
			body.settId((String) MDC.get("teid"));
			body.setBody(object);
			body.setVirtualHost(virtualHost==null?this.virtualHost:virtualHost);
			Field exchange=rabbitTemplate.getClass().getDeclaredField("exchange");
			exchange.setAccessible(true);
			body.setExchange((String)exchange.get(rabbitTemplate));
			Field routingKey=rabbitTemplate.getClass().getDeclaredField("routingKey");
			routingKey.setAccessible(true);
			body.setRoutingKey((String)routingKey.get(rabbitTemplate));
			final String m_id=MQ_PREFIX+getMessageId(object,body.getExchange(),body.getRoutingKey());
			body.setMessageId(m_id);
			
			Boolean tran=CurrentTransactionThreadlLocal.get();
			if(null!=tran&&tran==true){
				CurrentPublishThreadlLocal.set(body);
			}else{
				try {
					if(null==rabbitTemplate){
						rabbitTemplate=new RabbitTemplate(ampConnectionFactory.getConnectionFactory(virtualHost==null?this.virtualHost:virtualHost));
						rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
					}
					if(null!=ampConnectionFactory){
						rabbitTemplate.setConnectionFactory(ampConnectionFactory.getConnectionFactory(virtualHost==null?this.virtualHost:virtualHost));
					}
					rabbitTemplate.convertAndSend(object, new MessagePostProcessor() {
						public Message postProcessMessage(
								Message message) throws AmqpException {
							message.getMessageProperties().setMessageId(m_id);
							message.getMessageProperties().getHeaders().put("exchange",body.getExchange());
							message.getMessageProperties().getHeaders().put("routingKey",body.getRoutingKey());
							message.getMessageProperties().getHeaders().put("virtualHost",body.getVirtualHost());
							// 增加teid参数传递
							message.getMessageProperties().getHeaders().put("teid",(String) MDC.get("teid"));
							message.getMessageProperties().getHeaders().put("port", PublishMessageListener.getPort());
							message.getMessageProperties().getHeaders().put("host", PublishMessageListener.getHost());
							if(logger.isInfoEnabled())
								logger.info("exchange:"+body.getExchange()+" routingKey:"+body.getRoutingKey()+" body:"+new String(message.getBody()));
							body.setBody(new String(message.getBody()));
							return message;
						}
					});
					redisTemplate.boundHashOps(DataSourceTransactionManager.PUBLISH_COMMITED).put(m_id, body);
				} catch (Exception e) {
					logger.error("send Message error\n", e);
					redisTemplate.boundSetOps(PublishMessageListener.CLIENT_SEND_ERROR).add(new ErrorMessage(m_id,PublishMessageListener.getHost(),PublishMessageListener.getPort(),body.getExchange(),body.getRoutingKey(),body.getVirtualHost(),PublishMessageListener.getStackTrace(e),body.getBody().toString()));
				}
			}
		} catch (Exception e) {
			logger.error("send Message error\n", e);
		} 
	}
	public void send(final String exchange,final String routingKey, Object object) {
		send(exchange,routingKey,object,null,null);
	}
	public void send(final String exchange,final String routingKey, Object object,String virtualHost) {
		send(exchange,routingKey,object,virtualHost,null);
	}
	public void send(final String exchange,final String routingKey, Object object,String virtualHost,String errorService) {
		encryption(object);
		try {
			final MessageBody body=new MessageBody();
			body.setBody(object);
			body.setExchange(exchange);
			body.setErrorService(errorService);
			body.setRoutingKey(routingKey);
			body.settId((String) MDC.get("teid"));
			body.setVirtualHost(virtualHost==null?this.virtualHost:virtualHost);
			final String m_id=MQ_PREFIX+getMessageId(object,body.getExchange(),body.getRoutingKey());
			body.setMessageId(m_id);
			
			Boolean tran=CurrentTransactionThreadlLocal.get();
			if(null!=tran&&tran==true){
				CurrentPublishThreadlLocal.set(body);
			}else{
				try {
					if(null==rabbitTemplate){
						rabbitTemplate=new RabbitTemplate(ampConnectionFactory.getConnectionFactory(virtualHost==null?this.virtualHost:virtualHost));
						rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
					}
					if(null!=ampConnectionFactory){
						rabbitTemplate.setConnectionFactory(ampConnectionFactory.getConnectionFactory(virtualHost==null?this.virtualHost:virtualHost));
					}
					rabbitTemplate.convertAndSend(exchange,routingKey,object, new MessagePostProcessor() {
						public Message postProcessMessage(
								Message message) throws AmqpException {
							message.getMessageProperties().setMessageId(m_id);
							message.getMessageProperties().getHeaders().put("exchange",body.getExchange());
							message.getMessageProperties().getHeaders().put("routingKey",body.getRoutingKey());
							message.getMessageProperties().getHeaders().put("port", PublishMessageListener.getPort());
							message.getMessageProperties().getHeaders().put("host", PublishMessageListener.getHost());
							message.getMessageProperties().getHeaders().put("virtualHost",body.getVirtualHost());
							// 增加teid参数传递
							message.getMessageProperties().getHeaders().put("teid",(String) MDC.get("teid"));
							if(logger.isInfoEnabled())
								logger.info("exchange:"+body.getExchange()+" routingKey:"+body.getRoutingKey()+" body:"+new String(message.getBody()));
							body.setBody(new String(message.getBody()));
							return message;
						}
					});
					redisTemplate.boundHashOps(DataSourceTransactionManager.PUBLISH_COMMITED).put(m_id, body);
				} catch (Exception e) {
					logger.error("send Message error\n", e);
					redisTemplate.boundSetOps(PublishMessageListener.CLIENT_SEND_ERROR).add(new ErrorMessage(m_id,PublishMessageListener.getHost(),PublishMessageListener.getPort(),body.getExchange(),body.getRoutingKey(),body.getVirtualHost(),PublishMessageListener.getStackTrace(e),body.getBody().toString()));
				}
				
			}
		} catch (Exception e) {
			logger.error("send Message error\n", e);
		}
		
	}
	public void send(final String routingKey, Object object) {
		send(routingKey, object,null,null);
	}
	public void send(final String routingKey, Object object,String virtualHost) {
		send(routingKey, object,virtualHost,null);
	}
	public void send(final String routingKey, Object object,String virtualHost,String errorService) {
		encryption(object);
		try {
			final MessageBody body=new MessageBody();
			body.setBody(object);
			body.setErrorService(errorService);
			body.settId((String) MDC.get("teid"));
			body.setRoutingKey(routingKey);
			body.setVirtualHost(virtualHost==null?this.virtualHost:virtualHost);
			Field exchange=rabbitTemplate.getClass().getDeclaredField("exchange");
			exchange.setAccessible(true);
			body.setExchange((String)exchange.get(rabbitTemplate));
			final String m_id=MQ_PREFIX+getMessageId(object,body.getExchange(),body.getRoutingKey());
			body.setMessageId(m_id);
			
			Boolean tran=CurrentTransactionThreadlLocal.get();
			if(null!=tran&&tran==true){
				CurrentPublishThreadlLocal.set(body);
			}else{
				try {
					if(null==rabbitTemplate){
						rabbitTemplate=new RabbitTemplate(ampConnectionFactory.getConnectionFactory(virtualHost==null?this.virtualHost:virtualHost));
						rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
					}
					if(null!=ampConnectionFactory){
						rabbitTemplate.setConnectionFactory(ampConnectionFactory.getConnectionFactory(virtualHost==null?this.virtualHost:virtualHost));
					}
					rabbitTemplate.convertAndSend(routingKey,object, new MessagePostProcessor() {
						public Message postProcessMessage(
								Message message) throws AmqpException {
							message.getMessageProperties().setMessageId(m_id);
							message.getMessageProperties().getHeaders().put("exchange",body.getExchange());
							message.getMessageProperties().getHeaders().put("routingKey",body.getRoutingKey());
							message.getMessageProperties().getHeaders().put("virtualHost",body.getVirtualHost());
							// 增加teid参数传递
							message.getMessageProperties().getHeaders().put("teid",(String) MDC.get("teid"));
							message.getMessageProperties().getHeaders().put("port", PublishMessageListener.getPort());
							message.getMessageProperties().getHeaders().put("host", PublishMessageListener.getHost());
							if(logger.isInfoEnabled())
								logger.info("exchange:"+body.getExchange()+" routingKey:"+body.getRoutingKey()+" body:"+new String(message.getBody()));
							body.setBody(new String(message.getBody()));
							return message;
						}
					});
					redisTemplate.boundHashOps(DataSourceTransactionManager.PUBLISH_COMMITED).put(m_id, body);
				} catch (Exception e) {
					logger.error("send Message error\n", e);
					redisTemplate.boundSetOps(PublishMessageListener.CLIENT_SEND_ERROR).add(new ErrorMessage(m_id,PublishMessageListener.getHost(),PublishMessageListener.getPort(),body.getExchange(),body.getRoutingKey(),body.getVirtualHost(),PublishMessageListener.getStackTrace(e),body.getBody().toString()));
				}
			}
		} catch (Exception e) {
			logger.error("send Message error\n", e);
		} 
	}

	private String getMessageId(Object object,String exchange,String routingKey){
		String serialNo = AmqServiceMessageListener.getSerialNo(object,
				this.serialNoFieldName);
		if(null==serialNo){
			serialNo = UUID.randomUUID().toString();
		}
		StringBuffer sb = new StringBuffer(serialNo);
		sb.append("_");
		sb.append(exchange);
		sb.append("_");
		sb.append(routingKey);
		return sb.toString();
	}
	private void encryption(Object o) {
		Class<?> c = o.getClass();
		Field fs[]=c.getDeclaredFields();
		for (Field f:fs) {
			try {
				Encryption e=f.getAnnotation(Encryption.class);
				if(null!=e){
					f.setAccessible(true);
					String str=(String)f.get(o);
					str=DESUtils.getEncryptString(str);
					f.set(o, str);
				}
			} catch (Exception e) {
				logger.error("Field encryption\n",e);
			}
		}
	}
}
