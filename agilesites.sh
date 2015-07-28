#!/bin/bash
BASE="$(dirname $0)"
BASE="$(cd $BASE; pwd)"
export PATH="$BASE/bin:$BASE/wcs/home/bin:$PATH"
if test -n "$JAVA_HOME"
then export PATH="$JAVA_HOME/bin:$PATH"
else echo JAVA_HOME must be defined and pointing to a JDK - not a JRE ; exit
fi
if ! java -version
then echo java not found. Please install a JDK 1.7.x and set JAVA_HOME ; exit
fi
if ! javac -version
then echo javac not found. Please install a JDK 1.7.x and set JAVA_HOME ; exit
fi
if ! test -f build.sbt
then
  java -cp bin/wcs.jar wcs.Configurator
fi
java -Xms256m -Xmx512m -Xss1m -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384m -Dsbt.boot.directory=project/boot/ -Dsbt.ivy.home=project/ivy2 -jar bin/sbt-launch.jar core/publish api/publish app/publish
java -Xms256m -Xmx512m -Xss1m -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384m -Dsbt.boot.directory=project/boot/ -Dsbt.ivy.home=project/ivy2 -jar bin/sbt-launch.jar "$@"

