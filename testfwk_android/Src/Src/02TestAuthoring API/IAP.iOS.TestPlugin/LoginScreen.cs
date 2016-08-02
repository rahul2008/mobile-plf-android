using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS
{
    /// <summary>
    /// This class provides all the functionalities and constants for features related to Login/Registration page.
    /// </summary>
    public class LoginScreen
    {
        static MobilePageInstance Instance;
        public static string loginId;
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }

        /// <summary>
        /// Collection of all the buttons in Login/Registration screen
        /// </summary>
        public enum Button
        {
            CreatePhilipsAccount,
            LoginPhilipsAccount,
            LoginFacebook,
            LoginGoogleplus,
            Signin,
            ForgotPassword,
            ShowPassword,
            AcceptTermsToggle,
            Continue,
            Logout,
            CreateAccBack,
            LoginMainBack,
            SignInBack,
            ForgotPswdContinueButton,
            ForgotPswdDoneButton,
            ForgotPswdResetAlert
        }

        /// <summary>
        /// Collection of all editable text fields in Login/Registration screen
        /// </summary>
        public enum EditableTextFields
        {
            UsernameField,
            PasswordField,
            ForgotPswdEmailField
        }

        /// <summary>
        /// Collection of all read only text fields in Login/Registration screen
        /// </summary>
        public enum NonEditableTextFields
        {
            LoginTitle
        }

        /// <summary>
        /// This method can be used to click on the desired button in the Login/Registration screen
        /// </summary>
        /// <param name="btn">btn represents the name of the button.</param>
        public static void Click(Button btn)
        {
            Instance = _instance;
            string desiredElement = null;

            switch(btn)
            {
                case Button.CreatePhilipsAccount:
                    desiredElement = Repository.iOS.LoginScreen.CreatePhilipsAcc;
                    break;
                case Button.LoginPhilipsAccount:
                    desiredElement = Repository.iOS.LoginScreen.LoginPhilips;
                    break;
                case Button.LoginFacebook:
                    desiredElement = Repository.iOS.LoginScreen.LoginFacebook;
                    break;
                case Button.LoginGoogleplus:
                    desiredElement = Repository.iOS.LoginScreen.LoginGoogleplus;
                    break;
                case Button.SignInBack:
                    desiredElement = Repository.iOS.LoginScreen.LoginBack;
                    break;
                case Button.CreateAccBack:
                    desiredElement = Repository.iOS.LoginScreen.CreateAccBack;
                    break;
                case Button.LoginMainBack:
                    desiredElement = Repository.iOS.LoginScreen.LoginMainBack;
                    break;
                case Button.Signin:
                    desiredElement = Repository.iOS.LoginScreen.Signin;
                    break;
                case Button.ForgotPassword:
                    desiredElement = Repository.iOS.LoginScreen.ForgotPassword;
                    break;
                case Button.ShowPassword:
                    desiredElement = Repository.iOS.LoginScreen.ShowPassword;
                    break;
                case Button.AcceptTermsToggle:
                    desiredElement = Repository.iOS.LoginScreen.AcceptTermsToggle;
                    break;
                case Button.Continue:
                    desiredElement = Repository.iOS.LoginScreen.Continue;
                    break;
                case Button.Logout:
                    desiredElement = Repository.iOS.LoginScreen.SignOut;
                    break;
                case Button.ForgotPswdContinueButton:
                    desiredElement = Repository.iOS.LoginScreen.ForgotPswdContinueButton;
                    break;
                case Button.ForgotPswdDoneButton:
                    desiredElement = Repository.iOS.LoginScreen.ForgotPswdDoneButton;
                    break;
                case Button.ForgotPswdResetAlert:
                    desiredElement = Repository.iOS.LoginScreen.ForgotPswdResetAlert;
                    break;
            }
            Instance.GetElement(SearchBy.Xpath,desiredElement).Click();
        }

        /// <summary>
        /// This method can be used to check if the desired button is enabled in Login/Registration screen.
        /// </summary>
        /// <param name="btn">btn represents the name of the button.</param>
        /// <returns>It returns true if the button is enabled and false otherwise.</returns>
        public static bool IsEnabled(Button btn)
        {
            bool isEnabled = false;
            Instance = _instance;
            string desiredElement = null;

            switch (btn)
            {
                case Button.CreatePhilipsAccount:
                    desiredElement = Repository.iOS.LoginScreen.CreatePhilipsAcc;
                    break;
                case Button.LoginPhilipsAccount:
                    desiredElement = Repository.iOS.LoginScreen.LoginPhilips;
                    break;
                case Button.LoginFacebook:
                    desiredElement = Repository.iOS.LoginScreen.LoginFacebook;
                    break;
                case Button.LoginGoogleplus:
                    desiredElement = Repository.iOS.LoginScreen.LoginGoogleplus;
                    break;
                case Button.SignInBack:
                    desiredElement = Repository.iOS.LoginScreen.LoginBack;
                    break;
                case Button.CreateAccBack:
                    desiredElement = Repository.iOS.LoginScreen.CreateAccBack;
                    break;
                case Button.LoginMainBack:
                    desiredElement = Repository.iOS.LoginScreen.LoginMainBack;
                    break;
                case Button.Signin:
                    desiredElement = Repository.iOS.LoginScreen.Signin;
                    break;
                case Button.ForgotPassword:
                    desiredElement = Repository.iOS.LoginScreen.ForgotPassword;
                    break;
                case Button.ShowPassword:
                    desiredElement = Repository.iOS.LoginScreen.ShowPassword;
                    break;
                case Button.AcceptTermsToggle:
                    desiredElement = Repository.iOS.LoginScreen.AcceptTermsToggle;
                    break;
                case Button.Continue:
                    desiredElement = Repository.iOS.LoginScreen.Continue;
                    break;
                case Button.Logout:
                    desiredElement = Repository.iOS.LoginScreen.SignOut;
                    break;
                case Button.ForgotPswdContinueButton:
                    desiredElement = Repository.iOS.LoginScreen.ForgotPswdContinueButton;
                    break;
                case Button.ForgotPswdDoneButton:
                    desiredElement = Repository.iOS.LoginScreen.ForgotPswdDoneButton;
                    break;
                case Button.ForgotPswdResetAlert:
                    desiredElement = Repository.iOS.LoginScreen.ForgotPswdResetAlert;
                    break;
            }
            isEnabled = Instance.GetElement(SearchBy.Xpath, desiredElement).Enabled;
            return isEnabled;
        }

        /// <summary>
        /// This method waits for the Login/Registration screen page to appear.
        /// </summary>
        /// <returns>It returns true if the screen is loaded and false otherwise.</returns>
        public static bool WaitforLoginScreen()
        {
            IMobilePageControl element = null;
            int loopcount = -0;
            while (element == null)
            {
                element = _instance.GetElement(SearchBy.Xpath, Repository.iOS.LoginScreen.LoginSubTitle);
                loopcount++;
                if (element != null)
                    break;
                if (loopcount > 10)
                    break;
            }
            if (element != null)
                return true;
            else
                return false;
        }

        /// <summary>
        /// This method checks if the title of Login/Registration screen is 'Log In'.
        /// </summary>
        /// <returns>It returns a boolean true if the title matches and false otherwise.</returns>
        public static bool IsVisible()
        {
            bool bVisible = false;
            if (GetScreenTitle() == "Log In")
                bVisible = true;

            return bVisible;
        }

        /// <summary>
        /// This method returns the screen title of Login/Registration page.
        /// </summary>
        /// <returns>It returns a string value representing the screen title.</returns>
        public static string GetScreenTitle()
        {
            return _instance.GetElement(SearchBy.Xpath, Repository.iOS.LoginScreen.LoginTitle).Text;
        }

        /// <summary>
        /// This method can be used to enter the username and password values in the Login/Registration screen.
        /// </summary>
        /// <param name="tb">tb must contain the textbox name, for instance : EditableTextFields.UsernameField</param>
        /// <param name="name">name must contain the value to be entered in the textbox.</param>
        private static void EnterText(EditableTextFields tb, string txtVal)
        {
            string desiredtxtBox = string.Empty;
            if (tb == EditableTextFields.UsernameField)
                desiredtxtBox = Repository.iOS.LoginScreen.Username;
            else if (tb == EditableTextFields.PasswordField)
                desiredtxtBox = Repository.iOS.LoginScreen.Password;
            else if (tb == EditableTextFields.ForgotPswdEmailField)
                desiredtxtBox = Repository.iOS.LoginScreen.ForgotPswdEmailField;
            _instance.GetElement(SearchBy.Xpath, desiredtxtBox).SetText(txtVal);
        }

        /// <summary>
        /// This method can be used to sign in using Login/Registration screen.
        /// </summary>
        /// <param name="usrname">usrname must contain the value to be entered in Username field.</param>
        /// <param name="pwd">pwd must contain the value to be entered in Password field.</param>
        public static void SignIn(string usrname, string pwd)
        {
            try
            {
                loginId = usrname;
                EnterText(EditableTextFields.UsernameField, usrname);
                EnterText(EditableTextFields.PasswordField, pwd);
                Click(Button.Signin);
                Thread.Sleep(5000);
                Click(Button.Continue);
                Thread.Sleep(1000);
            }
            catch(Exception e)
            {
                Logger.Info("Exception occur "+e);
            }
        }
   
        /// <summary>
        /// This method can be used to opt for Forgot Password in Login/Registration screen.
        /// </summary>
        /// <param name="usrname">usrname must contain the value to be entered in Username/email field.</param>
        public static void PerformForgotPassword(string usrname)
        {
            Click(Button.ForgotPassword);
            EnterText(EditableTextFields.ForgotPswdEmailField, usrname);
            Click(Button.ForgotPswdContinueButton);
            Click(Button.ForgotPswdDoneButton);
            Click(Button.ForgotPswdResetAlert);
        }
    }
}
