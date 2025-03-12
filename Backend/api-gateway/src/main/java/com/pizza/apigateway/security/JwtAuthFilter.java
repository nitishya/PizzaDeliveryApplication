package com.pizza.apigateway.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements WebFilter {

	@Autowired
	private JwtUtil jwtUtil;

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		logger.info("Incoming request: {} {}", request.getMethod(), request.getURI());

		// Allow login requests to pass through
		if (request.getURI().getPath().equals("/auth/login")) {
			logger.info("Login request detected, skipping authentication filter.");
			return chain.filter(exchange);
		}

		// Check if Authorization header is present
		if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
			logger.warn("Missing Authorization header, rejecting request.");
			return unauthorizedResponse(exchange);
		}

		String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			logger.warn("Invalid Authorization header format, rejecting request.");
			return unauthorizedResponse(exchange);
		}

		String token = authHeader.substring(7);
		logger.info("Extracted JWT Token: {}", token);

		if (!jwtUtil.validateToken(token)) {
			logger.warn("Token validation failed, rejecting request.");
			return unauthorizedResponse(exchange);
		}

		logger.info("Token validation successful, proceeding with request.");
		return chain.filter(exchange);
	}

	private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		logger.error("Responding with 401 Unauthorized");
		return response.setComplete();
	}

}