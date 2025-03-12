package com.pizza.pizzaservice.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(PizzaNotFoundException.class)
	public ResponseEntity<Object> handlePizzaNotFoundException(PizzaNotFoundException ex) {
		logger.error("Pizza not found: {}", ex.getMessage());
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("timestamp", LocalDateTime.now());
		errorResponse.put("message", ex.getMessage());
		errorResponse.put("status", HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGeneralException(Exception ex) {
		logger.error("An error occurred: {}", ex.getMessage(), ex);
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("timestamp", LocalDateTime.now());
		errorResponse.put("message", "Internal Server Error");
		errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
