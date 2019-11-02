package com.lexisnexis.telematics.cloud.fca.journeyingest.app.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;

public interface JwtFactory {

	FCAJwt create(io.jsonwebtoken.Jwt<Header, Claims> jwt);
	
}
