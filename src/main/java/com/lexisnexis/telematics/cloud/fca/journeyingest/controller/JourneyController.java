package com.lexisnexis.telematics.cloud.fca.journeyingest.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lexisnexis.telematics.cloud.fca.journeyingest.dto.FcaServiceResponseDto;
import com.lexisnexis.telematics.cloud.fca.journeyingest.service.FcaAwsService;

@RestController
public class JourneyController {

	private static final Logger log = LoggerFactory.getLogger(JourneyController.class);

	@Autowired
	private FcaAwsService fcaAwsService;

	@RequestMapping(value = "/journeys", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_PERM_JOURNEY_INGEST')")
	public @ResponseBody String ingestJourney () {
		return "Authorized";
	}

	/**
	 * Expose a POST vehicle-signals API
	 */
	@RequestMapping(value = "/v2.0/ubi/vehicle-signals", method = RequestMethod.POST)
	//@PreAuthorize("hasRole('ROLE_PERM_JOURNEY_INGEST')")
	public FcaServiceResponseDto vehicleSignals(@RequestBody String fcaInput) {
		fcaAwsService.writeDataToS3(fcaInput);
		return new FcaServiceResponseDto("Vehicle data uploaded successfully", HttpStatus.OK.value());
	}

	@ExceptionHandler(Exception.class)
	public @ResponseBody FcaServiceResponseDto handleHttpException(Exception exception,
			HttpServletResponse response, Object handler) {
		log.error("Exception during FCA request processing", exception);
		FcaServiceResponseDto responseDto = new FcaServiceResponseDto("Exception during file upload", HttpStatus.INTERNAL_SERVER_ERROR.value());
		return responseDto;
	}

}