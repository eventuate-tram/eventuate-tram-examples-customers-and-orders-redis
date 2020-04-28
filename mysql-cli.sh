#! /bin/bash -e

docker run ${1:--it} \
   --name mysqlterm \
   --rm \
   --network=redisconf-2019_default \
   mysql:5.7.13 \
   sh -c 'exec mysql -hmysql -uroot -prootpassword -o eventuate'
