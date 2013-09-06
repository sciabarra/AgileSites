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
  javac -cp bin Configure.java
fi
if ! test -d project/repo/com/sciabarra
then java -Xms256m -Xmx512m -Xss1m -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384m -Dsbt.boot.directory=project/boot/ -jar bin/sbt-launch.jar core/publish
fi
java -Xms256m -Xmx512m -Xss1m -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384m -Dsbt.boot.directory=project/boot/ -jar bin/sbt-launch.jar "$@"

