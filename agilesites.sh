#!/bin/bash
if ! test -f build.sbt
then 
 echo "To start, copy build.sbt.sbt to build.sbt and EDIT IT."
 echo "Reading the README.md and the book does not hurt, too." 
else 
  java -Xms256M -Xmx1024M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -Dsbt.boot.directory=project/boot/ -jar bin/sbt-launch.jar "$@"
fi

