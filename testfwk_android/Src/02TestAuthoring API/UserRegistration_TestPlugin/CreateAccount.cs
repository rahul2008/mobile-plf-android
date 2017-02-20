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
    public class CreateAccount
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
            Back_Login,
            Create_Account,
            Activated_Account,
            Resend,
            URContinue,
            LoogedIn_Continue,
            GoogleSignIn
        }

        public enum TextBox
        {
            FirstName,
            Email,
            Password
        }

        public enum CheckBox
        {
            Philips_Announcements,
            Terms_Conditions
        }

        /// <summary>
        /// wait for CreatAccount screen
        /// </summary>
        /// <returns></returns>
        public static bool Wait()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, UserRegistration.Android.CreateAccount.CreateAccount_Header);
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
        /// header text
        /// </summary>
        /// <returns></returns>
        public static string GetHeader()
        {
            return _instance.GetTextById(UserRegistration.Android.CreateAccount.CreateAccount_Header);
        }
        public static bool CreateAccount_Screen()
        {
            bool bVisible = false;
            if (GetHeader() == "Create Account")
                bVisible = true;
            return bVisible;
        }

        /// <summary>
        /// button visible or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsVisible(Button btn)
        {
            bool isVisible = false;
            try
            {
                if (btn == Button.Create_Account)
                    isVisible = _instance.GetElement(SearchBy.Id, UserRegistration.Android.CreateAccount.Create_Account).Displayed;
                if (btn == Button.Activated_Account)
                    isVisible = _instance.GetElement(SearchBy.Id, UserRegistration.Android.CreateAccount.Activated_Account).Displayed;
                if (btn == Button.Resend)
                    isVisible = _instance.GetElement(SearchBy.Id, UserRegistration.Android.CreateAccount.Resend).Displayed;
            }
            catch (Exception e)
            {
                isVisible = false;
            }
            return isVisible;
        }

        /// <summary>
        /// button visible or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsEnable(Button btn)
        {
            bool isEnable = false;
            try
            {
                if (btn == Button.Create_Account)
                    isEnable = _instance.GetElement(SearchBy.Id, UserRegistration.Android.CreateAccount.Create_Account).Enabled;
                if (btn == Button.Activated_Account)
                    isEnable = _instance.GetElement(SearchBy.Id, UserRegistration.Android.CreateAccount.Activated_Account).Enabled;
                if (btn == Button.Resend)
                    isEnable = _instance.GetElement(SearchBy.Id, UserRegistration.Android.CreateAccount.Resend).Enabled;
            }
            catch (Exception e)
            {
                isEnable = false;
            }
            return isEnable;
        }

        /// <summary>
        /// button visible or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsEditable(TextBox txt)
        {
            bool isEnable = false;
            try
            {
                if (txt == TextBox.FirstName)
                    isEnable = _instance.GetElement(SearchBy.Id, UserRegistration.Android.CreateAccount.FirstName).Enabled;
                if (txt == TextBox.Email)
                    isEnable = _instance.GetElement(SearchBy.Id, UserRegistration.Android.CreateAccount.Email).Enabled;
                if (txt == TextBox.Password)
                    isEnable = _instance.GetElement(SearchBy.Id, UserRegistration.Android.CreateAccount.Password).Enabled;
            }
            catch (Exception e)
            {
                isEnable = false;
            }
            return isEnable;
        }

        /// <summary>
        /// Button click
        /// </summary>
        /// <param name="btn"></param>
        public static void Click(Button btn)
        {
            if (btn == Button.Back_Login)
                _instance.ClickById(UserRegistration.Android.CreateAccount.Back_Login);
            if (btn == Button.Create_Account)
                _instance.ClickById(UserRegistration.Android.CreateAccount.Create_Account);
            if (btn == Button.Activated_Account)
                _instance.ClickById(UserRegistration.Android.CreateAccount.Activated_Account);
            if (btn == Button.Resend)
                _instance.ClickById(UserRegistration.Android.CreateAccount.Resend);
            if (btn == Button.URContinue)
                _instance.ClickById(UserRegistration.Android.CreateAccount.URContinue);
            if (btn == Button.LoogedIn_Continue)
                _instance.ClickById(UserRegistration.Android.HomeScreen.Loggedin_Continue);
            if (btn == Button.GoogleSignIn)
                _instance.GetElement(SearchBy.ClassName, UserRegistration.Android.HomeScreen.LogintoGoogle).Click();
               

        }

        /// <summary>
        /// enter details
        /// </summary>
        /// <param name="firsName"></param>
        /// <param name="email"></param>
        /// <param name="password"></param>
        public static void Enter_Details(string firsName, string email, string password)
        {
            EnterText(CreateAccount.TextBox.FirstName, firsName);
            EnterText(CreateAccount.TextBox.Email, email);
            EnterText(CreateAccount.TextBox.Password, password);
        }
        public static void EnterText(TextBox txt, string str)
        {
            string desiredText = string.Empty;
            switch (txt)
            {
                case TextBox.FirstName:
                    desiredText = UserRegistration.Android.CreateAccount.FirstName;
                    break;
                case TextBox.Email:
                    desiredText = UserRegistration.Android.CreateAccount.Email;
                    break;
                case TextBox.Password:
                    desiredText = UserRegistration.Android.CreateAccount.Password;
                    break;
            }
            _instance.GetElement(SearchBy.Id, desiredText).Clear();
            _instance.GetElement(SearchBy.Id, desiredText).SetText(str);
        }

        /// <summary>
        /// Getting error msg
        /// </summary>
        /// <returns></returns>
        public static string FieldsEmptyErrorMsg()
        {
            _instance.GetElement(SearchBy.Id, UserRegistration.Android.CreateAccount.FirstName).Click();
            _instance.GetElement(SearchBy.Id, UserRegistration.Android.CreateAccount.Email).Click();
            string str = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.EmailError).Text;
            return str;
        }

        public static List<string> EmailInvalidErrorMsg()
        {
            List<IMobilePageControl> control = _instance.GetElements(SearchBy.Id, UserRegistration.Android.HomeScreen.EmailInvalidErrorMsg);
            List<string> str = new List<string>();
            foreach (IMobilePageControl cont in control)
            {
                string str1 = cont.Text;
                str.Add(str1);
            }
            return str;
        }



        /// <summary>
        /// Password Error msg
        /// </summary>
        /// <returns></returns>
        public static string PasswordErrMsg()
        {
            string str = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.PasswordErrMsg).Text;
            return str;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public static string Terms_ConditionsErrorMsg()
        {
            string str = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.Terms_ConditionsErrorMsg).Text;
            return str;
        }
        /// <summary>
        /// Enabling PhilipAnnouncements Check box
        /// </summary>
        public static void Click(CheckBox chkBox)
        {
            List<IMobilePageControl> control = _instance.GetElements(SearchBy.Id, UserRegistration.Android.CreateAccount.Checkbox);
            if (chkBox == CheckBox.Terms_Conditions)
            {
                control[0].Click();
            }
            else
            {
                control[1].Click();
            }
        }

        /// <summary>
        /// get verification title
        /// </summary>
        /// <returns></returns>
        public static string GetVerificationTitle()
        {
            string str = _instance.GetElement(SearchBy.Id, UserRegistration.Android.CreateAccount.Verification_Title).Text;
            return str;
        }

        /// <summary>
        /// getting Alreadu email in use error msg
        /// </summary>
        /// <returns></returns>
        public static string Email_AlreadyinUse_ErrMsg()
        {
            string str = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.EmailInvalidErrorMsg).Text;
            return str;
        }
    }
}
