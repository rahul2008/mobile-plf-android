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
            Alogout,
            HamburgerIcon,
            Settings,
            Connectivity,     
            AlogoutConfirm,
            ALoginButton,
            ALoginContinue,
            ALoginContinueConfirm,
            MeasurementValueFromReferenceDevice,
            MomentValueFromDatacore,
            Terms,
            Support

        }

        public static void Click(Button btn)
        {
            if (btn == Button.HamburgerIcon)
                _instance.ClickById(AppFrameWork.Android.HomeScreen.HamburgerIcon);
            else if (btn == Button.Settings)
                _instance.GetElementByXpath(AppFrameWork.Android.HomeScreen.Settings).Click();
            else if (btn == Button.Connectivity)
                _instance.GetElementByXpath(AppFrameWork.Android.HomeScreen.Connectivity).Click();
            if (btn == Button.MeasurementValueFromReferenceDevice)
                _instance.ClickById(AppFrameWork.Android.HomeScreen.MeasurementValueFromReferenceDevice);
            if (btn == Button.Alogout)
                _instance.ClickById(AppFrameWork.Android.HomeScreen.Alogout);
            if (btn == Button.AlogoutConfirm)
                _instance.ClickById(AppFrameWork.Android.HomeScreen.AlogoutConfirm);
            if (btn == Button.ALoginButton)
                _instance.ClickById(AppFrameWork.Android.HomeScreen.ALoginButton);
            if (btn == Button.ALoginContinue)
                _instance.ClickById(AppFrameWork.Android.HomeScreen.ALoginContinue);
            if (btn == Button.ALoginContinueConfirm)
                _instance.ClickById(AppFrameWork.Android.HomeScreen.ALoginContinueConfirm);
            if (btn == Button.MomentValueFromDatacore)
                 _instance.ClickById(AppFrameWork.Android.HomeScreen.MomentValueFromDatacore);
            if (btn == Button.Terms)
                _instance.ClickById(AppFrameWork.Android.HomeScreen.Terms);
            if (btn == Button.Support)
                _instance.GetElementByXpath(AppFrameWork.Android.HomeScreen.Support).Click();
        }

        public enum EditText
        {
            ReferenceDeviceIDValue,
            MeasurementValue,
            Moment
        }

        /// <summary>
        /// Returns a string containing the title of the screen.
        /// </summary>
        /// <returns>string value containing the title</returns>
        public static string GetScreenTitleAppFameworkHomeScreen()
        {
            return _instance.GetTextById(AppFrameWork.Android.HomeScreen.AppFrameworkHomeScreen);

        }


        public static string GetText(EditText et)
        {
            switch (et)
            {
                case EditText.ReferenceDeviceIDValue:
                    return _instance.GetElement(SearchBy.Id, ObjectRepository.ReferenceDeviceIDValue).Text;
                case EditText.MeasurementValue:
                    return _instance.GetElement(SearchBy.Id, ObjectRepository.MeasurementValue).Text;
                case EditText.Moment:
                    return _instance.GetElement(SearchBy.Id, ObjectRepository.MomentValue).Text;
                default:
                    Logger.Info("Error: AppHomeScreen.GetText not implemented for " + et.ToString());
                    break;
            }
            return String.Empty;
        }

        public static string GetDatacoreErrorMsg()
        {
            return _instance.GetElement(SearchBy.Id, ObjectRepository.DatacoreErrorMsg).Text;
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


        public static void EnterText(EditText editText, string value)
        {
            switch (editText)
            {
                case EditText.ReferenceDeviceIDValue:
                    _instance.GetElement(SearchBy.Id, ObjectRepository.ReferenceDeviceIDValue).SetText(value);
                    break;
                case EditText.MeasurementValue:
                    _instance.GetElement(SearchBy.Id, ObjectRepository.MeasurementValue).SetText(value);
                    break;
                default:
                    Logger.Info("Error: AppHomeScreen.GetText not implemented for " + editText.ToString());
                    break;
            }
        }

        public static bool IsVisibleScreenTitleText(string TitleText)
        {
            bool IsVisible1 = false;
            try
            {

                if (GetScreenTitleAppFameworkHomeScreen() == TitleText)
                    IsVisible1 = true;

            }
            catch (Exception)
            {
                IsVisible1 = false;
            }
            return IsVisible1;
        }

    
    }
}

