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

namespace Philips.CDP.Automation.IAP.Tests.Workflows
{
    [Binding]
    public class UJ02
    {
        [Then(@"Enter a valid shipping address and click continue")]
        public void ThenEnterAValidShippingAddressAndClickContinue()
        {
            ShippingAddress.WaitforShippingAddressScreen();
            Address.CreateAddress("shipToabhi", "shipToram", Address.ContextMenu.MenuItem.Mr, "Manyata Tech Park", "Banagalore", "35007", "US", false, "9876543211");
            Address.Click(Address.Button.Continue);
        }

        [When(@"billing address is different from shipping address")]
        public void WhenBillingAddressIsDifferentFromShippingAddress()
        {
            Address.WaitforAddressScreen();
            Address.Click(Address.Button.Switch);            
        }

        [Then(@"enter valid billing address and click continue")]
        public void ThenEnterValidBillingAddressAndClickContinue()
        {
            Address.CreateAddress("billToabhi", "billToram", Address.ContextMenu.MenuItem.Mr, "Manyata Tech Park", "Banagalore", "35007", "US", false, "9876543210");
            Address.Click(Address.Button.Continue);
        }

        [Then(@"verify user is on payment screen")]
        public void ThenVerifyUserIsOnPaymentScreen()
        {
            Payment.WaitforPaymentScreen();
        }

        [When(@"Invalid card data is entered")]
        public void WhenInvalidCardDataIsEntered()
        {
            Payment.EnterPaymentDetails("444433332222111", "08", "2020", "651");
        }

        [Then(@"verify user gets a failed message")]
        public void ThenVerifyUserGetsAFailedMessage()
        {
            List<String> vErrors = Payment.FindValidationErrors();
            if (vErrors.Contains("Enter a valid card number"))
                IapReport.Message("Pass: invalid card number error");
            else
                IapReport.Message("Fail: invalid card number not detected");
        }

        [When(@"Valid card data is entered")]
        public void WhenValidCardDataIsEntered()
        {
            Payment.Back();
            Thread.Sleep(3000);
            Summary.Click(Summary.Button.Pay_Now);
            Payment.EnterPaymentDetails("4444333322221111", "08", "2020", "651");
            Payment.Click(Payment.Button.MakePayment);
            Thread.Sleep(5000);
        }

        [Then(@"Verify user is able to complete transaction")]
        public void ThenVerifyUserIsAbleToCompleteTransaction()
        {
            Confirmation.Click(Confirmation.Button.OK);
            ProductScreen.WaitforProductScreen();
        }

    }

}
