#!/bin/bash
echo "Not updated"
exit
if test -n "$JAVA_HOME"
then export PATH="$JAVA_HOME/bin:wcs/home/bin:$PATH"
fi
if ! java -version
then echo java not found. Please install JDK and set JAVA_HOME ; exit
fi
if ! test -f build.sbt
then
  java -cp bin\wcs.jar wcs.Configurator
fi
java -Xms256m -Xmx512m -Xss1m -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384m -Dsbt.boot.directory=project/boot/ -Dsbt.ivy.home=project/ivy2 -jar bin/sbt-launch.jar core/publish api/publish app/compile app/update
java -Xms256m -Xmx512m -Xss1m -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384m -Dsbt.boot.directory=project/boot/ -Dsbt.ivy.home=project/ivy2 -jar bin/sbt-launch.jar "$@"

