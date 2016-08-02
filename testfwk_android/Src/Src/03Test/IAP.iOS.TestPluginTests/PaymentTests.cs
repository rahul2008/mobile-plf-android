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
    public class PaymentTests
    {
        static int _instance = 0;
        static List<Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS.ShippingAddress.Shipping_Address> addrList = null;

        public PaymentTests()
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
                    if (Address.IsEnabled(Address.EditText.FirstName))
                        Address.Click(Address.Button.Switch);
                    Address.Click(Address.Button.Continue);
                    Summary.WaitforSummaryScreen();
                    Summary.Click(Summary.Button.Pay_Now);
                    Thread.Sleep(3000);
                    Payment.WaitforPaymentScreen();
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
                Payment.EnterText(Payment.EditBox.CardNumber, "4444333322221111");
                Payment.EnterText(Payment.EditBox.SecurityCode, "651");
                Payment.Select(Payment.DropDown.ExpiryMonth, "09");
                Payment.Select(Payment.DropDown.ExpiryYear, "2017");
                Payment.Click(Payment.Button.MakePayment);
                Confirmation.WaitforConfirmationScreen();
                Confirmation.Click(Confirmation.Button.OK);
                HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(HomeScreen.Button.ShopNow);
                ProductScreen.WaitforProductScreen();
                ProductScreen.SelectProduct(ProductScreen._getListOfProducts()[2].name);
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
                Summary.Click(Summary.Button.Pay_Now);
                Thread.Sleep(3000);
                Payment.WaitforPaymentScreen();
                Payment.Click(Payment.Button.CancelPayment);
                CancelPayment.Click(CancelPayment.Button.Cancel);
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
                addrList = ShippingAddress.GetListofAddress();
                ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.DeliverToThisAddr);
                Address.WaitforAddressScreen();
                if (Address.IsEnabled(Address.EditText.FirstName))
                    Address.Click(Address.Button.Switch);
                Address.Click(Address.Button.Continue);
                Summary.WaitforSummaryScreen();
                Summary.Click(Summary.Button.Pay_Now);
                Thread.Sleep(3000);
                Payment.WaitforPaymentScreen();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void EnterTextTest()
        {
            try
            {
                //Payment.EnterText(Payment.EditBox.CardHolderName,"Ananya");
                Payment.EnterText(Payment.EditBox.CardNumber,"4444333322221111");
                Payment.EnterText(Payment.EditBox.SecurityCode,"135");
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
                Payment.Select(Payment.DropDown.ExpiryMonth,"9");
                Payment.Select(Payment.DropDown.ExpiryYear, "2017");
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
                string str = Payment.GetPageTitle();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void WaitforPaymentScreenTest()
        {
            try
            {
                Payment.WaitforPaymentScreen();
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
                string str = Payment.GetHeader();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void IsEnabledTest()
        {
            try
            {
                Payment.IsEnabled(Payment.Button.MakePayment);
                Payment.IsEnabled(Payment.Button.CancelPayment);
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void EnterPaymentDetailsTest()
        {
            try
            {
                Payment.EnterPaymentDetails("4444333322221111","12","2017","123");
                Confirmation.WaitforConfirmationScreen();
                Confirmation.Click(Confirmation.Button.OK);
                HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(HomeScreen.Button.ShopNow);
                ProductScreen.WaitforProductScreen();
                ProductScreen.SelectProduct(ProductScreen._getListOfProducts()[2].name);
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
                Summary.Click(Summary.Button.Pay_Now);
                Thread.Sleep(3000);
                Payment.WaitforPaymentScreen();
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
                Payment.Click(Payment.Button.CancelPayment);
                CancelPayment.Click(CancelPayment.Button.Cancel);
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
                Summary.Click(Summary.Button.Pay_Now);
                Thread.Sleep(3000);
                Payment.WaitforPaymentScreen();
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
                Payment.WaitforPaymentScreen();
                Payment.Click(Payment.Button.CancelPayment);
                bool b = CancelPayment.IsExist();
                CancelPayment.Click(CancelPayment.Button.Cancel);
                Payment.WaitforPaymentScreen();
            }
            catch(Exception)
            {
                Assert.Fail();
            }
        }
    }
}
