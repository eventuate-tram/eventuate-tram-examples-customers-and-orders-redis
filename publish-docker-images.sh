#!/bin/bash -e

BRANCH=$(git rev-parse --abbrev-ref HEAD)

DOCKER_IMAGE_TAG=$BRANCH ./gradlew javaDevelopmentComposeBuild

if ! [[  $BRANCH =~ ^[0-9]+ ]] ; then
  echo Not release $BRANCH - no PUSH
  exit 0
fi

VERSION=$BRANCH

echo Publishing $BRANCH

docker login -u ${DOCKER_USER_ID?} -p ${DOCKER_PASSWORD?}

DOCKER_IMAGE_TAG=$BRANCH ./gradlew javaDevelopmentComposePush

