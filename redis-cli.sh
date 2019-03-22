#! /bin/bash -e

docker run ${DOCKER_RUN_OPTIONS:--it} --rm \
   -e DOCKER_HOST_IP=redis \
   --network redisconf-2019_default \
   redis:5.0.3 bash -c 'redis-cli -h $DOCKER_HOST_IP'
