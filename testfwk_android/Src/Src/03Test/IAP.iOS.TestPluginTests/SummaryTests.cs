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
    public class SummaryTests
    {
        static int _instance = 0;

        public SummaryTests()
        {
            if (_instance == 0)
            {
                try
                {
                    IAPConfiguration.LoadiOSConfiguration();
                    HomeScreen.WaitforHomeScreen();
                    HomeScreen.Click(HomeScreen.Button.ShopNow);
                    ProductScreen.WaitforProductScreen();
                    ProductScreen.SelectProduct(ProductScreen._getListOfProducts()[0].name);
                    ShoppingCartItem.WaitforShoppingCartInfoScreen();
                    ShoppingCartItem.Click(ShoppingCartItem.Button.AddToCart);
                    ShoppingCart.WaitforShoppingCartScreen();
                    ShoppingCart.Click(ShoppingCart.Button.Checkout);
                    ShippingAddress.WaitforShippingAddressScreen();
                    List<Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS.ShippingAddress.Shipping_Address> addrList = ShippingAddress.GetListofAddress();
                    ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.DeliverToThisAddr);
                    Address.WaitforAddressScreen();
                    if(Address.IsEnabled(Address.EditText.FirstName))
                        Address.Click(Address.Button.Switch);
                    Address.Click(Address.Button.Continue);
                    Summary.WaitforSummaryScreen();
                }
                catch (Exception e)
                {
                    Assert.Fail();
                }
            }
            _instance++;

        }
       
        [TestMethod()]
        public void ClickTest()
        {
            try
            {
                Summary.Click(Summary.Button.Pay_Now);
                Thread.Sleep(3000);
                Payment.WaitforPaymentScreen();
                Payment.Click(Payment.Button.CancelPayment);
                CancelPayment.Click(CancelPayment.Button.Ok);
                HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(HomeScreen.Button.ShopNow);
                ProductScreen.WaitforProductScreen();
                ProductScreen.SelectProduct(ProductScreen._getListOfProducts()[0].name);
                ShoppingCartItem.WaitforShoppingCartInfoScreen();
                ShoppingCartItem.Click(ShoppingCartItem.Button.AddToCart);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.Checkout);
                ShippingAddress.WaitforShippingAddressScreen();
                List<Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS.ShippingAddress.Shipping_Address> addrList = ShippingAddress.GetListofAddress();
                ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.DeliverToThisAddr);
                Address.WaitforAddressScreen();
                if (Address.IsEnabled(Address.EditText.FirstName))
                    Address.Click(Address.Button.Switch);
                Address.Click(Address.Button.Continue);
                Summary.WaitforSummaryScreen();
                Summary.Click(Summary.Button.Cancel);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.Checkout);
                ShippingAddress.WaitforShippingAddressScreen();
                addrList = ShippingAddress.GetListofAddress();
                ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.DeliverToThisAddr);
                Address.WaitforAddressScreen();
                if (Address.IsEnabled(Address.EditText.FirstName))
                    Address.Click(Address.Button.Switch);
                Address.Click(Address.Button.Continue);
                Summary.WaitforSummaryScreen();
                Summary.Click(Summary.Button.UpButton);
                Address.WaitforAddressScreen();
                if (Address.IsEnabled(Address.EditText.FirstName))
                    Address.Click(Address.Button.Switch);
                Address.Click(Address.Button.Continue);
                Summary.WaitforSummaryScreen();
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
                Summary.GetText();
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
                string str = Summary.GetText(Summary.Text.Billing_Address);
                str = Summary.GetText(Summary.Text.Shipping_Address);
                str = Summary.GetText(Summary.Text.TotalPrice);
                str = Summary.GetText(Summary.Text.TotalLabel);
                str = Summary.GetText(Summary.Text.DeliveryPrice);
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void WaitforSummaryScreenTest()
        {
            try
            {
                Summary.WaitforSummaryScreen();
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
                string str = Summary.GetScreenTitle();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void IsExistTest()
        {
            try
            {
                bool b = Summary.IsExist(Summary.Text.Billing_Address);
                b = b & Summary.IsExist(Summary.Text.Shipping_Address);
                b = b & Summary.IsExist(Summary.Text.DeliveryPrice);
                b = b & Summary.IsExist(Summary.Text.TotalLabel);
                b = b & Summary.IsExist(Summary.Text.TotalPrice);
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }
    }
}
