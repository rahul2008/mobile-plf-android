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
    public class ParentalConsent
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
            Under_16,
            Above_16,
            Back_Home,
            Continue,
            Agree,
            DisAgree,
            Parental_Access_Continue
        }

        public enum TextView
        {
            How_Old,
            Birth_Year
        }

        /// <summary>
        /// wait for ParentalConsent screen
        /// </summary>
        /// <returns></returns>
        public static bool Wait()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, UserRegistration.Android.ParentalAccess.ParentalConsent_Title);
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

        /// <summary>
        /// getting header text
        /// </summary>
        /// <returns></returns>
        public static string GetHeader()
        {
            string str = _instance.GetElement(SearchBy.Id, UserRegistration.Android.ParentalAccess.ParentalConsent_Title).Text;
            return str;
        }
        /// <summary>
        /// Buttons Visible or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsVisible(Button btn)
        {
            bool isVisible = false;
            try
            {
                if (btn == Button.Under_16)
                    isVisible = _instance.GetElement(SearchBy.Id, UserRegistration.Android.ParentalAccess.Under_16).Displayed;
                if (btn == Button.Above_16)
                    isVisible = _instance.GetElement(SearchBy.Id, UserRegistration.Android.ParentalAccess.Above_16).Displayed;
                if (btn == Button.Continue)
                    isVisible = _instance.GetElement(SearchBy.Id, UserRegistration.Android.ParentalAccess.Continue).Displayed;
                if (btn == Button.Agree)
                    isVisible = _instance.GetElement(SearchBy.Id, UserRegistration.Android.ParentalAccess.Agree).Displayed;
            }
            catch (Exception e)
            {
                isVisible = false;
            }
            return isVisible;
        }

        /// <summary>
        /// Buttons Visible or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsEnable(Button btn)
        {
            bool isEnable = false;
            try
            {
                if (btn == Button.Under_16)
                    isEnable = _instance.GetElement(SearchBy.Id, UserRegistration.Android.ParentalAccess.Under_16).Enabled;
                if (btn == Button.Above_16)
                    isEnable = _instance.GetElement(SearchBy.Id, UserRegistration.Android.ParentalAccess.Above_16).Enabled;
                if (btn == Button.Continue)
                    isEnable = _instance.GetElement(SearchBy.Id, UserRegistration.Android.ParentalAccess.Continue).Enabled;
                if (btn == Button.Agree)
                    isEnable = _instance.GetElement(SearchBy.Id, UserRegistration.Android.ParentalAccess.Agree).Enabled;
            }
            catch (Exception e)
            {
                isEnable = false;
            }
            return isEnable;
        }

        /// <summary>
        /// Button clicking
        /// </summary>
        /// <param name="btn"></param>
        public static void Click(Button btn)
        {
            if (btn == Button.Back_Home)
                _instance.ClickById(UserRegistration.Android.ParentalAccess.BackHome);
            if (btn == Button.Under_16)
                _instance.ClickById(UserRegistration.Android.ParentalAccess.Under_16);
            if (btn == Button.Above_16)
                _instance.ClickById(UserRegistration.Android.ParentalAccess.Above_16);
            if (btn == Button.Continue)
                _instance.ClickById(UserRegistration.Android.ParentalAccess.Continue);
            if (btn == Button.Agree)
                _instance.ClickById(UserRegistration.Android.ParentalAccess.Agree);
            if (btn == Button.DisAgree)
                _instance.ClickById(UserRegistration.Android.ParentalAccess.DisAgree);
            if (btn == Button.Parental_Access_Continue)
                _instance.ClickById(UserRegistration.Android.ParentalAccess.Parental_Access_Continue);

        }

        public static void Click(TextView txt)
        {
            if (txt == TextView.How_Old)
                _instance.ClickById(UserRegistration.Android.ParentalAccess.How_Old);
            if (txt == TextView.Birth_Year)
                _instance.ClickById(UserRegistration.Android.ParentalAccess.Birth_Year);
        }
    }
}
