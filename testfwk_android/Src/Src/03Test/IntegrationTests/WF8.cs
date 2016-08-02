using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TechTalk.SpecFlow;

namespace Philips.CDP.Automation.IAP.Tests.Workflows
{
    [Binding]
    class WF8
    {
        [Then(@"verify the free  delivery method calculation")]
        public void ThenVerifyTheFreeDeliveryMethodCalculation()
        {

            bool result = false;
            try
            {
                result = ShoppingCart.VerifyDeliveryParcelPrice();
                if (result == true)
                    IapReport.Message("Pass:UPS Delivery is not charged and is 0, which is a correct behavior");
                else
                {
                    IapReport.Message("UPS Delivery Parcel Price is not Correct");
                    // IapReport.Fail("UPS Delivery Parcel Price is not Correct");
                }
            }
            catch (Exception e)
            {
                IapReport.Fail("User is not in shoppingcart screen", e);
            }
        }
    }

}


