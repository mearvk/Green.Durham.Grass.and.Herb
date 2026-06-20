#!/bin/bash
# Stop the Green.Durham.Grass.and.Herb server

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

if [ -f "$PROJECT_DIR/gdgh.pid" ]; then
    PID=$(cat "$PROJECT_DIR/gdgh.pid")
    kill "$PID" 2>/dev/null && echo "[STOP] Server stopped (PID: $PID)." || echo "[STOP] Process $PID not running."
    rm -f "$PROJECT_DIR/gdgh.pid"
else
    echo "[STOP] No PID file found. Attempting pkill..."
    pkill -f "java -cp out Main" && echo "[STOP] Server stopped." || echo "[STOP] No running server found."
fi
