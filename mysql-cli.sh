#! /bin/bash -e

docker run $* ${DOCKER_RUN_OPTIONS:--it} \
   --name mysqlterm \
   --rm \
   --network redisconf-2019_default \
   -e MYSQL_HOST=mysql \
   mysql:5.7.13 \
   sh -c 'exec mysql -h"$MYSQL_HOST"  -uroot -prootpassword -o eventuate'
