package org.test.soap.hotel.publisher;
import javax.xml.ws.Endpoint;
import org.springframework.beans.factory.annotation.Value;
import org.test.soap.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HotelServicePublisher implements CommandLineRunner {
    @Value("${server.port}")
    private String port;

	 @Autowired
	 private HotelService hotelServiceImpl;

	 @Override
	 public void run(String... args) throws Exception {
         String serviceUri = "http://0.0.0.0:" + (Integer.parseInt(port) + 100) + "/hotelService";

	     Endpoint.publish(serviceUri, hotelServiceImpl);
	     System.err.println("Web Service successfully published at: " + serviceUri);
	     System.err.println("Server ready!");
	 }
}