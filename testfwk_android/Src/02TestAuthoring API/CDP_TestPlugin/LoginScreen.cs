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

namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin
{
  /// <summary>
  /// This class provides all the functionalities and constants for features related to Login page.
  /// </summary>
    public class LoginScreen : IAP_Common
    {
        public static string loginId;
        
        /// <summary>
        /// Collection of all Buttons
        /// </summary>
        public enum Button
        {
            CreatePhilipsAccount,
            PhilipsAccount,
            Google,
            Facebook,
            LoginBackButton,
            LoginContinue,
            LogOut,
            ForgotPassWord,
            LoginButton,
            LoginContinue_1
        }

        /// <summary>
        /// Collection of all ExitTexts
        /// </summary>
        public enum EditText
        {
            UserName,
            PassWord
        }

        /// <summary>
        /// Collection of all CheckBoxes
        /// </summary>
        public enum CheckBox
        {
            TermsCheckBox
        }

        /// <summary>
        /// Collection of all TextViews
        /// </summary>
        public enum TextView
        {
            LoginTitle,
            PhilipsAccountTitle
        }

        /// <summary>
        /// Clicks on the desired Button
        /// </summary>
        /// <param name="btn">btn represents the name of the Button</param>
        public static void Click(Button btn)
        {
            Instance = _instance;

            if (btn == Button.CreatePhilipsAccount)
                Instance.ClickById(ObjectRepository.CreatePhilipsAccount);
            else if (btn == Button.Facebook)
                Instance.ClickById(ObjectRepository.Facebook);
            else if (btn == Button.Google)
                Instance.ClickById(ObjectRepository.GooglePlus);
            else if (btn == Button.LoginBackButton)
                Instance.ClickById(ObjectRepository.LoginBackButton);
            else if (btn == Button.PhilipsAccount)
                Instance.ClickById(ObjectRepository.PhilipsAccount);
            else if (btn == Button.LogOut)
                Instance.ClickById(ObjectRepository.LogOut);
            else if (btn == Button.LoginContinue)
                Instance.ClickById(ObjectRepository.LoginContinue);
            else if (btn == Button.ForgotPassWord)
                Instance.ClickById(ObjectRepository.ForgotPassWord);
            else if (btn == Button.LoginButton)
               Instance.ClickById(ObjectRepository.LoginButton);
            else if (btn == Button.LoginContinue_1)
                Instance.ClickById(ObjectRepository.LoginContinue_1);

        }

      /// <summary>
      /// Clicks on desired CheckBox
      /// </summary>
      /// <param name="chkBox">chkBox represents the name of the CheckBox</param>
        public static void Click(CheckBox chkBox){
            Instance = _instance;
            if (Instance.GetElement(SearchBy.Id,ObjectRepository.TermsCheckBox) != null)
            {
                if (chkBox == CheckBox.TermsCheckBox)
                {
                    Instance.ClickById(ObjectRepository.TermsCheckBox);
                }
            }
      }

      /// <summary>
      /// Returns true if the desired Button is Enabled
      /// </summary>
      /// <param name="btn">btn represents the name of the Button</param>
      /// <returns>a boolean value</returns>
        public static bool IsEnabled(Button btn)
        {
            bool isEnabled = false;
            Instance = _instance;
            if (btn == Button.CreatePhilipsAccount)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.CreatePhilipsAccount).Enabled;
            else if (btn == Button.Facebook)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.Facebook).Enabled;
            else if (btn == Button.Google)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.GooglePlus).Enabled;
            else if (btn == Button.LoginBackButton)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.LoginBackButton).Enabled;
            else if (btn == Button.PhilipsAccount)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.PhilipsAccount).Enabled;
            else if (btn == Button.LogOut)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.LogOut).Enabled;
            else if (btn == Button.LoginContinue)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.LoginContinue).Enabled;
            else if (btn == Button.ForgotPassWord)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.ForgotPassWord).Enabled;
            else if (btn == Button.LoginButton)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.LoginButton).Enabled;
            else if (btn == Button.LoginContinue_1)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.LoginContinue_1).Enabled;


            return isEnabled;
        }

      /// <summary>
      /// Returns true if the desired CheckBox is Enabled
      /// </summary>
      /// <param name="btn">btn represents the name of the CheckBox</param>
      /// <returns>a boolean value</returns>
        public static bool IsEnabled(CheckBox btn)
        {
            bool isEnabled = false;
            Instance = _instance;
            if (btn == CheckBox.TermsCheckBox)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.TermsCheckBox).Enabled;
            return isEnabled;
        }

        /// <summary>
        /// Waits for the Login Screen page to appear
        /// </summary>
        /// <returns>a boolean value</returns>
        public static bool WaitforLoginScreen()
        {
            IMobilePageControl element = null;
            int loopcount = -0;
            while (element == null)
            {
                element = _instance.GetElement(SearchBy.Id, ObjectRepository.LoginTitle);
                loopcount++;
                if (element != null)
                    break;
                if (loopcount > 5)
                    break;
            }
            if (element != null)
                return true;
            else
                return false;
        }

        /// <summary>
        /// Checks if the Screen Title is "Log In"
        /// </summary>
        /// <returns>a boolean value</returns>
        public static bool IsVisible()
        {
            bool bVisible = false;
            if (GetScreenTitle() == "Log In")
                bVisible = true;
            
            return bVisible;

        }
        //public static bool IsExist(Button btn)
        //{
        //    bool isEnabled = false;
        //    Instance = _instance;
        //    if (btn == Button.ForgotPassWord)
        //        isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.ForgotPassWord).Enabled;
        //    if (btn == Button.LogOut)
        //        isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.LogOut).Enabled;
        //    return isEnabled;
        //}
        /// <summary>
        /// Returns a string containing the Screen Title
        /// </summary>
        /// <returns>a string value</returns>
        public static string GetScreenTitle()
        {
            return _instance.GetTextById(ObjectRepository.LoginTitle);
        }

        /// <summary>
        /// Enters the Username and Password
        /// </summary>
        /// <param name="tb">tb must contain the textbox name</param>
        /// <param name="name">name must contain the string to be entered</param>
        private static void EnterText(EditText tb, string name)
        {
            string desiredtxtBox = string.Empty;
            if (tb == EditText.UserName)
                desiredtxtBox = ObjectRepository.UserName;
            if (tb == EditText.PassWord)
                desiredtxtBox = ObjectRepository.PassWord;
            _instance.GetElement(SearchBy.Id, desiredtxtBox).SetText(name);
        }

        /// <summary>
        /// Enters username and password and clicks on Submit Button
        /// </summary>
        /// <param name="usrname">usrname must contain the Username</param>
        /// <param name="pwd">pwd must contain the password</param>
        public static void Login(string usrname, string pwd)
        {
            try
            {
                loginId = usrname;
                EnterText(EditText.UserName, usrname);
                EnterText(EditText.PassWord, pwd);
                Thread.Sleep(1000);
                UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input keyevent" + " 111");
                Thread.Sleep(2000);
                Click(LoginScreen.Button.LoginButton);
                Thread.Sleep(6000);

            }
            catch (Exception e)
            {
                Logger.Info("Exception occur " + e);
               

            }
        }

      /// <summary>
      /// Executes all the Login process
      /// Clicks on Philips Account Button
      /// Enters username and password, stored in the AppConfig file
      /// Checks the Terms and Condition Checkbox
      /// Clicks continue till HomeScreen Psge appears
      /// </summary>
        public static void LoginUser(string username,string password)
        {
            try
            {
                if (LoginScreen.GetScreenTitle().Equals("Log In"))
                {
                    LoginScreen.Click(LoginScreen.Button.PhilipsAccount);
                    LoginScreen.WaitforLoginScreen();

                    if (LoginScreen.IsEnabled(LoginScreen.Button.ForgotPassWord))
                    {
                        LoginScreen.Login(username, password);
                       
                            LoginScreen.WaitforLoginScreen();
                            try
                            {
                                if (LoginScreen.IsEnabled(LoginScreen.CheckBox.TermsCheckBox))
                                {
                                    LoginScreen.Click(LoginScreen.CheckBox.TermsCheckBox);
                                    Thread.Sleep(2000);
                                    LoginScreen.Click(LoginScreen.Button.LoginContinue);
                                    Thread.Sleep(2000);
                                    if (LoginScreen.IsEnabled(LoginScreen.Button.LogOut))
                                    {
                                        LoginScreen.WaitforLoginScreen();
                                        LoginScreen.Click(LoginScreen.Button.LoginContinue_1);
                                        Thread.Sleep(2000);

                                    }
                                }
                            }

                            catch (Exception e)
                            {
                                LoginScreen.Click(LoginScreen.Button.LoginContinue_1);
                                Thread.Sleep(2000);


                            }
                    }
                }
            }
            catch (Exception e)
            {
                Logger.Info("Exception occur in Login screen" + e);
                Crash_Data.WaitforCrashScreen();
                if (Crash_Data.IsVisible())
                {
                    // UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input tap " + "1000 572");
                    Crash_Data.Click(Crash_Data.Button.DISMISS);
                    HomeScreen.WaitforHomeScreen();
                }
                
            }
        }
    }

    /// <summary>
    /// This class provides all the functionalities and constants for features related to Account Settings.
    /// </summary>
  public class AccountSettings
  {

      /// <summary>
      /// Collection of all CheckBoxes
      /// </summary>
      public enum CheckBox
      {
          RLXCheckBox
      }

      /// <summary>
      /// Collection of all Buttons
      /// </summary>
      public enum Button
      {
          LogOut,
          UpButton,
          PhilipsAnnouncementLink,
          BackButton,
          LogOutConfirm
      }

      private static MobilePageInstance _instance
      {
          get
          {
              return Application.MobilePageInstanceInstance;
          }
      }

      /// <summary>
      /// Clicks on the desired Button
      /// </summary>
      /// <param name="btn">btn represents the name of the Button</param>
      public static void Click(Button btn)
      {
          if (btn == Button.UpButton)
              _instance.ClickById(ObjectRepository.LoginBackButton);
            /*   else if (btn == Button.LogOut)
                   _instance.GetElementByXpath("//android.widget.TextView[@text='Log out']/..").Click();
              */
            else if (btn == Button.PhilipsAnnouncementLink)
              _instance.ClickById(ObjectRepository.PhilipsAnnouncementLink);
            /*else if (btn == Button.LogOutConfirm)
                _instance.GetElementByXpath("//android.widget.Button[@text='Log out']").Click();
      */
        }



      /// <summary>
      /// Selects a Checkbox
      /// </summary>
      /// <param name="cb">cb represents the name of the CheckBox</param>
      public static void Select(CheckBox cb)
      {
          if(cb==CheckBox.RLXCheckBox)
          _instance.ClickById(ObjectRepository.rlxCheckBox);
      }

      /// <summary>
      /// Returns true if the Account Settings page is visible
      /// </summary>
      /// <returns>a boolean value</returns>
      public static bool IsScreenVisible()
      {
          bool bExist=false;
          try
          {
              if (_instance.GetElementByText("Account settings").Displayed==true)
                  bExist = true;
          }
          catch(Exception e)
          {
          }
          return bExist;

      }

      
  }

    /// <summary>
   /// This class provides all the functionalities and constants related to Philips Announcements.
   /// </summary>
  public class PhilipsAnnouncements
  {
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
          BackButton,
          UpButton
      }

      /// <summary>
      /// Clicks the desired Button
      /// </summary>
      /// <param name="btn">btn represents the name of the Button</param>
      public static void Click(Button btn)
      {
          if (btn == Button.BackButton)
              _instance.ClickById(ObjectRepository.BackButton);
          else if (btn == Button.BackButton)
              _instance.ClickById(ObjectRepository.LoginBackButton);
      }

      /// <summary>
      /// Returns a string containing the title of the Page
      /// </summary>
      /// <returns>a string value</returns>
      public static string GetTitle()
      {
          return _instance.GetElement(SearchBy.Id, ObjectRepository.LoginTitle).Text;
      }
  }

}
