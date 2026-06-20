@echo off
REM Pre-installation: verify Java 21+, MySQL, and required directories

echo [PRE-INSTALL] Checking prerequisites...

where java >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: Java not found. Install JDK 21+.
    exit /b 1
)

where mysql >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: MySQL client not found.
    exit /b 1
)

echo [PRE-INSTALL] Java and MySQL detected.
echo [PRE-INSTALL] Prerequisites satisfied.
