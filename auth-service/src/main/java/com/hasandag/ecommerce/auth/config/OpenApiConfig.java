package com.hasandag.ecommerce.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Value("${spring.application.name}")
    private String applicationName;
    
    @Bean
    public OpenAPI authServiceOpenAPI() {
        Server localServer = new Server()
                .url("http://localhost:8081")
                .description("Local Development Server");
                
        Server gatewayServer = new Server()
                .url("http://localhost:8765")
                .description("API Gateway");
        
        Contact contact = new Contact()
                .name("Hasan Dag")
                .email("hasandag@example.com")
                .url("https://github.com/hasandag");
        
        License license = new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0");
        
        Info info = new Info()
                .title("Auth Service API Documentation")
                .version("1.0.0")
                .description("REST API Documentation for the E-Commerce Authentication Service")
                .contact(contact)
                .license(license);
        
        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, gatewayServer));
    }
} 