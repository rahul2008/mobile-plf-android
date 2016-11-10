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

namespace Philips.SIG.Automation.Android.CDPP.Tests.Workflows
{
    [Binding]

    public class MyPhilips
    {

        [Given(@"I am on the AppFramework Screen")]
        public void GivenIAmOnTheAppFrameworkScreen()
        {
            WelcomeScreen.WelcomeScreenImage();
            Thread.Sleep(10000);
        }

        [Then(@"I click on Skip")]
        public void ThenIClickOnSkip()
        {
            WelcomeScreen.Click(WelcomeScreen.Button.Skip);
            //WelcomeScreen.click1();
            //MobileDriver.FireKeyEvent(4);
        }

        [Then(@"Verify that the user is in User Registration screen")]
        public void ThenVerifyThatTheUserIsInUserRegistrationScreen()
        {
            bool isPresent = WelcomeScreen.CheckUserRegistration();
            if (!isPresent)
            {
                Logger.Fail("The User is not in login screen");
            }
           
        }

        [Then(@"I click on ""(.*)"" from the Hamburger Menu List")]
        public void ThenIClickOnFromTheHamburgerMenuList(string p0)
        {
            HamburgerMenu.HamburgerlistClick(p0);
        }

        [Then(@"Verify that the user can see the Home Screen")]
        public void ThenVerifyThatTheUserCanSeeTheHomeScreen()
        {
            AppHomeScreen.IsVisible1();
        }

        [Then(@"I click on Hamburger Menu Icon")]
        public void ThenIClickOnHamburgerMenuIcon()
        {
            AppHomeScreen.Click(AppHomeScreen.Button.HamburgerIcon);
            Thread.Sleep(3000);
        }


        [Then(@"I log in with the email 'datacore@mailinator.com' and password 'Philips@123'")]
        public void ThenIRegisterUsingMyPhilipsAccount()
        {
            //LoginScreen.LoginUser("inapptest@mailinator.com", "Philips@123");
            Log_In.Click();
            Log_In.SignIn("datacore@mailinator.com", "Philips@123");
        }


        [Then(@"I register using My Philips account")]
        public void IRegisterUsingMyPhilipsAccount()
        {
            //LoginScreen.LoginUser("inapptest@mailinator.com", "Philips@123");
            Log_In.Click();
            Log_In.SignIn("hubble@mailinator.com", "Philips@123");
        }



        [Then(@"I click on Settings from the Hamburger Menu List")]
        public void ThenIClickOnSettingsFromTheHamburgerMenuList()
        {
            AppHomeScreen.Click(AppHomeScreen.Button.Settings);
            Thread.Sleep(2000);
        }

        [Then(@"I verify that under my account the user status is Log out")]
        public void ThenIVerifyThatUnderMyAccountTheUserStatusIsLogOut()
        {
            WelcomeScreen.Status();

            Thread.Sleep(2000);
            AppHomeScreen.Click(AppHomeScreen.Button.Alogout);
            //AccountSettings.Click(AccountSettings.Button.LogOut);
            Thread.Sleep(2000);

            AppHomeScreen.Click(AppHomeScreen.Button.AlogoutConfirm);

            //AccountSettings.Click(AccountSettings.Button.LogOutConfirm);
            Thread.Sleep(2000);
            HomeScreen.WaitforHomeScreen();





        }




    }
}
