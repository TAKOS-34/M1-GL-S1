package org.anonbnr.web_services.employeeservice.data;

import org.anonbnr.web_services.employeeservice.model.Employee;
import org.anonbnr.web_services.employeeservice.repository.EmployeeRepository;
import org.anonbnr.web_services.employeeservice.service.EmployeeService;
import org.anonbnr.web_services.employeeservice.service.EmployeeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Configuration class for initializing the database with test data
@Configuration
public class DatabaseInitializer {
	// Logger instance for logging information
	private Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
	
	// Bean that runs on application startup to preload the database
	@Bean
	CommandLineRunner initDataBase(EmployeeRepository repository) {
		return args -> {
			// Preloading the database with test Employee data and logging each entry
			logger.info("Preloading database with " + repository.save(
					new Employee("Joe")));
			logger.info("Preloading database with " + repository.save(
					new Employee("Jane")));
			logger.info("Preloading database with " + repository.save(
					new Employee("Steve")));
			logger.info("Preloading database with " + repository.save(
					new Employee("Alice")));
			logger.info("Preloading database with " + repository.save(
					new Employee("Bob")));
		};
	}
	
	// Bean that runs on application startup to provide an implementation for the web service
	@Bean
	EmployeeService initEmployeeService() {
		return new EmployeeServiceImpl();
	}
}
