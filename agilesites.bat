@echo off
if exist build.sbt goto run
echo To start, copy build.sbt.sbt to build.sbt and EDIT IT.
echo Reading the README.md and the documentation does not hurt, too. 
pause
goto end 
:run
set SCRIPT_DIR=%~dp0
if exist project\core.built goto :corebuilt
java -Xms128m -Xmx512m -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -Dsbt.boot.directory=project\boot -jar "%SCRIPT_DIR%bin\sbt-launch.jar" core/publish-local
echo >project\core.built
:corebuilt
java -Xms128m -Xmx512m -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -Dsbt.boot.directory=project\boot -jar "%SCRIPT_DIR%bin\sbt-launch.jar" %*
:end

