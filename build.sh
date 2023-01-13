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
mvn clean package -q -DskipTests
popd

# Prune & build the docker images and run them
printf "\n\033[94mStep 2/4: Prune and build the docker images and run them\033[0m\n"
docker image prune -f
docker-compose up -d

# Wait for any builds to run
sleep 2

# Run project tests
printf "\n\033[94mStep 3/4: Run project tests\033[0m\n"
pushd DTUPayClient
mvn test -q
popd

pushd DTUPayTokenManagement
mvn test -q
popd

pushd DTUPayMessageQueue
mvn test -q
popd

pushd DTUPayWebService
mvn test -q
popd

# Clean docker images
printf "\n\033[94mStep 4/4: Clean docker images\033[0m\n"
./clean-docker.sh