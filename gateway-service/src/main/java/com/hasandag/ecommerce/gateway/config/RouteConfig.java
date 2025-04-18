package com.hasandag.ecommerce.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth Service Routes
                .route("auth_service_route", r -> r
                        .path("/api/auth/**")
                        .uri("lb://auth-service"))
                .route("auth_service_docs", r -> r
                        .path("/auth-service/api-docs/**", "/auth-service/swagger-ui/**")
                        .filters(f -> f.rewritePath("/auth-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://auth-service"))

                // Product Service Routes
                .route("product_service_route", r -> r
                        .path("/api/products/**")
                        .uri("lb://product-service"))
                .route("product_service_docs", r -> r
                        .path("/product-service/api-docs/**", "/product-service/swagger-ui/**")
                        .filters(f -> f.rewritePath("/product-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://product-service"))
                        
                // Cart Service Routes
                .route("cart_service_route", r -> r
                        .path("/api/carts/**")
                        .uri("lb://cart-service"))
                .route("cart_service_docs", r -> r
                        .path("/cart-service/api-docs/**", "/cart-service/swagger-ui/**")
                        .filters(f -> f.rewritePath("/cart-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://cart-service"))
                        
                // Payment Service Routes
                .route("payment_service_route", r -> r
                        .path("/api/payments/**")
                        .uri("lb://payment-service"))
                .route("payment_service_docs", r -> r
                        .path("/payment-service/api-docs/**", "/payment-service/swagger-ui/**")
                        .filters(f -> f.rewritePath("/payment-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://payment-service"))
                        
                // Notification Service Routes
                .route("notification_service_route", r -> r
                        .path("/api/notifications/**")
                        .uri("lb://notification-service"))
                .route("notification_service_docs", r -> r
                        .path("/notification-service/api-docs/**", "/notification-service/swagger-ui/**")
                        .filters(f -> f.rewritePath("/notification-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://notification-service"))
                        
                // Fallback route to discovery service as last resort
                .route("fallback_route", r -> r
                        .path("/**")
                        .uri("lb://discovery-service"))
                .build();
    }
} 