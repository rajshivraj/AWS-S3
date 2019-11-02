package com.lexisnexis.telematics.cloud.fca.journey.s3;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;

@Configuration
@Component
public class FcaAwsService {

	private static final Logger log = LoggerFactory.getLogger(FcaAwsService.class);

	@Autowired(required = true)
	AccessSSMParameterClient accessSSMParameterClient;

	@Autowired(required = true)
	AmazonS3Client amazonS3Client;

	private static Date date = new Date();

	/*
	 * @Value("${pulsar.AWS_KEY}") String AWS_KEY;// = AKIA4AHN33NMINWAJYMN
	 * 
	 * @Value("${pulsar.AWS_SECRET_KEY}") String AWS_SECRET_KEY;// =
	 * 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
	 */	
	/*
	 * @Value("${pulsar.LOCAL_FILE_PATH}") String LOCAL_FILE_PATH;//
	 */
	//private String CERT_CA;

	private String S3BUCKET_OEM_RAW;
	//private String S3BUCKET_OEM_RAW_REJECTED;

	@Value("${S3BucketOEMRawRejected}")
	private String S3BucketOEMRawRejected;
	@Value("${s3BucketOEMRaw}")
	private String s3BucketOEMRaw;

	private long processMessageCounter;
	private long startTime;

	public String writeDataToS3(String data) {
		String response = null;
		AWSSimpleSystemsManagement sSMClient = accessSSMParameterClient.init();
		S3BUCKET_OEM_RAW = accessSSMParameterClient.getParameter(sSMClient, s3BucketOEMRaw);
		//S3BUCKET_OEM_RAW_REJECTED = accessSSMParameterClient.getParameter(sSMClient, S3BucketOEMRawRejected);
		log.info("S3BUCKET_OEM_RAW    ::::::::::::::::::: " + S3BUCKET_OEM_RAW);

		try {
			response = uploadDataToS3bucket(data);
			log.info("Upload Status::" + response);
		} catch (Exception ex) {
			log.error("Unable to connect / create consumer. ");
		} finally {
			System.out.println("Time finish of process .................................. " + date.getTime());
		}
		return response;
	}

    private String uploadDataToS3bucket(String data) {
    	String response = null;
    	ByteArrayOutputStream byteArrayOutputStream = null;
    	try {
    		byteArrayOutputStream = new ByteArrayOutputStream();
    		byteArrayOutputStream.write(data.getBytes());
    		amazonS3Client.putObject("telematics-us-dev-gm-test", getPath() + System.currentTimeMillis() + "_" + UUID.randomUUID() + ".json.gz", byteArrayOutputStream.toByteArray());
    		response = "SUCCESS";
    	}catch(AmazonServiceException | IOException ex) {
    		log.error("Exception in uploadDataToS3bucket() " + ex.getMessage());
    		response = "ERROR"+ex.getMessage();
    		return response; 
	} finally {
		try {
			if (byteArrayOutputStream != null)
				byteArrayOutputStream.close();
		} catch (Exception ex) {
			log.error("Exception in uploadDataToS3bucket::" + ex.getMessage());
		}
	}
    	return response;
    }	
	
	private static String getPath() {
		date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH mm");
		String strDate = formatter.format(date);
		log.info(strDate);
		strDate = strDate.replace(' ', '-');
		String[] dateArray;
		String[] dateLabelArray = { "year", "month", "day", "hour", "minute" };
		dateArray = strDate.split("-");
		String finalSting = "";
		for (int i = 0; i < 5; i++) {
			finalSting = finalSting + dateLabelArray[i] + "=" + dateArray[i] + File.separator;
		}
		return finalSting;
	}

	private String uploadGzippedDataToS3(String data) {
		String response = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		GZIPOutputStream gzipOut = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			gzipOut = new GZIPOutputStream(byteArrayOutputStream);
			long startGzipTime = System.currentTimeMillis();
			gzipOut.write(data.toString().getBytes());
			gzipOut.finish();
			
			long endGzipTime = System.currentTimeMillis();
			log.info("Gzip time taken " + (endGzipTime - startGzipTime));
			UUID uuid = UUID.randomUUID();
			long startTm2 = System.currentTimeMillis();
			amazonS3Client.putObject("telematics-us-dev-gm-test", getPath() + System.currentTimeMillis() + "_" + uuid + ".json.gz", byteArrayOutputStream.toByteArray());
			long endTm2 = System.currentTimeMillis();
			log.info("Uploadeded to S3 duration " + (endTm2 - startTm2));
			response = "SUCCESS";
		} catch (IOException | IllegalStateException | ArrayIndexOutOfBoundsException ex) {
			log.error("Exception in uploadGzippedDataToS3() " + ex.getMessage());
    		response = "ERROR"+ex.getMessage();
    		return response; 
		} finally {
			try {
				if (gzipOut != null)
					gzipOut.close();
				if (byteArrayOutputStream != null)
					byteArrayOutputStream.close();
			} catch (Exception ex) {
				log.error("Exception in uploadGzippedDataToS3::" + ex.getMessage());
			}
		}
		
		return "Uploading Successfull -> ";
	}
	
	
}
