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
    public class HamburgerMenu
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }


        }

        

        public static void HamburgerlistClick(string str)
        {
           
            List<IMobilePageControl> control = _instance.GetElements(SearchBy.Id, AppFrameWork.Android.HomeScreen.HamburgerList);
            foreach (IMobilePageControl cntrl in control)
            {
                string str1 = cntrl.Text;
                if (str1 == str)
                {
                    _instance.ClickByName(str);
                    break;
                }
            }
        }

        public static void Click(String btn)
        {
            _instance.ClickByName(btn);
        }
        public static bool WaitforCoCOScreen(string screenName)
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Name, screenName);
                loopCount++;
                if (homeScreenElement != null)
                    break;
                if (loopCount > 10)
                {
                    Thread.Sleep(1000);
                    break;
                }
            }
            if (homeScreenElement != null)
                return true;
            else
                return false;
        }

    }

    public class Log_In
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }

        }
        public  enum TextField
        {
            UserName,
            PassWord
        }
        public static void Click()
        {
            _instance.ClickById(AppFrameWork.Android.HomeScreen.PhilipsAccountReg);
        }

        public static bool WaitForTerms()
        {
            int loopCount = 0;
            IMobilePageControl TermsElement = null;
            while (TermsElement == null)
            {
                TermsElement = _instance.GetElement(SearchBy.Id, AppFrameWork.Android.HomeScreen.Terms);
                loopCount++;
                if (TermsElement != null)
                    break;
                if (loopCount > 10)
                {
                    Thread.Sleep(1000);
                    break;
                }
            }
            if (TermsElement != null)
                return true;
            else
                return false;
        }

        public static void SignIn(string username, string password)
        {
            EnterText(TextField.UserName, username);
            EnterText(TextField.PassWord, password);
            AppHomeScreen.Click(AppHomeScreen.Button.ALoginButton);
            try
            {
                WaitForTerms();
                IMobilePageControl terms = _instance.GetElement(SearchBy.Id, AppFrameWork.Android.HomeScreen.Terms);
                terms.Click();
                Thread.Sleep(1000);
                AppHomeScreen.Click(AppHomeScreen.Button.ALoginContinue);
                Thread.Sleep(1000);
            }
            catch (Exception e) { }
            AppHomeScreen.Click(AppHomeScreen.Button.ALoginContinueConfirm);
           
        }


       


        public static void EnterText(TextField fld, string str)
        {
            string text = string.Empty;
            if (fld == TextField.UserName)
                text = AppFrameWork.Android.HomeScreen.UserName;
            else if (fld == TextField.PassWord)
                text = AppFrameWork.Android.HomeScreen.PassWord;
            _instance.GetElement(SearchBy.Id, text).SetText(str);
            

        }
    }
}
