@echo off
if not defined JAVA_HOME goto nojavahome
set PATH="%JAVA_HOME%"\bin;%PATH%
:nojavahome
java -version
if errorlevel 9009 if not errorlevel 9010 goto notfoundjava
javac -version
if errorlevel 9009 if not errorlevel 9010 goto notfoundjavac
if exist build.sbt goto foundbuildsbt
javac -d bin bin\Configurator.java 
java -cp bin Configurator
:foundbuildsbt
set SCRIPT_DIR=%~dp0
java -Xms128m -Xmx512m -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -Dsbt.boot.directory=project\boot -Dsbt.ivy.home=project\ivy2 -jar "%SCRIPT_DIR%bin\sbt-launch.jar" core/publish api/publish
:corebuilt
java -Xms128m -Xmx512m -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -Dsbt.boot.directory=project\boot -Dsbt.ivy.home=project\ivy2 -jar "%SCRIPT_DIR%bin\sbt-launch.jar" %*
goto end
:notfoundjava
echo java not found. Please install JDK and set JAVA_HOME environment variable. 
goto pause
:notfoundjavac
echo javac not found. Please install JDK and set JAVA_HOME environment variable. 
goto pause
:notfoundbuildsbt
echo To start, copy build.sbt.dist to build.sbt and EDIT IT.
echo Check also documentation on http://www.agilesites.org 
:pause
pause
:end
