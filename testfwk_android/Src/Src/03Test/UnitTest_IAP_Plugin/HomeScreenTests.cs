using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using System.Configuration;
using System.Threading;
//using Philips.H2H.Foundation.AutomationCore.Interface.IMobilePageInterface;
namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin.Tests
{
    [TestClass()]
    public class HomeScreenTests
    {
        static int _instance = 0;

        public HomeScreenTests()
        {
            try
            {
                if (_instance == 0)
                {
                    IAPConfiguration.LoadConfigurationForDebug();
                    HomeScreen.Click(HomeScreen.Button.Register);
                    string title1 = LoginScreen.GetScreenTitle();
                    if (title1.Equals("Log In"))
                    {
                        LoginScreen.WaitforLoginScreen();
                        LoginScreen.LoginUser(ConfigurationManager.AppSettings["UsernameIAP"], ConfigurationManager.AppSettings["PasswordIAP"]);

                        HomeScreen.WaitforHomeScreen();
                    }
                    else
                    {
                        LoginScreen.Click(LoginScreen.Button.LoginBackButton);
                        HomeScreen.WaitforHomeScreen();

                    }
                }
            }
            catch(Exception e)
            {
                Assert.Fail();
            }
           _instance++;
        }
        
        

        [TestMethod()]
        public void ClickTest()
        {
            try
            {
                for (int i = 0; i < 10; i++)
                {
                    if (HomeScreen.WaitforHomeScreen())
                        HomeScreen.Click(HomeScreen.Button.Shop_Now);
                    ProductScreen.WaitforProductScreen();
                    ShoppingCart.Click(ShoppingCart.Button.UpButton);
                    HomeScreen.WaitforHomeScreen();
                    HomeScreen.Click(HomeScreen.Button.CartImage);
                    ShoppingCart.WaitforShoppingCartScreen();
                    ShoppingCart.Click(ShoppingCart.Button.UpButton);
                    
                    HomeScreen.WaitforHomeScreen();
                    HomeScreen.Click(HomeScreen.Button.Register);
                   
                  
                    LoginScreen.WaitforLoginScreen();
                    LoginScreen.Click(LoginScreen.Button.LoginBackButton);
                    HomeScreen.WaitforHomeScreen();
                    Console.Write("Executed: " + i);
                    //NOT IMPLEMENTED YET
                    //HomeScreen.Click(HomeScreen.Button.Register);
                }
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void IsEnabledTest()
        {
            try { 
            HomeScreen.IsEnabled(HomeScreen.Button.Shop_Now);
           // HomeScreen.IsEnabled(HomeScreen.Button.AddToCart);
            HomeScreen.IsEnabled(HomeScreen.Button.CartImage);
            //HomeScreen.IsEnabled(HomeScreen.Button.Submit);
            HomeScreen.IsEnabled(HomeScreen.Button.Register);
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetProductCountTest()
        {
            try { 
            //HomeScreen.GetProductCount();
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

       //****NOT WORKING
        [TestMethod()]
        public void GetProductNameTest()
        {
            try { 
            //HomeScreen.GetProductName(0);
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void SelectProductTest()
        {
            try { 
           // HomeScreen.SelectProduct(1);
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetTextFromCartTest()
        {
            try {

            HomeScreen.GetTextFromCart();
                }
            catch(Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetProductListItemsTest()
        {
            try { 
           // HomeScreen.GetProductListItems();
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void WaitforHomeScreenTest()
        {
            try { 
            HomeScreen.WaitforHomeScreen();
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void IsVisibleTest()
        {
            try { 
            HomeScreen.IsVisible();
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetScreenTitleTest()
        {
            try { 
            HomeScreen.GetScreenTitle();
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
            //Assert.Fail();
        }

        [TestMethod()]
        public void ClickTest1()
        {
            try {
           // List<string> productid = HomeScreen.GetProductListItems();
            HomeScreen.ContextMenu.Select(HomeScreen.Fields.Country_Spinner, HomeScreen.ContextMenu.MenuItem.UK);
            Thread.Sleep(3000);
            HomeScreen.Click(HomeScreen.Button.Shop_Now);
            ProductScreen.WaitforProductScreen();
            ShoppingCart.Click(ShoppingCart.Button.UpButton);
            HomeScreen.WaitforHomeScreen();
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
            //Assert.Fail();
        }
    }
}
