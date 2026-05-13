#!/bin/bash
# Piano Application Launcher
# Checks if JAR file exists, builds if necessary, then runs the application

JAR_FILE="target/Piano.jar"
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo ""
echo "========================================"
echo "       Piano Application Launcher"
echo "========================================"
echo ""

if [ -f "$PROJECT_DIR/$JAR_FILE" ]; then
    echo "[*] Found compiled Piano application"
    echo "[*] Launching Piano..."
    echo ""
    java -jar "$PROJECT_DIR/$JAR_FILE"
else
    echo "[!] JAR file not found at: $JAR_FILE"
    echo "[*] Attempting to build the project..."
    echo ""
    
    if [ -f "$PROJECT_DIR/pom.xml" ]; then
        mvn -f "$PROJECT_DIR/pom.xml" clean package -q
        if [ $? -eq 0 ]; then
            echo "[+] Build successful!"
            echo "[*] Launching Piano..."
            echo ""
            java -jar "$PROJECT_DIR/$JAR_FILE"
        else
            echo "[-] Build failed. Make sure Maven is installed and configured."
            echo "[-] Please run: mvn clean package"
            exit 1
        fi
    else
        echo "[-] pom.xml not found in: $PROJECT_DIR"
        echo "[-] Please ensure you are in the Piano project directory."
        exit 1
    fi
fi
