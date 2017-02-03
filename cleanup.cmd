@echo off 
echo %PATH%
cd C:\Windows\System32
tskill "aapt"
tskill "adb"
tskill "java"
cd \