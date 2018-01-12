package com.eloancn.framework.queue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

public class JdbcMessageHelper implements MessageHelper,InitializingBean {

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());
	protected ConcurrentLinkedQueue<ErrorMessage> logList = new ConcurrentLinkedQueue<ErrorMessage>();
	private JdbcTemplate jdbcTemplate;
	private String selectIsExistSql;
	private String logInsertSql;
	private List<String> logInsertSqlParsName=new ArrayList<String>();

	
	public JdbcMessageHelper(){}
	
	public JdbcMessageHelper(DataSource dataSource){
		this.jdbcTemplate= new JdbcTemplate(dataSource);
	}
	public JdbcMessageHelper(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate= jdbcTemplate;
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setSelectIsExistSql(String selectIsExistSql) {
		this.selectIsExistSql = selectIsExistSql;
	}

	public void setLogInsertSql(String logInsertSql) {
		this.logInsertSql = logInsertSql;
	}

	public void setLogInsertSqlParsName(List<String> logInsertSqlParsName) {
		this.logInsertSqlParsName = logInsertSqlParsName;
	}

	public void log(Message message) {
		if (logger.isInfoEnabled())
			logger.info(message);
		 String messageId = message.getMessageProperties().getMessageId();
		 String host = (String)message.getMessageProperties().getHeaders().get("host");
		 Integer port = (Integer)message.getMessageProperties().getHeaders().get("port");
		 String exchange = (String)message.getMessageProperties().getHeaders().get("exchange");
		 String routingKey = (String)message.getMessageProperties().getHeaders().get("routingKey");
		 String virtualHost = (String)message.getMessageProperties().getHeaders().get("virtualHost");
		 logList.offer(new ErrorMessage(messageId,host,null==port?-1:port,exchange,routingKey,virtualHost,null,new String(message.getBody())));
	}
	
	public boolean isProcessed(String message_id) {

//		int ii=jdbcTemplate.query(selectIsExistSql, new Object[]{message_id},new ResultSetExtractor<Integer>(){
//			@Override
//			public Integer extractData(ResultSet rs) throws SQLException,
//					DataAccessException {
//				return rs.getInt(1);
//			}
//
//		});

		int ii=jdbcTemplate.queryForObject(selectIsExistSql, new Object[]{message_id},Integer.class);
		if(ii>0){
			return true;
		}
		return false;
	}
	
	public String getMessageId() {
		return UUID.randomUUID().toString();
	}

	
	public void log(Message message, Throwable e) {
		if (logger.isErrorEnabled()){
			logger.error(message, e);
		}
		String messageId = message.getMessageProperties().getMessageId();
		String host = (String)message.getMessageProperties().getHeaders().get("host");
		Integer port = (Integer)message.getMessageProperties().getHeaders().get("port");
		String exchange = (String)message.getMessageProperties().getHeaders().get("exchange");
		String routingKey = (String)message.getMessageProperties().getHeaders().get("routingKey");
		String virtualHost = (String)message.getMessageProperties().getHeaders().get("virtualHost");
		logList.offer(new ErrorMessage(messageId,host,null==port?-1:port,exchange,routingKey,virtualHost,PublishMessageListener.getStackTrace(e),new String(message.getBody())));
	}
	
	class LogWorker extends Thread {
		 public void run() {
			  try {
				  while(true){
					  flush();
					  sleep(1000 * 60);
				  }
                } catch (Exception e) {
                	if (logger.isErrorEnabled()){
            			logger.error(e);
            		}
                } 
		 }
		 private void flush(){
			 ErrorMessage msg =null;
			 
			 while ((msg = logList.poll()) != null) {
				 Object[] args= new Object[logInsertSqlParsName.size()];
				 Iterator<String> it=logInsertSqlParsName.iterator();
				 int i=0;
				 while(it.hasNext()){
					 String name = it.next();
					 try {
						args[i]=BeanUtilsBean.getInstance().getSimpleProperty(msg, name);
					} catch (Exception e) {
						e.printStackTrace();
					} 
					i++; 
				 }
				 jdbcTemplate.update(logInsertSql, args);
			 }
		 }
	}

	public void afterPropertiesSet() throws Exception {
		new LogWorker().start();
	}

}
