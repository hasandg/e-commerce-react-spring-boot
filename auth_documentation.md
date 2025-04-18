# Authentication System Documentation

This document provides detailed information about the login, logout, and signup features of this e-commerce project for developers.

## Core Components

1. **Auth Service**: 
   - Coordinates authentication flows
   - Interfaces with Keycloak for identity management
   - Provides OAuth2 integration with Google

2. **Keycloak**:
   - Handles user registration, login, and session management
   - Manages roles and permissions
   - Issues JWT tokens for authentication

## Authentication Flows

### 1. Signup/Registration

The system supports user registration through:

- **Keycloak Direct Registration**:
  - Endpoint: `POST /realms/{realm}/protocol/openid-connect/registrations`
  - Required fields: username, email, password, firstName, lastName
  - Optional custom attributes like phoneNumber
  - New users are automatically assigned the "user" role

- **Google OAuth2 Registration**:
  - Initiated via `GET /api/auth/google`
  - User is redirected to Google's consent screen
  - After approval, Google redirects to `/api/auth/google/callback` with an authorization code
  - Auth Service exchanges the code for an access token and retrieves user information
  - The service creates/updates the user in Keycloak

#### Direct Registration Sequence Diagram

```mermaid
sequenceDiagram
    participant Client
    participant AuthService
    participant Keycloak
    
    Client->>Keycloak: POST /realms/{realm}/protocol/openid-connect/registrations
    Note right of Client: User details (username, password, email, etc.)
    Keycloak->>Keycloak: Validate input
    Keycloak->>Keycloak: Create user account
    Keycloak->>Keycloak: Assign default role (user)
    Keycloak->>Client: Return user details with ID
```

#### Google OAuth2 Registration Sequence Diagram

```mermaid
sequenceDiagram
    participant Client
    participant AuthService
    participant Google
    participant Keycloak
    
    Client->>AuthService: GET /api/auth/google
    AuthService->>Client: Redirect to Google Auth URL
    Client->>Google: Redirect with client_id and redirect_uri
    Google->>Client: Display consent screen
    Client->>Google: User approves access
    Google->>AuthService: Redirect to callback URL with auth code
    AuthService->>Google: Exchange code for tokens
    Google->>AuthService: Return access token
    AuthService->>Google: Request user information
    Google->>AuthService: Return user profile
    AuthService->>Keycloak: Check if user exists
    AuthService->>Keycloak: Create/update user account
    AuthService->>Client: Return success message
```

### 2. Login

The system supports multiple login methods:

- **Keycloak Direct Login**:
  - Endpoint: `POST /realms/{realm}/protocol/openid-connect/token`
  - Parameters: grant_type=password, client_id, client_secret, username, password
  - Returns access_token, refresh_token, expires_in, token_type, and id_token

- **Google OAuth2 Login**:
  - Same flow as Google Registration
  - Flow is handled by Spring Security's OAuth2 login mechanism
  - After successful authentication, redirects to `/api/auth/google/success`

#### Direct Login Sequence Diagram

```mermaid
sequenceDiagram
    participant Client
    participant Keycloak
    
    Client->>Keycloak: POST /realms/{realm}/protocol/openid-connect/token
    Note right of Client: username, password, client_id, client_secret
    Keycloak->>Keycloak: Authenticate user
    Keycloak->>Keycloak: Generate access & refresh tokens
    Keycloak->>Client: Return tokens (JWT)
    Client->>Client: Store tokens
```

#### Google OAuth2 Login Sequence Diagram

```mermaid
sequenceDiagram
    participant Client
    participant AuthService
    participant Google
    participant Keycloak
    
    Client->>AuthService: GET /api/auth/google
    AuthService->>Client: Redirect to Google Auth URL
    Client->>Google: Redirect with client_id and redirect_uri
    Google->>Client: Display consent screen (if not previously granted)
    Client->>Google: User approves access
    Google->>AuthService: Redirect to callback URL with auth code
    AuthService->>Google: Exchange code for tokens
    Google->>AuthService: Return access token
    AuthService->>Google: Request user information
    Google->>AuthService: Return user profile
    AuthService->>Keycloak: Check if user exists
    alt User exists
        AuthService->>Keycloak: Get user information
    else User doesn't exist
        AuthService->>Keycloak: Create new user
    end
    AuthService->>Keycloak: Request token for user
    Keycloak->>AuthService: Return JWT tokens
    AuthService->>Client: Redirect to success URL with tokens
```

### 3. Logout

- **Token Invalidation**:
  - Endpoint: `POST /realms/{realm}/protocol/openid-connect/logout`
  - Requires the refresh_token to invalidate the session
  - Client must also discard the tokens from local storage

#### Logout Sequence Diagram

```mermaid
sequenceDiagram
    participant Client
    participant Keycloak
    
    Client->>Keycloak: POST /realms/{realm}/protocol/openid-connect/logout
    Note right of Client: Include refresh_token
    Keycloak->>Keycloak: Invalidate session
    Keycloak->>Keycloak: Revoke tokens
    Keycloak->>Client: Confirm logout
    Client->>Client: Delete stored tokens
```

## Token Management

- **Access Token**: JWT token with a default lifespan of 900 seconds (15 minutes)
- **Refresh Token**: Lasts 86400 seconds (24 hours) and can be used to obtain new access tokens
- **Token Validation**: Performed by Spring Security using Keycloak's public key

### Token Flow Diagram

```mermaid
graph TD
    A[Client] -->|1. Request with credentials| B[Authorization Server]
    B -->|2. Issue access & refresh tokens| A
    A -->|3. API request with access token| C[Resource Server]
    C -->|4. Validate token| C
    C -->|5. Return protected resource| A
    A -->|6. Token expired| A
    A -->|7. Request with refresh token| B
    B -->|8. Issue new access token| A
```

## Architecture Diagram

```mermaid
graph TD
    Client[Client Application] -->|Authentication Requests| Gateway[API Gateway]
    Gateway -->|Route Auth Requests| AuthService[Auth Service]
    AuthService -->|User Management| Keycloak[Keycloak]
    AuthService -->|OAuth2 Authentication| GoogleOAuth[Google OAuth]
    Gateway -->|Route API Requests| Services[Microservices]
    Services -->|Validate Tokens| Keycloak

    subgraph Security Layer
        Keycloak
        GoogleOAuth
    end

    subgraph Service Layer
        AuthService
        Services
    end
```

## Security Configuration

The `SecurityConfig` class in Auth Service configures:
- CSRF protection (disabled for API endpoints)
- Public endpoints (`/api/auth/**` and `/actuator/**`)
- OAuth2 login configuration with success URL

### Configuration Class Structure

```mermaid
classDiagram
    class SecurityConfig {
        +securityFilterChain(HttpSecurity http) SecurityFilterChain
    }
    
    class KeycloakConfig {
        -String serverUrl
        -String realm
        -String clientId
        -String clientSecret
        +keycloak() Keycloak
    }
    
    class AuthService {
        -ClientRegistrationRepository clientRegistrationRepository
        -OAuth2AuthorizedClientService authorizedClientService
        -String googleClientId
        -String googleClientSecret
        -String keycloakUrl
        -String keycloakRealm
        +getGoogleAuthUrl() String
        +handleGoogleCallback(String code) String
    }
    
    SecurityConfig ..> AuthService : uses
    AuthService ..> KeycloakConfig : uses
```

## For Developers

1. **Integration Points**:
   - Use the JWT token in the `Authorization` header with `Bearer` prefix for authenticated requests
   - Token contains user information and roles for authorization

2. **Development Setup**:
   - Keycloak runs on port 9090
   - Auth Service runs on port 8081
   - Environment variables required:
     - GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET (for Google OAuth)
     - KEYCLOAK_URL, KEYCLOAK_REALM, KEYCLOAK_CLIENT_ID, KEYCLOAK_CLIENT_SECRET

3. **Testing Authentication**:
   - Refer to API_TEST_SCENARIOS.md for detailed examples of authentication requests and responses
   - Preconfigured test users: "admin" (password: "admin") and "user" (password: "user")

### JWT Token Structure

```mermaid
graph TD
    JWT[JWT Token] --> Header[Header]
    JWT --> Payload[Payload]
    JWT --> Signature[Signature]
    
    subgraph "Header"
        alg[Algorithm: RS256]
        typ[Type: JWT]
    end
    
    subgraph "Payload"
        sub[Subject: User ID]
        name[Name]
        email[Email]
        roles[Roles]
        exp[Expiration Time]
        iat[Issued At]
    end
    
    subgraph "Signature"
        sign[HMAC SHA256 Signature]
    end
```

## Implementation Notes

1. The Auth Service acts primarily as an adapter between the client application and Keycloak.
2. Google OAuth integration provides an alternative registration/login path.
3. All tokens are JWTs that can be validated without calling back to Keycloak on every request.
4. Token renewal happens through the refresh token mechanism.

