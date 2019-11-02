package com.lexisnexis.telematics.cloud.fca.journeyingest.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Jwk {	
	
	@JsonProperty("kid")
	private String keyId;
	
	@JsonProperty("kty")
	private String keyType;
	
	@JsonProperty("alg")
	private String algorithm;
	
	@JsonProperty("n")
	private String modulus;
	
	@JsonProperty("e")
	private String exponent;
	
	@JsonProperty("use")
	private String use;

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getModulus() {
		return modulus;
	}

	public void setModulus(String modulus) {
		this.modulus = modulus;
	}

	public String getExponent() {
		return exponent;
	}

	public void setExponent(String exponent) {
		this.exponent = exponent;
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}
	
}
