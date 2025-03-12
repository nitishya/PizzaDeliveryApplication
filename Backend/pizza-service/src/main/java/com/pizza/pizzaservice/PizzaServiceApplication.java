package com.pizza.pizzaservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PizzaServiceApplication {
	private static final Logger logger = LoggerFactory.getLogger(PizzaServiceApplication.class);

	public static void main(String[] args) {
		logger.info("Starting Pizza Service...");
		SpringApplication.run(PizzaServiceApplication.class, args);
		logger.info("Pizza Service started successfully.");
	}
}
