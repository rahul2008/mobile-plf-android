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

namespace Philips.SIG.Automation.Android.CDPP.AppFramework_TestPlugin
{
    public class AppHomeScreen
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
            HamburgerIcon,
            Settings

        }

        public static void Click(Button btn)
        {
            if (btn == Button.HamburgerIcon)
                _instance.ClickById(AppFrameWork.Android.HomeScreen.HamburgerIcon);
            else if (btn == Button.Settings)
                _instance.GetElementByXpath(AppFrameWork.Android.HomeScreen.Settings).Click();
                
         }

        /// <summary>
        /// Returns a string containing the title of the screen.
        /// </summary>
        /// <returns>string value containing the title</returns>
        public static string GetScreenTitleAppFameworkHomeScreen()
        {
            return _instance.GetTextById(AppFrameWork.Android.HomeScreen.AppFrameworkHomeScreen);

        }

        public static bool IsVisible1()
        {
            bool IsVisible1 = false;
            if (GetScreenTitleAppFameworkHomeScreen() == "Mobile App Home")
                IsVisible1 = true;
            return IsVisible1;

        }

        public static void DismissHamburger()
        {
            HamburgerMenu.HamburgerlistClick("Mobile App Home");
        }



        /// <summary>
        /// waiting for homescreen to open
        /// </summary>
        /// <returns></returns>
        public static bool WaitforAppFrameworkHomeScreen(int Sec)
        {

            int ElapsedTime = 0;
            IMobilePageControl homeScreenElement = null;

            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, AppFrameWork.Android.HomeScreen.AppFrameworkHomeScreen);
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

