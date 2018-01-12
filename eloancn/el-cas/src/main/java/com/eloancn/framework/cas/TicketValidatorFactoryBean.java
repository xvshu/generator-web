package com.eloancn.framework.cas;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import org.jasig.cas.client.proxy.Cas20ProxyRetriever;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorage;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorageImpl;
import org.jasig.cas.client.ssl.AnyHostnameVerifier;
import org.jasig.cas.client.ssl.HttpURLConnectionFactory;
import org.jasig.cas.client.ssl.HttpsURLConnectionFactory;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.ReflectUtils;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

public class TicketValidatorFactoryBean implements FactoryBean<TicketValidator> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private boolean allowAnyProxy;

	private String allowedProxyChains;

	private String casServerUrlPrefix;

	private ProxyGrantingTicketStorage proxyGrantingTicketStorage = new ProxyGrantingTicketStorageImpl();
	
	private boolean renew = Boolean.FALSE;

	private String proxyCallbackUrl;

	private String encoding;
	
	private String hostnameVerifier = AnyHostnameVerifier.class.getName();
	
	private String hostnameVerifierConfig;
	
	private String sslConfigFile;
	
	public void setAllowAnyProxy(boolean allowAnyProxy) {
		this.allowAnyProxy = allowAnyProxy;
	}

	public void setAllowedProxyChains(String allowedProxyChains) {
		this.allowedProxyChains = allowedProxyChains;
	}


	public void setCasServerUrlPrefix(String casServerUrlPrefix) {
		this.casServerUrlPrefix = casServerUrlPrefix;
	}


	public void setProxyGrantingTicketStorage(
			ProxyGrantingTicketStorage proxyGrantingTicketStorage) {
		this.proxyGrantingTicketStorage = proxyGrantingTicketStorage;
	}


	public void setRenew(boolean renew) {
		this.renew = renew;
	}


	public void setProxyCallbackUrl(String proxyCallbackUrl) {
		this.proxyCallbackUrl = proxyCallbackUrl;
	}


	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}


	public void setHostnameVerifier(String hostnameVerifier) {
		this.hostnameVerifier = hostnameVerifier;
	}


	public void setHostnameVerifierConfig(String hostnameVerifierConfig) {
		this.hostnameVerifierConfig = hostnameVerifierConfig;
	}


	public void setSslConfigFile(String sslConfigFile) {
		this.sslConfigFile = sslConfigFile;
	}


	@Override
	public TicketValidator getObject() throws Exception {

		final Cas20ServiceTicketValidator validator;

		if (allowAnyProxy || CommonUtils.isNotBlank(allowedProxyChains)) {
			final Cas20ProxyTicketValidator v = new Cas20ProxyTicketValidator(this.casServerUrlPrefix);
			v.setAcceptAnyProxy(allowAnyProxy);
			v.setAllowedProxyChains(CommonUtils.createProxyList(allowedProxyChains));
			validator = v;
		} else {
			validator = new Cas20ServiceTicketValidator(casServerUrlPrefix);
		}
		validator.setProxyCallbackUrl(this.proxyCallbackUrl);
		validator.setProxyGrantingTicketStorage(this.proxyGrantingTicketStorage);

		final HttpURLConnectionFactory factory = new HttpsURLConnectionFactory(
				getHostnameVerifier(), getSSLConfig());
		validator.setURLConnectionFactory(factory);

		validator.setProxyRetriever(new Cas20ProxyRetriever(
				this.casServerUrlPrefix, this.encoding, factory));
		validator.setRenew(this.renew);
		validator.setEncoding(this.encoding);

		return validator;
	}


	/**
	 * Gets the ssl config to use for HTTPS connections if one is configured for
	 * this filter.
	 * 
	 * @return Properties that can contains key/trust info for Client Side
	 *         Certificates
	 */
	protected Properties getSSLConfig() {
		final Properties properties = new Properties();

		if (this.sslConfigFile != null) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(this.sslConfigFile);
				properties.load(fis);
				logger.trace("Loaded {} entries from {}", properties.size(),
						this.sslConfigFile);
			} catch (final IOException ioe) {
				logger.error(ioe.getMessage(), ioe);
			} finally {
				CommonUtils.closeQuietly(fis);
			}
		}
		return properties;
	}

	  /**
     * Gets the configured {@link HostnameVerifier} to use for HTTPS connections
     * if one is configured for this filter.
     * @return Instance of specified host name verifier or null if none specified.
     */
    protected HostnameVerifier getHostnameVerifier() {
        if (this.hostnameVerifier != null) {
            if (this.hostnameVerifierConfig != null) {
                return ReflectUtils.newInstance(this.hostnameVerifier, this.hostnameVerifierConfig);
            } else {
                return ReflectUtils.newInstance(this.hostnameVerifier);
            }
        }
        return null;
    }
	@Override
	public Class<?> getObjectType() {
		return TicketValidator.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
