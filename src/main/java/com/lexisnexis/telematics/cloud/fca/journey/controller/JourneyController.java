package com.lexisnexis.telematics.cloud.fca.journey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lexisnexis.telematics.cloud.fca.journey.s3.FcaAwsService;

//import org.springframework.security.access.prepost.PreAuthorize;

@RestController
public class JourneyController {

	@Autowired
	private FcaAwsService fcaAwsService;
	
	//POST ../journeys/
	
	/*
	 * @PreAuthorize("PERM") public void postJourney() {
	 * 
	 * }
	 */

	/** Validating JWT
	 * Expose a POST API with mapping /authenticate. 
	 * On passing correct username and password it will generate a JSON Web Token(JWT)
	 * It will allow access only if request has a valid JSON Web Token(JWT
	 * */	
	@RequestMapping(value = "/writeDataToS3", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody String fcaInput) throws Exception {
		return ResponseEntity.ok(fcaAwsService.writeDataToS3(fcaInput));
	}	
	
	
	
}
