#!/bin/bash

# First kill any running services
echo "Stopping any running services..."
./stop-services.sh

# Start infrastructure services using Docker
echo "Starting infrastructure services (Postgres, MongoDB, Redis, Kafka)..."
docker compose up -d postgres mongodb redis kafka

# Wait for infrastructure services to be ready
echo "Waiting for infrastructure services to be ready..."
sleep 15

# Start Config Server
echo "Starting Config Server..."
cd config-server
java -jar target/config-server-0.0.1-SNAPSHOT.jar &
cd ..

# Wait for Config Server to be ready
echo "Waiting for Config Server to be ready..."
sleep 15

# Start Discovery Service
echo "Starting Discovery Service..."
cd discovery-service
java -jar target/discovery-service-0.0.1-SNAPSHOT.jar &
cd ..

# Wait for Discovery Service to be ready
echo "Waiting for Discovery Service to be ready..."
sleep 15

# Start Gateway Service
echo "Starting Gateway Service..."
cd gateway-service
java -jar target/gateway-service-0.0.1-SNAPSHOT.jar &
cd ..

# Wait for Gateway Service to be ready
echo "Waiting for Gateway Service to be ready..."
sleep 15

# Start Auth Service
echo "Starting Auth Service..."
cd auth-service
java -jar target/auth-service-0.0.1-SNAPSHOT.jar &
cd ..

# Wait for Auth Service to be ready
echo "Waiting for Auth Service to be ready..."
sleep 10

# Start Product Service
echo "Starting Product Service..."
cd product-service
java -jar target/product-service-0.0.1-SNAPSHOT.jar &
cd ..

# Wait for Product Service to be ready
echo "Waiting for Product Service to be ready..."
sleep 10

# Start Cart Service
echo "Starting Cart Service..."
cd cart-service
java -jar target/cart-service-0.0.1-SNAPSHOT.jar &
cd ..

echo "All services have been started!"
echo "Access the API Gateway at: http://localhost:8765"
echo "Access Eureka Discovery Service at: http://localhost:8761" 