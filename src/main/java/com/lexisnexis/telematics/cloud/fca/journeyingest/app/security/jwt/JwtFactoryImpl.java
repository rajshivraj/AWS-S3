package com.lexisnexis.telematics.cloud.fca.journeyingest.app.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwsHeader;

public class JwtFactoryImpl implements JwtFactory {

	@Override
	public FCAJwt create(io.jsonwebtoken.Jwt<Header, Claims> jsonWebToken) {
		FCAJwt fcaJwt = new FCAJwt();

		fcaJwt.setAlgorithm((String)jsonWebToken.getHeader().get(JwsHeader.ALGORITHM));
		fcaJwt.setVersion((String)jsonWebToken.getHeader().get(JwtTokenConstants.VER));
		fcaJwt.setKeyId((String)jsonWebToken.getHeader().get(JwsHeader.KEY_ID));		
		fcaJwt.setScope((String)jsonWebToken.getBody().get(JwtTokenConstants.SCOPE));
		fcaJwt.setTokenUse(((String)jsonWebToken.getBody().get(JwtTokenConstants.TOKEN_USE)));
		fcaJwt.setIat(Long.valueOf(((Integer)jsonWebToken.getBody().get(JwtTokenConstants.IAT))).longValue());
		fcaJwt.setExp(Long.valueOf(((Integer)jsonWebToken.getBody().get(JwtTokenConstants.EXP))).longValue());		
		fcaJwt.setJti((String)jsonWebToken.getBody().get(JwtTokenConstants.JTI));

		return fcaJwt;
	}

}
