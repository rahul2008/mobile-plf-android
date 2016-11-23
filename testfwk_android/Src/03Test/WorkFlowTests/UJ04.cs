using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Threading;
using TechTalk.SpecFlow;
using System.Collections.Generic;
using Philips.H2H.Foundation.AutomationCore.Interface;
using System.Configuration;
using Philips.CDP.Automation.IAP.Tests.Workflows;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using System.Xml;
using System.Reflection;
using System.IO;

namespace Philips.CDP.Automation.IAP.Tests.Workflows
{
    [Binding]
    public class UJ04
    {
        HomeScreen hScreen = new HomeScreen();

        ShoppingCart shop = new ShoppingCart();

        ShippingAddress ship = new ShippingAddress();

        Address address = new Address();

        Summary summary = new Summary();

        Confirmation confirmation = new Confirmation();

        public static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();

        [Given(@"User is a first time user")]
        public void GivenUserIsAFirstTimeUser()
        {
        }

        [Then(@"Verify user is in address screen")]
        public void ThenVerifyUserIsInAddressScreen()
        {
            Address.WaitforAddressScreen();
        }

        [Then(@"select add a new address")]
        public void ThenSelectAddANewAddress()
        {
            Address.Click(Address.Button.Add_a_new_Address);
        }

        [When(@"user clicks on checkout")]
        public void WhenUserClicksOnCheckout()
        {
            ShoppingCart.WaitforShoppingCartScreen();
            ShoppingCart.Click(ShoppingCart.Button.Checkout);
            ShippingAddress.WaitforShippingAddressScreen();
        }

        [Then(@"Verify user is in shopping cart view")]
        public void ThenVerifyUserIsInShoppingCartView()
        {
            ShoppingCart.WaitforShoppingCartScreen();
        }


        [Then(@"enter valid shipping address and click cancel")]
        public void ThenEnterValidShippingAddressAndClickCancel()
        {
            Address.CreateAddress("abhi", "ram", Address.ContextMenu.MenuItem.Mr, "Manyata Tech Park", "Banagalore", "35007", "US", false, "9876543210");
            Thread.Sleep(3000);
            Address.Click(Address.Button.Cancel);
            Thread.Sleep(3000);
            ShoppingCart.WaitforShoppingCartScreen();
        }

        [When(@"billing address is same as shipping address, click on continue")]
        public void WhenBillingAddressIsSameAsShippingAddressClickOnContinue()
        {
            ShippingAddress.WaitforShippingAddressScreen();
            if(ShippingAddress.GetBillingAddress().Equals("Same as shipping address"))
            {
                Logger.Info("Pass: Billing address defaulted to shipping address");
            }
            else
            {
                Logger.Info("Fail: Billing address not defaulted to shipping address");
                Address.Click(Address.Button.Switch);
            }
            Address.Click(Address.Button.Continue);
        }

        [Then(@"Verify user is in order summary page")]
        public void ThenVerifyUserIsInOrderSummaryPage()
        {
            Summary.WaitforSummaryScreen();
        }

        [When(@"user clicks on pay now")]
        public void WhenUserClicksOnPayNow()
        {
            Summary.Click(Summary.Button.Pay_Now);
            Payment.WaitforPaymentScreen();
        }

        [Then(@"enter card details that would fail")]
        public void ThenEnterCardDetailsThatWouldFail()
        {
            Thread.Sleep(3000);
            Payment.EnterPaymentDetails("4444222233331111", "08", "2020", "651");
            Payment.Click(Payment.Button.MakePayment);
        }


        [Then(@"Card payment was not successful")]
        public void ThenCardPaymentWasNotSuccessful()
        {
            Payment.LogSource();
            Confirmation.WaitforConfirmationScreen();
            String paymentStatus = Confirmation.GetText(Confirmation.TextView.ThankYou);
            if (paymentStatus.Equals("Payment failed"))
            {
                Logger.Info("Pass: payment failed with bad cc");
            }
            else Logger.Info("Failed: payment did not fail with bad cc: " + paymentStatus);
        }

        [When(@"user selects back")]
        public void WhenUserSelectsBack()
        {
            Confirmation.Back();
            Thread.Sleep(3000);
            Confirmation.Click(Confirmation.Button.CONTINUESHOPPING_OK);
            Thread.Sleep(10000);
        }

        [Then(@"enter valid shipping address and click continue")]
        public void ThenEnterValidShippingAddressAndClickContinue()
        {
            Address.CreateAddress("abhi", "ram", Address.ContextMenu.MenuItem.Mr, "Manyata Tech Park", "Banagalore", "35007", "US", false, "9876543210");
            Address.Click(Address.Button.Continue);
        }
    }
}
