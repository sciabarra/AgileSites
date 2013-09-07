@echo off
if [%1] == [] goto error
if not exist %1.build goto error
copy %1.build build.sbt >nul
set "msg=shellPrompt in ThisBuild := { x =^> "%1^> ^" }"
echo %msg% >>build.sbt
call agilesites.cmd %2 %3 %4 %5 %6 %7 %8 %9
goto end
:error
echo usage: "switch <config>"
echo where config must be a valid build.sbt file
echo named "<config>.build"
:end
