#!/bin/bash
for i in 1 2 3; do
  # rm ../../lib/$i.jar
  cd $i
  zip -r ../../../lib/$i.jar .
  cd ..
done
