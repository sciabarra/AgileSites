@echo off
set jsk=z:\AgileSitesJSK\2.0\7.5.0\App_Server\apache-tomcat-6.0.32\webapps\cs
set v=7.5.0
set h="%jsk%\WEB-INF\lib"
if not exist %h%\systemtools-7.6.2.jar goto nofile
call mvn install:install-file -Dfile="%h%\cs-core.jar" -DgroupId=com.oracle.sites -DartifactId=cs-core -Dversion=%v% -Dpackaging=jar
call mvn install:install-file -Dfile="%h%\cs.jar" -DgroupId=com.oracle.sites -DartifactId=cs -Dversion=%v% -Dpackaging=jar
call mvn install:install-file -Dfile="%h%\assetapi.jar" -DgroupId=com.oracle.sites -DartifactId=assetapi -Dversion=%v% -Dpackaging=jar
call mvn install:install-file -Dfile="%h%\assetapi-impl.jar" -DgroupId=com.oracle.sites -DartifactId=assetapi-impl -Dversion=%v% -Dpackaging=jar
call mvn install:install-file -Dfile="%h%\xcelerate.jar" -DgroupId=com.oracle.sites -DartifactId=xcelerate -Dversion=%v% -Dpackaging=jar
call mvn install:install-file -Dfile="%h%\jsoup-1.5.2.jar" -DgroupId=com.oracle.sites -DartifactId=jsoup -Dversion=%v% -Dpackaging=jar
call mvn install:install-file -Dfile="%h%\xstream-1.2.2_modified.jar" -DgroupId=com.oracle.sites -DartifactId=xstream -Dversion=%v% -Dpackaging=jar
cd core
call sbt "core762/sitesTagWrapperGen %jsk%" "core762/publish-local" "core762/publishM2" "core762/publish"
cd ..
cd api
call sbt "api762/publish-local" "api762/publishM2" "api762/publish"
cd ..
goto end
:nofile
echo Edit this file and set the location of cs webapp for WebCenter Sites %v%
:end
