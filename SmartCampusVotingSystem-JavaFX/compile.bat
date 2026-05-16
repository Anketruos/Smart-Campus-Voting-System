@echo off
setlocal

for %%I in ("%~dp0.") do set "ROOT=%%~sI"
set "SRC=%ROOT%\src"
set "OUT=%ROOT%\out"
set "LIB=%ROOT%\lib"
set "JAVAFX_LIB=%LIB%\javafx"
set "SOURCES=%ROOT%\sources.txt"
set "ERR_LOG=%ROOT%\compile-errors.log"

if not exist "%JAVAFX_LIB%" (
    echo JavaFX jar folder not found: %JAVAFX_LIB%
    exit /b 1
)

dir /b "%JAVAFX_LIB%\*.jar" > nul 2>&1
if errorlevel 1 (
    echo No JavaFX jars were found in %JAVAFX_LIB%
    exit /b 1
)

dir /b "%LIB%\sqlite-jdbc-*.jar" > nul 2>&1
if errorlevel 1 (
    echo sqlite-jdbc jar not found in %LIB%.
    exit /b 1
)

if exist "%OUT%" rmdir /s /q "%OUT%"
mkdir "%OUT%"

if exist "%SOURCES%" del "%SOURCES%"
if exist "%ERR_LOG%" del "%ERR_LOG%"
pushd "%ROOT%"
cmd /c "dir /s /b src\*.java > sources.txt"

javac -cp "%JAVAFX_LIB%\*" -d "%OUT%" @"%SOURCES%" 2> "%ERR_LOG%"
if errorlevel 1 (
    type "%ERR_LOG%"
    exit /b 1
)

xcopy /e /i /y "%SRC%\resources" "%OUT%" > nul
popd
if exist "%SOURCES%" del "%SOURCES%"
if exist "%ERR_LOG%" del "%ERR_LOG%"

echo Compilation completed successfully.
