package com.lexisnexis.telematics.cloud.fca.journeyingest.infrastructure;

public interface JwksClient {

	Jwk getJwk(String keyId);
	
}
