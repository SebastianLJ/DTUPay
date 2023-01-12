#!/bin/bash
# Enforces build script fails on anything but SUCCESS codes
set -e

# Clean and build projects
pushd DTUPayMessageQueue
mvn clean install
popd

pushd DTUPayWebService
mvn package
popd

# Prune unused docker images
docker image prune -f

# Build the docker images and run them
#docker-compose up -d rabbitMq
docker-compose up -d DTUPayWebService

# Wait for any builds to run
sleep 2

# Run project tests
pushd DTUPayClient
mvn test
popd

pushd DTUPayTokenManagement
mvn test
popd

pushd DTUPayMessageQueue
mvn test
popd

pushd DTUPayWebService
mvn test
popd

./clean-docker.sh