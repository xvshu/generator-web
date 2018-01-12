package com.eloancn.framework.cas;

import org.springframework.context.ApplicationEvent;

public class SessionEvent extends ApplicationEvent{
	
	public SessionEvent(Object source) {
		super(source);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
