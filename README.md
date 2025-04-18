# Online e-commerce Application

## Introduction

This is a microservices-based online e-commerce application built with Spring Boot, Spring Cloud, and other modern technologies. The application provides a complete e-commerce solution with features like product browsing, cart management, order processing, payment integration, and more.

## Microservices Architecture

The application is built using a microservices architecture, which consists of the following services:

- **User Service**: Manages user profiles, authentication, and authorization
- **Product Service**: Handles product catalog, categories, and inventory
- **Cart Service**: Manages e-commerce carts
- **Order Service**: Processes and tracks orders
- **Payment Service**: Handles payment processing
- **Notification Service**: Sends notifications to users
- **Auth Service**: Manages authentication and authorization with Keycloak integration
- **Gateway Service**: API Gateway for routing requests to appropriate services
- **Config Server**: Centralized configuration management
- **Discovery Service**: Service discovery with Eureka

## Tech Stack

- **Java 24**: Latest Java version for optimal performance
- **Spring Boot 3.4.2**: Modern framework for building Java applications
- **Spring Cloud 2024.0.0**: Tools for building cloud-native applications
- **Spring Security**: Authentication and authorization
- **Keycloak**: Identity and access management
- **Kafka**: Event-driven communication between services
- **Redis**: Caching and session management
- **PostgreSQL**: Primary database for most services
- **MongoDB**: Alternative database for certain services requiring flexible schema
- **Resilience4j**: Circuit breaking and fault tolerance
- **MapStruct**: Object mapping between DTOs and domain models
- **Micrometer & Zipkin**: Distributed tracing
- **ELK Stack**: Centralized logging
- **Prometheus & Grafana**: Monitoring and visualization
- **Docker & Docker Compose**: Containerization and orchestration
- **JUnit & Mockito**: Testing framework

## Domain Driven Design (DDD)

This application follows Domain Driven Design principles:

- **Bounded Contexts**: Each microservice represents a bounded context
- **Entities & Value Objects**: Core domain objects
- **Aggregates**: Ensuring data consistency
- **Domain Events**: Communicating between bounded contexts
- **Repositories**: Data access abstraction
- **Domain Services**: Complex business logic spanning multiple entities

## Security

Security is implemented using:

- **Spring Security**: For authentication and authorization
- **Keycloak**: Identity and Access Management
- **JWT**: Token-based authentication
- **OAuth2**: Authorization framework
- **Role-Based Access Control**: Different access levels for different user roles

## Features

### User Management
- User registration and login
- Profile management
- Address management
- Role-based access control

### Product Management
- Product catalog
- Categories and subcategories
- Product search and filtering
- Product reviews and ratings

### e-commerce
- Add products to cart
- View and edit cart
- Checkout process
- Order tracking
- Payment processing

### Admin Dashboard
- Inventory management
- Order management
- User management
- Analytics and reporting

## Getting Started

### Prerequisites
- Java 24
- Docker and Docker Compose
- Maven

### Running Locally

1. Clone the repository
2. Start the infrastructure services:
   ```
   docker-compose up -d
   ```
3. Build the application:
   ```
   mvn clean install
   ```
4. Start the services in order:
   - Config Server
   - Discovery Service
   - Auth Service
   - Other services

### Using Docker

Simply run:
```
docker-compose up -d
```

This will start all services and required infrastructure.

## API Documentation

API documentation is available through Swagger UI at:
```
http://localhost:9090/swagger-ui.html
```

## Future Enhancements

- **Recommendation Engine**: Personalized product recommendations
- **Internationalization**: Multi-language support
- **Multi-tenancy**: Support for multiple shops on the same platform
- **Advanced Analytics**: AI-powered sales predictions
- **Mobile App Integration**: APIs for mobile applications
- **Content Management**: Rich content for products

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Profiles

The application supports two profiles:

1. **default**: Uses localhost for all service connections (development on local machine)
2. **dev**: Uses Docker container names for service discovery (running in Docker)

## Starting the Application

You can start the application in two ways:

### Local Development (default profile)

```bash
./start-services.sh
```

This will:
- Start infrastructure services (Postgres, MongoDB, Redis, Kafka) using Docker
- Start all microservices locally with the default profile
- Use local application.properties files for configuration

### With a Specific Profile

```bash
./start-services.sh dev
```

This will start all services with the dev profile.

### Using Docker for All Services

```bash
./start-services.sh dev true
```

This will:
- Start all services (infrastructure and application) using Docker Compose
- Configure them to use the dev profile

### Using Config Server

```bash
./start-services.sh default false true
```

This will:
- Start infrastructure services (Postgres, MongoDB, Redis, Kafka) using Docker
- Start the Config Server
- Start all other services configured to use the centralized configuration

You can combine all options:

```bash
# Format: ./start-services.sh [profile] [use-docker] [use-config-server]
./start-services.sh dev true true
```

This will:
- Start all services using Docker
- Configure them to use the dev profile
- Use the Config Server for configuration

## Stopping the Application

To stop all services:

```bash
./stop-services.sh
```

If you started with Docker for all services:

```bash
./stop-services.sh true
```

## Accessing the Application

- **API Gateway**: http://localhost:8765
- **Discovery Service Dashboard**: http://localhost:8761
- **Swagger API Documentation**: http://localhost:8765/swagger-ui.html

## Configuration

Each service has its own configuration in:
- Individual service configuration:
  - `src/main/resources/application.properties` - Default configuration
  - `src/main/resources/application-dev.properties` - Docker environment configuration

- Centralized configuration (Config Server):
  - `config-server/src/main/resources/config/<service-name>.yml` - Contains both default and dev profile configurations

## Using the Config Server

The Config Server provides centralized configuration for all services. Each service can load its configuration from the Config Server when `spring.cloud.config.enabled=true` is set.

To use the Config Server:

1. Make sure the Config Server is running: `cd config-server && java -jar target/config-server-0.0.1-SNAPSHOT.jar`
2. Start services with Config Server enabled: `java -Dspring.cloud.config.enabled=true -jar <service-jar>`

You can see the configurations at:
- Config Server API: http://localhost:8888/<service-name>/default
- Config Server API (dev profile): http://localhost:8888/<service-name>/dev

## Development

To add a new service:

1. Create a new Spring Boot project
2. Add the necessary dependencies for Spring Cloud
3. Create both application.properties and application-dev.properties
4. Update the docker-compose.yml file if needed 