@echo off 
cd C:\Windows\System32
call tskill "aapt"
call tskill "adb"
call tskill "java"
cd \