using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS;
using System.Threading;

namespace Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS.Tests
{
    [TestClass()]
    public class LoginScreenTests
    {
        static int instance = 0;

        public LoginScreenTests()
        {
            try {
                if (instance == 0)
                {
                    IAPConfiguration.LoadiOSConfiguration();
                    HomeScreen.WaitforHomeScreen();
                    HomeScreen.Click(HomeScreen.Button.Register);
                    LoginScreen.WaitforLoginScreen();
                }
                instance++;
            }
            catch(Exception)
            {
               
            }
        }

        [TestMethod()]
        public void ClickTest()
        {
            try
            {
                LoginScreen.Click(LoginScreen.Button.CreatePhilipsAccount);
                LoginScreen.Click(LoginScreen.Button.CreateAccBack);
                LoginScreen.WaitforLoginScreen();
                LoginScreen.Click(LoginScreen.Button.LoginMainBack);
                HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(HomeScreen.Button.Register);
                LoginScreen.Click(LoginScreen.Button.LoginPhilipsAccount);
                LoginScreen.SignIn("inapptest@mailinator.com", "Philips@123");
                HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(HomeScreen.Button.LogOut);
                HomeScreen.Click(HomeScreen.Button.Register);
                LoginScreen.Click(LoginScreen.Button.LoginPhilipsAccount);
                LoginScreen.SignIn("inapptest@mailinator.com", "Philips@123");
                HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(HomeScreen.Button.Register);
                Thread.Sleep(2000);
                LoginScreen.Click(LoginScreen.Button.Continue);
                Thread.Sleep(2000);
                HomeScreen.Click(HomeScreen.Button.LogOut);
                Thread.Sleep(2000);
                HomeScreen.Click(HomeScreen.Button.Register);
                //Forgot Password flow, google fb login flow, Erroneous inputs provided
            }
            catch(Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void IsEnabledTest()
        {
            bool isEnabledFlag = false;
            try
            {
                isEnabledFlag = LoginScreen.IsEnabled(LoginScreen.Button.CreatePhilipsAccount);
                isEnabledFlag = isEnabledFlag & LoginScreen.IsEnabled(LoginScreen.Button.LoginFacebook);
                isEnabledFlag = isEnabledFlag & LoginScreen.IsEnabled(LoginScreen.Button.LoginGoogleplus);
                isEnabledFlag = isEnabledFlag & LoginScreen.IsEnabled(LoginScreen.Button.LoginPhilipsAccount);
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void WaitforLoginScreenTest()
        {
            try
            {
                LoginScreen.WaitforLoginScreen();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void IsVisibleTest()
        {
            try
            {
                LoginScreen.IsVisible();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetScreenTitleTest()
        {
            string screenTitle = null;
            try
            {
                screenTitle = LoginScreen.GetScreenTitle();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void SignInTest()
        {
            try
            {
                LoginScreen.WaitforLoginScreen();
                LoginScreen.Click(LoginScreen.Button.LoginPhilipsAccount);
                LoginScreen.SignIn("inapptest@mailinator.com", "Philips@123");
                HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(HomeScreen.Button.LogOut);
                HomeScreen.Click(HomeScreen.Button.Register);
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void PerformForgotPasswordTest()
        {
            try
            {
                LoginScreen.WaitforLoginScreen();
                LoginScreen.Click(LoginScreen.Button.LoginPhilipsAccount);
                LoginScreen.PerformForgotPassword("shaheen.shaikh@philips.com");
                LoginScreen.SignIn("inapptest@mailinator.com", "Philips@123");
                HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(HomeScreen.Button.LogOut);
                HomeScreen.Click(HomeScreen.Button.Register);
            }
            catch (Exception ex)
            {
                Assert.Fail();
            }
        }
    }
}
