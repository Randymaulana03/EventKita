@echo off
REM Start EventKita Backend
REM Run this from: d:\Coding\EventKita\eventkita\

cd eventkita
echo Building...
call mvnw.cmd clean package -DskipTests

if %ERRORLEVEL% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo.
echo Build success! Starting backend...
echo Backend will run on http://localhost:8888
echo Press Ctrl+C to stop
echo.

java -jar target\eventkita-0.0.1-SNAPSHOT.jar

pause
