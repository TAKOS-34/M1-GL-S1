package org.test.rmi.client.config;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;
import org.test.rmi.common.interfaces.AnimalService;

@Component
public class RMIClientApplicationRunner implements ApplicationRunner {
    private RmiProxyFactoryBean proxy;

    public RMIClientApplicationRunner(RmiProxyFactoryBean proxy) {
        this.proxy = proxy;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        AnimalService service = (AnimalService) proxy.getObject();
        System.out.println(service.afficher());
    }
}