package org.test.rmi.server.main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Classe principale de l'application
@SpringBootApplication(scanBasePackages = {
        "org.test.rmi.server.impl",
        "org.test.rmi.server.config"
})

public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
