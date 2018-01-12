package com.eloancn.framework.queue;

import org.springframework.amqp.core.Message;

public interface MessageHelper {
	
	void log(Message msg);
	
	void log(Message msg,Throwable e);
	
	boolean isProcessed(String message_id);
	
	String getMessageId();

}
