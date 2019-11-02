package com.lexisnexis.telematics.cloud.fca.journey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.lexisnexis.telematics.cloud.fca.journey.s3.FcaAwsService;

@SpringBootApplication
@Configuration
@EnableAsync
public class JourneyApplication {

	private static final Logger LOG = LoggerFactory.getLogger(JourneyApplication.class);

	/*
	 * @Autowired FcaAwsService fcaAwsService;
	 * 
	 * @Autowired private org.springframework.core.env.Environment environment;
	 * 
	 * public static String ACTIVE_PROFILE = "dev";
	 */

	/*
	 * @Override public void run(String... args) throws Exception {
	 * 
	 * String[] activeProfiles = environment.getActiveProfiles(); if
	 * (activeProfiles.length > 0) ACTIVE_PROFILE = activeProfiles[0];
	 * LOG.info("ACTIVE_PROFILE   : " + ACTIVE_PROFILE); if
	 * (!ACTIVE_PROFILE.equals("unit")) { //fcaAwsService.loadConfig();
	 * LOG.info("loding is done  !!!"); fcaAwsService.writeDataToS3(); }
	 * 
	 * }
	 */

	public static void main(String[] args) {
		SpringApplication.run(JourneyApplication.class, args);

	}

}
