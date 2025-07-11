package com.hasandag.ecommerce.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            // Get the Authorization header
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                
                // Add the token to the request headers
                ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Id", extractUserId(token))
                    .header("X-User-Roles", extractUserRoles(token))
                    .build();
                
                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            }
            
            return chain.filter(exchange);
        };
    }

    private String extractUserId(String token) {
        // In a real implementation, you would decode the JWT token and extract the user ID
        // For now, we'll just return a placeholder
        return "user-id";
    }

    private String extractUserRoles(String token) {
        // In a real implementation, you would decode the JWT token and extract the roles
        // For now, we'll just return a placeholder
        return "ROLE_USER";
    }

    public static class Config {
        // Add configuration properties if needed
    }
} 