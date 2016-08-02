using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Threading;
using System.Configuration;
namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin.Tests
{
    [TestClass()]
    public class ProductScreenTests
    {
        
         static int _instance = 0;

        public ProductScreenTests()
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
                    HomeScreen.ContextMenu.Select(HomeScreen.Fields.Country_Spinner, HomeScreen.ContextMenu.MenuItem.UK);
                    Thread.Sleep(3000);
                    HomeScreen.Click(HomeScreen.Button.Shop_Now);
                    Thread.Sleep(6000);
                    ProductScreen.WaitforProductScreen();
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
               
                Dictionary<int, string> dictionaryvalues = ProductScreen.listofProducts();

                List<string> productid = ProductScreen.GetProductListItems();
                bool isExist = false;
                while (!isExist)
                {
                    try
                    {
                        for (int i = 0; i < dictionaryvalues.Count; i++)
                        {
                            productid = ProductScreen.GetProductListItems();
                            for (int j = 0; j < productid.Count; j++)
                            {
                                if (dictionaryvalues[i].Equals(productid[j]))
                                {

                                    isExist = true;
                                    ProductScreen.Click(dictionaryvalues[i], ProductScreen.Button.productScreen_InfoButton);
                                    ShoppingCartItem.WaitforShoppingCartInfoScreen();
                                    ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                                    ProductScreen.WaitforProductScreen();
                                  
                                    break;
                                }
                                else
                                    Console.WriteLine("Product is out of stock");
                                //ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                            }
                            if (isExist)
                                break;
                        }
                    }
                    catch (Exception)
                    {

                        ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                    }
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
            try
            {
               
                ProductScreen.IsEnabled(ProductScreen.Button.productScreen_BackButton);
                ProductScreen.IsEnabled(ProductScreen.Button.productScreen_ImageCart);
                
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void SwipeTest()
        {
            try
            {
               
                ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                ProductScreen.Swipe(ProductScreen.Direction.Down, 2);
               
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void listofProductsTest()
        {
            try
            {
               
                Dictionary<int, string> dictionaryvalues = ProductScreen.listofProducts();
                int productCount = dictionaryvalues.Count;
                
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void WaitforProductScreenTest()
        {
            try
            {
              
                ProductScreen.WaitforProductScreen();
               
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
                
                if (ProductScreen.IsVisible())
                    Console.WriteLine("Product screen is visible");
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void ClickTest1()
        {
            try
            {
                ProductScreen.Click(ProductScreen.Button.productScreen_ImageCart);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.UpButton);              
                ProductScreen.WaitforProductScreen();
               
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
               
                ProductScreen.WaitforProductScreen();
                ProductScreen.GetScreenTitle();
               
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetProductListItemsTest()
        {
            try
            {
               
                List<string> productId=ProductScreen.GetProductListItems();
                int productCount = productId.Count;
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void IsExistTest()
        {
            try
            {
               
                ProductScreen.IsExist(ProductScreen.Button.productScreen_BackButton);
                ProductScreen.IsExist(ProductScreen.Button.productScreen_ImageCart);
                ProductScreen.IsExist(ProductScreen.Button.productScreen_InfoButton);
               
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }
    }
}
