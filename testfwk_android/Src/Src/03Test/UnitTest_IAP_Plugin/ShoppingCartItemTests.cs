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
    public class ShoppingCartItemTests
    {
          static int _instance = 0;
         
          string productName; 
          public ShoppingCartItemTests()
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
        public void ClickTest()
        {
            try
            {
              bool  isExist = false;
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
                ShoppingCart.Click(ShoppingCart.Button.UpButton);
                ShoppingCartItem.WaitforShoppingCartInfoScreen();
            }
            catch(Exception e)
            {
                Assert.Fail();
            }
           
        }

        [TestMethod()]
        public void WaitforShoppingCartInfoScreenTest()
        {
            try
            {
                ShoppingCartItem.WaitforShoppingCartInfoScreen();
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
                ShoppingCartItem.IsVisible(productName);
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetTextTest()
        {
            try
            {
                ShoppingCartItem.GetText(ShoppingCartItem.TextView.CTN);
                ShoppingCartItem.GetText(ShoppingCartItem.TextView.ProductDescription);
                ShoppingCartItem.GetText(ShoppingCartItem.TextView.DiscountPrice);
                ShoppingCartItem.GetText(ShoppingCartItem.TextView.ProductOverview);
                ShoppingCartItem.GetText(ShoppingCartItem.TextView.ShoppingCartItemTitle);
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetProductImageTest()
        {
            try
            {
                //ShoppingCartItem.GetProductImage();
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void IsSelectedTest()
        {
            try
            {
                ShoppingCartItem.IsSelected(2);
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetSelectedImageIndexTest()
        {
            try
            {
                ShoppingCartItem.GetSelectedImageIndex();
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetCountofImagesTest()
        {
            try
            {
               int imageCount= ShoppingCartItem.GetCountofImages();
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
                ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Down, 2);
                ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Right,2);
                ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Left, 2);
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
               bool isExist = false;
                while (!isExist)
                {
                    try
                    {
                        ShoppingCartItem.IsExist(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                        ShoppingCartItem.IsExist(ShoppingCartItem.Button.BuyFromRetailer);
                        isExist = true;
                    }
                    catch(Exception e)
                    {
                        ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                    }
                }
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }
    }
}
