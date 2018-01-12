package com.eloancn.framework.cas;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.proxy.ProxyGrantingTicketStorage;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorageImpl;
import org.jasig.cas.client.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyCallbackFilter implements Filter {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private ProxyGrantingTicketStorage proxyGrantingTicketStorage = new ProxyGrantingTicketStorageImpl();

	private String proxyReceptorUrl = "/proxyCallback";

	public void setProxyReceptorUrl(String proxyReceptorUrl) {
		this.proxyReceptorUrl = proxyReceptorUrl;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void setProxyGrantingTicketStorage(
			final ProxyGrantingTicketStorage storage) {
		this.proxyGrantingTicketStorage = storage;
	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		final String requestUri = request.getRequestURI();

		if (!requestUri.endsWith(this.proxyReceptorUrl)) {
			filterChain.doFilter(request, response);
			return;
		}
		try {
			CommonUtils.readAndRespondToProxyReceptorRequest(request, response,
					this.proxyGrantingTicketStorage);
		} catch (final RuntimeException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
