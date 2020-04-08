#! /bin/bash -e

docker run ${DOCKER_RUN_OPTIONS:--i} --rm \
   -e DOCKER_HOST_IP=redis \
   --network=${PWD##*/}_default \
   redis:5.0.3 bash -c 'redis-cli -h $DOCKER_HOST_IP'
