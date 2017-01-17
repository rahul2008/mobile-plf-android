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

        [Given(@"that the user should be logged out if the user has logged in")]
        public void GivenThatTheUserShouldBeLoggedOutIfTheUserHasLoggedIn()
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
    }



}


