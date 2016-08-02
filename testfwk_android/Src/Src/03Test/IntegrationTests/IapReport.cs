using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TechTalk.SpecFlow;

namespace Philips.CDP.Automation.IAP.Tests.Workflows
{
    public static class IapReport
    {
        public static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
       static HomeScreen hScreen = new HomeScreen();
        public static void Message(string message)
        {
            Console.WriteLine(message);
            Logger.Info(message);
        }

        public static void Fail(string message, Exception e)
        {
                string scenarioName = ScenarioContext.Current.ScenarioInfo.Title.Replace(" ", "").Replace("/", "");
                MobileDriver.TakeScreenshot(scenarioName + "_" + Application.ActivityName + "_" + UtilityAuto.GetRandomString());
                string innerMessage = (e.InnerException != null) ? e.InnerException.Message : e.Message;

                //check if the activity is launcher activity most probable is application crash
                if (Application.ActivityName.Equals("Launcher"))
                {
                    Console.WriteLine("Current Activity is Android Launcher activity / Iap Application Crashed.");
                }
                else { Console.WriteLine("Failure Message: \n" + message + "\n Exception Message: \n" + innerMessage); }
                //Logger.Fail("Failure Message: \n" + innerMessage + "\n " + e.StackTrace);
                UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell am force-stop com.philips.cdp.di.iapdemo ");
                UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell am  start -n com.philips.cdp.di.iapdemo/com.philips.cdp.di.iapdemo.DemoAppActivity ");
                //HomeScreen.Login(ConfigurationManager.AppSettings["UsernameIAP"], ConfigurationManager.AppSettings["PasswordIAP"]);
                Logger.Fail("Failure Message: \n" + innerMessage + "\n " + e.StackTrace);
            
        }
        

        public static void Fail(string message)
        {
            MobileDriver.TakeScreenshot(Application.ActivityName + "_" + UtilityAuto.GetRandomString());
            Console.WriteLine(message);
            if (Application.ActivityName.Equals("Launcher"))
            {
                Console.WriteLine("Current Activity is Android Launcher activity / Iap Application Crashed.");
            }
            UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell am force-stop com.philips.cdp.di.iapdemo ");
            UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell am  start -n com.philips.cdp.di.iapdemo/com.philips.cdp.di.iapdemo.DemoAppActivity ");
            LoginScreen.Login(ConfigurationManager.AppSettings["UsernameIAP"], ConfigurationManager.AppSettings["PasswordIAP"]);
            Logger.Fail(ScenarioContext.Current.ScenarioInfo.Title + ":" + message);
        }

    }
}

