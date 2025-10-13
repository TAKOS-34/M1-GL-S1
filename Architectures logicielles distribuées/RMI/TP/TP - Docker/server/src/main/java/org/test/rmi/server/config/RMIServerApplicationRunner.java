package org.test.rmi.server.config;
import java.rmi.Naming;
import java.util.logging.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.stereotype.Component;
import org.test.rmi.common.interfaces.CabinetService;
import org.test.rmi.server.impl.Cabinet;

@Component
public class RMIServerApplicationRunner implements ApplicationRunner {
    private Cabinet cabinetService;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public RMIServerApplicationRunner(Cabinet cabinetService) {
        this.cabinetService = cabinetService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (String serviceName : Naming.list("rmi://localhost:1099")) {
            System.out.println("Service RMI enregistré : " + serviceName);
        }
    }

    @Bean
    RmiServiceExporter ServiceExporter() {
        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName(CabinetService.class.getSimpleName());
        exporter.setServiceInterface(CabinetService.class);
        exporter.setService(cabinetService);
        exporter.setRegistryPort(1099);
        logger.info("Serveur RMI démarré sur le port 1099");
        System.setProperty("java.rmi.server.hostname", "rmi-server");
        return exporter;
    }
}