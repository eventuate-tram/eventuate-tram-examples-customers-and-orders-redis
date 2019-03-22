#!/bin/bash -e

docker run -p 8889:8888 -e DOCKER_DIAGNOSTICS_PORT=8889 -e DOCKER_HOST_IP \
     --rm eventuateio/eventuateio-docker-networking-diagnostics:0.2.0.RELEASE
