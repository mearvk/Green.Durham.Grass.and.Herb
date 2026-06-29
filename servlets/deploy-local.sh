#!/bin/bash
# Green.Durham.Grass.and.Herb™ — Deploy Local
# Usage: bash modules/black/presidential/Green.Durham.Grass.and.Herb/servlets/deploy-local.sh [tomcat_home]
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
GDGH_ROOT="$(dirname "$SCRIPT_DIR")"
WEBAPP_SRC="$GDGH_ROOT/servlets/servlet/src/main/webapp"
TOMCAT_HOME="${1:-${CATALINA_HOME:-/opt/tomcat}}"
CONTEXT="gdgh"
DEPLOY_DIR="$TOMCAT_HOME/webapps/$CONTEXT"

echo "═══════════════════════════════════════════════════════════════"
echo " Green.Durham.Grass.and.Herb™ — Deploy Local"
echo " Target:  $DEPLOY_DIR"
echo "═══════════════════════════════════════════════════════════════"

rm -rf "$DEPLOY_DIR"
mkdir -p "$DEPLOY_DIR/WEB-INF/lib"
cp -r "$WEBAPP_SRC/"* "$DEPLOY_DIR/"

# Copy JDBC driver
JDBC_JAR=$(find "$(dirname "$GDGH_ROOT")" -name "mysql-connector-j*.jar" -type f 2>/dev/null | head -1)
[ -z "$JDBC_JAR" ] && JDBC_JAR=$(find "$TOMCAT_HOME/lib" -name "mysql-connector-j*.jar" -type f 2>/dev/null | head -1)
if [ -n "$JDBC_JAR" ]; then
    cp "$JDBC_JAR" "$DEPLOY_DIR/WEB-INF/lib/"
    echo "[*] MySQL connector: $(basename "$JDBC_JAR")"
else
    echo "[!] WARNING: mysql-connector-j not found — JDBC pages will fail"
fi

chown -R tomcat:tomcat "$DEPLOY_DIR" 2>/dev/null || true
echo "[✓] Deployed: http://localhost:8080/$CONTEXT/"
echo "═══════════════════════════════════════════════════════════════"
