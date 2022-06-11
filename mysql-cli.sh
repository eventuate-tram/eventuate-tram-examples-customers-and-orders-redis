#! /bin/bash -e

docker run ${1:--it} \
   --name mysqlterm \
   --rm \
   --network=redisconf-2019_default \
   mysql/mysql-server:8.0.27-1.2.6-server \
   sh -c 'exec mysql -hmysql -uroot -prootpassword -o eventuate'
