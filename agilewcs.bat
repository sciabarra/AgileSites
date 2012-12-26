set SCRIPT_DIR=%~dp0
java -Xms256M -Xmx1024M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -jar "%SCRIPT_DIR%bin\sbt-launch.jar" %*
