#!/bin/bash
mysql -u root < "$(dirname "$0")/install.sql"
echo "Tables created: ethical, labor, moral, mortality"
