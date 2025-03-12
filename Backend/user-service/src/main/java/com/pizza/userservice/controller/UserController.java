package com.pizza.userservice.controller;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pizza.userservice.exception.UserNotFoundException;
import com.pizza.userservice.model.User;
import com.pizza.userservice.service.UserService;

@RestController
@RequestMapping("/auth")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Registers a new user by encoding their password before saving.
	 */
	@PostMapping("/register")
	public User registerUser(@RequestBody User user) {
		logger.info("Registering new user: {}", user.getEmail());
		user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash password
		return userService.registerUser(user);
	}

	/**
	 * Logs in a user by validating their credentials.
	 */
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
		String email = loginRequest.get("email");
		String password = loginRequest.get("password");

		logger.info("Login request received for email: {}", email);
		Optional<User> userOptional = userService.findUserByEmail(email);
		if (userOptional.isEmpty()) {
			logger.warn("User not found: {}", email);
			throw new UserNotFoundException("User not found with email: " + email);
		}

		User user = userOptional.get();
		if (passwordEncoder.matches(password, user.getPassword())) {
			logger.info("Login successful for user: {}", email);
			// Ensure user ID is returned correctly
			return ResponseEntity.ok(Map.of("id", String.valueOf(user.getId()), // Convert ID to String
					"email", user.getEmail(), "role", user.getRole()));
		}

		logger.warn("Invalid credentials for user: {}", email);
		return ResponseEntity.status(401).body("Invalid credentials");
	}
}
