@echo off
REM Connect to the base 20000 port server
REM Server asks for NationalID or references port 49152 registration

set HOST=%1
if "%HOST%"=="" set HOST=localhost
set PORT=20000
set REG_PORT=49152

echo Connecting to %HOST%:%PORT%...

REM Send IDENTIFY request and capture response
set TMPFILE=%TEMP%\gdgh_response_%RANDOM%.txt
echo IDENTIFY | ncat %HOST% %PORT% > "%TMPFILE%" 2>nul

if %ERRORLEVEL% neq 0 (
    echo [ERROR] Cannot reach server on port %PORT%
    exit /b 1
)

findstr /i "NationalID" "%TMPFILE%" >nul 2>nul
if %ERRORLEVEL% equ 0 (
    set /p NID="Enter NationalID: "
    echo NationalID:%NID% | ncat %HOST% %PORT%
    goto :cleanup
)

findstr /i "register" "%TMPFILE%" >nul 2>nul
if %ERRORLEVEL% equ 0 (
    echo Server requires registration. Redirecting to port %REG_PORT%...
    echo REGISTER | ncat %HOST% %REG_PORT%
    goto :cleanup
)

echo Server response:
type "%TMPFILE%"

:cleanup
if exist "%TMPFILE%" del "%TMPFILE%"
