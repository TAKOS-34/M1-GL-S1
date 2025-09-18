package org.test.rmi.client.main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Classe principale de l'application
@SpringBootApplication(scanBasePackages = {
        "org.test.rmi.client.config" // Package pour la configuration du client
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args); // Démarre l'application Spring Boot
    }
}