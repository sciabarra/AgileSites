#!/bin/bash
if test -n "$JAVA_HOME"
then export PATH="$JAVA_HOME/bin:$PATH"
fi
if ! java -version
then echo java not found. Please install JDK and set JAVA_HOME ; exit
fi
if ! javac -version
then echo javac not found. Please install JDK and set JAVA_HOME ; exit
fi
if ! test -f build.sbt
then
  javac bin/Configurator.java -d bin
  java -cp bin Configurator
fi
java -Xms256m -Xmx512m -Xss1m -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384m -Dsbt.boot.directory=project/boot/ -Dsbt.ivy.home=project/ivy2 -jar bin/sbt-launch.jar core/publish api/publish
java -Xms256m -Xmx512m -Xss1m -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384m -Dsbt.boot.directory=project/boot/ -Dsbt.ivy.home=project/ivy2 -jar bin/sbt-launch.jar "$@"

