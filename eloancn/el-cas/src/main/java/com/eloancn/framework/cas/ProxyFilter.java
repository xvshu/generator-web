package com.eloancn.framework.cas;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyFilter implements Filter {

	private static Logger logger = LoggerFactory.getLogger(ProxyFilter.class);

	// the name of the parameter service ticket in url
	private static final String TICKET_PARAMETER = "ticket";
	
	private String ticketParameter = TICKET_PARAMETER;

	public String getTicketParameter() {
		return ticketParameter;
	}

	public void setTicketParameter(String ticketParameter) {
		this.ticketParameter = ticketParameter;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String ticket = httpRequest.getParameter(getTicketParameter());
		if(StringUtils.isNotEmpty(ticket)){
			try {
	            Subject subject = SecurityUtils.getSubject();
	            subject.login(new CasToken(ticket));
	            response.getOutputStream().print("s");
	        } catch (AuthenticationException e) {
	        	 response.getOutputStream().print("f");
	        	logger.error("login error\n",e);
	        }
		}else{
			chain.doFilter(request, response);
		}
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	@Override
	public void destroy() {

	}
}
