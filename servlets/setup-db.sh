#!/bin/bash
# Green.Durham.Grass.and.Herb™ — Setup MySQL Database
# Usage: bash modules/black/presidential/Green.Durham.Grass.and.Herb/servlets/setup-db.sh
set -e

DB_USER="root"
DB_PASS='$$Ironman1'
DB_HOST="127.0.0.1"
DB_NAME="nwe_gdgh"
MYSQL="mysql -u$DB_USER -p$DB_PASS -h$DB_HOST"

echo "═══════════════════════════════════════════════════════════════"
echo " Green.Durham.Grass.and.Herb™ — Setup Database"
echo "═══════════════════════════════════════════════════════════════"

$MYSQL <<SQL
CREATE DATABASE IF NOT EXISTS $DB_NAME CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE $DB_NAME;

CREATE TABLE IF NOT EXISTS labor_concerns (
    id INT AUTO_INCREMENT PRIMARY KEY,
    concern TEXT NOT NULL,
    category VARCHAR(100),
    source VARCHAR(255),
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_category (category)
);

CREATE TABLE IF NOT EXISTS ethical_concerns (
    id INT AUTO_INCREMENT PRIMARY KEY,
    concern TEXT NOT NULL,
    principle VARCHAR(100),
    source VARCHAR(255),
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_principle (principle)
);

CREATE TABLE IF NOT EXISTS moral_concerns (
    id INT AUTO_INCREMENT PRIMARY KEY,
    concern TEXT NOT NULL,
    context VARCHAR(255),
    severity VARCHAR(20),
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_severity (severity)
);

CREATE TABLE IF NOT EXISTS listeners (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    port INT NOT NULL,
    region VARCHAR(50),
    status VARCHAR(20) DEFAULT 'offline',
    last_seen TIMESTAMP NULL,
    INDEX idx_port (port)
);

INSERT IGNORE INTO listeners (id, name, port, region, status) VALUES
(1, 'Strernary Directory', 2000, 'National', 'configured'),
(2, 'Appree Base', 20000, 'National', 'configured'),
(3, 'East Coast Listener', 40002, 'East Coast', 'configured'),
(4, 'West Coast Listener', 40003, 'West Coast', 'configured'),
(5, 'Texas Listener', 40007, 'Texas', 'configured'),
(6, 'NationalFinanceID', 49152, 'National', 'configured');
SQL

echo "[✓] Database $DB_NAME ready (labor_concerns, ethical_concerns, moral_concerns, listeners)"
echo "═══════════════════════════════════════════════════════════════"
