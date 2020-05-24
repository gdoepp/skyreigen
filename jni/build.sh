#!/bin/bash

: "${NDK_HOME:=/opt/android/ndk}"
echo creating interfaces to java:
cd swiggen
./swiggen.sh
echo now compiling c++ with $NDK_HOME:...
cd ..
$NDK_HOME/ndk-build
echo copying libraries...
cp -r ../libs/* ../src/main/jniLibs/
echo ready
