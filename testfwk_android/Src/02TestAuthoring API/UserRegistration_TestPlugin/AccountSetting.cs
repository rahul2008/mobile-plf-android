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
    public class AccountSetting
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
            Logout,
            Back_Home
        }

        public enum CheckBox
        {
            Terms_Conditions,
            Philips_Announcements
        }

        /// <summary>
        /// wait for Account Setting screen
        /// </summary>
        /// <returns></returns>
        public static bool Wait()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, UserRegistration.Android.AccountSetting.AccountSetting_Title);
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


        public static bool IsVisible(Button btn)
        {
            bool isVisibe = false;
            try
            {
                if (btn == Button.Logout)
                    isVisibe = _instance.GetElement(SearchBy.Name, "Log Out").Displayed;
            }
            catch (Exception e)
            {
                isVisibe = false;
            }
            return isVisibe;

        }

        public static bool IsEnable(Button btn)
        {
            bool isEnable = false;
            try
            {
                if (btn == Button.Logout)
                    isEnable = _instance.GetElement(SearchBy.Name, "Log Out").Enabled;
            }
            catch (Exception e)
            {
                isEnable = false;
            }
            return isEnable;

        }

        /// <summary>
        /// get Header title
        /// </summary>
        /// <returns></returns>
        public static string GetHeader()
        {
            string str = _instance.GetElement(SearchBy.Id, UserRegistration.Android.AccountSetting.AccountSetting_Title).Text;
            return str;
        }

        /// <summary>
        /// get Welcome title
        /// </summary>
        /// <returns></returns>
        public static string GetWelcome_Title()
        {
            string str = _instance.GetElement(SearchBy.Id, UserRegistration.Android.AccountSetting.Welcome_Title).Text;
            return str;
        }

        public static void Click(Button btn)
        {
            if (btn == Button.Back_Home)
                _instance.GetElement(SearchBy.Id, UserRegistration.Android.AccountSetting.Back).Click();
            else if (btn == Button.Logout)
                _instance.GetElement(SearchBy.Name, "Log Out").Click();
        }

        public static void Click(CheckBox chkBox)
        {
            List<IMobilePageControl> control = _instance.GetElements(SearchBy.Id, UserRegistration.Android.CreateAccount.Checkbox);
            if (control.Count == 1)
            {
                if (chkBox == CheckBox.Philips_Announcements)
                    control[0].Click();
            }
            else
            {
                if (chkBox == CheckBox.Terms_Conditions)
                {
                    control[0].Click();
                }
                else
                {
                    control[1].Click();

                }
            }
        }
    }
}
