@echo off
REM Stop the Green.Durham.Grass.and.Herb server

echo [STOP] Stopping Green.Durham.Grass.and.Herb...
for /f "tokens=2" %%a in ('tasklist /fi "imagename eq java.exe" /fo list ^| find "PID:"') do (
    taskkill /pid %%a /f >nul 2>&1
)
echo [STOP] Server stopped.
