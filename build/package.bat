@echo off
title Packaging hibernate-unproxy
call msg info "[INFO] Packaging hibernate-unproxy" & echo.

set BASEDIR=%~dp0

pushd %BASEDIR%\..\src
  call mvn clean package
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

timeout /t 10 >NUL 