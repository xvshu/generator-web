package com.eloancn.framework.exception.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.NoSuchMessageException;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.util.WebUtils;

public class SimpleHandlerExceptionResolver extends
		AbstractHandlerExceptionResolver implements MessageSourceAware{

	private MessageSource messageSource;
	
	/** The default name of the exception attribute: "exception". */
	public static final String DEFAULT_EXCEPTION_ATTRIBUTE = "exception";
	
	private String defaultErrorView;

	private Integer defaultStatusCode = 200;

	private String exceptionAttribute = DEFAULT_EXCEPTION_ATTRIBUTE;
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		applyStatusCodeIfPossible(request, response, defaultStatusCode);
		
		return getModelAndView(request,defaultErrorView,ex);
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
		
	}
	/**
	 * Apply the specified HTTP status code to the given response, if possible (that is,
	 * if not executing within an include request).
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @param statusCode the status code to apply
	 * @see #determineStatusCode
	 * @see #setDefaultStatusCode
	 * @see HttpServletResponse#setStatus
	 */
	protected void applyStatusCodeIfPossible(HttpServletRequest request, HttpServletResponse response, int statusCode) {
		if (!WebUtils.isIncludeRequest(request)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Applying HTTP status code " + statusCode);
			}
			response.setStatus(statusCode);
			request.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, statusCode);
		}
	}
	/**
	 * Return a ModelAndView for the given view name and exception.
	 * <p>The default implementation adds the specified exception attribute.
	 * Can be overridden in subclasses.
	 * @param viewName the name of the error view
	 * @param ex the exception that got thrown during handler execution
	 * @return the ModelAndView instance
	 * @see #setExceptionAttribute
	 */
	protected ModelAndView getModelAndView(HttpServletRequest request,String viewName, Exception ex) {
		ModelAndView mv = new ModelAndView(viewName);
		
		if (this.exceptionAttribute != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Exposing Exception as model attribute '" + this.exceptionAttribute + "'");
			}
			String msg=null;
			try {
				String cn=ex.getClass().getSimpleName();
				if(logger.isErrorEnabled()){
					logger.error(cn+" error \n", ex);
				}
				msg = ex.getMessage();
				if(!StringUtils.isEmpty(msg)&&msg.startsWith("${")){
					msg=messageSource.getMessage(msg.substring(2, msg.length()-1), null, Locale.ROOT);
				}else{
					msg=messageSource.getMessage(this.exceptionAttribute+"."+cn+".msg", null, Locale.ROOT);
				}
			} catch (NoSuchMessageException e) {
				
			}
			
			if((null!=request.getContentType()&&request.getContentType().equalsIgnoreCase(MappingJackson2JsonView.DEFAULT_CONTENT_TYPE))||(null!=request.getHeader("Accept")&&request.getHeader("Accept").contains("application/json"))){
				MappingJackson2JsonView view = new MappingJackson2JsonView();
				view.addStaticAttribute(this.exceptionAttribute, msg);
				mv.setView(view);
			}else{
				mv.addObject(this.exceptionAttribute, msg);
			}
		
		}
		return mv;
	}

	public void setDefaultErrorView(String defaultErrorView) {
		this.defaultErrorView = defaultErrorView;
	}

	public void setDefaultStatusCode(Integer defaultStatusCode) {
		this.defaultStatusCode = defaultStatusCode;
	}
}
