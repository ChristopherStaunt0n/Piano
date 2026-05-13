@echo off
REM Piano Application Launcher
REM Runs the Piano application

setlocal enabledelayedexpansion

set JAR_FILE=target\Piano.jar
set PROJECT_DIR=%~dp0

echo.
echo ========================================
echo       Piano Application Launcher
echo ========================================
echo.

if exist "%PROJECT_DIR%%JAR_FILE%" (
    echo [*] Starting Piano Application...
    echo [*] Press keys on your keyboard to play music!
    echo [*] Close the window to exit.
    echo.
    start javaw -jar "%PROJECT_DIR%%JAR_FILE%"
) else (
    echo [-] ERROR: Piano.jar not found at: %JAR_FILE%
    echo [-] Please ensure the application has been compiled.
    echo [-] Run: javac -d target\classes -sourcepath src\main\java src\main\java\com\piano\*.java src\main\java\com\piano\util\*.java
    echo [-] Then: jar cvfm target\Piano.jar manifest.mf -C target\classes .
    pause
    exit /b 1
)

endlocal
