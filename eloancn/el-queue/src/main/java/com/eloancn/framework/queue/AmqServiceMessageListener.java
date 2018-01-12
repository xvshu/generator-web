package com.eloancn.framework.queue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.AmqpIOException;
import org.springframework.amqp.core.Address;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.RabbitExceptionTranslator;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;
import org.springframework.util.MethodInvoker;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.eloancn.framework.annotation.Decryption;
import com.eloancn.framework.cipher.DESUtils;
import com.eloancn.framework.transaction.DataSourceTransactionManager;
import com.eloancn.framework.utils.NettyClient;
import com.rabbitmq.client.Channel;

public class AmqServiceMessageListener implements MessageListener,
		ChannelAwareMessageListener {

	/**
	 * Out-of-the-box value for the default listener method: "handleMessage".
	 */
	public static final String ORIGINAL_DEFAULT_LISTENER_METHOD = "handleMessage";

	private static final String DEFAULT_RESPONSE_ROUTING_KEY = "";

	private static final String DEFAULT_ENCODING = "UTF-8";
	
	public static final String SERIALNO_FIELD_NAME = "serialNo";

	/** Logger available to subclasses */
	protected final static Log logger = LogFactory.getLog(AmqServiceMessageListener.class);

	private Object delegate;

	private String defaultListenerMethod = ORIGINAL_DEFAULT_LISTENER_METHOD;

	private String responseRoutingKey = DEFAULT_RESPONSE_ROUTING_KEY;

	private String responseExchange = null;
	
	private String appId;

	private volatile boolean mandatoryPublish;

	private MessageConverter messageConverter;

	private volatile MessagePropertiesConverter messagePropertiesConverter = new DefaultMessagePropertiesConverter();

	private String encoding = DEFAULT_ENCODING;

	private MessageHelper messageHelper = new DefaultMessageHelper();
	
	private RedisTemplate<String, Object> redisTemplate;
	
	private volatile RetryTemplate retryTemplate;
	
	private String serialNoFieldName;

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * Create a new {@link MessageListenerAdapter} with default settings.
	 */
	public AmqServiceMessageListener() {
		initDefaultStrategies();
		this.delegate = this;
	}

	/**
	 * Create a new {@link MessageListenerAdapter} for the given delegate.
	 * 
	 * @param delegate
	 *            the delegate object
	 */
	public AmqServiceMessageListener(Object delegate) {
		initDefaultStrategies();
		setDelegate(delegate);
	}

	/**
	 * Create a new {@link MessageListenerAdapter} for the given delegate.
	 * 
	 * @param delegate
	 *            the delegate object
	 * @param messageConverter
	 *            the message converter to use
	 */
	public AmqServiceMessageListener(Object delegate,
			MessageConverter messageConverter) {
		initDefaultStrategies();
		setDelegate(delegate);
		setMessageConverter(messageConverter);
	}

	/**
	 * Create a new {@link MessageListenerAdapter} for the given delegate while
	 * also declaring its POJO method.
	 * 
	 * @param delegate
	 *            the delegate object
	 * @param defaultListenerMethod
	 *            name of the POJO method to call upon message receipt
	 */
	public AmqServiceMessageListener(Object delegate,
			String defaultListenerMethod) {
		this(delegate);
		setDefaultListenerMethod(defaultListenerMethod);
	}

	/**
	 * Set a target object to delegate message listening to. Specified listener
	 * methods have to be present on this target object.
	 * <p>
	 * If no explicit delegate object has been specified, listener methods are
	 * expected to present on this adapter instance, that is, on a custom
	 * subclass of this adapter, defining listener methods.
	 * 
	 * @param delegate
	 *            The delegate listener or POJO.
	 */
	public void setDelegate(Object delegate) {
		Assert.notNull(delegate, "Delegate must not be null");
		this.delegate = delegate;
	}

	/**
	 * @return The target object to delegate message listening to.
	 */
	protected Object getDelegate() {
		return this.delegate;
	}

	/**
	 * The encoding to use when inter-converting between byte arrays and Strings
	 * in message properties.
	 * 
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * Specify the name of the default listener method to delegate to, for the
	 * case where no specific listener method has been determined.
	 * Out-of-the-box value is {@link #ORIGINAL_DEFAULT_LISTENER_METHOD
	 * "handleMessage"}.
	 * 
	 * @param defaultListenerMethod
	 *            The name of the default listener method.
	 * 
	 * @see #getListenerMethodName
	 */
	public void setDefaultListenerMethod(String defaultListenerMethod) {
		this.defaultListenerMethod = defaultListenerMethod;
	}

	/**
	 * @return The name of the default listener method to delegate to.
	 */
	protected String getDefaultListenerMethod() {
		return this.defaultListenerMethod;
	}

	/**
	 * Set the routing key to use when sending response messages. This will be
	 * applied in case of a request message that does not carry a "ReplyTo"
	 * property
	 * <p>
	 * Response destinations are only relevant for listener methods that return
	 * result objects, which will be wrapped in a response message and sent to a
	 * response destination.
	 * 
	 * @param responseRoutingKey
	 *            The routing key.
	 */
	public void setResponseRoutingKey(String responseRoutingKey) {
		this.responseRoutingKey = responseRoutingKey;
	}

	/**
	 * Set the exchange to use when sending response messages. This is only used
	 * if the exchange from the received message is null.
	 * <p>
	 * Response destinations are only relevant for listener methods that return
	 * result objects, which will be wrapped in a response message and sent to a
	 * response destination.
	 * 
	 * @param responseExchange
	 *            The exchange.
	 */
	public void setResponseExchange(String responseExchange) {
		this.responseExchange = responseExchange;
	}

	/**
	 * Set the converter that will convert incoming Rabbit messages to listener
	 * method arguments, and objects returned from listener methods back to
	 * Rabbit messages.
	 * <p>
	 * The default converter is a {@link SimpleMessageConverter}, which is able
	 * to handle "text" content-types.
	 * 
	 * @param messageConverter
	 *            The message converter.
	 */
	public void setMessageConverter(MessageConverter messageConverter) {
		this.messageConverter = messageConverter;
	}

	/**
	 * Return the converter that will convert incoming Rabbit messages to
	 * listener method arguments, and objects returned from listener methods
	 * back to Rabbit messages.
	 * 
	 * @return The message converter.
	 */
	protected MessageConverter getMessageConverter() {
		return this.messageConverter;
	}

	public void setMandatoryPublish(boolean mandatoryPublish) {
		this.mandatoryPublish = mandatoryPublish;
	}

	public MessageHelper getMessageHelper() {
		return messageHelper;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public void setMessageHelper(MessageHelper messageHelper) {
		this.messageHelper = messageHelper;
	}

	/**
	 * Rabbit {@link MessageListener} entry point.
	 * <p>
	 * Delegates the message to the target listener method, with appropriate
	 * conversion of the message argument. In case of an exception, the
	 * {@link #handleListenerException(Throwable)} method will be invoked.
	 * <p>
	 * <b>Note:</b> Does not support sending response messages based on result
	 * objects returned from listener methods. Use the
	 * {@link ChannelAwareMessageListener} entry point (typically through a
	 * Spring message listener container) for handling result objects as well.
	 * 
	 * @param message
	 *            the incoming Rabbit message
	 * @see #handleListenerException
	 * @see #onMessage(Message, Channel)
	 */
	
	public void onMessage(Message message) {
		try {
			onMessage(message, null);
		} catch (Throwable ex) {
			handleListenerException(ex);
		}
	}

	/**
	 * Spring {@link ChannelAwareMessageListener} entry point.
	 * <p>
	 * Delegates the message to the target listener method, with appropriate
	 * conversion of the message argument. If the target method returns a
	 * non-null object, wrap in a Rabbit message and send it back.
	 * 
	 * @param message
	 *            the incoming Rabbit message
	 * @param channel
	 *            the Rabbit channel to operate on
	 * @throws Exception
	 *             if thrown by Rabbit API methods
	 */
	
	public void onMessage(Message message, Channel channel) throws Exception {
		//从message中获取teid放入mdc中。
		String teid = (String) message.getMessageProperties().getHeaders().get("teid");
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(teid)){
			MDC.put("teid",teid);
		}
		Method method = getListenerMethod();
		if(null==method){
			if(logger.isErrorEnabled()){
				logger.error("onMessage-Method:"+getDefaultListenerMethod()+" Non-existent!");
			}
			return;
		}
		Class<?> c = method.getParameterTypes()[0];
		message.getMessageProperties().getHeaders().put(AbstractJavaTypeMapper.DEFAULT_CLASSID_FIELD_NAME,c.getName());
		
		// Invoke the handler method with appropriate arguments.
		Object convertedMessage = extractMessage(message);
		decryption(convertedMessage);
		message.getMessageProperties().setAppId(appId);
		if(null!=getSerialNoFieldName()){
			 String messageId=getSerialNo(convertedMessage,getSerialNoFieldName());
			 message.getMessageProperties().setMessageId(messageId);
		}
		final String messageId = message.getMessageProperties().getMessageId();
		
		message.getMessageProperties().setHeader(ORIGINAL_DEFAULT_LISTENER_METHOD, getDefaultListenerMethod());
		String name=getDelegate().getClass().getSimpleName();
		if(null!=name){
			name=name.substring(0, 1).toLowerCase()+name.substring(1);
			message.getMessageProperties().setHeader("MessageListenerService", name);
		}
		if(redisTemplate.boundValueOps("lock:"+messageId).setIfAbsent(messageId)){
			redisTemplate.boundValueOps("lock:"+messageId).expire(20, TimeUnit.SECONDS);
		}else{
			if(logger.isInfoEnabled()){
				logger.info("The message has been processed messageId:"+messageId+" body:"+new String(message.getBody()));
			}
			return;
		}
			
		if (this.messageHelper.isProcessed(message.getMessageProperties().getMessageId())) {
			if(logger.isInfoEnabled()){
				logger.info("The message has been {"+messageId+"} consumption"+" body:"+new String(message.getBody()));
			}
			return;
		}
		try {
			Object[] listenerArguments = buildListenerArguments(convertedMessage);
			Object result = invokeListenerMethod(getDefaultListenerMethod(),listenerArguments, message);
			if (result != null) {
				handleResult(result, message, channel);
			}
			this.messageHelper.log(message);
			try {
				if(null!=messageId)
					redisTemplate.boundHashOps(DataSourceTransactionManager.PUBLISH_COMMITED).delete(messageId);
				redisTemplate.delete("lock:"+messageId);
			} catch (Exception e) {
				logger.error("delete publish_commited message error", e);
			}
		} catch (Exception e) {
			logger.error("onMessage error!", e);
			try {
				try {
					redisTemplate.delete("lock:"+messageId);
				} catch (Exception e2) {}
				String errorService = (String)message.getMessageProperties().getHeaders().get("errorService");
				final String host = (String)message.getMessageProperties().getHeaders().get("host");
				final Integer port = (Integer)message.getMessageProperties().getHeaders().get("port");
				final String exchange = (String)message.getMessageProperties().getHeaders().get("exchange");
				final String routingKey = (String)message.getMessageProperties().getHeaders().get("routingKey");
				final String virtualHost = (String)message.getMessageProperties().getHeaders().get("virtualHost");
				redisTemplate.boundSetOps(PublishMessageListener.SERVER_CONSUME_ERROR).add(new ErrorMessage(messageId,host,null==port?-1:port,exchange,routingKey,virtualHost,PublishMessageListener.getStackTrace(e),new String(message.getBody())));
				if(!StringUtils.isEmpty(errorService)&&!StringUtils.isEmpty(host)&&null!=port){
					if(null!=retryTemplate){
						try {
							retryTemplate.execute(new RetryCallback<Object, Exception>(){
								public Object doWithRetry(RetryContext context)
										throws Exception {
									NettyClient.send(host, port, "MessageId:"+messageId);
									return null;
								}
							});
						} catch (Throwable e1) {
							redisTemplate.boundSetOps(PublishMessageListener.ERROR_NOTICE_ERROR).add(new ErrorMessage(messageId,host,port,exchange,routingKey,virtualHost,PublishMessageListener.getStackTrace(e1),new String(message.getBody())));
							logger.error("retryTemplate error", e);
						}
					}else{
						NettyClient.send(host, port, messageId);
					}
				}
			} catch (Exception e1) {
				logger.error("Exception error!", e1);
			}
		}
	}

	/**
	 * Initialize the default implementations for the adapter's strategies.
	 * 
	 * @see #setMessageConverter
	 * @see org.springframework.amqp.support.converter.SimpleMessageConverter
	 */
	protected void initDefaultStrategies() {
		setMessageConverter(new SimpleMessageConverter());
	}

	/**
	 * Handle the given exception that arose during listener execution. The
	 * default implementation logs the exception at error level.
	 * <p>
	 * This method only applies when using a Rabbit {@link MessageListener}.
	 * With {@link ChannelAwareMessageListener}, exceptions get handled by the
	 * caller instead.
	 * 
	 * @param ex
	 *            the exception to handle
	 * @see #onMessage(Message)
	 */
	protected void handleListenerException(Throwable ex) {
		logger.error("Listener execution failed", ex);
	}

	/**
	 * Extract the message body from the given Rabbit message.
	 * 
	 * @param message
	 *            the Rabbit <code>Message</code>
	 * @return the content of the message, to be passed into the listener method
	 *         as argument
	 * @throws Exception
	 *             if thrown by Rabbit API methods
	 */
	protected Object extractMessage(Message message) throws Exception {

		MessageConverter converter = getMessageConverter();
		if (converter != null) {
			return converter.fromMessage(message);
		}
		return message;
	}

	/**
	 * Determine the name of the listener method that is supposed to handle the
	 * given message.
	 * <p>
	 * The default implementation simply returns the configured default listener
	 * method, if any.
	 * 
	 * @param originalMessage
	 *            the Rabbit request message
	 * @param extractedMessage
	 *            the converted Rabbit request message, to be passed into the
	 *            listener method as argument
	 * @return the name of the listener method (never <code>null</code>)
	 * @throws Exception
	 *             if thrown by Rabbit API methods
	 * @see #setDefaultListenerMethod
	 */
	protected Method getListenerMethod() throws Exception {
		Class<?> c = getDelegate().getClass();
		for (Method m : c.getMethods()) {
			if (m.getName().equalsIgnoreCase(getDefaultListenerMethod())) {
				return m;
			}
		}
		return null;
	}

	/**
	 * Build an array of arguments to be passed into the target listener method.
	 * Allows for multiple method arguments to be built from a single message
	 * object.
	 * <p>
	 * The default implementation builds an array with the given message object
	 * as sole element. This means that the extracted message will always be
	 * passed into a <i>single</i> method argument, even if it is an array, with
	 * the target method having a corresponding single argument of the array's
	 * type declared.
	 * <p>
	 * This can be overridden to treat special message content such as arrays
	 * differently, for example passing in each element of the message array as
	 * distinct method argument.
	 * 
	 * @param extractedMessage
	 *            the content of the message
	 * @return the array of arguments to be passed into the listener method
	 *         (each element of the array corresponding to a distinct method
	 *         argument)
	 */
	protected Object[] buildListenerArguments(Object extractedMessage) {
		return new Object[] { extractedMessage };
	}

	/**
	 * Invoke the specified listener method.
	 * 
	 * @param methodName
	 *            the name of the listener method
	 * @param arguments
	 *            the message arguments to be passed in
	 * @param originalMessage
	 *            the original message
	 * @return the result returned from the listener method
	 * @throws Exception
	 *             if thrown by Rabbit API methods
	 * @see #getListenerMethodName
	 * @see #buildListenerArguments
	 */
	protected Object invokeListenerMethod(String methodName,
			Object[] arguments, Message originalMessage) throws Exception {
		try {
			MethodInvoker methodInvoker = new MethodInvoker();
			methodInvoker.setTargetObject(getDelegate());
			methodInvoker.setTargetMethod(methodName);
			methodInvoker.setArguments(arguments);
			methodInvoker.prepare();
			return methodInvoker.invoke();
		} catch (InvocationTargetException ex) {
			Throwable targetEx = ex.getTargetException();
			if (targetEx instanceof IOException) {
				throw new AmqpIOException((IOException) targetEx);
			} else {
				throw new ListenerExecutionFailedException("Listener method '"
						+ methodName + "' threw exception", targetEx,
						originalMessage);
			}
		} catch (Throwable ex) {
			ArrayList<String> arrayClass = new ArrayList<String>();
			if (arguments != null) {
				for (Object argument : arguments) {
					arrayClass.add(argument.getClass().toString());
				}
			}
			throw new ListenerExecutionFailedException(
					"Failed to invoke target method '"
							+ methodName
							+ "' with argument type = ["
							+ StringUtils.collectionToCommaDelimitedString(arrayClass)
							+ "], value = ["
							+ ObjectUtils.nullSafeToString(arguments) + "]",
					ex, originalMessage);
		}
	}

	/**
	 * Handle the given result object returned from the listener method, sending
	 * a response message back.
	 * 
	 * @param result
	 *            the result object to handle (never <code>null</code>)
	 * @param request
	 *            the original request message
	 * @param channel
	 *            the Rabbit channel to operate on (may be <code>null</code>)
	 * @throws Exception
	 *             if thrown by Rabbit API methods
	 * @see #buildMessage
	 * @see #postProcessResponse
	 * @see #getReplyToAddress(Message)
	 * @see #sendResponse
	 */
	protected void handleResult(Object result, Message request, Channel channel)
			throws Exception {
		if (channel != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Listener method returned result [" + result
						+ "] - generating response message for it");
			}
			Message response = buildMessage(channel, result);
			postProcessResponse(request, response);
			Address replyTo = getReplyToAddress(request);
			sendResponse(channel, replyTo, response);
		} else if (logger.isWarnEnabled()) {
			logger.warn("Listener method returned result ["
					+ result
					+ "]: not generating response message for it because of no Rabbit Channel given");
		}
	}

	protected String getReceivedExchange(Message request) {
		return request.getMessageProperties().getReceivedExchange();
	}

	/**
	 * Build a Rabbit message to be sent as response based on the given result
	 * object.
	 * 
	 * @param session
	 *            the Rabbit Channel to operate on
	 * @param result
	 *            the content of the message, as returned from the listener
	 *            method
	 * @return the Rabbit <code>Message</code> (never <code>null</code>)
	 * @throws Exception
	 *             if thrown by Rabbit API methods
	 * @see #setMessageConverter
	 */
	protected Message buildMessage(Channel session, Object result)
			throws Exception {
		MessageConverter converter = getMessageConverter();
		if (converter != null) {
			return converter.toMessage(result, new MessageProperties());
		} else {
			if (!(result instanceof Message)) {
				throw new MessageConversionException(
						"No MessageConverter specified - cannot handle message ["
								+ result + "]");
			}
			return (Message) result;
		}
	}

	/**
	 * Post-process the given response message before it will be sent.
	 * <p>
	 * The default implementation sets the response's correlation id to the
	 * request message's correlation id, if any; otherwise to the request
	 * message id.
	 * 
	 * @param request
	 *            the original incoming Rabbit message
	 * @param response
	 *            the outgoing Rabbit message about to be sent
	 * @throws Exception
	 *             if thrown by Rabbit API methods
	 */
	protected void postProcessResponse(Message request, Message response)
			throws Exception {
		byte[] correlation = request.getMessageProperties().getCorrelationId();

		if (correlation == null) {
			String messageId = request.getMessageProperties().getMessageId();
			if (messageId != null) {
				correlation = messageId
						.getBytes(SimpleMessageConverter.DEFAULT_CHARSET);
			}
		}
		response.getMessageProperties().setCorrelationId(correlation);
	}

	/**
	 * Determine a reply-to Address for the given message.
	 * <p>
	 * The default implementation first checks the Rabbit Reply-To Address of
	 * the supplied request; if that is not <code>null</code> it is returned; if
	 * it is <code>null</code>, then the configured default response Exchange
	 * and routing key are used to construct a reply-to Address. If the
	 * responseExchange property is also <code>null</code>, then an
	 * {@link AmqpException} is thrown.
	 * 
	 * @param request
	 *            the original incoming Rabbit message
	 * @return the reply-to Address (never <code>null</code>)
	 * @throws Exception
	 *             if thrown by Rabbit API methods
	 * @throws AmqpException
	 *             if no {@link Address} can be determined
	 * @see #setResponseExchange(String)
	 * @see #setResponseRoutingKey(String)
	 * @see org.springframework.amqp.core.Message#getMessageProperties()
	 * @see org.springframework.amqp.core.MessageProperties#getReplyTo()
	 */
	protected Address getReplyToAddress(Message request) throws Exception {
		Address replyTo = request.getMessageProperties().getReplyToAddress();
		if (replyTo == null) {
			if (this.responseExchange == null) {
				throw new AmqpException(
						"Cannot determine ReplyTo message property value: "
								+ "Request message does not contain reply-to property, and no default response Exchange was set.");
			}
			replyTo = new Address(null, this.responseExchange,
					this.responseRoutingKey);
		}
		return replyTo;
	}

	/**
	 * Send the given response message to the given destination.
	 * 
	 * @param channel
	 *            the Rabbit channel to operate on
	 * @param replyTo
	 *            the Rabbit ReplyTo string to use when sending. Currently
	 *            interpreted to be the routing key.
	 * @param message
	 *            the Rabbit message to send
	 * @throws Exception
	 *             if thrown by Rabbit API methods
	 * @see #postProcessResponse(Message, Message)
	 */
	protected void sendResponse(Channel channel, Address replyTo,
			Message message) throws Exception {
		// postProcessChannel(channel, message);

		try {
			logger.debug("Publishing response to exchanage = ["
					+ replyTo.getExchangeName() + "], routingKey = ["
					+ replyTo.getRoutingKey() + "]");
			channel.basicPublish(replyTo.getExchangeName(), replyTo
					.getRoutingKey(), this.mandatoryPublish,
					this.messagePropertiesConverter.fromMessageProperties(
							message.getMessageProperties(), encoding), message
							.getBody());
		} catch (Exception ex) { 
			throw RabbitExceptionTranslator.convertRabbitAccessException(ex);
		}
	}
	
	public void setRetryTemplate(RetryTemplate retryTemplate) {
		this.retryTemplate = retryTemplate;
	}

	public static String getSerialNo(Object o, String fieldName) {
		Class<?> c = o.getClass();
		Field f;
		try {
			f = c.getDeclaredField(fieldName);
			f.setAccessible(true);
			return f.get(o).toString();
		} catch (Exception e) {
			//logger.error("get"+fieldName+" field errr");
		} 
		for (; c != Object.class; c = c.getSuperclass()) {
			try {
				 f = o.getClass().getSuperclass()
						.getDeclaredField(fieldName);
				f.setAccessible(true);
				return f.get(o).toString();
			} catch (Exception e) {
				// logger.error("get"+fieldName+" field errr");
			}
		}
		if(logger.isInfoEnabled()){
			 logger.error("get"+fieldName+" field errr");
		}
		return null;
	}
	private void decryption(Object o) {
		Class<?> c = o.getClass();
		Field fs[]=c.getDeclaredFields();
		for (Field f:fs) {
			try {
				Decryption e=f.getAnnotation(Decryption.class);
				if(null!=e){
					f.setAccessible(true);
					String str=(String)f.get(o);
					str=DESUtils.getDecryptString(str);
					f.set(o, str);
				}
			} catch (Exception e) {
				 logger.error("Field decryption\n",e);
			}
		}
	}

	public String getSerialNoFieldName() {
		return serialNoFieldName;
	}

	public void setSerialNoFieldName(String serialNoFieldName) {
		this.serialNoFieldName = serialNoFieldName;
	}
	
}
