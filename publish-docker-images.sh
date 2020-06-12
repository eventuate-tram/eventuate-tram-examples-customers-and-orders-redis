#!/bin/bash -e

BRANCH=$(git rev-parse --abbrev-ref HEAD)

if [ "$BRANCH" != "master" ] ; then
  echo Not master
  exit 0
fi

export DOCKER_IMAGE_TAG=latest

docker login -u ${DOCKER_USER_ID?} -p ${DOCKER_PASSWORD?}

./gradlew javaDevelopmentComposePull || echo no image to pull

./gradlew composeBuild composePush
./gradlew javaDevelopmentComposeBuild javaDevelopmentComposePush
