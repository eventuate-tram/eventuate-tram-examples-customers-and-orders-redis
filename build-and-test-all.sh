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

./gradlew $GRADLE_OPTIONS buildAndTestAll


