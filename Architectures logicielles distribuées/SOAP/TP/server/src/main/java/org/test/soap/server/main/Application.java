package org.test.soap.server.main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//EntityScan annotation to specify the package containing JPA entities
@EntityScan(basePackages = {
		"org.test.soap.server.model"
})
//EnableJpaRepositories annotation to specify the package for JPA repositories
@EnableJpaRepositories(basePackages = {
		"org.test.soap.server.repository"
})
//Main Spring Boot application class with configurations for package scanning
@SpringBootApplication(scanBasePackages = {
		"org.test.soap.server.exceptions",
		"org.test.soap.server.service",
		"org.test.soap.server.data",
		"org.test.soap.server.publisher",
})
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}