package com.lexisnexis.telematics.cloud.fca.journeyingest.service;

public class FcaAwsServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3152479327798380615L;

	public FcaAwsServiceException() {
	}

	public FcaAwsServiceException(String message) {
		super(message);

	}

	public FcaAwsServiceException(Throwable cause) {
		super(cause);
	}

	public FcaAwsServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public FcaAwsServiceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
