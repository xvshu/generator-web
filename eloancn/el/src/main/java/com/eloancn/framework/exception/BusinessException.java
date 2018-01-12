/**
 * 
 */
package com.eloancn.framework.exception;

/**
 * <p>
 * BusinessException.java
 * </p>
 * <p>
 * <异常处理>
 * </p>
 * <Detail Description>
 * 
 * @author jia
 * @since 2013-5-8 9:38:36
 */
public class BusinessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}
}
