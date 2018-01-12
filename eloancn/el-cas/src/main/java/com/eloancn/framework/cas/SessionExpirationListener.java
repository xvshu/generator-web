package com.eloancn.framework.cas;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SessionExpirationListener implements SessionListener,
		ApplicationContextAware {

	private CacheManager cacheManager;

	private ApplicationContext applicationContext;

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public void onStart(Session session) {
	}

	@Override
	public void onStop(Session session) {
		onExpiration( session);
	}

	@Override
	public void onExpiration(Session session) {
		String sessionId = (String) cacheManager.getCache(
				CasFilter.TICKET_SESSION).get(session.getId());
		if (sessionId != null) {
			this.applicationContext.publishEvent(new SessionEvent(sessionId));
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}
