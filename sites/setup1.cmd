@ECHO OFF
set BDRV=%~d0
set BASE=%~dp0
set JAVA_HOME=%~1
%BDRV%
cd "%BASE%\Sites"
set PATH="%JAVA_HOME%\bin";%PATH%
"%JAVA_HOME%\bin\java" -cp "%BASE%\bin\setup.jar" setup.PressEnter | call csInstall -silent >log.out
exit

