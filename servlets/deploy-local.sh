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

mkdir -p "$DEPLOY_DIR/WEB-INF/lib"
cp -r "$WEBAPP_SRC/"* "$DEPLOY_DIR/"

# Copy JDBC driver
for JAR_DIR in "$GDGH_ROOT/../Brarner.M.Alete/jars" "$GDGH_ROOT/jars"; do
    if ls "$JAR_DIR/mysql-connector-j"*.jar &>/dev/null 2>&1; then
        cp "$JAR_DIR/mysql-connector-j"*.jar "$DEPLOY_DIR/WEB-INF/lib/"
        echo "[*] MySQL connector copied"
        break
    fi
done

chown -R tomcat:tomcat "$DEPLOY_DIR" 2>/dev/null || true
echo "[✓] Deployed: http://localhost:8080/$CONTEXT/"
echo "═══════════════════════════════════════════════════════════════"
