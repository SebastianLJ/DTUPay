#!/bin/bash
# Enforces build script fails on anything but SUCCESS codes
set -e

# Clean and build projects

# @author Alexander Faarup Christensen - s174355
printf "\033[94mStep 1/5: Clean and build projects\033[0m\n"
pushd DTUPayMessageQueue
mvn clean install -q -DskipTests
popd

# @author Alexander Faarup Christensen - s174355
pushd DTUPayTokenManagement
mvn clean install -q -DskipTests
popd

# @author Jákup Viljam Dam - s185095
pushd DTUPayWebService
mvn clean install -q -DskipTests
mvn package -q -DskipTests
popd

# Clean docker images

# @author Alexander Faarup Christensen - s174355
printf "\n\033[94mStep 2/5: Clean docker images\033[0m\n"
./clean-docker.sh
docker image prune -f

# Prune & build the docker images and run them

# @author Jákup Viljam Dam - s185095
printf "\n\033[94mStep 3/5: build the docker images and run them\033[0m\n"
docker-compose up -d rabbitMq
sleep 10
docker-compose up -d

# Wait for any builds to run
sleep 5

# Run project tests

# @author Alexander Faarup Christensen - s174355
printf "\n\033[94mStep 4/5: Run project tests\033[0m\n"
pushd DTUPayMessageQueue
#mvn test
popd

# @author Alexander Faarup Christensen - s174355
pushd DTUPayTokenManagement
#mvn test
popd

# @author Jákup Viljam Dam - s185095
pushd DTUPayWebService
#mvn test
popd

# @author Jákup Viljam Dam - s185095
pushd DTUPayClient
mvn test
popd

# @author Jákup Viljam Dam - s185095
printf "\n\033[94mStep 5/5: Finishing up and generating swagger document\033[0m\n"
curl http://localhost:8080/swagger >> swagger.document