package org.anonbnr.web_services.employeeservice.config;

import java.net.MalformedURLException;
import java.net.URL;

import org.anonbnr.web_services.employeeservice.generated.EmployeeService;
import org.anonbnr.web_services.employeeservice.generated.EmployeeServiceImplService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* Configuration class for the EmployeeService.
*/
@Configuration
public class EmployeeServiceConfig {
	 private static final String SERVICE_URL = "http://localhost:8080/employeeservice?wsdl";
	
	 /**
	  * Creates a EmployeeService proxy bean.
	  *
	  * @return an instance of EmployeeService
	  * @throws MalformedURLException if the URL is invalid
	  */
	 @Bean
	 EmployeeService employeeServiceProxy() throws MalformedURLException {
	     return new EmployeeServiceImplService(new URL(SERVICE_URL)).getEmployeeServiceImplPort();
	 }
}
