#!/bin/bash

# Check if docker was used
USE_DOCKER=${1:-false}

echo "Stopping all services..."

# Kill all Java processes related to our services
echo "Stopping Java services..."
pkill -f "auth-service-0.0.1-SNAPSHOT.jar" || true
pkill -f "product-service-0.0.1-SNAPSHOT.jar" || true
pkill -f "cart-service-0.0.1-SNAPSHOT.jar" || true
pkill -f "gateway-service-0.0.1-SNAPSHOT.jar" || true
pkill -f "discovery-service-0.0.1-SNAPSHOT.jar" || true
pkill -f "config-server-0.0.1-SNAPSHOT.jar" || true

if [ "$USE_DOCKER" = "true" ]; then
    # Stop and remove Docker containers
    echo "Stopping Docker containers..."
    docker compose down
else
    # Stop only infrastructure services
    echo "Stopping infrastructure Docker containers..."
    docker compose stop kafka postgres mongodb redis
fi

echo "All services have been stopped!" 