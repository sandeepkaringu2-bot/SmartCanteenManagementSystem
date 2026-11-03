@echo off
cd /d "%~dp0"
echo Starting Smart Canteen Management System...
mvn exec:java -Dexec.mainClass="com.smartcanteen.Main"
pause
