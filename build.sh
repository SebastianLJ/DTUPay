#!/bin/bash
# Enforces build script fails on anything but SUCCESS codes
set -e

# Clean and build projects
printf "\033[94mStep 1/4: Clean and build projects\033[0m\n"
pushd DTUPayMessageQueue
mvn clean install -q -DskipTests
popd

pushd DTUPayTokenManagement
mvn clean install -q -DskipTests
popd

pushd DTUPayWebService
./mvnw clean install -q -DskipTests
./mvnw package -q -DskipTests
popd

# Clean docker images
printf "\n\033[94mStep 2/4: Clean docker images\033[0m\n"
./clean-docker.sh
docker image prune -f

# Prune & build the docker images and run them
printf "\n\033[94mStep 3/4: build the docker images and run them\033[0m\n"
docker-compose up -d rabbitMq
sleep 5
docker-compose up -d

# Wait for any builds to run
sleep 5

# Run project tests
printf "\n\033[94mStep 4/4: Run project tests\033[0m\n"
pushd DTUPayMessageQueue
#mvn test -q
popd

pushd DTUPayTokenManagement
#mvn test -q
popd

pushd DTUPayClient
mvn test -q
popd

pushd DTUPayWebService
#mvn test -q
popd

