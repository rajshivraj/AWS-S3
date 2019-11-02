package com.lexisnexis.telematics.cloud.fca.journeyingest.app.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.lexisnexis.telematics.cloud.fca.journeyingest.infrastructure.Jwk;
import com.lexisnexis.telematics.cloud.fca.journeyingest.infrastructure.JwksClient;
import com.lexisnexis.telematics.cloud.jsonwebkey.JsonWebKey;
import com.lexisnexis.telematics.cloud.jsonwebkey.JsonWebKeyService;
import com.lexisnexis.telematics.cloud.jsonwebkey.KeySourceEnum;

public class JwtServiceImpl implements JwtService, InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtServiceImpl.class);

	private JsonWebKeyService jsonWebKeyService;	

	private JwksClient jwksClient;
	
	private JwtFactory jwtFactory;	
	
	public void setJsonWebKeyService(JsonWebKeyService jsonWebKeyService) {
		this.jsonWebKeyService = jsonWebKeyService;
	}

	public void setJwtFactory(JwtFactory fcaJwtFactory) {
		this.jwtFactory = fcaJwtFactory;
	}

	public void setJwksClient(JwksClient jsksClient) {
		this.jwksClient = jsksClient;
	}

	@Override
	public void afterPropertiesSet() throws Exception {		
		Assert.notNull(jsonWebKeyService, "jsonWebKeyService must be set");
		Assert.notNull(jwksClient, "jwksClient must be set");
		Assert.notNull(jwtFactory, "jwtFactory must be set");
	}
	
    private String convertBytesToHex(byte[] hashInBytes) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hashInBytes.length; i++) {
            String hex = Integer.toHexString(0xff & hashInBytes[i]);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }

    /*
     * Method to save the Jwk.  This will allow local lookups for Jwks matching the jwk key id in the future
     * without the need to make request to the JwksClient.
     */
    private JsonWebKey saveJwk(Jwk jwk) {
    	JsonWebKey jsonWebKey = new JsonWebKey();
		jsonWebKey.setAlgorithm(jwk.getAlgorithm());
		jsonWebKey.setExponent(jwk.getExponent());
		jsonWebKey.setKeyId(jwk.getKeyId());
		jsonWebKey.setKeySource(KeySourceEnum.FCA);
		jsonWebKey.setKeyType(jwk.getKeyType());
		jsonWebKey.setModuluous(jwk.getModulus());
		jsonWebKey.setUse(jwk.getUse());
		jsonWebKey.setPublicKey("unknown");
		
		jsonWebKeyService.save(jsonWebKey);
		return jsonWebKey;
    }
    
    private JsonWebKey getKey(final String token) {		
		int i = token.lastIndexOf('.');
		String withoutSignature = token.substring(0, i+1);
		io.jsonwebtoken.Jwt<Header,Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);			

		String keyId = (String)untrusted.getHeader().get(JwtTokenConstants.KID);
		Optional<JsonWebKey> securityOauth2JwtRSAKey = jsonWebKeyService.getSecurityKey(
				keyId, KeySourceEnum.FCA);
		if (securityOauth2JwtRSAKey.isPresent()) {
			return securityOauth2JwtRSAKey.get();
		}
		//Get Jwk from JwksClient when not present locally
		Jwk jwk = jwksClient.getJwk(keyId);
		
		//Save Jwk in locally and return saved SecurityOauth2JwtRSAKey
		return saveJwk(jwk);		
	}
    
	private io.jsonwebtoken.Jwt parseJwt(String token) {
		try {
			JsonWebKey jsonWebKey = getKey(token);
			byte[] modulusBytes = Base64.getUrlDecoder().decode(jsonWebKey.getModuluous());
		    byte[] exponentBytes = Base64.getUrlDecoder().decode(jsonWebKey.getExponent());	        
	        BigInteger modulus = new BigInteger(convertBytesToHex(modulusBytes), 16);
	        BigInteger exponent = new BigInteger(convertBytesToHex(exponentBytes), 16);
 
	        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
	        KeyFactory factory = KeyFactory.getInstance(jsonWebKey.getKeyType());		
	        RSAPublicKey publicKey = (RSAPublicKey) factory.generatePublic(rsaPublicKeySpec);
	        
	        return Jwts.parser().setSigningKey(publicKey).parse(token);
		} catch (JwtFormatException jwtFormatException) {
			throw jwtFormatException;			
		} catch (MalformedJwtException | UnsupportedJwtException formatException){
			LOGGER.error("Unable to parse JWT, format exception: " + formatException.getMessage());
			throw new JwtFormatException("Unable to parse JWT, format exception for JWT " + token);
		} catch (ExpiredJwtException expiredJwtException) {
			LOGGER.error("Unable to parse JWT, expired exception: " + expiredJwtException.getMessage());
			throw new JwtExpiredException("Unable to parse JWT, expired exception: " + expiredJwtException.getMessage());
		} catch (Exception e) {
			LOGGER.error("Unable to parse JWT, unhandled exception: " + e.getMessage());			
			throw new JwtDecodeException("Unable to parse JWT, unhandled exception: " + e.getMessage());
		}
	}	
	
	private void validateToken(FCAJwt fcaJwt) {
		if (!fcaJwt.getTokenUse().equalsIgnoreCase("access")) {
			throw new JwtDecodeException("Token's use not as expected");
		}
	}

	@Override
	public FCAJwt decodeAccessToken(String token) {
		io.jsonwebtoken.Jwt jwt = parseJwt(token);
		FCAJwt fcaJwt = jwtFactory.create(jwt);
		validateToken(fcaJwt);

		return fcaJwt;		
	}
	
}
