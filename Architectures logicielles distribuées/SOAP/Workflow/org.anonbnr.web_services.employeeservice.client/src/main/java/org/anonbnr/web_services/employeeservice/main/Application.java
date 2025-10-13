package org.anonbnr.web_services.employeeservice.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"org.anonbnr.web_services.employeeservice.client",
        "org.anonbnr.web_services.employeeservice.config",
        "org.anonbnr.web_services.employeeservice.ui",
})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
