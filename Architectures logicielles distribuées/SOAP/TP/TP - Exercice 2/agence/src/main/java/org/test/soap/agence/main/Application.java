package org.test.soap.agence.main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {
        "org.test.soap.agence.model"
})

@EnableJpaRepositories(basePackages = {
        "org.test.soap.agence.repository"
})

@SpringBootApplication(scanBasePackages = {
        "org.test.soap.agence.exceptions",
        "org.test.soap.agence.service",
        "org.test.soap.agence.data",
        "org.test.soap.agence.publisher",
})

public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}