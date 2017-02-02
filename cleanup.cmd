@echo off
cd c:\windows\system32
call tskill "java"
call tskill "adb"
call tskill "aapt"