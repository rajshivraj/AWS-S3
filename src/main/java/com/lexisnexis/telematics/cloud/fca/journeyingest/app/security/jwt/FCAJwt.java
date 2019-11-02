package com.lexisnexis.telematics.cloud.fca.journeyingest.app.security.jwt;

public class FCAJwt {
	
	private String version;
		
	private String algorithm;
		
	private String keyId;
		
	private String scope;
	
	private Long clientId;
	
	private String tokenUse;

	private Long exp;

	private Long iat;

	private String jti;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public String getTokenUse() {
		return tokenUse;
	}

	public void setTokenUse(String tokenUse) {
		this.tokenUse = tokenUse;
	}

	public Long getExp() {
		return exp;
	}

	public void setExp(Long exp) {
		this.exp = exp;
	}

	public Long getIat() {
		return iat;
	}

	public void setIat(Long iat) {
		this.iat = iat;
	}

	public String getJti() {
		return jti;
	}

	public void setJti(String jti) {
		this.jti = jti;
	}

	public String getUsername() {
		return scope;
	}

}
