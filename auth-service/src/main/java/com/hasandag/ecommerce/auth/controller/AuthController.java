package com.hasandag.ecommerce.auth.controller;

import com.hasandag.ecommerce.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/google")
    public ResponseEntity<String> getGoogleAuthUrl() {
        return ResponseEntity.ok(authService.getGoogleAuthUrl());
    }

    @GetMapping("/google/callback")
    public ResponseEntity<String> handleGoogleCallback(@RequestParam String code) {
        return ResponseEntity.ok(authService.handleGoogleCallback(code));
    }

    @GetMapping("/google/success")
    public ResponseEntity<String> handleGoogleSuccess() {
        return ResponseEntity.ok("Google authentication successful!");
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "auth-service");
        status.put("version", "0.0.1-SNAPSHOT");
        return ResponseEntity.ok(status);
    }
} 