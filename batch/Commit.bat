@echo off
git add ..
set /p com="Enter what you changed: "
git commit -m "%com%"
PAUSE