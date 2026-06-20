@echo off
REM Installation: compile Java source, create database tables

set SCRIPT_DIR=%~dp0
set PROJECT_DIR=%SCRIPT_DIR%..

echo [INSTALL] Running pre-install checks...
call "%SCRIPT_DIR%pre-install.bat"
if %ERRORLEVEL% neq 0 exit /b 1

echo [INSTALL] Creating database and tables...
mysql -u root < "%PROJECT_DIR%\install\install.sql"

echo [INSTALL] Compiling Java sources...
if not exist "%PROJECT_DIR%\out" mkdir "%PROJECT_DIR%\out"
javac -d "%PROJECT_DIR%\out" "%PROJECT_DIR%\source-code\Main.java" 2>nul

echo [INSTALL] Installation complete.
