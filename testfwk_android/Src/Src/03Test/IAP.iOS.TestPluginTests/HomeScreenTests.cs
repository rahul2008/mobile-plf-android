using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Threading;
using Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS;
namespace Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS.Tests
{
    [TestClass()]
    public class HomeScreenTests_iOS
    {
        static int instance = 0;

        public HomeScreenTests_iOS()
        {
            if (instance == 0)
            {
                IAPConfiguration.LoadiOSConfiguration();
                HomeScreen.WaitforHomeScreen();
            }
            instance++;
        }

        [TestMethod()]
        public void ClickTest()
        {
            HomeScreen.SelectCountry("US");
            Thread.Sleep(3000);
            HomeScreen.Click(HomeScreen.Button.ShopNow);
            ProductScreen.WaitforProductScreen();
            ProductScreen.Click(ProductScreen.Button.Back);
            HomeScreen.WaitforHomeScreen();
            HomeScreen.Click(HomeScreen.Button.CartImage);
            Thread.Sleep(2000);
            ShoppingCart.WaitforShoppingCartScreen();
            ShoppingCart.Click(ShoppingCart.Button.UpButton);
            HomeScreen.WaitforHomeScreen();
            HomeScreen.Click(HomeScreen.Button.LogOut);
            HomeScreen.Click(HomeScreen.Button.Register);
            LoginScreen.Click(LoginScreen.Button.LoginPhilipsAccount);
            LoginScreen.SignIn("inapptest@mailinator.com", "Philips@123");
            HomeScreen.WaitforHomeScreen();
        }

        [TestMethod()]
        public void IsEnabledTest()
        {
            try
            {
                HomeScreen.WaitforHomeScreen();
                bool b = HomeScreen.IsEnabled(HomeScreen.Button.CartImage);
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void WaitforHomeScreenTest()
        {
            try
            {
                bool b = HomeScreen.WaitforHomeScreen();
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void IsVisibleTest()
        {
            try
            {
                bool b = HomeScreen.IsVisible();
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetScreenTitleTest()
        {
            try
            {
                string str = HomeScreen.GetScreenTitle();
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void SelectCountryTest()
        {
            try
            {
                HomeScreen.SelectCountry("US");
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }
    }
}