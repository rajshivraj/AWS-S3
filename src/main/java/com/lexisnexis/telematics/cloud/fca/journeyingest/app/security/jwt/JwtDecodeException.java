package com.lexisnexis.telematics.cloud.fca.journeyingest.app.security.jwt;

public class JwtDecodeException extends RuntimeException {

	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	    private static transient final String DEFAULT_ERROR_MESSAGE = "An error decoding Jwt has occurred.";
	    
	    public JwtDecodeException() {
	        super(DEFAULT_ERROR_MESSAGE);
	    }

	    public JwtDecodeException(String message) {
	        super(message);
	    }

	    public JwtDecodeException(String message, Throwable cause) {
	        super(message, cause);
	    }

	    public JwtDecodeException(Throwable cause) {
	        super(cause);
	    }

}
