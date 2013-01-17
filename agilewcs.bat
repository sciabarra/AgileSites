@echo off
if exist build.sbt goto run
echo "To start, copy build.sbt.dist in build.sbt and EDIT IT. Reading the README.md does not hurt, too."
goto end 
:run
set SCRIPT_DIR=%~dp0
java -Xms256M -Xmx1024M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -jar "%SCRIPT_DIR%bin\sbt-launch.jar" %*
:end

