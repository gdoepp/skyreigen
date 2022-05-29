#!/bin/sh

 sed 's/\[\[nodiscard\]\]//g' ../AADate.h >AADate.h
 mv -f AADate.h ../

 swig -c++ -java -package com.naughter.aaplus -outdir src/com/naughter/aaplus/ aaplus.i

 swig -c++ -java -package com.projectpluto.astro -outdir src/com/projectpluto/astro astro.i

 for f in *.cxx
 do
   sed -E 's/#include <[.]([.].*)/#include <\1/g' <$f >../${f%cxx}cpp
 done

 cp -rf src/com ../../src/main/java
