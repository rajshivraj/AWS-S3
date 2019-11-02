package com.lexisnexis.telematics.cloud.fca.journeyingest.infrastructure;

public class JwksClientKeyNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	    
	public JwksClientKeyNotFoundException() {
		super();
	}
	    
	public JwksClientKeyNotFoundException(String message) {
		super(message);       
	}

}