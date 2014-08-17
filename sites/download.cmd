if not "%~1"=="" set JAVA_HOME=%1
set BDRV=%~d0
set BASE=%~dp0
%BDRV%
cd "%BASE%"
"%JAVA_HOME%\bin\javaw" -jar "%BASE%\bin\tools.jar" Downloader

