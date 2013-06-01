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
 echo "To start, copy build.sbt.sbt to build.sbt and EDIT IT."
 echo "Reading the README.md and the documentation does not hurt, too." 
else 
  if ! test -d $HOME/.ivy2/local/com.sciabarra/1.0_*
  then java -Xms256m -Xmx512m -Xss1m -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384m -Dsbt.boot.directory=project/boot/ -jar bin/sbt-launch.jar core/publish-local
  fi
  java -Xms256m -Xmx512m -Xss1m -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384m -Dsbt.boot.directory=project/boot/ -jar bin/sbt-launch.jar "$@"
fi

