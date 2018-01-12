package com.eloancn.framework.queue;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.FactoryBean;

public class DefaultConnectionFactory implements FactoryBean<ConnectionFactory>{

	private AmpConnectionFactory ampConnectionFactory;
	
	
	public AmpConnectionFactory getAmpConnectionFactory() {
		return ampConnectionFactory;
	}

	public void setAmpConnectionFactory(AmpConnectionFactory ampConnectionFactory) {
		this.ampConnectionFactory = ampConnectionFactory;
	}

	public ConnectionFactory getObject() throws Exception {
		return ampConnectionFactory.getConnectionFactory();
	}

	public Class<?> getObjectType() {
		return ConnectionFactory.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
