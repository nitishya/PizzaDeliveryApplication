package com.pizza.apigateway.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.pizza.apigateway.security.JwtUtil;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private WebClient.Builder webClientBuilder;

	@PostMapping("/login")
	public Mono<ResponseEntity<?>> login(@RequestBody Map<String, String> loginRequest) {
		logger.info("Received login request for email: {}", loginRequest.get("email"));
		String userServiceUrl = "http://localhost:8081/auth/login";

		return webClientBuilder.build().post().uri(userServiceUrl).bodyValue(loginRequest).retrieve()
				.bodyToMono(Map.class).flatMap(response -> {
					logger.info("Response from User Service: {}", response);
					if (response.containsKey("email")) {
						String id = String.valueOf(response.get("id")); // Extract user ID
						String email = (String) response.get("email");
						String role = (String) response.get("role");

						logger.info("Generating JWT token for user: {} with role: {}", email, role);
						String token = jwtUtil.generateToken(email);
						logger.info("Generated Token: {}", token);

						return Mono.just(
								ResponseEntity.ok(Map.of("id", id, "token", token, "email", email, "role", role)));
					}
					return Mono.just(ResponseEntity.status(401).body("Invalid credentials"));
				}).onErrorResume(e -> {
					logger.error("Error calling User Service: {}", e.getMessage());
					return Mono.just(ResponseEntity.status(500).body("Internal Server Error"));
				});
	}
}
