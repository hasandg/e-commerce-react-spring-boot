# Running the E-Commerce Application

This document provides instructions for running the E-Commerce application, both frontend and backend services.

## Prerequisites

- Java 24 (recommended) or Java 17+
- Node.js (v14+) and npm
- Docker and Docker Compose (for full microservices setup)

## Running the Frontend

The frontend is a React application with TypeScript, Material-UI, and other modern web technologies.

### Steps:

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

4. The frontend will be available at [http://localhost:3000](http://localhost:3000)

### Development Notes:

- The frontend is configured to use mock data by default, so it can run independently of the backend services
- Login with username: `testuser` (regular user) or `admin` (admin user)
- Password can be anything during development

## Running the Backend Services

The backend is built using a microservices architecture with Spring Boot, Spring Cloud, and other modern technologies.

### Option 1: Using Docker Compose (Recommended for full setup)

1. Make sure Docker is running on your system
2. Navigate to the project root
3. Run Docker Compose to start all services:
   ```bash
   docker compose up -d
   ```
4. This will start all infrastructure services (Postgres, MongoDB, Redis, Kafka) and microservices

### Option 2: Running Individual Services

If you prefer to run services individually or Docker isn't available, start the services in this order:

1. **Config Server** (mandatory for other services):
   ```bash
   cd config-server
   java -jar target/config-server-0.0.1-SNAPSHOT.jar
   ```

2. **Discovery Service** (after Config Server is running):
   ```bash
   cd discovery-service
   java -jar target/discovery-service-0.0.1-SNAPSHOT.jar
   ```

3. **Gateway Service** (after Discovery Service is running):
   ```bash
   cd gateway-service
   java -jar target/gateway-service-0.0.1-SNAPSHOT.jar
   ```

4. **Other services as needed:**
   - Product Service: `cd product-service && java -jar target/product-service-0.0.1-SNAPSHOT.jar`
   - Auth Service: `cd auth-service && java -jar target/auth-service-0.0.1-SNAPSHOT.jar`
   - etc.

### Environment Configuration

- The backend services use Spring Cloud Config for configuration
- Environment variables can be set in `.env` file or directly in your system

## Troubleshooting

### Frontend Issues:

- If you see `Could not find a required file. Name: index.html`, make sure the `public/index.html` file exists
- If npm packages are missing, run `npm install` in the frontend directory

### Backend Issues:

- If a service can't connect to Config Server, ensure Config Server is running first
- Seeing `Port XXXX already in use` error? Find and kill the process using that port:
  ```bash
  lsof -i :XXXX
  kill -9 <PID>
  ```

## Development Mode

For development, you can:

1. Run the frontend with mock data (the default setup)
2. Run only the backend services you're working with
3. Update the frontend to connect to real APIs by modifying the service files

## API Documentation

When running, API documentation is available at:
- Swagger UI: `http://localhost:9090/swagger-ui.html` 