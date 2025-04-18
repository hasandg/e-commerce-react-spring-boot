# Auth Service Setup with Keycloak

This guide explains how to set up and configure the auth-service with Keycloak integration.

## Prerequisites

- Docker and Docker Compose installed
- Java 17 or later
- Maven

## Setup Steps

### 1. Start Infrastructure Services

First, start all the required infrastructure services using Docker Compose:

```bash
docker compose up -d
```

This will start:
- PostgreSQL
- MongoDB
- Redis
- Kafka
- Zookeeper
- Config Server
- Discovery Service
- Gateway Service
- Keycloak

### 2. Configure Keycloak

1. Access the Keycloak Admin Console:
   - URL: http://localhost:9090
   - Username: admin
   - Password: admin

2. Create a new realm:
   - Click "Create Realm"
   - Name: `ecommerce`
   - Click "Create"

3. Create a new client:
   - Go to "Clients" in the left sidebar
   - Click "Create Client"
   - Client ID: `auth-service`
   - Client Protocol: `openid-connect`
   - Root URL: `http://localhost:8081`
   - Click "Next"

4. Configure client settings:
   - Access Type: `confidential`
   - Standard Flow Enabled: `ON`
   - Direct Access Grants Enabled: `ON`
   - Service Accounts Enabled: `ON`
   - Click "Save"

5. Get client secret:
   - Go to the "Credentials" tab
   - Copy the "Client Secret"
   - Update the `client-secret` in `application.yml`

6. Configure client scopes:
   - Go to "Client Scopes"
   - Add roles: `user`, `admin`

### 3. Configure Auth Service

1. Update environment variables in `application.yml`:
```yaml
keycloak:
  auth-server-url: http://localhost:9090
  realm: ecommerce
  client-id: auth-service
  client-secret: your-client-secret
  resource: auth-service
  public-client: false
  use-resource-role-mappings: true
  principal-attribute: preferred_username
  ssl-required: external
  confidential-port: 0
```

2. Start the auth-service:
```bash
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

### 4. Available Endpoints

The auth-service exposes the following endpoints:

- `POST /api/auth/keycloak/login`
  - Parameters: username, password
  - Returns: Access token

- `POST /api/auth/keycloak/validate`
  - Headers: Authorization (Bearer token)
  - Returns: Boolean indicating token validity

- `POST /api/auth/keycloak/logout`
  - Headers: Authorization (Bearer token)
  - Returns: 200 OK on successful logout

### 5. Testing the Integration

1. Test login:
```bash
curl -X POST "http://localhost:8081/api/auth/keycloak/login" \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -d "username=your-username&password=your-password"
```

2. Test token validation:
```bash
curl -X POST "http://localhost:8081/api/auth/keycloak/validate" \
     -H "Authorization: Bearer your-token"
```

3. Test logout:
```bash
curl -X POST "http://localhost:8081/api/auth/keycloak/logout" \
     -H "Authorization: Bearer your-token"
```

## Troubleshooting

1. If the auth-service fails to start:
   - Check if all required services are running
   - Verify database connection settings
   - Check Keycloak configuration

2. If authentication fails:
   - Verify client secret in application.yml
   - Check Keycloak realm and client settings
   - Ensure user exists in Keycloak

3. If token validation fails:
   - Check token format
   - Verify token expiration
   - Ensure proper Authorization header format

## Security Considerations

1. Always use HTTPS in production
2. Keep client secrets secure
3. Implement proper token validation
4. Use appropriate token expiration times
5. Implement rate limiting
6. Monitor authentication attempts 