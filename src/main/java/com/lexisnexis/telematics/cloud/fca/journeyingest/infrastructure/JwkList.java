package com.lexisnexis.telematics.cloud.fca.journeyingest.infrastructure;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwkList {

	@JsonProperty("keys")
	private List<Jwk> jwks;

	public List<Jwk> getJwks() {
		return jwks;
	}

	public void setJwks(List<Jwk> jwks) {
		this.jwks = jwks;
	}
	
}
