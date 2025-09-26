package org.test.rmi.server.config;
import java.rmi.Naming;
import java.util.logging.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.stereotype.Component;
import org.test.rmi.common.interfaces.AnimalService;
import org.test.rmi.server.impl.Animal;

@Component
public class RMIServerApplicationRunner implements ApplicationRunner {
    private Animal animalService;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public RMIServerApplicationRunner(Animal animalService) {
        this.animalService = animalService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (String serviceName : Naming.list("rmi://localhost:1099")) {
            System.out.println("Service RMI enregistré : " + serviceName);
        }
    }
    
    @Bean
    RmiServiceExporter helloServiceExporter() {
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName(AnimalService.class.getSimpleName());
        exporter.setServiceInterface(AnimalService.class);
        exporter.setService(animalService);
        exporter.setRegistryPort(1099);
        logger.info("Serveur RMI démarré sur le port 1099");
        return exporter;
    }
}