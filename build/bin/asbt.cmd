@echo off
set BASE=%~dp0\..\..
if not defined JAVA_HOME goto nojavahome
set REPO=%BASE%\wcs\repo
set PATH="%JAVA_HOME%"\bin;"%BASE%"\wcs\home\bin;%PATH%
java -version
if errorlevel 9009 if not errorlevel 9010 goto notfoundjava
javac -version
if errorlevel 9009 if not errorlevel 9010 goto notfoundjavac
java -Xms128m -Xmx512m -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -Dsbt.boot.directory=%REPO%\boot -Dsbt.ivy.home=%REPO% -jar "%BASE%\build\bin\sbt-launch.jar" %*
goto end
:nojavahome
echo JAVA_HOME not defined. JAVA_HOME must be defined and pointing to a JDK (not a JRE). 
goto pause
:notfoundjava
echo java not found. Please install a JDK 1.7.x and set JAVA_HOME environment variable. 
goto pause
:notfoundjavac
echo javac not found. Please install a JDK 1.7.x and set JAVA_HOME environment variable. 
goto pause
:pause
pause
:end
