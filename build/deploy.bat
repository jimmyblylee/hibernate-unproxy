@echo off
title Deploying hibernate-unproxy
call msg info "[INFO] Deploying hibernate-unproxy" & echo.

set BASEDIR=%~dp0

pushd %BASEDIR%\..\src\unproxy
  call mvn clean deploy -DperformTest=true -DperformSource=true -DperformDeploy=true
popd

call beep.bat
timeout /t 1 >NUL 
call beep.bat
timeout /t 1 >NUL 
call beep.bat
timeout /t 1 >NUL 
call beep.bat
timeout /t 1 >NUL 
call beep.bat
timeout /t 1 >NUL 

pause