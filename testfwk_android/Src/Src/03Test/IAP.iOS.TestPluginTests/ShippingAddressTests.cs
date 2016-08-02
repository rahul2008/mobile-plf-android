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
    public class ShippingAddressTests
    {
        static int _instance = 0;

        public ShippingAddressTests()
        {
            if (_instance == 0)
            {
                try
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
                    ShoppingCart.Click(ShoppingCart.Button.Checkout);
                    ShippingAddress.WaitforShippingAddressScreen();
                }
                catch (Exception e)
                {
                    Assert.Fail();
                }
            }
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
                ShippingAddress.WaitforShippingAddressScreen();
                ShippingAddress.Click(ShippingAddress.Button.BackButton);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.Checkout);
                ShippingAddress.WaitforShippingAddressScreen();
                ShippingAddress.Click(ShippingAddress.Button.Cancel);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.Checkout);
                ShippingAddress.WaitforShippingAddressScreen();
            }
            catch(Exception){
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetPageTitleTest()
        {
            try
            {
                string s = ShippingAddress.GetPageTitle();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetHeaderTest()
        {
            try
            {
                ShippingAddress.GetHeader();
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
                List<ShippingAddress.Shipping_Address> addrList = ShippingAddress.GetListofAddress();
            }
            catch(Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void SelectAddressTest()
        {
            try
            {
                ShippingAddress.SelectAddress(ShippingAddress.GetListofAddress()[2].completeAddress);
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void ClickTest1()
        {
            try
            {
                ShippingAddress.WaitforShippingAddressScreen();
                ShippingAddress.Click(ShippingAddress.GetListofAddress()[0].completeAddress, ShippingAddress.Button.AddNewAddr);
                Address.WaitforAddressScreen();
                Address.Click(Address.Button.UpButton);
                ShippingAddress.Click(ShippingAddress.GetListofAddress()[0].completeAddress, ShippingAddress.Button.DeliverToThisAddr);
                Address.WaitforAddressScreen();
                Address.Click(Address.Button.UpButton); 
                ShippingAddress.Click(ShippingAddress.GetListofAddress()[0].completeAddress, ShippingAddress.Button.Dots);
                Address.WaitforAddressScreen();
                Address.Click(Address.Button.UpButton);
                ShippingAddress.WaitforShippingAddressScreen();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }
    }
}
