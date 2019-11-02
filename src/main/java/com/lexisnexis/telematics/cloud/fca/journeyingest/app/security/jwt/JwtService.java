package com.lexisnexis.telematics.cloud.fca.journeyingest.app.security.jwt;

public interface JwtService {

	FCAJwt decodeAccessToken(String token);
	
}
