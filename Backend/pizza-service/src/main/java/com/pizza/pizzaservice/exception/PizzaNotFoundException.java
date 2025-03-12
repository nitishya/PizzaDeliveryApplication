package com.pizza.pizzaservice.exception;

/**
 * Custom exception thrown when a pizza is not found in the database.
 */
public class PizzaNotFoundException extends RuntimeException {
    public PizzaNotFoundException(String message) {
        super(message);
    }
}
