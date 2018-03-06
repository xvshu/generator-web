package com.eloancn.framework.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;


public class PropertiesUtil {

	private static final String SYSTEM_CODE = "system.code";
	private static Logger log = LoggerFactory.getLogger(PropertiesUtil.class);
	private static Configuration config = null;
	static {
		try {
			InputStream configFile = getResourceAsStream("/properties/application.properties");
			config = new PropertiesConfiguration();
			((PropertiesConfiguration) config).load(configFile);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	private static ClassLoader getContextClassLoader() {
		ClassLoader classLoader = null;
		if (classLoader == null) {
			try {
				// Are we running on a JDK 1.2 or later system?
				Method method = Thread.class.getMethod("getContextClassLoader",
						null);
				// Get the thread context class loader (if there is one)
				try {
					classLoader = (ClassLoader) method.invoke(
							Thread.currentThread(), null);
				} catch (IllegalAccessException e) {
					; // ignore
				} catch (InvocationTargetException e) {
					;
				}
			} catch (NoSuchMethodException e) {
				// Assume we are running on JDK 1.1
				; // ignore
			}
		}
		if (classLoader == null) {
			classLoader = PropertiesUtil.class.getClassLoader();
		}
		// Return the selected class loader
		return classLoader;
	}

	private static InputStream getResourceAsStream(final String name) {
		return (InputStream) AccessController
				.doPrivileged(new PrivilegedAction() {
					public Object run() {
						ClassLoader threadCL = getContextClassLoader();
						if (threadCL != null) {
							return threadCL.getResourceAsStream(name);
						} else {
							return ClassLoader.getSystemResourceAsStream(name);
						}
					}
				});
	}

	public static String getProperties(String key) {
		log.debug("getProperties:" + key);
		return config.getString(key);
	}
}
