package com.pizza.userservice.service;

import com.pizza.userservice.exception.UserNotFoundException;
import com.pizza.userservice.model.User;
import com.pizza.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	/**
	 * Registers a new user.
	 */
	public User registerUser(User user) {
		logger.info("Registering new user: {}", user.getEmail());
		return userRepository.save(user);
	}

	/**
	 * Finds a user by email, throws exception if not found.
	 */
	public Optional<User> findUserByEmail(String email) {
		logger.info("Searching for user with email: {}", email);
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isEmpty()) {
			logger.warn("User not found: {}", email);
			throw new UserNotFoundException("User not found with email: " + email);
		}
		return user;
	}
}
