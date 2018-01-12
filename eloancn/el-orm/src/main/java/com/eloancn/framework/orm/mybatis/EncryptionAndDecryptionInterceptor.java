package com.eloancn.framework.orm.mybatis;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eloancn.framework.annotation.Decryption;
import com.eloancn.framework.annotation.Encryption;
import com.eloancn.framework.annotation.Md5Password;
import com.eloancn.framework.annotation.WebDecryption;
import com.eloancn.framework.annotation.WebEncryption;
import com.eloancn.framework.cipher.Encryptor;
import com.eloancn.framework.cipher.Md5PasswordEncoder;
import com.eloancn.framework.orm.mybatis.paginator.domain.PageList;

@Intercepts({
		@Signature(type = Executor.class, method = "update", args = {
				MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = {
				MappedStatement.class, Object.class, RowBounds.class,
				ResultHandler.class }) })
public class EncryptionAndDecryptionInterceptor implements Interceptor {

	private  static  final Logger logger = LoggerFactory.getLogger(EncryptionAndDecryptionInterceptor.class);
	private static final String SECRET_KEY = "encryptKey";
	private static final String SECRET_IMP = "encryptImp";
	
	private Encryptor encryptor = null;

	public void setEncryptor(Encryptor encryptor) {
		this.encryptor = encryptor;
	}

	public Object intercept(Invocation invocation) throws Throwable {
		String methodName = invocation.getMethod().getName();
		Object returnValue = null;
		//查询处理
		if (methodName.equals("query")) {
			Object parameter = invocation.getArgs()[1];
			
			if (null != parameter) {
				Class<?> p_c = parameter.getClass();
				Encryption e = p_c.getAnnotation(Encryption.class);
				WebDecryption wd=p_c.getAnnotation(WebDecryption.class);
				Md5Password mp= p_c.getAnnotation(Md5Password.class);
				//参数加密处理
				if (null != e) {
					String fields[] = e.value();
					for (String field : fields) {
						String s = field.substring(0, 1).toUpperCase()
								+ field.substring(1);
						Method get = p_c.getMethod("get" + s, null);
						Method set = p_c.getMethod("set" + s,
								get.getReturnType());
						String value = (String) get.invoke(parameter, null);
						if (value != null)
							set.invoke(parameter, encryptor.encrypt(value));
					}
				}
				//参数md5加密
				if (mp != null) {
					String[] password=mp.password();
					String[] salt=mp.salt();
					for(int i=0;i<password.length;i++){
						String pass=BeanUtilsBean.getInstance().getSimpleProperty(parameter, password[i]);
						String _salt=BeanUtilsBean.getInstance().getSimpleProperty(parameter, salt[i]);
						if(StringUtils.isNotEmpty(pass)&&StringUtils.isNotEmpty(_salt)){
							Md5PasswordEncoder mpe= new Md5PasswordEncoder();
							pass=mpe.encodePassword(pass, _salt);
							BeanUtilsBean.getInstance().setProperty(parameter, password[i], pass);
						}
						
					}
					
				}
				//参数解密处理
				if(null!=wd){
					String fields[] = e.value();
					for (String field : fields) {
						String f[] =field.split("|");
						if(f.length>1){
							String s = f[0].substring(0, 1).toUpperCase()
									+ f[0].substring(1);
							String _s = f[1].substring(0, 1).toUpperCase()
									+ f[1].substring(1);
							Method get = p_c.getMethod("get" + s, null);
							Method _get = p_c.getMethod("get" + _s, null);
							Method set = p_c.getMethod("set" + _s,
									_get.getReturnType());
							String value = (String) get.invoke(parameter, null);
							if (value != null){
								String va=encryptor.decrypt(value);
								if(_get.getReturnType().getSimpleName().equalsIgnoreCase("long"))
									set.invoke(parameter, Long.parseLong(va));
								else if(_get.getReturnType().getSimpleName().equalsIgnoreCase("int")||get.getReturnType().getSimpleName().equalsIgnoreCase("integer"))
									set.invoke(parameter, Integer.parseInt(va));
								else if(_get.getReturnType().getSimpleName().equalsIgnoreCase("double"))
									set.invoke(parameter, Double.parseDouble(va));
								else
									set.invoke(parameter, Integer.parseInt(va));
							}
								
						}else{
							String s = field.substring(0, 1).toUpperCase()
									+ field.substring(1);
							Method get = p_c.getMethod("get" + s, null);
							Method set = p_c.getMethod("set" + s,
									get.getReturnType());
							String value = (String) get.invoke(parameter, null);
							if (value != null)
								set.invoke(parameter, encryptor.decrypt(value));
						}
						
					}
				}
			}
			//返回值解密
			returnValue = invocation.proceed();
			if (returnValue instanceof List) {
				List emp_list = (List) returnValue;
				int len = emp_list.size();
				if (len > 0) {
					Class<?> c = emp_list.get(0).getClass();
					Decryption dn = c.getAnnotation(Decryption.class);
					WebEncryption we = c.getAnnotation(WebEncryption.class);
					if (we != null) {
						List emp = new PageList();
						String fields[] = we.value();
						for (int i = 0; i < len; i++) {
							Object o = emp_list.get(i);
							for (String field : fields) {
								String f[] =field.split("|");
								String s = f[0].substring(0, 1).toUpperCase()
										+ f[0].substring(1);
								String _s = f[1].substring(0, 1).toUpperCase()
										+ f[1].substring(1);
								Method get = c.getMethod("get" + s, null);
								Method set = c.getMethod("set" + _s,
										get.getReturnType());
								String value = (String) get.invoke(o, null);
								if (value != null&&null!=encryptor)
									set.invoke(o, encryptor.decrypt(value));
							}
							emp.add(o);
						}
						returnValue = emp;
					}
					if(null!=dn){
						List emp = new PageList();
						String fields[] = dn.value();
						for (int i = 0; i < len; i++) {
							Object o = emp_list.get(i);
							for (String field : fields) {
								String s = field.substring(0, 1).toUpperCase()
										+ field.substring(1);
								Method get = c.getMethod("get" + s, null);
								Method set = c.getMethod("set" + s,
										get.getReturnType());
								String value = (String) get.invoke(o, null);
								if (value != null&&null!=encryptor)
									set.invoke(o, encryptor.decrypt(value));
							}
							emp.add(o);
						}
						returnValue = emp;
					}
				}
			}
			//增加 更新处理加密
		} else if (methodName.equals("update")) {
			Object parameter = invocation.getArgs()[1];
			Class<?> c = parameter.getClass();
			Encryption en = c.getAnnotation(Encryption.class);
			Md5Password mp= c.getAnnotation(Md5Password.class);
			if (en != null) {
				String fields[] = en.value();
				for (String field : fields) {
					String s = field.substring(0, 1).toUpperCase()
							+ field.substring(1);
					Method get = c.getMethod("get" + s, null);
					Method set = c.getMethod("set" + s, get.getReturnType());
					String value = (String) get.invoke(parameter, null);
					if (value != null&&null!=encryptor)
						set.invoke(parameter, encryptor.encrypt(value));
				}
			}
			if (mp != null) {
				String[] password=mp.password();
				String[] salt=mp.salt();
				for(int i=0;i<password.length;i++){
					String pass=BeanUtilsBean.getInstance().getSimpleProperty(parameter, password[i]);
					String _salt=BeanUtilsBean.getInstance().getSimpleProperty(parameter, salt[i]);
					if(StringUtils.isNotEmpty(pass)&&StringUtils.isNotEmpty(_salt)){
						Md5PasswordEncoder mpe= new Md5PasswordEncoder();
						pass=mpe.encodePassword(pass, _salt);
						BeanUtilsBean.getInstance().setProperty(parameter, password[i], pass);
					}
					
				}
				
			}
			returnValue = invocation.proceed();
		} else {
			returnValue = invocation.proceed();
		}
		return returnValue;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
	 */
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)
	 */
	public void setProperties(Properties properties) {
		String imp=properties.getProperty(SECRET_IMP);
		try {
			Class cls = Class.forName(imp); 
			encryptor =(Encryptor)cls.newInstance();	
		} catch (Exception e) {
			logger.error("Initialization encryption error!\n", e);
		} 
		encryptor.setPassword(properties.getProperty(SECRET_KEY));
	}
	

}