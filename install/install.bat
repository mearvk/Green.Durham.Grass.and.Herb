@echo off
mysql -u root < "%~dp0install.sql"
echo Tables created: ethical, labor, moral, mortality
