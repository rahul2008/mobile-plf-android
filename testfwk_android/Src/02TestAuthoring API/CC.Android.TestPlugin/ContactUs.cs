using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Threading;

namespace Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin
{
    public class ContactUs
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
            On_Twitter,
            On_faceBook,
            Live_Chat,
            Send_Email,
            Call,
            Backto_HomeScreen,
            OK
        }

        /// <summary>
        /// header text
        /// </summary>
        /// <returns></returns>
        public static string GetHeaderTextFAQ()
        {
            return _instance.GetTextById(ConsumerCare.Android.ContactUs.HeaderTxt);
        }
        public static bool IsVisible1()
        {
            bool bVisible = false;
            if (GetHeaderTextFAQ() == "Contact us")
                bVisible = true;
            return bVisible;

        }

        /// <summary>
        /// waiting for FAQ home screen
        /// </summary>
        /// <returns></returns>
        public static bool WaitforContactUsHomeScreen()
        {
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ContactUs.HeaderTxt);
                if (homeScreenElement != null)
                    break;
            }
            if (homeScreenElement != null)
                return true;
            else
                return false;
        }

        public static void Back()
        {
            Thread.Sleep(1000);
            MobileDriver.FireKeyEvent(4);
        }

        /// <summary>
        /// checking whether btn visible or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsVisible(Button btn)
        {
            bool isVisble = false;
            if (btn == Button.Backto_HomeScreen)
                isVisble = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ContactUs.BacktoSupport).Displayed;
            if (btn == Button.On_Twitter)
                isVisble = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.ContactUs.OnTwitter).Displayed;
            if (btn == Button.On_faceBook)
                isVisble = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.ContactUs.OnFacebook).Displayed;
            if (btn == Button.Live_Chat)
                isVisble = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ContactUs.LiveChat).Displayed;
            if (btn == Button.Send_Email)
                isVisble = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ContactUs.SendEmail).Displayed;
            if (btn == Button.Call)
                isVisble = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ContactUs.Call).Displayed;
            return isVisble;
        }

        /// <summary>
        /// checking whether button is enabled or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsEnable(Button btn)
        {
            bool isEnable = false;
            if (btn == Button.Backto_HomeScreen)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ContactUs.BacktoSupport).Enabled;
            if (btn == Button.On_Twitter)
                isEnable = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.ContactUs.OnTwitter).Enabled;
            else if (btn == Button.On_faceBook)
                isEnable = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.ContactUs.OnFacebook).Enabled;
            else if (btn == Button.Live_Chat)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ContactUs.LiveChat).Enabled;
            else if (btn == Button.Send_Email)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ContactUs.SendEmail).Enabled;
            else if (btn == Button.Call)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ContactUs.Call).Enabled;
            return isEnable;
        }

        /// <summary>
        /// checking whether button or clickable or not
        /// </summary>
        /// <param name="btn"></param>
        public static void ClickContact(Button btn)
        {
            if (btn == Button.Backto_HomeScreen)
                _instance.ClickById(ConsumerCare.Android.ContactUs.BacktoSupport);
            if (btn == Button.On_Twitter)
                _instance.ClickByName(ConsumerCare.Android.ContactUs.OnTwitter);
            else if (btn == Button.On_faceBook)
                _instance.ClickByName(ConsumerCare.Android.ContactUs.OnFacebook);
            else if (btn == Button.Live_Chat)
                _instance.ClickById(ConsumerCare.Android.ContactUs.LiveChat);
            else if (btn == Button.Send_Email)
                _instance.ClickById(ConsumerCare.Android.ContactUs.SendEmail);
            else if (btn == Button.Call)
                _instance.ClickById(ConsumerCare.Android.ContactUs.Call);
        }

        /// <summary>
        /// Checking with twitter page
        /// </summary>
        /// <returns></returns>
        public static bool TwitterPage()
        {           
            bool isEnable = false;
            IMobilePageControl control = _instance.GetElement(SearchBy.ClassName, "android.webkit.WebView");
            if (control != null)
            {
                ContactUs.ClickContact(ContactUs.Button.Backto_HomeScreen);
                return true;
            }
            else
                try
                {
                    isEnable = TwitterView.IsEnableTwitter(TwitterView.Button.SignUp);
                    isEnable = TwitterView.IsEnableTwitter(TwitterView.Button.LogIn);
                    MobileDriver.FireKeyEvent(4);
                    ContactUs.WaitforContactUsHomeScreen();
                }
                catch (Exception e)
                {
                    isEnable = false;
                }
        return isEnable;
        }

        /// <summary>
        /// Checking with Facebook page
        /// </summary>
        /// <returns></returns>
        public static bool FacebookPage()
        {
            bool isEnable = false;
            IMobilePageControl control = _instance.GetElement(SearchBy.ClassName, "android.webkit.WebView");
            if (control != null)
            {
                ContactUs.ClickContact(ContactUs.Button.Backto_HomeScreen);
                return true;
            }
            else
                try
                {
                    isEnable = FacebookView.IsEnable(FacebookView.Button.LogIn);
                    isEnable = FacebookView.IsEnable1(FacebookView.EditBox.Password);
                    isEnable = FacebookView.IsEnable1(FacebookView.EditBox.Username);
                    MobileDriver.FireKeyEvent(4);
                    ContactUs.WaitforContactUsHomeScreen();
                }
                catch (Exception e)
                {
                    isEnable = false;
                }
            return isEnable;
        }

        /// <summary>
        /// Click Ok button after pressing call button
        /// </summary>
        /// <param name="btn"></param>
        public static void ClickCallOk(Button btn)
        {
            if (btn == Button.OK)
                _instance.ClickById(ConsumerCare.Android.ContactUs.OK);
        }
    }

    public class FacebookView
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
            LogIn,
            Join
        }

        public enum EditBox
        {
            Username,
            Password,

        }

        public static bool IsEnable(Button btn)
        {
            bool isEnable = false;
            try
            {


                if (btn == Button.LogIn)
                    isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.FacebookViewPage.LoginButton).Enabled;
            }
            catch
            {

            }
            return isEnable;
        }

        public static bool IsEnable1(EditBox editTxt)
        {
            bool isEnable = false;
            if (editTxt == EditBox.Username)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.FacebookViewPage.UserName).Enabled;
            if (editTxt == EditBox.Username)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.FacebookViewPage.Password).Enabled;
            return isEnable;
        }
    }

    public class TwitterView
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
            LogIn,
            SignUp
        }

        public static bool IsEnableTwitter(Button btn)
        {
            bool isEnable = false;
            if (btn == Button.SignUp)
                isEnable = _instance.GetElement(SearchBy.Name, "Sign up").Enabled;
            else if(btn == Button.LogIn)
                isEnable = _instance.GetElement(SearchBy.Name, "Log in and Tweet").Enabled;
            return isEnable;
        }
    }
}
