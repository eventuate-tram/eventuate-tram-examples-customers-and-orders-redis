#! /bin/bash

set -e

GRADLE_OPTIONS=

while [ ! -z "$*" ] ; do
  case $1 in
    "--use-existing" )
      GRADLE_OPTIONS="$GRADLE_OPTIONS -P buildAndTestAllUseExisting=true"
      ;;
    "--keep-running" )
      GRADLE_OPTIONS="$GRADLE_OPTIONS -P buildAndTestAllKeepRunning=true"
      ;;
    "--help" )
      echo ./build-and-test-all.sh --use-existing --keep-running
      exit 0
      ;;
  esac
  shift
done

if [ -z "$DOCKER_HOST_IP" ] ; then
    echo must set DOCKER_HOST_IP to the IP address of your machine
    exit 1
fi

./gradlew $GRADLE_OPTIONS buildAndTestAll


