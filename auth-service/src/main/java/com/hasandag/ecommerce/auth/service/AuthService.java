package com.hasandag.ecommerce.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Value("${keycloak.auth-server-url:http://localhost:9090/auth}")
    private String keycloakUrl;

    @Value("${keycloak.realm:ecommerce}")
    private String keycloakRealm;

    public String getGoogleAuthUrl() {
        // This is a mock implementation for development
        return "http://localhost:8081/api/auth/google/callback?code=mock-code";
    }

    public String handleGoogleCallback(String code) {
        // This is a mock implementation for development
        // In a real implementation, we would exchange the code for an access token
        // and then fetch user information from Google
        
        // Mock user attributes
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", "user@example.com");
        attributes.put("given_name", "John");
        attributes.put("family_name", "Doe");
        
        // Get user information from the attributes map
        String email = (String) attributes.get("email");
        String firstName = (String) attributes.get("given_name");
        String lastName = (String) attributes.get("family_name");
        
        return "Mock authentication successful! User: " + firstName + " " + lastName + " (" + email + ")";
    }
} 