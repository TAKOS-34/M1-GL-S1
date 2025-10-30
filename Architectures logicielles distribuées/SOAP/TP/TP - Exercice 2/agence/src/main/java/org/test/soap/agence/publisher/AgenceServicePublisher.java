package org.test.soap.agence.publisher;
import javax.xml.ws.Endpoint;
import org.test.soap.agence.service.AgenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgenceServicePublisher implements CommandLineRunner {
	 private static final String SERVICE_URI = "http://0.0.0.0:8080/agenceService";
	 
	 @Autowired
	 private AgenceService agenceServiceImpl;
	
	 @Override
	 public void run(String... args) throws Exception {
	     Endpoint.publish(SERVICE_URI, agenceServiceImpl);
	     System.err.println("Web Service successfully published at: " + SERVICE_URI);
	     System.err.println("Server ready!");
	 }
}