package com.pizza.orderservice.exception;

/**
 * Custom exception thrown when an order is not found in the system.
 */
public class OrderNotFoundException extends RuntimeException {
	public OrderNotFoundException(String message) {
		super(message);
	}
}