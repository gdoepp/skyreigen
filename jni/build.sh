#!/bin/bash

: "${NDK_HOME:=/home/gd/Android/Sdk/ndk/29.0.14033849}"
echo creating interfaces to java:
cd swiggen
./swiggen.sh
echo now compiling c++ with $NDK_HOME:...
cd ..
$NDK_HOME/ndk-build
echo copying libraries...
cp -r ../libs/* ../src/main/jniLibs/
echo ready
