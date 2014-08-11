rem @echo off
if not "%1"=="" set JAVA_HOME=%1
if not "%2"=="" set PORT=8181
if not defined JAVA_HOME goto nojavahome
set PATH="%JAVA_HOME%"\bin;%PATH%
if not exist "%JAVA_HOME%"\bin goto notfoundjava
set BASE=%~dp0
set REPLACE=%BASE:\=/%
set SETUP="%JAVA_HOME%"\bin\java -cp bin\setup.jar
del home\*.done
echo >home\hsql.flag
echo >home\setup.flag
call mksbt "%JAVA_HOME%"
if exist ContentServer\csinstall.bat move ContentServer Sites
if not exist Sites\csinstall.bat goto notfoundsites
%SETUP% setup.Silent %BASE% misc\silentinstaller\generic_omii.ini Sites\install.ini Sites\omii.ini
%SETUP% setup.Replacer ../ %REPLACE% <context.xml >webapps\cs\META-INF\context.xml
%SETUP% setup.Unzip Sites\csdt.zip home
cd ..
rem %SETUP% sites.Configurator sites %1
cd sites/Sites
del log.out
start cmd /c "%SETUP% setup.PressEnter | call csinstall -silent >log.out"
%SETUP% setup.WaitUntil log.out ENTER.
pause
cd ../..
sbt "sites stop" "sitesSetup" "sitesServe start"
%SETUP% sites.PressEnter now
%SETUP% sites.WaitUntil sites\Sites\log.out "Installation Finished Successfully"
start http://localhost:%PORT%/cs/
goto pause
:notfoundsites
echo Sites installer not found.
echo Please download it from the Oracle website, 
echo then unzip the WCS_Sites*.zip in the wcs folder.
echo It is required the Sites installer in the wcs\Sites folder
goto pause 
:notfoundjava
echo java not found. Please install JDK and set JAVA_HOME environment variable. 
goto pause
:pause
pause
:end