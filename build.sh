#!/bin/bash
# Enforces build script fails on anything but SUCCESS codes
set -e

# Clean old builds, Repeat for all maven projects
pushd DTUPayWebService
mvn clean package

# Build the docker images and run them
popd
docker-compose build
docker-compose up -d

# Clean up any images and container not in use
docker image prune -f
docker container prune

# Wait for any builds to run
sleep 2

# Run project tests
pushd DTUPayClient
mvn test
popd
