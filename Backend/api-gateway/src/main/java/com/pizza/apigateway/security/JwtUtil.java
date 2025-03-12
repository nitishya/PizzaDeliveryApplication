package com.pizza.apigateway.security;

import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    
    // Generate a properly encoded Base64 secret key
    private static final String SECRET_KEY_STRING = "your_strong_secret_key";
    private static final SecretKey SECRET_KEY = new SecretKeySpec(Base64.getEncoder().encode(SECRET_KEY_STRING.getBytes()), "HmacSHA256");
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    public String generateToken(String username) {
        logger.info("Generating JWT token for user: {}", username);
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        logger.info("Generated Token: {}", token);
        return token;
    }

    public boolean validateToken(String token) {
        try {
            logger.info("Validating JWT token: {}", token);
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            logger.info("Token is valid");
            return true;
        } catch (Exception e) {
            logger.error("Invalid or expired token: {}", e.getMessage());
            return false;
        }
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}