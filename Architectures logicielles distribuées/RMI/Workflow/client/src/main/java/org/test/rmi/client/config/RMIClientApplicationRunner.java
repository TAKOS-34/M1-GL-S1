package org.test.rmi.client.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;
import org.test.rmi.common.interfaces.HelloService;

// Composant qui s'exécute lors du démarrage de l'application
@Component
public class RMIClientApplicationRunner implements ApplicationRunner {
    private RmiProxyFactoryBean proxy; // Proxy pour le service RMI

    // Injection de dépendance du proxy RMI
    public RMIClientApplicationRunner(RmiProxyFactoryBean proxy) {
        this.proxy = proxy;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Récupération de l'instance de HelloService via le proxy
        HelloService service = (HelloService) proxy.getObject();
        // Invocation de la méthode helloWorld et affichage du résultat
        System.out.println(service.helloWorld("Dude"));
    }
}