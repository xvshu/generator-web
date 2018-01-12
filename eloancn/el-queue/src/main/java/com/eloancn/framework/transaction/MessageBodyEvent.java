package com.eloancn.framework.transaction;

import org.springframework.context.ApplicationEvent;

public class MessageBodyEvent extends ApplicationEvent{

	public MessageBodyEvent(Object source) {
		super(source);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
