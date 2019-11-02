package com.lexisnexis.telematics.cloud.fca.journeyingest.dto;

public class FcaServiceResponseDto {

	private int code;
	private String status;

	public FcaServiceResponseDto() {
		super();
	}

	public FcaServiceResponseDto(String status, int code) {
		super();
		this.status = status;
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
	
	
	
}
