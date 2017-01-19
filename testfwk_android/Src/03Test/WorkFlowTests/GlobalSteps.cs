using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using TechTalk.SpecFlow;
using System.Threading;

namespace Philips.CDP.Automation.IAP.Test.ApplicationStartup
{
    [Binding]
    public sealed class PreDataSetup
    {
        private string _scenarioName = "";
        [BeforeTestRun]
        public static void BeforeTestRun()
        {
            string mobilePlatform = ConfigurationManager.AppSettings["MobilePlatformForTest"];
            if (mobilePlatform.Equals("Android"))
            {
                //For Debug Purpose a lightweight LoadConfiguration is being called. Need to comment after everything is working fine.
                // IAPConfiguration.LoadConfigurationForDebug();
                IAPConfiguration.LoadConfiguration();
            }
            else if (mobilePlatform.Equals("iOS"))
            {
                IAPConfiguration.LoadConfigurationForiOS();
            }
        }

        [AfterTestRun]
        public static void AfterTestRun()
        {
            string mobilePlatform = ConfigurationManager.AppSettings["MobilePlatformForTest"];
            if (mobilePlatform.Equals("Android"))
            {
                Thread.Sleep(10000);
                // Below code executes an ADB command and saves the logcat to a file with Timestamp.
                UtilityAuto.ExecuteProcessNoWait("/c adb -s 0676ac6e005c9655 logcat -d > C:\\Downloads\\\"Log-%DATE:/=-%_%TIME::=-%.txt\"");
                // Below code executes an ADB command and saves the logcat to a temp file, so that it can be used to crop Leak Canary information alone
                UtilityAuto.ExecuteProcessNoWait("/c adb -s 0676ac6e005c9655 logcat -d > C:\\Downloads\\TempLog.txt");
                //Below code executes a DOS command and greps the Leak Canary information from the log and saves it in a separate file with timestamp.
                UtilityAuto.ExecuteProcessNoWait("/c findstr c\\:\"D LeakCanary\" C:\\Downloads\\TempLog.txt > C:\\Downloads\\\"Leak-%DATE:/=-%_%TIME::=-%.txt\"");
                //Below code checks for the presence of memory leakage in the connected device and flags it as "FAIL" if memory leak has happened.
                UtilityAuto.ExecuteProcessNoWait("/c adb shell \"[ -d /sdcard/Download/leakcanary-com.philips.platform.referenceapp/ ] && echo \"Fail\" || echo \"Pass\"\"   ");
                if (UtilityAuto.output.Contains("Fail"))
                {
                    Logger.Fail("Memory Failure");
                    //Below code pulls the memory leak information from device to local machine.
                    UtilityAuto.ExecuteProcessNoWait("/c adb -s 0676ac6e005c9655 pull \"/sdcard/Download/leakcanary-com.philips.platform.referenceapp/\" C:\\Downloads\\");
                    //Below code removes the memory information from device, so that it won't give a false positive during next test case run
                    UtilityAuto.ExecuteProcessNoWait("/c adb -s 0676ac6e005c9655 shell rm -rf \"/sdcard/Download/leakcanary-com.philips.platform.referenceapp/\" ");
                }

                IAPConfiguration.CloseAll();
            }
            else if (mobilePlatform.Equals("iOS"))
            {

            }

        }

        [BeforeScenario]
        public void BeforeScenario()
        {
            _scenarioName = ScenarioContext.Current.ScenarioInfo.Title;
            Logger.Info(_scenarioName + " started");
        }

        [AfterScenario]
        public void AfterScenario()
        {
            Logger.Info(_scenarioName + " stopped");
			MobileDriver.ReLaunchApp(4);
        }
    }
}
