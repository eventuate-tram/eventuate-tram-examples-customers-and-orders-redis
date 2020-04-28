#! /bin/bash -e

docker run ${1:--it} --rm \
   --network=redisconf-2019_default \
   redis:5.0.7 bash -c 'redis-cli -h redis'
