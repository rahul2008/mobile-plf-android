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
    public class Registration
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
        /// Collection of all Buttons
        /// </summary>
        public enum Button
        {
            Back_Home,
            Create_PhilipsAccount,
            Philips_Account,
            Google_plus,
            Facebook,
            Continue,
            Logout,
            Merge,
            TermsConditions,
            LogintoGoogle,
            Philips_Announcements,
            FacebookSign,
            SwitchAccount,
            SocialMergeGoogle
        }

        public enum CheckBox
        {
            Terms_Conditions,
            Philips_Announcements
        }
        /// <summary>
        /// wait for login screen
        /// </summary>
        /// <returns></returns>
        public static bool Wait()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, UserRegistration.Android.Registration.Login_Header);
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
        /// Entering email address
        /// </summary>
        /// <param name="mailID"></param>
        public static void Enter_Email(string mailID)
        {
            _instance.GetElement(SearchBy.Id, UserRegistration.Android.LoginScreen.Email).SetText(mailID);
        }

        /// <summary>
        /// Clicking check boxes
        /// </summary>
        /// <param name="chkBox"></param>
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

        public static int Howmanycheckboxvailable()
        {
            List<IMobilePageControl> control = _instance.GetElements(SearchBy.Id, UserRegistration.Android.CreateAccount.Checkbox);
            if (control.Count == 2)
            {
                return 2;
            }
            if (control.Count == 1)
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
        /// <summary>
        /// header text
        /// </summary>
        /// <returns></returns>
        public static string GetHeader()
        {
            return _instance.GetTextById(UserRegistration.Android.Registration.Login_Header);
        }
        public static bool Login_Screen()
        {
            bool bVisible = false;
            if (GetHeader() == "Log In")
                bVisible = true;
            return bVisible;
        }

        public static string Setting_GetHeader()
        {
            return _instance.GetTextById(UserRegistration.Android.HomeScreen.Header);
        }

        public static bool Setting_Login_Screen()
        {
            bool bVisible = false;
            if (Setting_GetHeader() == UserRegistration.Android.HomeScreen.LogInHeader)
                bVisible = true;
            return bVisible;
        }



        /// <summary>
        /// Device back
        /// </summary>
        public static void DeviceBack()
        {
            MobileDriver.FireKeyEvent(4);

        }

        /// <summary>
        /// Buttons displayed or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsVisible(Button btn)
        {

            bool isVisible = false;
            try
            {
                if (btn == Button.Create_PhilipsAccount)
                    isVisible = _instance.GetElement(SearchBy.Id, UserRegistration.Android.Registration.CreatePhilipsAccount).Displayed;
                if (btn == Button.Philips_Account)
                    isVisible = _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.PhilipsAccountName).Displayed;
                if (btn == Button.Facebook)
                    isVisible = _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.FacebookName).Displayed;
                if (btn == Button.Google_plus)
                    isVisible = _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.GoogleName).Displayed;
                if (btn == Button.Logout)
                    isVisible = _instance.GetElement(SearchBy.Id, UserRegistration.Android.Registration.Logout).Displayed;
                if (btn == Button.Merge)
                    isVisible = _instance.GetElement(SearchBy.Id, UserRegistration.Android.Registration.Merge).Displayed;
                if (btn == Button.TermsConditions)
                    isVisible = _instance.GetElement(SearchBy.Id, UserRegistration.Android.CreateAccount.Checkbox).Displayed;
            }
            catch (Exception e)
            {
                isVisible = false;
            }
            return isVisible;
        }


        /// <summary>
        /// Buttons Enabled or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsEnable(Button btn)
        {
            bool isEnable = false;
            try
            {
                if (btn == Button.Create_PhilipsAccount)
                    isEnable = _instance.GetElement(SearchBy.Id, UserRegistration.Android.Registration.CreatePhilipsAccount).Enabled;
                if (btn == Button.Philips_Account)
                    isEnable = _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.PhilipsAccountName).Enabled;
                if (btn == Button.Facebook)
                    isEnable = _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.FacebookName).Enabled;
                if (btn == Button.Google_plus)
                    isEnable = _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.GoogleName).Enabled;
                if (btn == Button.TermsConditions)
                    isEnable = _instance.GetElement(SearchBy.Id, UserRegistration.Android.CreateAccount.Checkbox).Enabled;
            }
            catch (Exception e)
            {
                isEnable = false;
            }
            return isEnable;
        }

        /// <summary>
        /// enter_password
        /// </summary>
        /// <param name="password"></param>
        public static void Enter_Password(string password)
        {
            _instance.GetElement(SearchBy.Id, UserRegistration.Android.LoginScreen.Password).SetText(password);
        }

        /// <summary>
        /// Buttons Clickable or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static void Click(Button btn)
        {
            if (btn == Button.Create_PhilipsAccount)
                _instance.GetElement(SearchBy.Id, UserRegistration.Android.Registration.CreatePhilipsAccount).Click();
            if (btn == Button.Philips_Account)
                _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.PhilipsAccountName).Click();
            if (btn == Button.Facebook)
                _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.FacebookName).Click();
            if (btn == Button.Google_plus)
                _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.GoogleName).Click();
            if (btn == Button.Back_Home)
                _instance.ClickById(UserRegistration.Android.Registration.LoginBack);
            if (btn == Button.Logout)
                _instance.ClickById(UserRegistration.Android.Registration.Logout);
            if (btn == Button.SwitchAccount)
                _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.SwitchAccount).Click();
            if (btn == Button.FacebookSign)
                _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.FacebookSign).Click();
            if(btn == Button.SocialMergeGoogle)
                _instance.GetElementByXpath(UserRegistration.Android.HomeScreen.SocialMergeGoogle).Click();
            if (btn == Button.LogintoGoogle)
            {
                List<IMobilePageControl> control = _instance.GetElements(SearchBy.ClassName, UserRegistration.Android.HomeScreen.LogintoGoogle);
                if (control != null)
                {
                    control[0].Click();
                }
            }

            if (btn == Button.Continue)
            {
                IMobilePageControl control = _instance.GetElement(SearchBy.Id, UserRegistration.Android.CreateAccount.URContinue);
                if (control != null)
                {
                    control.Click();
                }

                //IMobilePageControl controll = _instance.GetElement(SearchBy.Id, "com.philips.cdp.registration:id/fl_reg_sign_in");
                //if (controll != null)
                //{
                //    _instance.GetElement(controll, SearchBy.Id, "com.philips.cdp.registration:id/btn_reg_continue").Click();
                //    //FindElement(OpenQA.Selenium.By.Id("com.philips.cdp.registration:id/btn_reg_continue")).Click();
                //    //controll.Click();

                //}
                //IMobilePageControl controlll = _instance.GetElement(SearchBy.Id, "com.philips.cdp.registration:id/btn_continue_reg");
                //if (controlll != null)
                //{
                //    controlll.Click();

                //}
            }
            if (btn == Button.Merge)
                _instance.ClickById(UserRegistration.Android.Registration.Merge);
        }

    }

    public class Forgot_Password
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
            Continue,
            Back_Home
        }

        public enum TextBox
        {
            Enter_Email
        }

        /// <summary>
        /// wait for login_with_PhilipsAccount screen
        /// </summary>
        /// <returns></returns>
        public static bool Wait()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, UserRegistration.Android.Forgot_Password.Forgot_Title);
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

        public static string EmailErrorMsg()
        {
            string str = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.EmailError).Text;
            return str;
        }

        /// <summary>
        /// Textbox editable or not
        /// </summary>
        /// <param name="txt"></param>
        /// <returns></returns>
        public static bool IsEditable(TextBox txt)
        {
            bool editable = false;
            try
            {
                if (txt == TextBox.Enter_Email)
                    editable = _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.EmailEnterText).Enabled;
            }
            catch (Exception e)
            {
                editable = false;
            }
            return editable;
        }

        /// <summary>
        /// Button is enable or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsEnable(Button btn)
        {
            bool isEnable = false;
            try
            {
                if (btn == Button.Continue)
                    isEnable = _instance.GetElement(SearchBy.Id, UserRegistration.Android.Forgot_Password.Continue).Enabled;
            }
            catch (Exception e)
            {
                isEnable = false;
            }
            return isEnable;
        }

        /// <summary>
        /// Buttons clickable or not
        /// </summary>
        /// <param name="btn"></param>
        public static void Click(Button btn)
        {
            if (btn == Button.Continue)
                _instance.ClickById(UserRegistration.Android.Forgot_Password.Continue);
            else if (btn == Button.Back_Home)
                _instance.ClickById(UserRegistration.Android.Registration.LoginBack);
        }


        /// <summary>
        /// Entering email
        /// </summary>
        /// <param name="email"></param>
        public static void Entry_Email(string email)
        {
            IMobilePageControl cnt = _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.EmailEnterText);
            if (cnt != null)
            {
                cnt.Clear();
                cnt.SetText(email);
            }
        }

    }

    public class LoginFacebook
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }

        public enum TextBox
        {
            Fb_Email,
            Fb_Password,
        }

        public enum Button
        {
            LogIn
        }

        public static void Continue_Facebook()
        {
            List<IMobilePageControl> cnt2 = _instance.GetElements(SearchBy.ClassName, UserRegistration.Android.HomeScreen.LogintoGoogle);
            for (var m = 0; m < cnt2.Count; m++)
            {
                string a1 = cnt2[m].GetAttribute("name");
                if(a1 == "Continue ")
                {
                    cnt2[m].Click();
                    break;
                }
            }
        }
        public static bool Wait()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.ClassName, UserRegistration.Android.HomeScreen.LogintoGoogle);
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
        /// Enter details
        /// </summary>
        /// <param name="fbEmail"></param>
        /// <param name="fbPassword"></param>
        public static void Enter_FBDetails(string fbEmail, string fbPassword)
        {
            Enter_FBEmail(fbEmail);
            Enter_FbPassword(fbPassword);
        }
        public static void Enter_FBEmail(string str)
        {
            List<IMobilePageControl> control = _instance.GetElements(SearchBy.ClassName, UserRegistration.Android.HomeScreen.EditTextClass);
            foreach (IMobilePageControl cnt in control)
            {
                cnt.Click();
                cnt.SetText(str);
                break;
            }
        }
        public static void Enter_FbPassword(string str)
        {
            List<IMobilePageControl> control = _instance.GetElements(SearchBy.ClassName, UserRegistration.Android.HomeScreen.EditTextClass);
            foreach (IMobilePageControl cnt in control.Skip(1))
            {
                cnt.Click();
                cnt.SetText(str);
                break;
            }
        }

        public static void Click(Button btn)
        {
            if (btn == Button.LogIn)
                _instance.GetElement(SearchBy.ClassName, UserRegistration.Android.HomeScreen.LogintoGoogle).Click();
        }

        public static string GetSocialMergeTxt()
        {
            string str = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.User_LoginConfirm).Text;
            return str;

        }
        public static bool GetSocialMerge_Screen()
        {
            bool bVisible = false;
            if (GetSocialMergeTxt() == UserRegistration.Android.HomeScreen.WelcomeTextMerge)
                bVisible = true;
            return bVisible;
        }


    }

    public class LoginGooglePlus
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }

        public enum TextBox
        {
            Google_Email,
            Google_Password,
        }

        public enum Button
        {
            SignIn,
            Next,
            Allow,
            SignOut
        }

        /// <summary>
        /// wait for Gmail screen
        /// </summary>
        /// <returns></returns>
        public static bool Wait()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.EnterEmail);
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
        /// Enter details
        /// </summary>
        /// <param name="fbEmail"></param>
        /// <param name="fbPassword"></param>
        public static void Enter_Details(string googleEmail, string googlePassword)
        {
            Enter_Email(googleEmail);
            LoginGooglePlus.Click(LoginGooglePlus.Button.Next);
            Enter_Password(googlePassword);
        }
        public static void Enter_Email(string str)
        {
            List<IMobilePageControl> control = _instance.GetElements(SearchBy.ClassName, UserRegistration.Android.HomeScreen.EditTextClass);
            foreach (IMobilePageControl cnt in control)
            {
                cnt.Click();
                cnt.Clear();
                cnt.SetText(str);
                break;
            }
        }
        /// <summary>
        /// Buttons Visible or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsEnable(String email_id)
        {
            bool isEnable = false;
            try
            {
                isEnable = _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.NextEmail).Displayed;
            }
            catch (Exception e)
            {
                isEnable = false;
            }
            return isEnable;
        }

        public static void Enter_Password(string str)
        {
            List<IMobilePageControl> control = _instance.GetElements(SearchBy.ClassName, UserRegistration.Android.HomeScreen.EditTextClass);
            foreach (IMobilePageControl cnt in control)
            {
                cnt.Click();
                cnt.SetText(str);
                break;
            }
        }

        public static void Click(Button btn)
        {
            if (btn == Button.Next)
                _instance.ClickByName(UserRegistration.Android.HomeScreen.NextEmail);
            else if (btn == Button.SignIn)
                _instance.ClickByName(UserRegistration.Android.HomeScreen.SigninName);
            else if (btn == Button.Allow)
                _instance.ClickById(UserRegistration.Android.HomeScreen.AllowName);
            else if (btn == Button.SignOut)
                _instance.ClickByName(UserRegistration.Android.HomeScreen.Signout);
            
        }

        public static void ClickToLogout()
        {
            List<IMobilePageControl> control = _instance.GetElements(SearchBy.ClassName, UserRegistration.Android.HomeScreen.LogintoGoogle);
            foreach (IMobilePageControl cnt in control)
            {
                cnt.Click();
                break;
            }
        }
        public static string GetMergeTxt()
        {
            string str = _instance.GetElement(SearchBy.Id, UserRegistration.Android.HomeScreen.GetMergeTxt).Text;
            return str;
           
        }
        public static bool GetMerge_Screen()
        {
            bool bVisible = false;
            if (GetMergeTxt() == UserRegistration.Android.HomeScreen.GetMergeHeader)
                bVisible = true;
            return bVisible;
        }



    }

    public class Philips_Registartion
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }

        public enum TextView
        {
            AOL,
            GooglePlus,
            Yahoo,
            OpenID,
            LinkedIn,
            Flicker
        }

        public enum Button
        {
            Back_Home
        }

        /// <summary>
        /// wait for Gmail screen
        /// </summary>
        /// <returns></returns>
        public static bool Wait()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, UserRegistration.Android.Philips_Registreation.Philps_Title);
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

        public static void Click(TextView txt)
        {
            if (txt == TextView.AOL)
                _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.AOL).Click();
            if (txt == TextView.GooglePlus)
                _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.GoogleName).Click();
            if (txt == TextView.Yahoo)
                _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.Yahoo).Click();
            if (txt == TextView.OpenID)
                _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.OpenID).Click();
            if (txt == TextView.LinkedIn)
                _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.LinkedIn).Click();
            if (txt == TextView.Flicker)
                _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.Flickr).Click();
        }

        public static void Click(Button btn)
        {
            if (btn == Button.Back_Home)
                _instance.ClickById(UserRegistration.Android.Philips_Registreation.Back_Login);
        }

        /// <summary>
        /// Enter AOL details
        /// </summary>
        /// <param name="email"></param>
        /// <param name="pwd"></param>
        public static void Enter_AOLDetails(string email, string pwd)
        {
            Enter_AOLEmail(email);
            Enter_AOLPassword(pwd);
            _instance.GetElement(SearchBy.ClassName, UserRegistration.Android.HomeScreen.LogintoGoogle).Click();
        }
        public static void Enter_AOLEmail(string str)
        {
            List<IMobilePageControl> control = _instance.GetElements(SearchBy.ClassName, UserRegistration.Android.HomeScreen.EditTextClass);
            foreach (IMobilePageControl cnt in control)
            {
                cnt.Click();
                cnt.SetText(str);
                break;
            }
        }
        public static void Enter_AOLPassword(string str)
        {
            List<IMobilePageControl> control = _instance.GetElements(SearchBy.ClassName, UserRegistration.Android.HomeScreen.EditTextClass);
            foreach (IMobilePageControl cnt in control.Skip(1))
            {
                cnt.Click();
                cnt.SetText(str);
                break;
            }
        }

        /// <summary>
        /// Enter Yahoo details
        /// </summary>
        /// <param name="email"></param>
        /// <param name="pwd"></param>
        public static void Enter_YahooDetails(string email, string pwd)
        {
            Enter_YahooEmail(email);
            _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.NextEmail).Click();
            Enter_YahooPassword(pwd);
            _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.SignInText).Click();
            Thread.Sleep(5000);
            _instance.GetElement(SearchBy.Name, "Agree").Click();

        }
        public static void Enter_YahooEmail(string str)
        {
            List<IMobilePageControl> control = _instance.GetElements(SearchBy.ClassName, UserRegistration.Android.HomeScreen.EditTextClass);
            foreach (IMobilePageControl cnt in control)
            {
                cnt.Click();
                cnt.Clear();
                cnt.SetText(str);
                break;
            }
        }
        public static void Enter_YahooPassword(string str)
        {
            List<IMobilePageControl> control = _instance.GetElements(SearchBy.ClassName, UserRegistration.Android.HomeScreen.EditTextClass);
            foreach (IMobilePageControl cnt in control)
            {
                cnt.Click();
                cnt.SetText(str);
                break;
            }
        }

        /// <summary>
        /// Enter OpenID details
        /// </summary>
        /// <param name="email"></param>
        /// <param name="pwd"></param>
        public static void Enter_OpenIDDetails(string url)
        {
            Enter_OpenIdURL(url);
            _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.SignInText).Click();

        }
        public static void Enter_OpenIdURL(string str)
        {
            List<IMobilePageControl> control = _instance.GetElements(SearchBy.ClassName, UserRegistration.Android.HomeScreen.EditTextClass);
            foreach (IMobilePageControl cnt in control)
            {
                cnt.Clear();
                cnt.Click();
                cnt.Clear();
                cnt.SetText(str);
                break;
            }
        }

        /// <summary>
        /// Enter LinkedInn details
        /// </summary>
        /// <param name="email"></param>
        /// <param name="pwd"></param>
        public static void Enter_LinkedInDetails(string email, string pwd)
        {
            Enter_LinkedInEmail(email);
            Enter_LinkedInPassword(pwd);
            _instance.GetElement(SearchBy.Name, UserRegistration.Android.HomeScreen.SignInAndAllow).Click();
        }
        public static void Enter_LinkedInEmail(string str)
        {
            List<IMobilePageControl> control = _instance.GetElements(SearchBy.ClassName, UserRegistration.Android.HomeScreen.EditTextClass);
            foreach (IMobilePageControl cnt in control)
            {
                cnt.Click();
                cnt.SetText(str);
                break;
            }
        }
        public static void Enter_LinkedInPassword(string str)
        {
            List<IMobilePageControl> control = _instance.GetElements(SearchBy.ClassName, UserRegistration.Android.HomeScreen.EditTextClass);
            foreach (IMobilePageControl cnt in control.Skip(1))
            {
                cnt.Click();
                cnt.SetText(str);
                break;
            }
        }

    }

}
