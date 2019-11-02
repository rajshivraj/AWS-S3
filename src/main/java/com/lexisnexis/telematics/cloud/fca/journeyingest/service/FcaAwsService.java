package com.lexisnexis.telematics.cloud.fca.journeyingest.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lexisnexis.telematics.cloud.fca.journeyingest.service.AmazonS3Client;

@Configuration
@Component
public class FcaAwsService {

	private static final Logger log = LoggerFactory.getLogger(FcaAwsService.class);

	@Autowired(required = true)
	AmazonS3Client amazonS3Client;

	@Value("${s3.bucket.oem.fca.raw}")
	private String s3BucketOEMRaw;

	@Value("${s3.bucket.oem.fca.rawRejected}")
	private String s3BucketOEMRawRejected;

	@Value("${s3.path.prefix}")
	private String s3PathPrefix;

	public void writeDataToS3(String jsonJourney) {
		Map<String, String> metadata = buildFileMetadata(jsonJourney);
		uploadGzippedDataToS3(jsonJourney, metadata);
	}

	private void uploadGzippedDataToS3(String data, Map<String, String> userMetadata) {
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
			long startTm2 = System.currentTimeMillis();
			amazonS3Client.putObject(s3BucketOEMRaw, buildS3Name(), byteArrayOutputStream.toByteArray(), userMetadata) ;
			long endTm2 = System.currentTimeMillis();
			log.info("Uploadeded to S3 duration " + (endTm2 - startTm2));
		} catch ( Exception e) {
			throw new FcaAwsServiceException("File Compress Or Upload Failure",e);
		}  finally {
			IOUtils.closeQuietly(gzipOut);
			IOUtils.closeQuietly(byteArrayOutputStream);
		}
	}

	private String buildS3Name() {
		DateTime dt = new DateTime(DateTimeZone.UTC); 
		String s3PathPrefixToUse = s3PathPrefix;
		String path = String.format("%syear=%04d/month=%2d/day=%02d/hour=%02d/min=%02d/%d_%s.json.gz",
				s3PathPrefixToUse, dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth(), dt.getHourOfDay(), dt.getMinuteOfHour(), dt.getMillis(),
				UUID.randomUUID()); 
		return path;
	}


	private Map<String,String> buildFileMetadata(String jsonJourney) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, Object> journeyMap = mapper.readValue(jsonJourney, new TypeReference<Map<String,String>>() { });
			String schemaVersion = (String)journeyMap.get("schemaVersion");
			String dataCatalogVersion = (String)journeyMap.get("dataCatalogVersion");
			Map<String,String> metadataMap = new  HashMap<String,String>();
			if(StringUtils.isNotEmpty(schemaVersion)) metadataMap.put("schemaVersion", schemaVersion);
			if(StringUtils.isNotEmpty(dataCatalogVersion)) metadataMap.put("dataCatalogVersion", dataCatalogVersion);
			return metadataMap;
		} catch (IOException e) {
			throw new FcaAwsServiceException("Failed to parse json payload", e); 
		}
	}

}