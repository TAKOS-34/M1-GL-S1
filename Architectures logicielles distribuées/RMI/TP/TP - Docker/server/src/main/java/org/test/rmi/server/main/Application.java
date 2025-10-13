package org.test.rmi.server.main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.nio.file.Paths;
import java.util.Objects;

@SpringBootApplication(scanBasePackages = {
        "org.test.rmi.server.impl",
        "org.test.rmi.server.config"
})

public class Application {
    public static void main(String[] args) {
        // Security Manager
        String policyUrl = Objects.requireNonNull(Application.class.getClassLoader().getResource("security.policy")).toString();
        System.setProperty("java.security.policy", policyUrl);
        System.setSecurityManager(new SecurityManager());

        // Codebase
        String codebase = Paths.get("/app/commons").toUri().toString();
        System.setProperty("java.rmi.server.codebase", codebase);

        // Start Application
        SpringApplication.run(Application.class, args);
    }
}
