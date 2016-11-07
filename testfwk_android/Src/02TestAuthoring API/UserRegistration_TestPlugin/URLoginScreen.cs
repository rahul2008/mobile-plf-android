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
    public class URLoginScreen
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
            Forgotten_Password,
            Back_Home,
            Merge,
            Cancel
        }

        public enum TextBox
        {
            Email,
            Password
        }

        /// <summary>
        /// wait for login_with_PhilipsAccount screen
        /// </summary>
        /// <returns></returns>
        public static bool WaitForLogin_with_PhilipsAccount()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, UserRegistration.Android.LoginScreen.Welcome_Title);
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
        /// TextBoxes editable or not
        /// </summary>
        /// <param name="txt"></param>
        /// <returns></returns>
        public static bool IsEditable(TextBox txt)
        {
            bool editable = false;
            try
            {
                if (txt == TextBox.Email)
                    editable = _instance.GetElement(SearchBy.Id, UserRegistration.Android.LoginScreen.Email).Enabled;
                if (txt == TextBox.Password)
                    editable = _instance.GetElement(SearchBy.Id, UserRegistration.Android.LoginScreen.Password).Enabled;
            }
            catch (Exception e)
            {
                editable = false;
            }
            return editable;
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
                if (btn == Button.Login)
                    isVisible = _instance.GetElement(SearchBy.Id, UserRegistration.Android.LoginScreen.Login).Displayed;
                if (btn == Button.Forgotten_Password)
                    isVisible = _instance.GetElement(SearchBy.Id, UserRegistration.Android.LoginScreen.Forgotten_Password).Displayed;
                if (btn == Button.Cancel)
                    isVisible = _instance.GetElement(SearchBy.Id, UserRegistration.Android.LoginScreen.Cancel).Displayed;
                if (btn == Button.Merge)
                    isVisible = _instance.GetElement(SearchBy.Id, UserRegistration.Android.LoginScreen.Merge).Displayed;
            }
            catch (Exception e)
            {
                isVisible = false;
            }
            return isVisible;
        }

        /// <summary>
        /// Buttons enabled or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsEnable(Button btn)
        {
            bool isEnable = false;
            try
            {
                if (btn == Button.Login)
                    isEnable = _instance.GetElement(SearchBy.Id, UserRegistration.Android.LoginScreen.Login).Enabled;
                if (btn == Button.Forgotten_Password)
                    isEnable = _instance.GetElement(SearchBy.Id, UserRegistration.Android.LoginScreen.Forgotten_Password).Enabled;
                if (btn == Button.Cancel)
                    isEnable = _instance.GetElement(SearchBy.Id, UserRegistration.Android.LoginScreen.Cancel).Enabled;
                if (btn == Button.Merge)
                    isEnable = _instance.GetElement(SearchBy.Id, UserRegistration.Android.LoginScreen.Merge).Enabled;
            }
            catch (Exception e)
            {
                isEnable = false;
            }
            return isEnable;
        }

        public static void Click(Button btn)
        {
            if (btn == Button.Login)
                _instance.ClickById(UserRegistration.Android.LoginScreen.Login);
            else if (btn == Button.Forgotten_Password)
                _instance.ClickById(UserRegistration.Android.LoginScreen.Forgotten_Password);
            else if (btn == Button.Back_Home)
                _instance.ClickById(UserRegistration.Android.LoginScreen.Login_BackButton);
            else if (btn == Button.Cancel)
                _instance.ClickById(UserRegistration.Android.LoginScreen.Cancel);
            else if (btn == Button.Merge)
                _instance.ClickById(UserRegistration.Android.LoginScreen.Merge);
        }
        /// <summary>
        /// entering values to textbox
        /// </summary>
        /// <param name="email"></param>
        /// <param name="password"></param>
        public static void Entry_Details(string email, string password)
        {
            EnterText(URLoginScreen.TextBox.Email, email);
            EnterText(URLoginScreen.TextBox.Password, password);
        }
        public static void EnterText(TextBox txt, string str)
        {
            string desiredText = string.Empty;
            switch (txt)
            {
                case TextBox.Email:
                    desiredText = UserRegistration.Android.LoginScreen.Email;
                    break;
                case TextBox.Password:
                    desiredText = UserRegistration.Android.LoginScreen.Password;
                    break;
            }
            _instance.GetElement(SearchBy.Id, desiredText).SetText(str);
        }

        /// <summary>
        /// Getting error msgs
        /// </summary>
        /// <returns></returns>
        public static string EmailErrorMsg()
        {
            string str = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.EmailError).Text;
            return str;
        }

        public static string PasswordErrMsg()
        {
            string str = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.PasswordErrMsg).Text;
            return str;

        }

        /// Login User confirmation
        /// </summary>
        /// <param name="name"></param>
        /// <returns></returns>
        public static bool User_LoginConfirm(string name)
        {
            string userName = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.User_LoginConfirm).Text;
            if (userName.Contains(name))
                return true;
            else
                return false;
        }

        /// <summary>
        /// error msg
        /// </summary>
        /// <param name="errorMail"></param>
        /// <param name="pwd"></param>
        /// <returns></returns>
        public static string InvalidEmail_pwd_ErrMsg()
        {
            string str = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.Terms_ConditionsErrorMsg).Text;
            return str;
        }
    }
}
