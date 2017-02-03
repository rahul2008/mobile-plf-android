@echo off 
echo %PATH%
cd /d C:\Windows\System32
dir
tskill "aapt"
tskill "adb"
tskill "java"
cd \