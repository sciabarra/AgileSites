#!/bin/bash
if ! test -f build.sbt
then echo "To start, copy build.sbt.dist in build.sbt and EDIT IT. Reading the README.md does not hurt, too." 
else java -Xms256M -Xmx1024M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -jar bin/sbt-launch.jar "$@"
fi

