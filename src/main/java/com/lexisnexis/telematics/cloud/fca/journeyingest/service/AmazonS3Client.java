package com.lexisnexis.telematics.cloud.fca.journeyingest.service;

import java.io.ByteArrayInputStream;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Configuration
@Component
public class AmazonS3Client {

	private AmazonS3 amazonS3 = null;

	public AmazonS3Client() {
		super();
		amazonS3 = AmazonS3ClientBuilder.defaultClient();
	}

	public void putObject(String bucketName, String key, byte[] zippedBytes, Map<String, String> userMetadata) {
		ByteArrayInputStream bytearray = new ByteArrayInputStream(zippedBytes);
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(zippedBytes.length);
		objectMetadata.setContentType("application/gzip");

		objectMetadata.setUserMetadata(userMetadata);
		amazonS3.putObject(new PutObjectRequest(bucketName, key, bytearray, objectMetadata));
	}

}
