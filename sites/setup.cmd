@echo off
if not "%~1"=="" set JAVA_HOME=%~1
if "%~2"=="" set HOST=localhost
if not "%~2"=="" set HOST=%2
if "%~3"=="" set PORT=8181
if not "%~3"=="" set PORT=%3
if "%~4"=="" set DB=HSQLDB
if not "%~4"=="" set DB=%4
if not defined JAVA_HOME goto nojavahome
set PATH="%JAVA_HOME%\bin";%PATH%
if not exist "%JAVA_HOME%\bin\java.exe" goto notfoundjava
set BDRV=%~d0
set BASE=%~dp0
set REPLACE=%BASE:\=/%
set SETUP="%JAVA_HOME%\bin\java" -cp bin\setup.jar
%BDRV%
cd "%BASE%"
del home\*.done
call mksbt "%JAVA_HOME%"
if exist ContentServer\csInstall.bat move ContentServer Sites
if not exist Sites\csInstall.bat goto notfoundsites
%SETUP% setup.Silent "." misc\silentinstaller\generic_omii.ini Sites\install.ini Sites\omii.ini %HOST% %PORT% %DB% 
%SETUP% setup.Replacer ../ "%REPLACE%" <context.xml >webapps\cs\META-INF\context.xml
%SETUP% setup.Unzip Sites\csdt.zip home
echo >"%BASE%\Sites\log.out"
call sbt "sitesServer stop"
%SETUP% setup.PressEnter now
start /b setup1 "%JAVA_HOME%"
%SETUP% setup.WaitUntil "%BASE%\Sites\log.out" "press ENTER."
%SETUP% setup.DetectConfig "%BASE%\home" 
%SETUP% setup.SwitchDb home %DB%
call sbt "sitesServer start"
%SETUP% setup.PressEnter now
%SETUP% setup.WaitUntil Sites\log.out "Installation Finished Successfully"
sbt "sitesServer stop" "sitesServer script"
goto end
:notfoundsites
echo Sites installer not found.
echo Please download it from the Oracle website, 
echo then unzip the WCS_Sites*.zip in the wcs folder.
echo It is required the Sites installer in the wcs\Sites folder
goto end
:notfoundjava
echo java not found. Please install JDK and set JAVA_HOME environment variable. 
:end