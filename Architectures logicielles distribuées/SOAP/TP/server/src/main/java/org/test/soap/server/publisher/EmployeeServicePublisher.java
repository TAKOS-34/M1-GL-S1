package org.test.soap.server.publisher;
import javax.xml.ws.Endpoint;
import org.test.soap.server.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

//Configuration pour publier le service web lors du démarrage de l'application
@Configuration
public class EmployeeServicePublisher implements CommandLineRunner {
	 private static final String SERVICE_URI = "http://0.0.0.0:8080/employeeservice"; // URI du service
	 
	 @Autowired
	 private EmployeeService employeeServiceImpl; // Autowire the service
	
	 @Override
	 public void run(String... args) throws Exception {
	     // Publication du service web
	     Endpoint.publish(SERVICE_URI, employeeServiceImpl);
	     System.err.println("Web Service successfully published at: " + SERVICE_URI);
	     System.err.println("Server ready!");
	 }
}