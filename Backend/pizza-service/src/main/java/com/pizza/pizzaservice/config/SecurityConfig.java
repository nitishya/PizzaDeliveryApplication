package com.pizza.pizzaservice.config;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		logger.info("Configuring Security Web Filter Chain...");

		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable
																											// CORS
				.authorizeHttpRequests(auth -> auth.requestMatchers("/public/**").permitAll() // Allow public endpoints
						.anyRequest().authenticated() // Secure other endpoints
				).oauth2ResourceServer(
						oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
		logger.info("Security configuration successfully applied.");
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Allow frontend origin
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		logger.info("Configuring JWT Authentication Converter...");

		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

		logger.info("JWT Authentication Converter configured successfully.");
		return jwtAuthenticationConverter;
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		logger.info("Creating JwtDecoder Bean...");

		String secret = "your_strong_secret_key"; // Use the same key as API Gateway
		SecretKey secretKey = new SecretKeySpec(Base64.getEncoder().encode(secret.getBytes()), "HmacSHA256");

		logger.info("JwtDecoder Bean created successfully.");
		return NimbusJwtDecoder.withSecretKey(secretKey).build();
	}

}