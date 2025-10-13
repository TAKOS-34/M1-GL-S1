package org.test.rmi.server.config;
import java.rmi.Naming;
import java.util.logging.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.stereotype.Component;
import org.test.rmi.common.interfaces.HelloService;
import org.test.rmi.server.impl.HelloServiceImpl;

@Component
public class RMIServerApplicationRunner implements ApplicationRunner {
    private HelloServiceImpl helloService;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    // Injection de dépendance de HelloServiceImpl
    public RMIServerApplicationRunner(HelloServiceImpl helloService) {
        this.helloService = helloService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Liste des services RMI enregistrés
        for (String serviceName : Naming.list("rmi://localhost:1099")) {
            System.out.println("Service RMI enregistré : " + serviceName);
        }
    }

    // Exposition du service HelloService en tant que service RMI
    @Bean
    RmiServiceExporter helloServiceExporter() {
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName(HelloService.class.getSimpleName()); // Nom duservice
        exporter.setServiceInterface(HelloService.class); // Interface du service
        exporter.setService(helloService); // Implémentation du service
        exporter.setRegistryPort(1099); // Port pour le registre RMI
        logger.info("Serveur RMI démarré sur le port 1099"); // Journalisation du démarrage du serveur
        return exporter; // Retourne l'exportateur de service RMI
    }
}