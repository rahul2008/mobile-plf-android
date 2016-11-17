using OpenQA.Selenium.Appium.MultiTouch;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Diagnostics;
using Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin;

namespace ProdReg.Android.TestPlugin
{
    public class PRSuccess
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }


        }


        public static string SuccessMessage()
        {
            string text = _instance.GetElement(SearchBy.Id, ProductRegistration.Android.HomeScreen.PRSuccess).Text;
            return text;
        }

        public enum Button
        {
            PR_Continue,
            PR_Back,
            PROK
        }

        public static void Click(Button btn)
        {
            if (btn == Button.PR_Continue)
                _instance.GetElement(SearchBy.Id, ProductRegistration.Android.HomeScreen.PRContinue).Click();
            if (btn == Button.PR_Back)
                _instance.GetElement(SearchBy.Id, ProductRegistration.Android.HomeScreen.PRBack).Click();
            if (btn == Button.PROK)
                _instance.GetElement(SearchBy.Id, ProductRegistration.Android.HomeScreen.PROK).Click();
        }

        public static string RepeatProdRegisterMessage()
        {
            string text = _instance.GetElement(SearchBy.Id, ProductRegistration.Android.HomeScreen.RepeatPRText).Text;
            return text;
        }

        public static bool WaitforSuccessRegister(int Sec)
        {

            int ElapsedTime = 0;
            IMobilePageControl homeScreenElement = null;

            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ProductRegistration.Android.HomeScreen.PRContinue);
                if (homeScreenElement != null)
                    break;
                Thread.Sleep(1000);
                ElapsedTime = ElapsedTime + 1;
                if (ElapsedTime > Sec)
                    break;
            }

            if (homeScreenElement != null)
                return true;
            else
                return false;
        }

    }
}
