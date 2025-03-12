package com.pizza.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer  // Enables Eureka Server
public class PizzaServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzaServiceRegistryApplication.class, args);
	}

}
