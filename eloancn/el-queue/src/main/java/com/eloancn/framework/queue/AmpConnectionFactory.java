package com.eloancn.framework.queue;

import java.util.HashMap;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

public class AmpConnectionFactory{

	private String hostname;

	private int port = com.rabbitmq.client.ConnectionFactory.USE_DEFAULT_PORT;

	private String username;

	private String password;
	
	private String defaultVirtualHost = com.rabbitmq.client.ConnectionFactory.DEFAULT_VHOST;

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDefaultVirtualHost() {
		return defaultVirtualHost;
	}

	public void setDefaultVirtualHost(String defaultVirtualHost) {
		this.defaultVirtualHost = defaultVirtualHost;
	}

	private static HashMap<String, CachingConnectionFactory> vhostMap = new HashMap<String, CachingConnectionFactory>();
	
	public CachingConnectionFactory getConnectionFactory(String virtualHost) { 
		CachingConnectionFactory connectionFactory = vhostMap.get(virtualHost);
		if(connectionFactory==null){
			connectionFactory = new CachingConnectionFactory(hostname,port);  
	        connectionFactory.setUsername(username);  
	        connectionFactory.setPassword(password);  
	        connectionFactory.setVirtualHost(virtualHost);
	        vhostMap.put(virtualHost, connectionFactory);
		}
        return connectionFactory;  
    }

	public CachingConnectionFactory getConnectionFactory() throws Exception {
		return getConnectionFactory(this.defaultVirtualHost);
	}

}
