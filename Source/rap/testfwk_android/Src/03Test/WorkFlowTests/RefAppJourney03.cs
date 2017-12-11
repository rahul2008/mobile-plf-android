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
using Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin;


namespace Philips.CDP.Automation.IAP.Tests.Workflows
{
    [Binding]

    public class RefAppJourney03
    {
        [Given(@"user is on the User Registration screen")]
        public void GivenUserIsOnTheUserRegistrationScreen()
        {
            bool isPresent = WelcomeScreen.IsVisible(WelcomeScreen.Button.Skip);
            if (isPresent)
            {
                WelcomeScreen.Click(WelcomeScreen.Button.Skip);
                
            }
            else
            {
                AppHomeScreen.Click(AppHomeScreen.Button.HamburgerIcon);
                AppHomeScreen.Click(AppHomeScreen.Button.Settings);
                Settings.Click(Settings.Button.Login);
            }
        }

        [Given(@"user select the country as ""(.*)""")]
        public void GivenUserSelectTheCountryAs(string p0)
        {
            Registration.Click(Registration.Button.Country);
            Registration.Click(Registration.Button.CountrySelect);
            Registration.Select_Country(p0);
        }


        [Given(@"that the user should be logged out")]
        public void GivenThatTheUserShouldBeLoggedOut()
        {
            Thread.Sleep(3000);
            bool title = AppHomeScreen.IsVisibleScreenTitleText("Mobile App home");

            if (title == true)
            {
                AppHomeScreen.Click(AppHomeScreen.Button.HamburgerIcon);
                Thread.Sleep(3000);
                AppHomeScreen.Click(AppHomeScreen.Button.Settings);
                Settings.Click(Settings.Button.Logout);
                Thread.Sleep(8000);
                Settings.Click(Settings.Button.LogoutDialog);

            }
            else
            {
                IapReport.Message("App is in launch Screen");
            }

        }

        [Given(@"user has to launch the support screen")]
        public void GivenUserHasToLaunchTheSupportScreen()
        {
            AppHomeScreen.Click(AppHomeScreen.Button.HamburgerIcon);
            AppHomeScreen.Click(AppHomeScreen.Button.Support);
            Thread.Sleep(3000);
            if (Support.WaitforSupportScreen())
            {
                IapReport.Message("Support Screen is Visible");
            }
            else
            {
                IapReport.Message("Support Screen is not Visible");
            }
        }


        [Then(@"I verify that the My Account has LogIn button")]
        public void ThenIVerifyThatTheMyAccountHasLogInButton()
        {
            bool isPresent = Settings.IsVisibleButton(Settings.Button.Login);
            if (!isPresent)
            {
                IapReport.Fail("The login button is not present");
            }
        }

        [Then(@"I launch the user registration home screen from Account settings")]
        public void ThenILaunchTheUserRegistrationHomeScreenFromAccountSettings()
        {
            Settings.Click(Settings.Button.Login);
            Registration.Wait();
            bool logVerify = Settings.Login_Screen();
            if (!logVerify)
            {
                IapReport.Fail("The User is not in user registration screen");
            }

        }

        [Then(@"I click on Support from Hamburger Menu List and Validate Support Page is launched")]
        public void ThenIClickOnSupportFromHamburgerMenuListAndValidateSupportPageIsLaunched()
        {
            AppHomeScreen.Click(AppHomeScreen.Button.HamburgerIcon);
            AppHomeScreen.Click(AppHomeScreen.Button.Support);
            Thread.Sleep(3000);
            if (Support.WaitforSupportScreen())
            {
                IapReport.Message("Support Screen is Visible");
            }
            else
            {
                IapReport.Message("Support Screen is not Visible");
            }
        }

        [Then(@"verify that user can logout from Account Settings")]
        public void ThenVerifyThatUserCanLogoutFromAccountSettings()
        {
            AppHomeScreen.Click(AppHomeScreen.Button.HamburgerIcon);
            Thread.Sleep(3000);
            AppHomeScreen.Click(AppHomeScreen.Button.Settings);
            Settings.Click(Settings.Button.Logout);
            Thread.Sleep(8000);
            Settings.Click(Settings.Button.LogoutDialog);
        }









    }


}




