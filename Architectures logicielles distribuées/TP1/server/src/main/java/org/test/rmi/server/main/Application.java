package org.test.rmi.server.main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Objects;

@SpringBootApplication(scanBasePackages = {
        "org.test.rmi.server.impl",
        "org.test.rmi.server.config"
})

public class Application {
    public static void main(String[] args) {
        String policyUrl = Objects.requireNonNull(Application.class.getClassLoader().getResource("security.policy")).toString();
        System.setProperty("java.security.policy", policyUrl);
        System.setSecurityManager(new SecurityManager());

        SpringApplication.run(Application.class, args);
    }
}
