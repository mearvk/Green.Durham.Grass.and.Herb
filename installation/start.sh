#!/bin/bash
# Start the Green.Durham.Grass.and.Herb server

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

echo "[START] Launching Green.Durham.Grass.and.Herb..."
cd "$PROJECT_DIR"
java -cp out Main &
echo $! > "$PROJECT_DIR/gdgh.pid"
echo "[START] Server started (PID: $(cat "$PROJECT_DIR/gdgh.pid"))"
