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
    public class ConfirmationTests
    {
        static int _instance = 0;

        public ConfirmationTests()
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
                    Payment.EnterPaymentDetails("4444333322221111", "12", "2017", "123");
                    Confirmation.WaitforConfirmationScreen();
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
                Confirmation.Click(Confirmation.Button.OK);
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
                string str = Confirmation.GetText(Confirmation.TextView.Confirm_Email);
                str = Confirmation.GetText(Confirmation.TextView.OrderNumber);
                str = Confirmation.GetText(Confirmation.TextView.ThankYou);
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void WaitforConfirmationScreenTest()
        {
            try
            {
                Confirmation.WaitforConfirmationScreen();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }
    }
}
