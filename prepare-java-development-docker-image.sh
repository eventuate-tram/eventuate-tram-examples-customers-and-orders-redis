#! /bin/bash

rm -fr java-development/build
mkdir java-development/build

tar -cf - *gradle* */src/* */build.gradle | tar -C java-development/build -xf -

docker build -t test-java-development java-development
