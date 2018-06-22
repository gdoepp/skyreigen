#!/bin/bash

: "${NDK_HOME:=/opt/android/ndk}"

cd swiggen
./swiggen.sh
cd ..
$NDK_HOME/ndk-build
cp -r ../libs/* ../src/main/jniLibs/
