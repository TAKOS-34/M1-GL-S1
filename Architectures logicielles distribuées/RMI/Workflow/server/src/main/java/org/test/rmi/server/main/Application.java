package org.test.rmi.server.main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Classe principale de l'application
@SpringBootApplication(scanBasePackages = {
        "org.test.rmi.server.impl", // Package pour l'implémentation du service
        "org.test.rmi.server.config" // Package pour la configuration
})

public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args); // Démarre l'application Spring Boot
    }
}
