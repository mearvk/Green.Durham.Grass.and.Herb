#!/usr/bin/env bash
set -euo pipefail

# Bootstrap MySQL for Green.Durham.Grass.and.Herb.
# This script is an installation/bootstrap operation. The application runtime
# must use a dedicated database account, not MySQL root.

if command -v apt-get >/dev/null 2>&1; then
    sudo apt-get update
    sudo apt-get install -y mysql-server
    sudo systemctl enable --now mysql
elif command -v yum >/dev/null 2>&1; then
    sudo yum install -y mysql-server
    sudo systemctl enable --now mysqld
elif command -v brew >/dev/null 2>&1; then
    brew install mysql
    brew services start mysql
else
    echo "Unsupported package manager. Install MySQL manually." >&2
    exit 1
fi

if ! command -v mysql >/dev/null 2>&1; then
    echo "MySQL client was not found after installation." >&2
    exit 1
fi

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
SCHEMA="${SCRIPT_DIR}/schema.sql"

if [[ ! -f "${SCHEMA}" ]]; then
    echo "Schema not found: ${SCHEMA}" >&2
    exit 1
fi

# Use the local administrative account only for schema bootstrap.
# Do not configure the application itself to use this account.
sudo mysql < "${SCHEMA}"

echo "MySQL bootstrap completed for green_durham_grass_and_herb."
echo "Runtime configuration must use a dedicated least-privilege account."
echo "See configuration/db-config.example.xml and SOURCE-CODE-AUDIT.md."
