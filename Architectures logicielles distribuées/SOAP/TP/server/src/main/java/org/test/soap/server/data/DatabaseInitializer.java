package org.test.soap.server.data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.test.soap.server.model.Employee;
import org.test.soap.server.repository.EmployeeRepository;
import org.test.soap.server.service.EmployeeService;
import org.test.soap.server.service.EmployeeServiceImpl;

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
					new org.test.soap.server.model.Employee("Joe")));
			logger.info("Preloading database with " + repository.save(
					new org.test.soap.server.model.Employee("Jane")));
			logger.info("Preloading database with " + repository.save(
					new org.test.soap.server.model.Employee("Steve")));
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
