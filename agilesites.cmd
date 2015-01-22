@echo off
set SCRIPT_DIR=%~dp0
if not defined JAVA_HOME goto nojavahome
set PATH="%JAVA_HOME%"\bin;"%SCRIPT_DIR%"\bin;"%SCRIPT_DIR%"\wcs\home\bin;%PATH%
java -version
if errorlevel 9009 if not errorlevel 9010 goto notfoundjava
if exist build.sbt goto foundbuildsbt
javac -version
if errorlevel 9009 if not errorlevel 9010 goto notfoundjavac
java -cp bin\wcs.jar wcs.Configurator
:foundbuildsbt
java -Xms128m -Xmx512m -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -Dsbt.boot.directory=project\boot -Dsbt.ivy.home=project\ivy2 -jar "%SCRIPT_DIR%bin\sbt-launch.jar" core/publish api/publish app/publish
:corebuilt
java -Xms128m -Xmx512m -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -Dsbt.boot.directory=project\boot -Dsbt.ivy.home=project\ivy2 -jar "%SCRIPT_DIR%bin\sbt-launch.jar" %*
goto end
:nojavahome
echo JAVA_HOME not defined. JAVA_HOME must be defined and point to a JDK (not a JRE). 
goto pause
:notfoundjava
echo java not found. Please install a JDK 1.7.x and set JAVA_HOME environment variable. 
goto pause
:notfoundjavac
echo javac not found. Please install a JDK 1.7.x and set JAVA_HOME environment variable. 
goto pause
:notfoundbuildsbt
echo To start, copy build.sbt.dist to build.sbt and EDIT IT.
echo Check also documentation on http://www.agilesites.org 
:pause
pause
:end
