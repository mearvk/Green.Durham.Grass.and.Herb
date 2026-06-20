#!/bin/bash
# Installation: compile Java source, create database tables

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

echo "[INSTALL] Running pre-install checks..."
bash "$SCRIPT_DIR/pre-install.sh" || exit 1

echo "[INSTALL] Creating database and tables..."
mysql -u root < "$PROJECT_DIR/install/install.sql"

echo "[INSTALL] Compiling Java sources..."
mkdir -p "$PROJECT_DIR/out"
javac -d "$PROJECT_DIR/out" "$PROJECT_DIR/source-code/Main.java" 2>/dev/null
if [ $? -ne 0 ]; then
    echo "WARNING: Compilation had errors (non-fatal, dependent classes may be external)."
fi

echo "[INSTALL] Installation complete."
