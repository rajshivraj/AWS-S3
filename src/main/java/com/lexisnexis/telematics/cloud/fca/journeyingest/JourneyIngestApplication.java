package com.lexisnexis.telematics.cloud.fca.journeyingest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@ImportResource({ "classpath*:/META-INF/spring/app-context.xml", "classpath*:/META-INF/spring/security-context.xml"})
@EnableWebSecurity
public class JourneyIngestApplication {

    public static void main(String[] args) {
        SpringApplication.run(new Class[] {JourneyIngestApplication.class}, args);
    }
}
