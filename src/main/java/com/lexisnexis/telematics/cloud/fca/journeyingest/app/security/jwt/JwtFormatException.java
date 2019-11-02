package com.lexisnexis.telematics.cloud.fca.journeyingest.app.security.jwt;

public class JwtFormatException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private static transient final String DEFAULT_ERROR_MESSAGE = "An error has occurred due to the Jwt format.";
    
    public JwtFormatException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public JwtFormatException(String message) {
        super(message);
    }

    public JwtFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtFormatException(Throwable cause) {
        super(cause);
    }
	
}