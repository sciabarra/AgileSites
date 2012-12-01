set SCRIPT_DIR=%~dp0
java -Xmx512M -jar "%SCRIPT_DIR%bin\sbt-launch.jar" %*
