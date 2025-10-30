package org.test.soap.client.main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"org.test.soap.client.generated",
        "org.test.soap.client.config",
        "org.test.soap.client.ui",
})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}