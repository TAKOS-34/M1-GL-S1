package org.test.rmi.client.config;
import java.util.logging.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import  org.test.rmi.common.interfaces.CabinetService;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class RMIClientConfig {
    public final Logger logger = Logger.getLogger(this.getClass().getName());
    
    @Value("${RMI_SERVER_HOST:rmi-server}")
    private String rmiHost;

    @Value("${RMI_SERVER_PORT:1099}")
    private int rmiPort;

    @Bean
    public RmiProxyFactoryBean proxyFactoryBean() {
        String rmiServerURL = String.format("rmi://%s:%d/%s", rmiHost, rmiPort, CabinetService.class.getSimpleName());
        logger.info("URL du serveur RMI : " + rmiServerURL);
        RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
        proxy.setServiceInterface(CabinetService.class);
        proxy.setServiceUrl(rmiServerURL);
        return proxy;
    }
}
