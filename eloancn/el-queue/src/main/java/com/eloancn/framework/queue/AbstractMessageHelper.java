package com.eloancn.framework.queue;

import java.util.UUID;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;

public abstract class AbstractMessageHelper implements MessageHelper {

	
	public void log(Message msg) {
		String serialno = msg.getMessageProperties().getMessageId();
		String bodyType=(String) msg.getMessageProperties().getHeaders()
				.get(AbstractJavaTypeMapper.DEFAULT_CLASSID_FIELD_NAME);
		String methodName=(String) msg.getMessageProperties().getHeaders()
				.get(AmqServiceMessageListener.ORIGINAL_DEFAULT_LISTENER_METHOD);
		String messageListenerService= (String) msg.getMessageProperties().getHeaders()
				.get("MessageListenerService");
		String exchange=(String) msg.getMessageProperties().getHeaders()
				.get("exchange");
		String routingKey=(String) msg.getMessageProperties().getHeaders()
				.get("routingKey");
		log(msg.getMessageProperties().getAppId(),serialno,messageListenerService,methodName,bodyType,
				new String(msg.getBody()),exchange,routingKey);

	}

	public abstract void log(String appID,String messageId, String messageListenerService,String methodName,String bodyType,
			String body,String exchange,String routingKey);


	public void log(Message msg, Throwable e) {
		String serialno = msg.getMessageProperties().getMessageId();
		String bodyType=(String) msg.getMessageProperties().getHeaders()
				.get(AbstractJavaTypeMapper.DEFAULT_CLASSID_FIELD_NAME);
		String methodName=(String) msg.getMessageProperties().getHeaders()
				.get(AmqServiceMessageListener.ORIGINAL_DEFAULT_LISTENER_METHOD);
		String messageListenerService= (String) msg.getMessageProperties().getHeaders()
				.get("MessageListenerService");
		String exchange=(String) msg.getMessageProperties().getHeaders()
				.get("exchange");
		String routingKey=(String) msg.getMessageProperties().getHeaders()
				.get("routingKey");
		log(msg.getMessageProperties().getAppId(),serialno,messageListenerService,methodName,bodyType,
				new String(msg.getBody()),exchange,routingKey, e);

	}

	public abstract void log(String appID,String messageId, String messageListenerService,String methodName,String bodyType,
			String body,String exchange,String routingKey, Throwable e);

	
	public boolean isProcessed(String message_id) {
		return false;
	}

	public String getMessageId() {
		return UUID.randomUUID().toString();
	}

}
