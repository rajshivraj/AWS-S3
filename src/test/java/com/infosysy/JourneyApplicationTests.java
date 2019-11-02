package com.infosysy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.lexisnexis.telematics.cloud.fca.journey.JourneyApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JourneyApplication.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class) 
@ActiveProfiles("unit")
public class JourneyApplicationTests {
    
    @Test
    public void contextLoads() {
    }

	

}
