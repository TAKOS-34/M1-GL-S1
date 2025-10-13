package org.test.rmi.client.config;

import java.util.logging.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.test.rmi.common.interfaces.HelloService;

// Classe de configuration pour le client RMI
@Configuration
public class RMIClientConfig {
    public final Logger logger = Logger.getLogger(this.getClass().getName());
    // Création d'un proxy pour le service RMI
    @Bean
    RmiProxyFactoryBean proxyFactoryBean() {
        String rmiServerURL = String.format("rmi://localhost:1099/%s", HelloService.class.getSimpleName()); // URL du serveur RMI
        logger.info("URL du serveur RMI : " + rmiServerURL); // Journalisation de l'URL du serveur
        RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
        proxy.setServiceInterface(HelloService.class); // Définir l'interface du service
        proxy.setServiceUrl(rmiServerURL); // Définir l'URL du service
        proxy.afterPropertiesSet(); // Initialiser le proxy
        return proxy; // Retourner le proxy
    }
}