package org.test.soap.hotel.main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {
        "org.test.soap.hotel.model"
})

@EnableJpaRepositories(basePackages = {
        "org.test.soap.hotel.repository"
})

@SpringBootApplication(scanBasePackages = {
        "org.test.soap.hotel.exceptions",
        "org.test.soap.hotel.service",
        "org.test.soap.hotel.data",
        "org.test.soap.hotel.publisher",
        "org.test.soap.hotel.config"
})

public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}