@echo off 
echo %PATH%
cd /d C:\Windows\System32
dir ts*
tskill "aapt"
tskill "adb"
tskill "java"
cd \