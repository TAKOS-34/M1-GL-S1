package org.anonbnr.web_services.employeeservice.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//EntityScan annotation to specify the package containing JPA entities
@EntityScan(basePackages = {
		"org.anonbnr.web_services.employeeservice.model"
})
//EnableJpaRepositories annotation to specify the package for JPA repositories
@EnableJpaRepositories(basePackages = {
		"org.anonbnr.web_services.employeeservice.repository"
})
//Main Spring Boot application class with configurations for package scanning
@SpringBootApplication(scanBasePackages = {
		"org.anonbnr.web_services.employeeservice.exceptions",
		"org.anonbnr.web_services.employeeservice.service",
		"org.anonbnr.web_services.employeeservice.data",
		"org.anonbnr.web_services.employeeservice.publisher",
})
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}