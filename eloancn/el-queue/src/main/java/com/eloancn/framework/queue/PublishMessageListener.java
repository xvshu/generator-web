package com.eloancn.framework.queue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.MethodInvoker;

import com.eloancn.framework.transaction.DataSourceTransactionManager;
import com.eloancn.framework.transaction.MessageBody;
import com.eloancn.framework.transaction.MessageBodyEvent;
import com.eloancn.framework.utils.JsonMapper;
import com.eloancn.framework.utils.NettyClient;

public class PublishMessageListener implements ApplicationContextAware,
		InitializingBean, ApplicationListener<MessageBodyEvent> {

	public static final String CLIENT_SEND_ERROR = "message_client_send_error";
	public static final String SERVER_CONSUME_ERROR = "message_service_consume_error";
	public static final String ERROR_NOTICE_ERROR = "message_error_notice_error";

	private static Logger logger = Logger
			.getLogger(PublishMessageListener.class);
	
	private static String host = "127.0.0.1";

	private static int port = 9999;

	private RedisTemplate<String, Object> redisTemplate;

	private ApplicationContext applicationContext;
	
	private AmpConnectionFactory ampConnectionFactory;
	
	private MessageConverter messageConverter =new Jackson2JsonMessageConverter();

	public PublishMessageListener() {
		try {
			InetAddress ia = InetAddress.getLocalHost();
			host = ia.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;

	}

	public void setAmpConnectionFactory(AmpConnectionFactory ampConnectionFactory) {
		this.ampConnectionFactory = ampConnectionFactory;
	}

	public void setMessageConverter(MessageConverter messageConverter) {
		this.messageConverter = messageConverter;
	}

	public void setPort(int port) {
		PublishMessageListener.port = port;
	}

	public static String getHost() {
		return host;
	}

	public static int getPort() {
		return port;
	}

	private void callbackProcess(String recieved) {
		try {
			MessageBody body = (MessageBody) redisTemplate.boundHashOps(
					DataSourceTransactionManager.PUBLISH_COMMITED)
					.get(recieved);
			if (null != body && null != body.getErrorService()) {
				Object obj = applicationContext.getBean(body.getErrorService());
				MethodInvoker methodInvoker = new MethodInvoker();
				methodInvoker.setTargetObject(obj);
				methodInvoker.setTargetMethod("run");
				methodInvoker.setArguments(new Object[] { body.getBody() });
				methodInvoker.prepare();
				methodInvoker.invoke();
			}
		} catch (Exception e) {
			logger.error("ErrorMessage error", e);
		}
	}

	public void afterPropertiesSet() throws Exception {
		startServer();
	}
	public void startServer() {
		// Configure the server.
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		// Set up the pipeline factory.
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new SimpleChannelUpstreamHandler() {
					@Override
					public void messageReceived(ChannelHandlerContext ctx,
							MessageEvent e) {
						// Send back the received message to the remote peer.
						ChannelBuffer acceptBuff = (ChannelBuffer) e
								.getMessage();
						String info = acceptBuff.toString(Charset
								.defaultCharset());
						if (info != null && !"".equals(info)) {
							if(info.startsWith("MessageId:")){
								callbackProcess(info.substring(info.indexOf("MessageId:")));
							}else if(info.startsWith("ErrorNotify:")){
								String error = info.substring(info.indexOf("ErrorNotify:"));
								ErrorMessage errorMessage =JsonMapper.buildNonDefaultMapper().fromJson(error, ErrorMessage.class);
								NettyClient.send(errorMessage.getHost(), errorMessage.getPort(), "MessageId:"+errorMessage.getMessageId());
							}
							
						}
						e.getChannel().close();
					}

					@Override
					public void exceptionCaught(ChannelHandlerContext ctx,
							ExceptionEvent e) {
						// Close the connection when an exception is raised.
						e.getCause();
						e.getChannel().close();
					}
				});
			}
		});
		// Bind and start to accept incoming connections.
		bootstrap.bind(new InetSocketAddress(PublishMessageListener.host,
				PublishMessageListener.port));

	}

	private void send(final MessageBody body){
		try {
			CachingConnectionFactory ccf = ampConnectionFactory.getConnectionFactory(body.getVirtualHost());
			RabbitTemplate rtp=new RabbitTemplate(ccf);
			rtp.setMessageConverter(messageConverter);
			rtp.convertAndSend(body.getExchange(), body.getRoutingKey(), body.getBody(), new MessagePostProcessor() {
				public Message postProcessMessage(Message message)
						throws AmqpException {
					message.getMessageProperties().setMessageId(body.getMessageId());
					// 增加teid参数传递
					message.getMessageProperties().getHeaders().put("teid", body.gettId());
					message.getMessageProperties().getHeaders().put("virtualHost",body.getVirtualHost());
					message.getMessageProperties().getHeaders().put("port", port);
					message.getMessageProperties().getHeaders().put("errorService",body.getErrorService());
					message.getMessageProperties().getHeaders().put("host", host);
					message.getMessageProperties().getHeaders().put("exchange",body.getExchange());
					message.getMessageProperties().getHeaders().put("routingKey",body.getRoutingKey());
					if(logger.isInfoEnabled())
						logger.info("exchange:"+body.getExchange()+" routingKey:"+body.getRoutingKey()+" body:"+new String(message.getBody()));
					body.setBody(new String(message.getBody()));
					return message;
				}
			});
			redisTemplate.boundHashOps(DataSourceTransactionManager.PUBLISH_COMMITED).put(body.getMessageId(), body);
			redisTemplate.boundHashOps(DataSourceTransactionManager.PUBLISH_COMMIT).delete(body.getMessageId());
		} catch (Exception e1) {
			logger.error("rabbitClient send error\n", e1);
			redisTemplate.boundListOps(PublishMessageListener.CLIENT_SEND_ERROR).leftPush(
							new ErrorMessage(
									body.getMessageId(),
									PublishMessageListener.getHost(),
									PublishMessageListener.getPort(),
									body.getExchange(),
									body.getRoutingKey(),body.getVirtualHost(),getStackTrace(e1),body.getBody().toString()));
			
		}
	}
	public static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		try {
			t.printStackTrace(pw);
			return sw.toString();
		} finally {
			pw.close();
		}
	}
	ExecutorService pool = Executors.newFixedThreadPool(10);
	
	public void onApplicationEvent(MessageBodyEvent event) {
		final MessageBody body =(MessageBody)event.getSource();
		if(logger.isInfoEnabled()){
			logger.info("onApplicationEvent:"+body.toString());
		}
		pool.execute(new SendThread(body));
	}
	class SendThread implements Runnable {
		private MessageBody body;
		
		public SendThread(MessageBody body){
			this.body=body;
		}
	    public void run() {
	    	if(logger.isInfoEnabled()){
				logger.info(Thread.currentThread().getName()+" onApplicationEvent:"+body.toString());
			}
	        send(body);
	    }
	}
}
