using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using TechTalk.SpecFlow;

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
