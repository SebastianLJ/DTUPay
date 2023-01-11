#!/bin/bash
set -e

docker-compose down
docker container rm -f $'(docker container ls -aq)'
docker rmi $'(docker images -q)'