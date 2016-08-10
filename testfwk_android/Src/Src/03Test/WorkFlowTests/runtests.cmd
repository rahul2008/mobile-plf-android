

@cd ..\packages\SpecRun.Runner.*\tools
@pwd
@dir

@set profile=%1
@if "%profile%" == "" set profile=Default

SpecRun.exe run %profile%.srprofile "/baseFolder:D:\Jenkins_Slave\workspace\Reference_Test\testfwk_android\Src\Bin" /log:specrun.log %2 %3 %4 %5

:end

@popd
