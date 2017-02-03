@echo off
cmd.exe 
cd c:\windows\system32
tskill "aapt"
tskill "adb"
tskill "java"
 ^& exit