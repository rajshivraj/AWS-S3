package com.lexisnexis.telematics.cloud.fca.journey.s3;

import java.io.ByteArrayInputStream;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
 
@Configuration
@Component
public class AmazonS3Client {
	
	static AmazonS3  amazonS3 = null;
	
	public void putObject(String bucketName,      String key,     byte[]  zippedBytes) {
		
		if (amazonS3 == null) amazonS3 =   AmazonS3ClientBuilder.defaultClient();

		ByteArrayInputStream bytearray =new ByteArrayInputStream(zippedBytes);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(zippedBytes.length);
        metadata.setContentType("application/gzip");
        try {
    		amazonS3.putObject(new PutObjectRequest(bucketName, key, bytearray  , metadata));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

		 
	}

}
