package org.test.rmi.server.impl;
import org.springframework.stereotype.Service;
import org.test.rmi.common.interfaces.HelloService;

// Implémentation de l'interface HelloService
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String helloWorld(String msg) {
        return "Hello " + msg; // Retourne un message de salutation
    }
}