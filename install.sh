#!/bin/bash

# Install MySQL Server
if command -v apt-get &> /dev/null; then
    sudo apt-get update
    sudo apt-get install -y mysql-server
    sudo systemctl start mysql
    sudo systemctl enable mysql
elif command -v yum &> /dev/null; then
    sudo yum install -y mysql-server
    sudo systemctl start mysqld
    sudo systemctl enable mysqld
elif command -v brew &> /dev/null; then
    brew install mysql
    brew services start mysql
else
    echo "Unsupported package manager. Install MySQL manually."
    exit 1
fi

# Create database and tables
mysql -u root < schema.sql

echo "MySQL installed and green_durham_grass_and_herb database created."
