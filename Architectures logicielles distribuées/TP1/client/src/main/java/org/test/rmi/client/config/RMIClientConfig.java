package org.test.rmi.client.config;
import java.util.logging.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import  org.test.rmi.common.interfaces.CabinetService;

@Configuration
public class RMIClientConfig {
    public final Logger logger = Logger.getLogger(this.getClass().getName());

    @Bean
    RmiProxyFactoryBean proxyFactoryBean() {
        String rmiServerURL = String.format("rmi://localhost:1099/%s", CabinetService.class.getSimpleName());
        logger.info("URL du serveur RMI : " + rmiServerURL);
        RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
        proxy.setServiceInterface(CabinetService.class);
        proxy.setServiceUrl(rmiServerURL);
        proxy.afterPropertiesSet();
        return proxy;
    }
}