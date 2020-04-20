#! /bin/bash -e

docker run ${DOCKER_RUN_OPTIONS:--i} --rm \
   --network=${PWD##*/}_default \
   redis:5.0.3 bash -c 'redis-cli -h redis'
