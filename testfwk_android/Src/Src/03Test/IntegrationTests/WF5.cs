using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Threading;
using TechTalk.SpecFlow;
using System.Collections.Generic;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.CDP.Automation.IAP.Tests.Workflows;

namespace Philips.CDP.Automation.IAP.Tests.Regression
{
    [Binding]
    public class WF5
    {
        ShoppingCart shop = new ShoppingCart();
        HomeScreen hScreen = new HomeScreen();
        [Then(@"User applies invalid voucher option")]
        public void ThenUserAppliesInvalidVoucherOption()
        {
            try
            {
                IapReport.Message("Not yet implemented");
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in shoppingcart screen", e);
            } 

        }


        [Then(@"Verify that the voucher can be applied only once and not multiple times \.")]
        public void ThenVerifyThatTheVoucherCanBeAppliedOnlyOnceAndNotMultipleTimes_()
        {
            try
            {
                IapReport.Message("Not yet implemented");
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in shoppingcart screen", e);
            } 
        }

        [Then(@"Verify the VAT calculation")]
        public void ThenVerifyTheVATCalculation()
        {
            try
            {
                string VatValue = ShoppingCart.GetText(ShoppingCart.TextView.VAT);
                string vat = ShoppingCart.GetRefinedText(VatValue);
                IapReport.Message(vat);
 
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in shoppingcart screen", e);
            } 
        }


        [Then(@"Verify the user is able to revoke the voucher")]
        public void ThenVerifyTheUserIsAbleToRevokeTheVoucher()
        {
            try
            {
                IapReport.Message("Not yet implemented");

                ShoppingCart.Click(ShoppingCart.Button.UpButton);
                HomeScreen.WaitforHomeScreen();
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in shoppingcart screen", e);
            } 
        }

    }
}
