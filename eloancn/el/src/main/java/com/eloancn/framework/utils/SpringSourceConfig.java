package com.eloancn.framework.utils;

import java.util.Locale;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/** 
 * <p>SpringSourceConfig.java</p> 
 * <p><Method Simple Comment></p>
 * <Detail Description>
 * @author jia
 * @since 2014-1-14 上午11:12:15
 */
public class SpringSourceConfig implements MessageSourceAware ,ResourceLoaderAware,DisposableBean{

	private static MessageSource messageSource;
	private static ResourceLoader resourceLoader;
	private static Logger logger = LoggerFactory.getLogger(SpringSourceConfig.class);
	/**
	 * <Method Simple Description>
	 * @param messageSource
	 * @see org.springframework.context.MessageSourceAware#setMessageSource(org.springframework.context.MessageSource)
	 */
	
	public void setMessageSource(MessageSource messageSource) {
		if(logger.isInfoEnabled())
			logger.info("注入messageSource到SpringSourceConfig:{}", messageSource);
		SpringSourceConfig.messageSource = messageSource;
	}
	public static MessageSource getMessageSource(){
		Validate.validState(messageSource != null,
				"messageSourcet属性未注入, 请在applicationContext.xml中定义SpringSourceConfig.");
		return SpringSourceConfig.messageSource;
	}
	public static String getMessage(String code, Object[] args, Locale locale){
		Validate.validState(messageSource != null,
				"messageSourcet属性未注入, 请在applicationContext.xml中定义SpringSourceConfig.");
		return messageSource.getMessage(code, args, locale);
	}
	public static String getMessage(String code){
		Validate.validState(messageSource != null,
				"messageSourcet属性未注入, 请在applicationContext.xml中定义SpringSourceConfig.");
		return messageSource.getMessage(code, null, Locale.ROOT);
	}
	public static String getMessage(String code, Object[] args, String defaultMessage, Locale locale){
		Validate.validState(messageSource != null,
				"messageSourcet属性未注入, 请在applicationContext.xml中定义SpringSourceConfig.");
		return messageSource.getMessage(code, args, defaultMessage, locale);
	}
	/**
	 * <Method Simple Description>
	 * @param resourceLoader
	 * @see org.springframework.context.ResourceLoaderAware#setResourceLoader(org.springframework.core.io.ResourceLoader)
	 */
	
	public void setResourceLoader(ResourceLoader resourceLoader) {
		if(logger.isInfoEnabled())
			logger.info("注入resourceLoader到SpringSourceConfig:{}", resourceLoader);
		SpringSourceConfig.resourceLoader = resourceLoader;		
	}
	public static ResourceLoader getResourceLoader(){
		Validate.validState(resourceLoader != null,
				"resourceLoader属性未注入, 请在applicationContext.xml中定义SpringSourceConfig.");
		return resourceLoader;
	}
	public static Resource getResource(String location) {
		Validate.validState(resourceLoader != null,
				"resourceLoader属性未注入, 请在applicationContext.xml中定义SpringSourceConfig.");
		return resourceLoader.getResource(location);
	}
	public void destroy() throws Exception {
		SpringSourceConfig.resourceLoader = null;
		SpringSourceConfig.messageSource = null;		
	}
}
