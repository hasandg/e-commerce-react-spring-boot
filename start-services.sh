#!/bin/bash

# Default profile
PROFILE=${1:-default}

# Check if docker should be used
USE_DOCKER=${2:-false}

# Check if config server should be used
USE_CONFIG=${3:-false}

CONFIG_PARAM=""
if [ "$USE_CONFIG" = "true" ]; then
    CONFIG_PARAM="-Dspring.cloud.config.enabled=true"
else
    CONFIG_PARAM="-Dspring.cloud.config.enabled=false -Dspring.config.import=optional:configserver:"
fi

echo "Starting services with profile: $PROFILE"
echo "Using Docker: $USE_DOCKER"
echo "Using Config Server: $USE_CONFIG"

if [ "$USE_DOCKER" = "true" ]; then
    # Start infrastructure and core services using Docker
    echo "Starting infrastructure services, Config Server, Discovery Service, and Gateway Service with Docker..."
    docker compose up -d

    # Wait for core services to be ready
    echo "Waiting for core services to be ready..."
    sleep 30
else
    # Start infrastructure services separately
    echo "Starting infrastructure services only..."
    docker compose up -d kafka postgres mongodb redis
    
    # Wait for infrastructure to be ready
    echo "Waiting for infrastructure services to be ready..."
    sleep 20
    
    # Start Config Server if needed
    if [ "$USE_CONFIG" = "true" ]; then
        echo "Starting Config Server..."
        cd config-server
        java -jar target/config-server-0.0.1-SNAPSHOT.jar &
        cd ..
        
        # Wait for Config Server to be ready
        echo "Waiting for Config Server to be ready..."
        sleep 10
    fi
    
    # Start Discovery Service
    echo "Starting Discovery Service..."
    cd discovery-service
    java -Dspring.profiles.active=$PROFILE $CONFIG_PARAM -jar target/discovery-service-0.0.1-SNAPSHOT.jar &
    cd ..
    
    # Wait for Discovery Service to be ready
    echo "Waiting for Discovery Service to be ready..."
    sleep 10
    
    # Start Gateway Service
    echo "Starting Gateway Service..."
    cd gateway-service
    java -Dspring.profiles.active=$PROFILE $CONFIG_PARAM -jar target/gateway-service-0.0.1-SNAPSHOT.jar &
    cd ..
    
    # Wait for Gateway Service to be ready
    echo "Waiting for Gateway Service to be ready..."
    sleep 10
    
    # Start Auth Service
    echo "Starting Auth Service..."
    cd auth-service
    java -Dspring.profiles.active=$PROFILE $CONFIG_PARAM -jar target/auth-service-0.0.1-SNAPSHOT.jar &
    cd ..
    
    # Wait for Auth Service to be ready
    echo "Waiting for Auth Service to be ready..."
    sleep 10
    
    # Start Product Service
    echo "Starting Product Service..."
    cd product-service
    java -Dspring.profiles.active=$PROFILE $CONFIG_PARAM -jar target/product-service-0.0.1-SNAPSHOT.jar &
    cd ..
    
    # Wait for Product Service to be ready
    echo "Waiting for Product Service to be ready..."
    sleep 10
    
    # Start Cart Service
    echo "Starting Cart Service..."
    cd cart-service
    java -Dspring.profiles.active=$PROFILE $CONFIG_PARAM -jar target/cart-service-0.0.1-SNAPSHOT.jar &
    cd ..
fi

echo "All services have been started!"
echo "Access the API Gateway at: http://localhost:8765"
echo "Access Eureka Discovery Service at: http://localhost:8761"
if [ "$USE_CONFIG" = "true" ]; then
    echo "Access Config Server at: http://localhost:8888"
fi 