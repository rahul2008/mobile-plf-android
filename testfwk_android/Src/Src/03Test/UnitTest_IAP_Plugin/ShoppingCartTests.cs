using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using System.Configuration;
using System.Threading;
using Philips.H2H.Foundation.AutomationCore;

namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin.Tests
{
    [TestClass()]
    public class ShoppingCartTests
    {
       static int _instance = 0;
       string productName; 
       public ShoppingCartTests()
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
        public void GetTextTest()
        {
            try
            {
                //ShoppingCart.GetText(ShoppingCart.TextView.Title);
                //HomeScreen.Click(HomeScreen.Button.BuyNow);

                bool isExist = false;
                while (!isExist)
                {
                    try
                    {
                        ShoppingCart.GetText(ShoppingCart.TextView.TotalCost);
                        isExist = true;
                    }
                    catch (Exception)
                    {
                        ShoppingCart.SwipeProductList(ShoppingCart.Direction.Up, 2);
                    }
                }
                ShoppingCart.GetText(ShoppingCart.TextView.TotalItems);
                ShoppingCart.GetText(ShoppingCart.TextView.UPS_PARCEL);
                ShoppingCart.GetText(ShoppingCart.TextView.VAT);
                List<CartList> values = ShoppingCart.getCartItems();
               int productCount = values[0].carts[0].entries.Count();
               for (int i = productCount; i > 0; i--)
               {
                   if (i == 0)
                       break;
                   else
                       ShoppingCart.SwipeProductList(ShoppingCart.Direction.Down, 2);
               }
            }

            catch (Exception e)
            {
                Assert.Fail();
            }

        }

        [TestMethod()]
        public void ClickTest()
        {
            try
            {
                List<CartList> values = ShoppingCart.getCartItems();
                bool stock = values[0].carts[0].entries[0].product.purchasable;
                if (stock)
                {
                    ShoppingCart.Click(ShoppingCart.Button.Checkout);
                    ShippingAddress.Click(ShippingAddress.Button.arrow);
                }
                ShoppingCart.Click(ShoppingCart.Button.Dots);
                ShoppingCart.Click(ShoppingCart.Button.ClaimVoucherArrow);
                ShoppingCart.Click(ShoppingCart.Button.UpButton);
                ShoppingCartItem.WaitforShoppingCartInfoScreen();
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                ProductScreen.WaitforProductScreen();
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(HomeScreen.Button.CartImage);
                ShoppingCart.Click(ShoppingCart.Button.ContinueShopping);
                ProductScreen.WaitforProductScreen();
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(HomeScreen.Button.CartImage);
                if (ShoppingCart._getproductList().Count == 0)
                {
                    ShoppingCart.Click(ShoppingCart.Button.ContinueShopping_1);
                    HomeScreen.Click(HomeScreen.Button.CartImage);
                }

            }
            catch (Exception)
            {

                Assert.Fail();
            }
            //Assert.Fail();
        }

        [TestMethod()]
        public void IsEnabledTest()
        {
            try
            {
                
                ShoppingCart.IsEnabled(ShoppingCart.Button.Checkout);
                ShoppingCart.IsEnabled(ShoppingCart.Button.UpButton);
                ShoppingCart.IsEnabled(ShoppingCart.Button.Dots);
               // ShoppingCart.IsEnabled(ShoppingCart.Button.ClaimVoucherArrow);
                if (ShoppingCart._getproductList().Count == 0)
                    ShoppingCart.IsEnabled(ShoppingCart.Button.ContinueShopping_1);
                ShoppingCart.IsEnabled(ShoppingCart.Button.ContinueShopping);
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetScreenTitleTest()
        {
            try
            {
                ShoppingCart.GetScreenTitle();
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
                ShoppingCart.IsVisible();
            }
            catch (Exception)
            {

                Assert.Fail();
            }
           
        }

        [TestMethod()]
        public void SelectTest()
        {
            try
            {
                List<String> ProdNames = ShoppingCart.GetProductNames();
                ShoppingCart.Select(ProdNames[0], 3);
            }
            catch (Exception)
            {
                Assert.Fail();
            }
           
        }

        [TestMethod()]
        public void WaitforShoppingCartScreenTest()
        {
            ShoppingCart.WaitforShoppingCartScreen();
            //Assert.Fail();
        }

        [TestMethod()]
        public void IsProductExistTest()
        {
            try
            {
                List<String> ProdNames = ShoppingCart.GetProductNames();
                ShoppingCart.IsProductExist(ProdNames[0]);
            }
            catch (Exception)
            {
                Assert.Fail();
            }

        }

        [TestMethod()]
        public void GetAllProductPriceTest()
        {
            try
            {
                ShoppingCart.GetAllProductPrice();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetProductNamesTest()
        {
            try
            {
                ShoppingCart.GetProductNames();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void DeleteAllProductsTest()
        {
            try
            {
                ShoppingCart.DeleteAllProducts();
                ShoppingCart.Click(ShoppingCart.Button.ContinueShopping_1);
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                HomeScreen.WaitforHomeScreen();
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
                    }
                    catch (Exception)
                    {

                        ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                    }
                }
            }
            catch (Exception)
            {
                Assert.Fail();
            }

        }

        [TestMethod()]
        public void VerifyDeliveryParcelPriceTest()
        {
            try
            {
                bool isExist = false;
                while (!isExist)
                {
                    try
                    {
                        ShoppingCart.GetText(ShoppingCart.TextView.TotalCost);
                        isExist = true;
                        
                    }
                    catch (Exception)
                    {

                        ShoppingCart.SwipeProductList(ShoppingCart.Direction.Up, 1);
                    }
                }
                ShoppingCart.VerifyDeliveryParcelPrice();
                List<CartList> values = ShoppingCart.getCartItems();
                int productCount = values[0].carts[0].entries.Count();
                for (int i = productCount; i > 0; i--)
                {
                    if (i == 0)
                        break;
                    else
                        ShoppingCart.SwipeProductList(ShoppingCart.Direction.Down, 2);
                }
              
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetTextTest1()
        {
            try
            {
                try
                {
                    List<string> productid = ShoppingCart.GetProductNames();
                    ShoppingCart.ContextMenu.Select(productid[0], ShoppingCart.ContextMenu.MenuItem.Info);
                    ShoppingCartItem.GetText(ShoppingCartItem.TextView.CTN);
                    ShoppingCartItem.GetText(ShoppingCartItem.TextView.Individual_Price);
                    ShoppingCartItem.GetText(ShoppingCartItem.TextView.ProductDescription);
                    ShoppingCartItem.GetText(ShoppingCartItem.TextView.ProductOverview);
                    ShoppingCartItem.GetText(ShoppingCartItem.TextView.ShoppingCartItemTitle);
                    ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                }
                catch (Exception)
                {
                    Assert.Fail();
                }
            }
            catch (Exception e)
            {
                Assert.Fail();

            }
        }

        [TestMethod()]
        public void GetRefinedTextTest()
        {
            try
            {
                ShoppingCart.GetRefinedText("USD 12.0");
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void SwipeProductListTest()
        {
            try
            {

                ShoppingCart.SwipeProductList(ShoppingCart.Direction.Up, 1);
                ShoppingCart.SwipeProductList(ShoppingCart.Direction.Down, 2);
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void SwipeQuantityTest()
        {
            try
            {
                List<String> ProdNames = ShoppingCart.GetProductNames();
                ShoppingCart.SwipeQuantity(ShoppingCart.Direction.Down, 3);
                
                

            }
            catch (Exception)
            {

                Assert.Fail();
            }
        }

        [TestMethod()]
        public void _getproductListTest()
        {
            try
            {
                ShoppingCart._getproductList();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }
    }
}
