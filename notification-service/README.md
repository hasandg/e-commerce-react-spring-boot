# Notification Service

A microservice for managing notifications in the Online e-commerce App.

## Overview

The Notification Service is responsible for:
- Creating, retrieving, and managing user notifications
- Tracking notification status (pending, sent, failed, delivered)
- Supporting different notification types (email, SMS, push, in-app)
- Marking notifications as read/unread
- Receiving notification events from Kafka topics

## Technologies

- Java 17
- Spring Boot 2.7.x
- Spring Data JPA
- PostgreSQL
- Kafka
- Spring Security with OAuth2/JWT
- JUnit 5 for testing

## API Endpoints

The service exposes the following RESTful endpoints:

| Method | Endpoint                          | Description                                    |
|--------|-----------------------------------|------------------------------------------------|
| POST   | /api/notifications                | Create a new notification                      |
| GET    | /api/notifications                | Get user notifications (paginated)             |
| GET    | /api/notifications/unread         | Get unread notifications for a user            |
| GET    | /api/notifications/count          | Get count of unread notifications for a user   |
| PUT    | /api/notifications/{id}/read      | Mark a notification as read                    |
| PUT    | /api/notifications/read-all       | Mark all user notifications as read            |
| DELETE | /api/notifications/{id}           | Delete a notification                          |
| GET    | /api/notifications/status/{status}| Get notifications by status                    |

## Kafka Integration

The service listens to the following Kafka topics:
- `order-notifications`: Notifications related to order processing
- `user-notifications`: General user-related notifications

## Security

All endpoints require authentication using JWT tokens. The security configuration allows:
- Public access to Swagger documentation and actuator endpoints
- Authenticated access to notification endpoints

## Running the Service

### Prerequisites
- JDK 17
- PostgreSQL
- Kafka
- Keycloak (for authentication)

### Configuration

The service can be configured through `application.properties`. The main settings include:

```properties
# Server Configuration
server.port=8083
spring.application.name=notification-service

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/notification_service
spring.datasource.username=postgres
spring.datasource.password=postgres

# Kafka Configuration
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=notification-service-group

# Security
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:9090/realms/e-commerce-app
```

### Building and Running

```bash
# Build the project
./mvnw clean package

# Run the service
java -jar target/notification-service-0.0.1-SNAPSHOT.jar
```

## Testing

Run the tests with:

```bash
./mvnw test
```

For API testing, see the [Notification API Tests](./NOTIFICATION_API_TESTS.md) document. 