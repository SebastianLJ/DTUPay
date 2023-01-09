#!/bin/cmd
set -e

#
pushd DTUPayWebService

#
mvn package

#
popd
docker-compose build
docker-compose up -d
docker image prune -f


# Wait for web server to run
sleep 3

pushd DTUPayClient
mvn test
popd
