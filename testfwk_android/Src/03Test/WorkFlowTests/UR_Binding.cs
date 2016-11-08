using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Threading;
using TechTalk.SpecFlow;
using System.Collections.Generic;
using Philips.H2H.Foundation.AutomationCore.Interface;
using System.Configuration;
using Philips.CDP.Automation.IAP.Tests.Workflows;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using System.Xml;
using System.Reflection;
using System.IO;
using Philips.SIG.Automation.Android.CDPP.AppFramework_TestPlugin;
using NUnit.Framework;
using UserRegistration_TestPlugin;

namespace Philips.CDP.Automation.IAP.Tests.Workflows
{

   [Binding]
    public class UR_Binding
    {


       [Given(@"the user should not be registered user")]
       public void GivenTheUserShouldNotBeRegisteredUser()
       {
          bool title = AppHomeScreen.IsVisibleScreenTitleText("Mobile App Home");
           
           if (title == true)
           {
               AppHomeScreen.Click(AppHomeScreen.Button.HamburgerIcon);
               Thread.Sleep(3000);
               AppHomeScreen.Click(AppHomeScreen.Button.Settings);
               Settings.Click(Settings.Button.Logout);
               Thread.Sleep(8000);
               Settings.Click(Settings.Button.LogoutDialog);
               Thread.Sleep(8000);
               MobileDriver.ReLaunchApp(4);
           }
           else
           {
               IapReport.Message("App is in launch Screen");
           }
       }



        [Then(@"I click on Create Philips Account")]
        public void ThenIClickOnCreatePhilipsAccount()
        {
            Registration.Wait();
            bool logVerify = Registration.Login_Screen();
            if (logVerify)
            {
                IapReport.Message("The User is login in screen");
            }
            Registration.Click(Registration.Button.Create_PhilipsAccount);
        }

        [Then(@"enter valid name as ""(.*)"" email as ""(.*)"" and password as ""(.*)""")]
        public void ThenEnterValidNameAsEmailAsAndPasswordAs(string p0, string p1, string p2)
        {
            CreateAccount.Wait();
            bool createAccountVerify = CreateAccount.CreateAccount_Screen();
            if (createAccountVerify)
            {
                IapReport.Message("The User is in Create Account screen");
            }
            CreateAccount.Enter_Details(p0, p1, p2);
            MobileDriver.FireKeyEvent(4);

        }

        [Then(@"accept terms conditions and create philips account")]
        public void ThenAcceptTermsConditionsAndCreatePhilipsAccount()
        {
            CreateAccount.Click(CreateAccount.CheckBox.Terms_Conditions);
            CreateAccount.Click(CreateAccount.Button.Create_Account);
            Thread.Sleep(60000);
            CreateAccount.Click(CreateAccount.Button.Activated_Account);
            CreateAccount.Click(CreateAccount.Button.URContinue);
        }


        [Then(@"I click on Account Settings from Hamburger Menu")]
        public void ThenIClickOnAccountSettingsFromHamburgerMenu()
        {
            AppHomeScreen.Click(AppHomeScreen.Button.Settings);
        }



        [Then(@"I verify that the My Account has Logout button")]
        public void ThenIVerifyThatTheMyAccountHasLogoutButton()
        {
            bool Verify = Settings.IsVisible();
            if (Verify != true)
            {
                IapReport.Fail("The user is not in  Account Setting Screen");
            }
            bool Verify1 = Settings.IsVisibleButton(Settings.Button.Logout);
            if (Verify1 != true)
            {
                IapReport.Message("The user is not successfully logged into the Application");
            }

        }


        /// <summary>
        /// This step definition  provides all the functionalities related to Philips account login.
        /// </summary>



        [Then(@"I click on Philips Account")]
        public void ThenIClickOnPhilipsAccount()
        {
            try
            {
                Registration.Wait();
                Registration.Click(Registration.Button.Philips_Account);
                URLoginScreen.WaitForLogin_with_PhilipsAccount();
                Logger.Info("Pass : Reached philips account login screen");
            }
            catch (Exception e)
            {
                IapReport.Fail("Fail : Not in philips account login screen", e);
            }
        }

        [Then(@"enter email as ""(.*)"" and password as ""(.*)""")]
        public void ThenEnterEmailAsAndPasswordAs(string p0, string p1)
        {
            URLoginScreen.Entry_Details(p0, p1);
            MobileDriver.FireKeyEvent(4);
        }

        [Then(@"I click on Log In button")]
        public void ThenIClickOnLogInButton()
        {
            URLoginScreen.Click(URLoginScreen.Button.Login);
            Thread.Sleep(20000);
        }

        [Then(@"accept terms conditions and click on continue")]
        public void ThenAcceptTermsConditionsAndClickOnContinue()
        {
            try
            {
                bool b = Registration.IsEnable(Registration.Button.TermsConditions);
                if (b == true)
                {
                    Registration.Click(Registration.CheckBox.Terms_Conditions);
                    Forgot_Password.Click(Forgot_Password.Button.Continue);
                    Thread.Sleep(3000);
                    CreateAccount.Click(CreateAccount.Button.LoogedIn_Continue);
                }
                else
                {
                    CreateAccount.Click(CreateAccount.Button.LoogedIn_Continue);
                }
            }
            catch (Exception e)
            {
                IapReport.Fail("Fail :User did not accept Terms and condition", e);
            }
        }

        [Then(@"Verify that user is indicated that he is logged in and click on continue")]
        public void ThenVerifyThatUserIsIndicatedThatHeIsLoggedInAndClickOnContinue()
        {
            CreateAccount.Click(CreateAccount.Button.URContinue);
        }

        /// <summary>
        /// This step definition  provides all the functionalities related to Traditional Merge.
        /// </summary>
        /// 

        [Then(@"I click on Logout in Account Setting Screen")]
        public void ThenIClickOnLogoutInAccountSettingScreen()
        {
            Settings.Click(Settings.Button.Logout);
            Thread.Sleep(8000);
            Settings.Click(Settings.Button.LogoutDialog);
            Thread.Sleep(8000);
        }

        [Then(@"I launch the user registration home screen from Account setting view")]
        public void ThenILaunchTheUserRegistrationHomeScreenFromAccountSettingView()
        {

            Settings.Click(Settings.Button.Login);
            Registration.Wait();
            bool logVerify = Registration.Setting_Login_Screen();
            if (logVerify != true)
            {
                IapReport.Fail("The User is not logged into the Application");
            }

        }

        [Then(@"I click on google\+ to login with gmail account")]
        public void ThenIClickOnGoogleToLoginWithGmailAccount()
        {
            try
            {
            Registration.Click(Registration.Button.Google_plus);
            Thread.Sleep(2000);
            LoginGooglePlus.Wait();
            IapReport.Message("Pass : successfuly reached login to Google web page");
            }
            catch (Exception e)
            {
                IapReport.Fail("Fail : failed to reach Google login page", e);
            }

        }


        [Then(@"Verify the traditional merge funtional by entering the gmail password ""(.*)""")]
        public void ThenVerifyTheTraditionalMergeFuntionalByEnteringTheGmailPassword(string p0)
        {
            try
            {
                bool val = LoginGooglePlus.GetMerge_Screen();
                if (val)
                {
                    IapReport.Message("The User is in Merge your Accounts Screen");
                }
                LoginGooglePlus.Enter_Password(p0);
                URLoginScreen.Click(URLoginScreen.Button.Merge);
                Thread.Sleep(3000);
                CreateAccount.Click(CreateAccount.Button.URContinue);
                IapReport.Message("The User is able to  Merge the Accounts Screen");
            }
            catch (Exception e)
            {
                IapReport.Fail("Fail :Traditional Merge cannot be verified", e);
            }
        }



        [Then(@"I enter email as ""(.*)"" and password as ""(.*)"" in gmail webview screen")]
        public void ThenIEnterEmailAsAndPasswordAsInGmailWebviewScreen(string p0, string p1)
        {
            try
            {

                bool b = LoginGooglePlus.IsEnable(p0);
                if (b == true)
                {
                    LoginGooglePlus.Enter_Details(p0, p1);
                    LoginGooglePlus.Click(LoginGooglePlus.Button.SignIn);
                    Thread.Sleep(10000);
                    LoginGooglePlus.Click(LoginGooglePlus.Button.Allow);
                    Thread.Sleep(15000);
                }
                else
                {
                    LoginGooglePlus.Enter_Password(p1);
                    LoginGooglePlus.Click(LoginGooglePlus.Button.SignIn);
                    Thread.Sleep(10000);
                    LoginGooglePlus.Click(LoginGooglePlus.Button.Allow);
                    Thread.Sleep(15000);
                }
                IapReport.Message("Pass : User is able to enter email and password successfully in Gmail webview ");
            }
            catch (Exception e)
            {
                IapReport.Fail("Fail : User is not able to enter email and password successfully in Gmail webview", e);
            }
        }

        [Then(@"verify the accept and terms condition in Facebook")]
        public void ThenVerifyTheAcceptAndTermsConditionInFacebook()
        {
            try
            {
                Thread.Sleep(10000);
                String s = Registration.GetHeader();
                if (s == "Log In")
                {
                    Logger.Info("Reached login screen");
                }
                else
                {
                    Logger.Fail("It failed to reached in login screen");
                }
                int b = Registration.Howmanycheckboxvailable();

                if (b == 0)
                {

                    Registration.Click(Registration.Button.Continue);
                    IapReport.Message("Pass : Accepted terms conditions and Account created successfully");
                    Thread.Sleep(3000);
                    URHomeScreen.Wait();
                }
                if (b == 1)
                {
                    Registration.Click(Registration.CheckBox.Terms_Conditions);
                    Thread.Sleep(3000);
                    Forgot_Password.Click(Forgot_Password.Button.Continue);
                    IapReport.Message("Pass : Accepted terms conditions and Account created successfully");
                    Thread.Sleep(3000);
                    CreateAccount.Click(CreateAccount.Button.LoogedIn_Continue);
                }
                if (b == 2)
                {
                    Registration.Click(Registration.CheckBox.Terms_Conditions);
                    Thread.Sleep(3000);
                    Registration.Click(Registration.CheckBox.Philips_Announcements);
                    Thread.Sleep(3000);
                    Forgot_Password.Click(Forgot_Password.Button.Continue);
                    IapReport.Message("Pass : Accepted terms conditions and Account created successfully");
                    Thread.Sleep(3000);
                    // ParentalConsent.Click(ParentalConsent.Button.Agree);
                    //Thread.Sleep(3000);
                    URHomeScreen.Wait();
                    CreateAccount.Click(CreateAccount.Button.LoogedIn_Continue);
                }
            }

            catch (Exception e)
            {
                IapReport.Fail("Fail : Not in Log In  screen OR title is wrong", e);
            }
        }


        [Then(@"verify the accept and terms condition in google plus")]
        public void ThenVerifyTheAcceptAndTermsConditionInGooglePlus()
        {
            try
            {
                Thread.Sleep(10000);
                String s = Registration.GetHeader();
                if (s == "Log In")
                {
                    Logger.Info("Reached login screen");
                }
                else
                {
                    Logger.Fail("It failed to reached in login screen");
                }
                int b = Registration.Howmanycheckboxvailable();

                if (b == 0)
                {

                    Registration.Click(Registration.Button.Continue);
                    IapReport.Message("Pass : Accepted terms conditions and Account created successfully");
                    Thread.Sleep(3000);
                    URHomeScreen.Wait();
                }
                if (b == 1)
                {
                    Registration.Click(Registration.CheckBox.Terms_Conditions);
                    Thread.Sleep(3000);
                    Forgot_Password.Click(Forgot_Password.Button.Continue);
                    IapReport.Message("Pass : Accepted terms conditions and Account created successfully");
                    Thread.Sleep(3000);
                    CreateAccount.Click(CreateAccount.Button.LoogedIn_Continue); 
                }
                if (b == 2)
                {
                    Registration.Click(Registration.CheckBox.Terms_Conditions);
                    Thread.Sleep(3000);
                    Registration.Click(Registration.CheckBox.Philips_Announcements);
                    Thread.Sleep(3000);
                    Forgot_Password.Click(Forgot_Password.Button.Continue);
                    IapReport.Message("Pass : Accepted terms conditions and Account created successfully");
                    Thread.Sleep(3000);
                    // ParentalConsent.Click(ParentalConsent.Button.Agree);
                    //Thread.Sleep(3000);
                    URHomeScreen.Wait();
                    CreateAccount.Click(CreateAccount.Button.LoogedIn_Continue);
                }
            }

            catch (Exception e)
            {
                IapReport.Fail("Fail : Not in Log In  screen OR title is wrong", e);
            }
        }

        /// <summary>
        /// This step definition  provides all the functionalities related to Social Merge.
        /// </summary>
        ///
        [Then(@"I click on Facebook to login with Facebook account")]
        public void ThenIClickOnFacebookToLoginWithFacebookAccount()
        {
            try
            {
                Registration.Click(Registration.Button.Facebook);
                Thread.Sleep(18000);
                LoginFacebook.Wait();
                IapReport.Message("Pass : successfuly reached login to facebook web page");
            }
            catch (Exception e)
            {
                IapReport.Fail("Fail : failed to reach facebook login page", e);
            }
        }
        [Then(@"login to facebook account with name ""(.*)"" and password as ""(.*)""")]
        public void ThenLoginToFacebookAccountWithNameAndPasswordAs(string p0, string p1)
        {
            try
            {
                LoginFacebook.Wait();
                LoginFacebook.Enter_FBDetails(p0, p1);
                Thread.Sleep(8000);
                LoginFacebook.Click(LoginFacebook.Button.LogIn);
                Thread.Sleep(10000);

                //Need to add connect and cancel button in api after facebook login
                IapReport.Message("Pass : successful login to facebook");
            }
            catch (Exception e)
            {
                IapReport.Fail("Fail : Failed to login to facebook account", e);
            }
        }

        [Then(@"Verify the social merge functionality by entering the gmail username as ""(.*)"" and password ""(.*)""")]
        public void ThenVerifyTheSocialMergeFunctionalityByEnteringTheGmailUsernameAsAndPassword(string p0, string p1)
        {
            try
            {
                LoginFacebook.Continue_Facebook();
                Thread.Sleep(3000);

                bool val = LoginFacebook.GetSocialMerge_Screen();
                if (val)
                {
                    IapReport.Message("The User is asked to for Social Merging the Account");
                }
                URLoginScreen.Click(URLoginScreen.Button.Merge);
                Registration.Click(Registration.Button.SwitchAccount);
                Registration.Click(Registration.Button.SocialMergeGoogle);
                LoginGooglePlus.Enter_Password(p1);
                LoginGooglePlus.Click(LoginGooglePlus.Button.SignIn);
                Thread.Sleep(10000);
                LoginGooglePlus.Click(LoginGooglePlus.Button.Allow);
                Thread.Sleep(15000);
                bool b = Registration.IsEnable(Registration.Button.TermsConditions);
                if (b == true)
                {
                    Registration.Click(Registration.CheckBox.Terms_Conditions);
                    CreateAccount.Click(CreateAccount.Button.URContinue);
                    Registration.Click(Registration.Button.Continue);
                    Thread.Sleep(3000);
                    URHomeScreen.Wait();
                }
                else
                {
                    Registration.Click(Registration.Button.Continue);

                }
                
            }
            catch (Exception e)
            {
                IapReport.Fail("Fail : Social Merge cannot be Verified", e);
            }


        }
    }
   }
