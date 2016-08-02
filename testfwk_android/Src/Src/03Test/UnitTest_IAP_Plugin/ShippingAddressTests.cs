using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Configuration;
using System.Threading;

namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin.Tests
{
    [TestClass()]
    public class ShippingAddressTests
    {
     static int _instance = 0;
       string productName;
       public ShippingAddressTests()
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
                    HomeScreen.ContextMenu.Select(HomeScreen.Fields.Country_Spinner, HomeScreen.ContextMenu.MenuItem.US);
                    Thread.Sleep(3000);
                    HomeScreen.Click(HomeScreen.Button.Shop_Now);
                    Thread.Sleep(6000);
                    ProductScreen.WaitforProductScreen();
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
                                        productName = dictionaryvalues[i].ToString();
                                        break;
                                    }
                                    else
                                        Console.WriteLine("Product is out of stock");
                                    //ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                                }
                                if (isExist)
                                    break;
                            }
                             isExist = false;
                            while (!isExist)
                            {
                                try
                                {
                                    isExist = ShoppingCartItem.IsExist(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                                    if (isExist == false)
                                        ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                                }
                                catch (Exception)
                                {

                                    ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                                }
                            }


                            ShoppingCartItem.Click(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                            ShoppingCart.WaitforShoppingCartScreen();
                            ShoppingCart.Click(ShoppingCart.Button.Checkout);
                            ShippingAddress.WaitforShippingAddressScreen();
                        }
                        catch (Exception)
                        {

                            ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                        }
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
        public void WaitforShippingAddressScreenTest()
        {
            try
            {
                ShippingAddress.WaitforShippingAddressScreen();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void ClickTest()
        {
            try
            {
                ShippingAddress.Click(ShippingAddress.Button.Deliver_to_This_Address);
                ShippingAddress.WaitforShippingAddressScreen();
                Address.Click(Address.Button.UpButton);
               // Summary.Click(Summary.Button.UpButton);
                ShippingAddress.Click(ShippingAddress.Button.Add_a_new_Address);
                Address.Click(Address.Button.UpButton);
                ShippingAddress.Click(ShippingAddress.Button.Cancel);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.Checkout);
                ShippingAddress.WaitforShippingAddressScreen();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetPageTitleTest()
        {
            try
            {
                ShippingAddress.GetPageTitle();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetListofAddressTest()
        {
            try
            {
                ShippingAddress.GetListofAddress("US");
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void SelectAddressTest()
        {
            try
            {
                ShippingAddress.SelectAddress("abhi ram","US");
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

         [TestMethod()]
        public void deleteAllAddressTest()
        {
            try
            {
                ShippingAddress.deleteAllAddress("US");
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
                ShippingAddress.Swipe(ShippingAddress.Direction.Up, 2);
                ShippingAddress.Swipe(ShippingAddress.Direction.Down, 2);
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

    }
}
