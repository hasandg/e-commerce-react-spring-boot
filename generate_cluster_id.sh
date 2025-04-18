#!/bin/bash

# Generate a random UUID for Kafka cluster ID
CLUSTER_ID=$(kafka-storage random-uuid)
echo "Generated Kafka Cluster ID: $CLUSTER_ID"

# Update the docker-compose.yml file with the new cluster ID
sed -i "s/CLUSTER_ID: '.*'/CLUSTER_ID: '$CLUSTER_ID'/g" docker-compose.yml

echo "Updated docker-compose.yml with new cluster ID" 