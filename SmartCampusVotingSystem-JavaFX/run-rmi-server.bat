@echo off
setlocal

call "%~dp0compile.bat"
if errorlevel 1 exit /b 1

for %%I in ("%~dp0.") do set "ROOT=%%~sI"
set "OUT=%ROOT%\out"
set "LIB=%ROOT%\lib"
set "JAVAFX_LIB=%LIB%\javafx"

java --module-path "%JAVAFX_LIB%" --add-modules javafx.controls -cp "%OUT%;%LIB%\*;%JAVAFX_LIB%\*" rmi.VotingRMIServer
