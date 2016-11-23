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
    public class LaunchPR
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }


        }

        /// <summary>
        /// This class provides all the functionalities related to HomeScreen.
        /// </summary>
        public enum Button
        {
            ProductReg
        }


        /// <summary>
        /// checking whether btn visible or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsVisible(Button btn)
        {
            bool isVisble = false;
            try
            {
                if (btn == Button.ProductReg)
                    isVisble = _instance.GetElementByXpath(ProductRegistration.Android.HomeScreen.ProductReg).Displayed;                    
            }
            catch (Exception e)
            {
                isVisble = false;
            }
            return isVisble;
        }


        public static void Click(Button btn)
        {
            if (btn == Button.ProductReg)
                _instance.GetElementByXpath(ProductRegistration.Android.HomeScreen.ProductReg).Click();

        }

        public static bool WaitforProRegisterHomeScreen(int Sec)
        {

            int ElapsedTime = 0;
            IMobilePageControl homeScreenElement = null;

            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ProductRegistration.Android.HomeScreen.PRRegister);
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
