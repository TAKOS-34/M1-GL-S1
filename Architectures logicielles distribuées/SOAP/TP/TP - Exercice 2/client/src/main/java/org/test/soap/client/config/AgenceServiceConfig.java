package org.test.soap.client.config;
import java.net.MalformedURLException;
import java.net.URL;
import org.test.soap.client.generated.AgenceService;
import org.test.soap.client.generated.AgenceServiceImplService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgenceServiceConfig {
	 private static final String SERVICE_URL = "http://localhost:8080/agenceService?wsdl";

	 @Bean
	 AgenceService agenceServiceProxy() throws MalformedURLException {
	     return new AgenceServiceImplService(new URL(SERVICE_URL)).getAgenceServiceImplPort();
	 }
}
