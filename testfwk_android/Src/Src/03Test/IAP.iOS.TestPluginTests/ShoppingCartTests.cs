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
    public class ShoppingCartTests_iOS
    {
        static int instance = 0;
        public ShoppingCartTests_iOS()
        {
            if (instance == 0)
            {
                IAPConfiguration.LoadConfigurationForiOS();
                HomeScreen.WaitforHomeScreen();
                HomeScreen.SelectCountry("US");
                HomeScreen.WaitforHomeScreen();
                Thread.Sleep(3000);
                HomeScreen.Click(HomeScreen.Button.ShopNow);
                ProductScreen.WaitforProductScreen();
                ProductScreen.SelectProduct(ProductScreen._getListOfProducts()[1].name);
                ShoppingCartItem.Click(ShoppingCartItem.Button.AddToCart);
                //if out of stok, click ok in the popup
                //UIAApplication[1]/UIAWindow[1]/UIAStaticText[2]
                //UIAApplication[1]/UIAWindow[1]/UIAButton[4]

                ShoppingCart.WaitforShoppingCartScreen();
            }
            instance++;
        }

        [TestMethod()]
        public void VerifyDeliveryParcelPriceTest()
        {
            try
            {
                ShoppingCart.VerifyDeliveryParcelPrice();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetTextTest()
        {
            try
            {
                string str = ShoppingCart.GetText(ShoppingCart.TextView.TotalCost);
                str = ShoppingCart.GetText(ShoppingCart.TextView.TotalItems);
                str = ShoppingCart.GetText(ShoppingCart.TextView.UPS_Parcel);
                str = ShoppingCart.GetText(ShoppingCart.TextView.Tax);
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetTextTest1()
        {
            try
            {
                string temp = ShoppingCart.GetText(ShoppingCart._getproductList()[0].Prod_Description, ShoppingCart.ProductDetailsText.Price);
                temp = ShoppingCart.GetText(ShoppingCart._getproductList()[0].Prod_Description, ShoppingCart.ProductDetailsText.Quantity);
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetRefinedTextTest()
        {
            try
            {
                string val = ShoppingCart.GetRefinedText("$102");
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
                ShoppingCart.Click(ShoppingCart.Button.ContinueShopping);
                ProductScreen.WaitforProductScreen();
                ProductScreen.SelectProduct(ProductScreen._getListOfProducts()[0].name);
                ShoppingCartItem.WaitforShoppingCartInfoScreen();
                ShoppingCartItem.Click(ShoppingCartItem.Button.AddToCart);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.ClickCartItem(ShoppingCart._getproductList()[0].Prod_Description);
                ShoppingCartItem.WaitforShoppingCartInfoScreen();
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.Checkout);
                ShippingAddress.WaitforShippingAddressScreen();
                ShippingAddress.Click(ShippingAddress.Button.BackButton);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.UpButton);
                ShoppingCartItem.Click(ShoppingCartItem.Button.AddToCart);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.ClickCartItem(ShoppingCart._getproductList()[0].Prod_Description);
                ShoppingCartItem.Click(ShoppingCartItem.Button.Delete);
                ShoppingCartItem.Click(ShoppingCartItem.Button.Ok);
                ShoppingCart.WaitforShoppingCartScreen();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void SwipeShoppingCartItemTest()
        {
            try
            {
                ShoppingCart.SwipeShoppingCartItem(ShoppingCart._getproductList()[0].Prod_Description, ShoppingCart.Direction.Left, 2);
                ShoppingCartItem.Click(ShoppingCartItem.Button.DeleteCartItem);
            }
            catch (Exception ex)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void SwipeShoppingCartListTest()
        {
            try
            {
                ShoppingCart.SwipeShoppingCartList(ShoppingCart.Direction.Up, 2);
            }
            catch (Exception ex)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void IsEnabledTest()
        {
            try
            {
                bool b = ShoppingCart.IsEnabled(ShoppingCart.Button.Checkout);
                b = ShoppingCart.IsEnabled(ShoppingCart.Button.ContinueShopping);
                if(ShoppingCart.GetProductNames().Count == 0)
                    b = ShoppingCart.IsEnabled(ShoppingCart.Button.ContinueShopping_1);
                b = ShoppingCart.IsEnabled(ShoppingCart.Button.UpButton);
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
                string val = ShoppingCart.GetScreenTitle();
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
                ShoppingCart.Select(ShoppingCart._getproductList()[0].Prod_Description, 5);
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void WaitforShoppingCartScreenTest()
        {
            try
            {
                ShoppingCart.WaitforShoppingCartScreen();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void IsProductExistTest()
        {
            try
            {
                ShoppingCart.IsProductExist(ShoppingCart.GetProductNames()[0]);
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
               List <string> str = ShoppingCart.GetProductNames();
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
               List<ShoppingCart.ProductDetails> pDetails = ShoppingCart._getproductList();
            }
            catch (Exception)
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
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void IsVisibleTest1()
        {
            try
            {
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.ClickCartItem(ShoppingCart._getproductList()[0].Prod_Description);
                ShoppingCartItem.IsVisible();
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetTextTest2()
        {
            try
            {
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.UpButton);
                ShoppingCartItem.WaitforShoppingCartInfoScreen();
                string str = ShoppingCartItem.GetText(ShoppingCartItem.TextView.CTN);
                str = ShoppingCartItem.GetText(ShoppingCartItem.TextView.NewPrice);
                str = ShoppingCartItem.GetText(ShoppingCartItem.TextView.OriginalPrice);
                str = ShoppingCartItem.GetText(ShoppingCartItem.TextView.ProductDescription);
                str = ShoppingCartItem.GetText(ShoppingCartItem.TextView.ProductOverview);
                ShoppingCartItem.Click(ShoppingCartItem.Button.AddToCart);
                Thread.Sleep(3000);
                ShoppingCart.ClickCartItem(ShoppingCart.GetProductNames()[0]);
                str = ShoppingCartItem.GetText(ShoppingCartItem.TextView.CTN);
                str = ShoppingCartItem.GetText(ShoppingCartItem.TextView.NewPrice);
                str = ShoppingCartItem.GetText(ShoppingCartItem.TextView.OriginalPrice);
                str = ShoppingCartItem.GetText(ShoppingCartItem.TextView.ProductDescription);
                str = ShoppingCartItem.GetText(ShoppingCartItem.TextView.ProductOverview);
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void ClickCartItemTest()
        {
            try
            {
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.ClickCartItem(ShoppingCart._getproductList()[0].Prod_Description);
                ShoppingCartItem.WaitforShoppingCartInfoScreen();
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }
    }
}
