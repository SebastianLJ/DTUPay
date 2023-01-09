#!/bin/bash
set -e

# Clean old builds
pushd DTUPayWebService
mvn clean package

# Build the docker image and run it
popd
docker-compose build
docker-compose up -d
docker image prune -f

# Wait for web server to run
sleep 2

# Run the client tests against the server
pushd DTUPayClient
mvn test
popd
