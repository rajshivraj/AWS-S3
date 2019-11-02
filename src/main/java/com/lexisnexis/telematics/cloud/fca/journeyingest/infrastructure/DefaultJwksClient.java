package com.lexisnexis.telematics.cloud.fca.journeyingest.infrastructure;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

public class DefaultJwksClient implements JwksClient, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJwksClient.class);    

    private RestOperations restTemplate;
    
    private int maxRetries = 3;
	
	private String proxyHost;

	private Integer proxyPort;
	
	private Integer connectTimeout = 0;
	
	private Integer readTimeout = 0;
	
	private String url = "https://cognito-idp.eu-west-1.amazonaws.com/eu-west-1_YrEQb69gX/.well-known/jwks.json";
	
	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}
	
	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public void setReadTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.url, "url must be set");
		initRestTemplate();		
	}

	private void initRestTemplate() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

		if(!(proxyHost==null || proxyHost.isEmpty())) {			
			requestFactory.setProxy(new Proxy(Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
		}
		
		if (connectTimeout > 0) {
			requestFactory.setConnectTimeout(connectTimeout);			
		}
		
		if (readTimeout > 0) {
			requestFactory.setReadTimeout(readTimeout);			
		}
		
		restTemplate = new RestTemplate(requestFactory);		
		((RestTemplate) restTemplate).setErrorHandler(new DefaultResponseErrorHandler());
	}
	
	@Override
	public Jwk getJwk(String keyId) {
		int attemptCount = 0;
        while (true) {
            try {
                attemptCount++;                
                JwkList jwkList = restTemplate.getForObject(this.url, JwkList.class);
                Optional<Jwk> jwk = jwkList.getJwks().stream()
                		.filter(j -> j.getKeyId().equals(keyId))
                		.findFirst();
                
                return jwk.orElseThrow(() -> new JwksClientKeyNotFoundException(String.format("JwksClient KeyId [%s] not found", keyId)));
            } catch(ResourceAccessException resourceAccessException) {
            	String logMessage = String.format("DefaultJwksClient.getAccessToken - ResourceAccessException while getting Jwks "
            		+ "(Attempt count [%d] of [%d])", 
            		attemptCount, maxRetries);                	
            	if (attemptCount < maxRetries) {
            		LOGGER.warn(logMessage);
            	} else {
            		LOGGER.warn(logMessage, resourceAccessException);
                    throw resourceAccessException;
            	}
            } catch(RestClientResponseException restClientResponseException) {
                LOGGER.warn(String.format("DefaultJwksClient.getAccessToken - RestClientResponseException [%d] while getting Jwks "
                	+ "(Attempt count [%d] of [%d])",
                    restClientResponseException.getRawStatusCode(), attemptCount, maxRetries), 
                	restClientResponseException);
                throw restClientResponseException;
            } catch (Throwable throwable) {                
                LOGGER.warn(String.format("DefaultJwksClient.getJwks - Unknown Exception while getting Jwks "
                		+ "(Attempt count [%d] of [%d])", 
                		attemptCount, maxRetries), throwable);                 
                throw throwable;                
            }
        }
	}

}
