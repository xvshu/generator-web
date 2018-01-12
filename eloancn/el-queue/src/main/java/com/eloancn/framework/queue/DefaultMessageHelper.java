package com.eloancn.framework.queue;

import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Message;

public class DefaultMessageHelper implements MessageHelper {

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());
	
	public void log(Message msg) {
		if (logger.isInfoEnabled())
			logger.info(msg);

	}

	public boolean isProcessed(String message_id) {
		return false;
	}
	
	public String getMessageId() {
		return UUID.randomUUID().toString();
	}

	public void log(Message msg, Throwable e) {
		if (logger.isErrorEnabled()){
			logger.error(msg, e);
		}
	}

}
