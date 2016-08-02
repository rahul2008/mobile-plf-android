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
    public class UJ05
    {

        HomeScreen hScreen = new HomeScreen();

        ShoppingCart shop = new ShoppingCart();

        ShippingAddress ship = new ShippingAddress();

        Address address = new Address();

        Summary summary = new Summary();

        Confirmation confirmation = new Confirmation();

        [Then(@"Verify that the user is in Select Address screen")]
        public void ThenVerifyThatTheUserIsInSelectAddressScreen()
        {
            ShippingAddress.WaitforShippingAddressScreen();
        }


        [Then(@"verify previous used shipping address is selected")]
        public void ThenVerifyPreviousUsedShippingAddressIsSelected()
        {
            bool isAddressPresent = ShippingAddress.IsEnabled(ShippingAddress.Button.Deliver_to_This_Address);
            Logger.Info("Deliver to this address button is enabled: " +isAddressPresent);
        }

    

        [Then(@"scroll to see other registered shipping addresses")]
        public void ThenScrollToSeeOtherRegisteredShippingAddresses()
        {
            Address.Swipe(Address.Direction.Up);
            Thread.Sleep(3000);


            List<ShippingAddress.Shipping_Address> currentAddressList = ShippingAddress.GetList();

            if (currentAddressList.Count > 1)
            {
                Logger.Info("Pass: More than one address is listed");
            }
            else
            {
                Logger.Info("Fail: More than one addresses is not listed");
            }
        }


        [Then(@"select a different registered shipping address")]
        public void ThenSelectADifferentRegisteredShippingAddress()
        {

            List<IMobilePageControl> radioButtonLst = ShippingAddress._instance.GetElements(H2H.Foundation.AutomationCore.Common.SearchBy.Id, ObjectRepository.RadioButton);
            radioButtonLst[1].Click();
            Thread.Sleep(4000);
            
        }

        [Then(@"verify Select Shipping address is present")]
        public void ThenVerifySelectShippingAddressIsPresent()
        {
            //String selAddStr = ShippingAddress._instance.GetTextById("com.philips.cdp.di.iapdemo:id/tv_select_address ");
            IMobilePageControl selAddStr= ShippingAddress._instance.GetElement(H2H.Foundation.AutomationCore.Common.SearchBy.Id, "com.philips.cdp.di.iapdemo:id/tv_select_address");


            if (selAddStr.Text.Equals("Select address"))
            {
                Logger.Info("Pass: Select address text is displayed");
            }
            else
            {
                Logger.Info("Fail: Select address text is not displayed: "+ selAddStr);
            }
        }



        [Then(@"click on Deliver to this address")]
        public void ThenClickOnDeliverToThisAddress()
        {
            ShippingAddress.WaitforShippingAddressScreen();
            Thread.Sleep(3000);
            ShippingAddress.Click(ShippingAddress.Button.Deliver_to_This_Address);
            Thread.Sleep(5000);
            if (Payment.GetPageTitle().Equals("Payment"))
            {
                Payment.Click(Payment.Button.Use_this_Payment);
            }
            else
            {
                Address.Click(Address.Button.Continue);
            }
            Summary.WaitforSummaryScreen();

        }

        [Then(@"Click on Cancel in Order Summary")]
        public void ThenClickOnCancelInOrderSummary()
        {
            Summary.Click(Summary.Button.Cancel);
            ShoppingCart.WaitforShoppingCartScreen();
        }

    }
}
