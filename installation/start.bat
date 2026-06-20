@echo off
REM Start the Green.Durham.Grass.and.Herb server

set SCRIPT_DIR=%~dp0
set PROJECT_DIR=%SCRIPT_DIR%..

echo [START] Launching Green.Durham.Grass.and.Herb...
cd /d "%PROJECT_DIR%"
start /b java -cp out Main
echo [START] Server started.
