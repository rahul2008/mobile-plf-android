using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Threading;
namespace Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS.Tests
{
    [TestClass()]
    public class ProductScreenTests
    {
        static int instance = 0;
        public ProductScreenTests()
        {
            if (instance == 0)
            {
                IAPConfiguration.LoadiOSConfiguration();
                HomeScreen.WaitforHomeScreen();
                HomeScreen.SelectCountry("US");
                Thread.Sleep(3000);
                HomeScreen.Click(HomeScreen.Button.ShopNow);
                ProductScreen.WaitforProductScreen();
            }
            instance++;
        }

        [TestMethod()]
        public void SelectProductTest()
        {
            try
            {
                ProductScreen.SelectProduct(ProductScreen._getListOfProducts()[0].name);
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
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
                ProductScreen.Click(ProductScreen.Button.Back);
                //Thread.sleep(2000);
                HomeScreen.Click(HomeScreen.Button.ShopNow);
                ProductScreen.WaitforProductScreen();
                ProductScreen.Click(ProductScreen.Button.CartImage);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.UpButton);
                ProductScreen.SelectProduct(ProductScreen._getListOfProducts()[0].name);
                ShoppingCartItem.WaitforShoppingCartInfoScreen();
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
            }
            catch (Exception)
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
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void _getListOfProductsTest()
        {
            try
            {
                List<ProductScreen.Product> products = ProductScreen._getListOfProducts();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void swipeForiOSTest()
        {
            try
            {
                ProductScreen.SwipeForiOS(ProductScreen.Direction.up, 2);
            }
            catch(Exception)
            {
                Assert.Fail();
            }
        }
    }
}
