package com.lexisnexis.telematics.cloud.fca.journey.s3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;

@Configuration
@Component
public class AccessSSMParameterClient {

	private static final Logger LOG = LoggerFactory.getLogger(AccessSSMParameterClient.class);

	public AWSSimpleSystemsManagement init() {
		AWSSimpleSystemsManagement simpleSystemsManagementClient =
				AWSSimpleSystemsManagementClientBuilder.defaultClient();

		return simpleSystemsManagementClient;
	}

	public String getParameter(AWSSimpleSystemsManagement simpleSystemsManagementClient, String parameter) {
		GetParameterRequest parameterRequest = new GetParameterRequest();
		parameterRequest.withName(parameter).setWithDecryption(Boolean.valueOf(true));
		GetParameterResult parameterResult = simpleSystemsManagementClient.getParameter(parameterRequest);
		LOG.info(" parameter got the value  : "+parameter);
		return parameterResult.getParameter().getValue();
	}
}