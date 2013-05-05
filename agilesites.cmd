@echo off
if not defined JAVA_HOME goto nojavahome
set PATH="%JAVA_HOME%"\bin;%PATH%
:nojavahome
java -version
if errorlevel 9009 if not errorlevel 9010 goto notfoundjava
javac -version
if errorlevel 9009 if not errorlevel 9010 goto notfoundjavac
if not exist build.sbt goto notfoundbuildsbt
set SCRIPT_DIR=%~dp0
if exist %HOMEDRIVE%%HOMEPATH%\.ivy2\local\com.sciabarra\nul goto :corebuilt
java -Xms128m -Xmx512m -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -Dsbt.boot.directory=project\boot -jar "%SCRIPT_DIR%bin\sbt-launch.jar" core/publish-local
:corebuilt
java -Xms128m -Xmx512m -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -Dsbt.boot.directory=project\boot -jar "%SCRIPT_DIR%bin\sbt-launch.jar" %*
goto end
:notfoundjava
echo java not found. Please install JDK and set JAVA_HOME environment variable. 
goto pause
:notfoundjavac
echo javac not found. Please install JDK and set JAVA_HOME environment variable. 
goto pause
:notfoundbuildsbt
echo To start, copy build.sbt.sbt to build.sbt and EDIT IT.
echo Reading the README.md and the documentation does not hurt, too. 
:pause
pause
:end
