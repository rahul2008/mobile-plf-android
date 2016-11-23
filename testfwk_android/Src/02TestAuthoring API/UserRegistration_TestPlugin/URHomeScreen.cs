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


namespace UserRegistration_TestPlugin
{
    public class URHomeScreen
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
        /// This class provides all the functionalities related to Home page.
        /// </summary>
        public enum Button
        {
            Account_Setting,
            Registration,
            Parental_Consent,
            Refresh_User
        }

        public static void Click(Button btn)
        {
            if (btn == Button.Account_Setting)
                _instance.ClickById(UserRegistration.Android.HomeScreen.AccountSetting);
            else if (btn == Button.Registration)
                _instance.ClickById(UserRegistration.Android.HomeScreen.Registration);
            else if (btn == Button.Parental_Consent)
                _instance.ClickById(UserRegistration.Android.HomeScreen.Parental_Consent);
            else if (btn == Button.Refresh_User)
                _instance.ClickById(UserRegistration.Android.HomeScreen.RefreshUser);
        }


        /// <summary>
        /// Checks if a button is Enabled or not
        /// </summary>

        public static bool IsEnable(Button btn)
        {
            bool isEnabled = false;
            try
            {
                if (btn == Button.Account_Setting)
                    isEnabled = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.AccountSetting).Enabled;
                if (btn == Button.Registration)
                    isEnabled = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.Registration).Enabled;
                if (btn == Button.Parental_Consent)
                    isEnabled = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.Parental_Consent).Enabled;
                if (btn == Button.Refresh_User)
                    isEnabled = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.RefreshUser).Enabled;
            }
            catch (Exception e)
            {
                isEnabled = false;
            }
            return isEnabled;
        }

        public static bool IsVisible(Button btn)
        {
            bool isVisible = false;
            try
            {
                if (btn == Button.Account_Setting)
                    isVisible = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.AccountSetting).Displayed;
                if (btn == Button.Registration)
                    isVisible = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.Registration).Displayed;
                if (btn == Button.Parental_Consent)
                    isVisible = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.Parental_Consent).Displayed;
                if (btn == Button.Refresh_User)
                    isVisible = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.RefreshUser).Displayed;
            }
            catch (Exception e)
            {
                isVisible = false;
            }
            return isVisible;
        }

        public static bool Wait()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.AccountSetting);
                loopCount++;
                if (homeScreenElement != null)
                    break;
                if (loopCount > 5)
                    break;
            }
            if (homeScreenElement != null)
                return true;
            else
                return false;
        }

    }
}
