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
    public class Settings
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }


        }

        public enum Button
        {
            Login,
            Logout,
            LogoutDialog
        }

        public static void Click(Button btn)
        {
            if (btn == Button.Login)
                _instance.ClickById(AppFrameWork.Android.HomeScreen.Login);
            else if (btn == Button.Logout)
                _instance.ClickById(AppFrameWork.Android.HomeScreen.Logout);
            else if (btn == Button.LogoutDialog)
                _instance.ClickById(AppFrameWork.Android.HomeScreen.LogoutDialog);
        }



        public static string GetHeader()
        {
            return _instance.GetTextById(AppFrameWork.Android.HomeScreen.AppFrameworkHomeScreen);

        }

        public static bool IsVisible()
        {
            bool bVisible = false;
            if (GetHeader() == "Account settings")
                bVisible = true;
            return bVisible;
        }

        public static bool IsVisibleButton(Button btn)
        {
            bool IsVisible = false;

            if (btn == Button.Logout)
                IsVisible = _instance.GetElement(SearchBy.Id, AppFrameWork.Android.HomeScreen.Logout).Displayed;

            return IsVisible;

        }



    }

}

