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

        public static void SignIn(string username, string password)
        {
            EnterText(TextField.UserName, username);
            EnterText(TextField.PassWord, password);
            AppHomeScreen.Click(AppHomeScreen.Button.ALoginButton);
            Thread.Sleep(30000);
            try
            {
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
