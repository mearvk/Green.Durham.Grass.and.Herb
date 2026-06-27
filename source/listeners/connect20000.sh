#!/bin/bash
# Connect to the base 20000 port server
# Server asks for NationalID or references port 49152 registration

HOST="${1:-localhost}"
PORT=20000
REG_PORT=49152

echo "Connecting to $HOST:$PORT..."
exec 3<>/dev/tcp/$HOST/$PORT 2>/dev/null

if [ $? -ne 0 ]; then
  echo "[ERROR] Cannot reach server on port $PORT"
  exit 1
fi

echo "IDENTIFY" >&3
read -r RESPONSE <&3

if echo "$RESPONSE" | grep -qi "NationalID"; then
  read -p "Enter NationalID: " NID
  echo "NationalID:$NID" >&3
  read -r RESULT <&3
  echo "Server: $RESULT"
elif echo "$RESPONSE" | grep -qi "register"; then
  echo "Server requires registration. Redirecting to port $REG_PORT..."
  exec 3<&-
  exec 3<>/dev/tcp/$HOST/$REG_PORT 2>/dev/null
  if [ $? -ne 0 ]; then
    echo "[ERROR] Cannot reach registration server on port $REG_PORT"
    exit 1
  fi
  echo "REGISTER" >&3
  read -r REG_RESULT <&3
  echo "Registration: $REG_RESULT"
else
  echo "Server: $RESPONSE"
fi

exec 3<&-
