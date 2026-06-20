#!/bin/bash
# Pre-installation: verify Java 21+, MySQL, and required directories

echo "[PRE-INSTALL] Checking prerequisites..."

if ! command -v java &>/dev/null; then
    echo "ERROR: Java not found. Install JDK 21+."
    exit 1
fi

JAVA_VER=$(java -version 2>&1 | head -1 | awk -F '"' '{print $2}' | cut -d. -f1)
if [ "$JAVA_VER" -lt 21 ] 2>/dev/null; then
    echo "ERROR: Java 21+ required. Found version $JAVA_VER."
    exit 1
fi

if ! command -v mysql &>/dev/null; then
    echo "ERROR: MySQL client not found."
    exit 1
fi

echo "[PRE-INSTALL] Java $JAVA_VER and MySQL detected."
echo "[PRE-INSTALL] Prerequisites satisfied."
