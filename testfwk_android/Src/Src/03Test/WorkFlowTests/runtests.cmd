

@cd ..\packages\SpecRun.Runner.*\tools

@set profile=%1
@if "%profile%" == "" set profile=Default

SpecRun.exe run %profile%.srprofile "/baseFolder:C:\Philips\CDP_TestAutomation\Bin" /log:specrun.log %2 %3 %4 %5

:end

@popd
