package com.lexisnexis.telematics.cloud.fca.journeyingest.app.security.jwt;

public class JwtExpiredException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private static transient final String DEFAULT_ERROR_MESSAGE = "An error decoding: Jwt is expired.";
    
    public JwtExpiredException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public JwtExpiredException(String message) {
        super(message);
    }

    public JwtExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtExpiredException(Throwable cause) {
        super(cause);
    }

}