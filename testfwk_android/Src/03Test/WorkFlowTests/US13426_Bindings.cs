using Philips.CDP.Automation.IAP.Tests.Workflows;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using Philips.SIG.Automation.Android.CDPP.AppFramework_TestPlugin;
using Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TechTalk.SpecFlow;
using System.Threading;

namespace Philips.CDP.Automation.IAP.Tests.Workflows
{
    [Binding]
    public class CoCo_Bindings
    {

        [Then(@"I click on ""(.*)"" from Hamburger Menu List and verify ""(.*)"" screen")]
        public void ThenIClickOnFromHamburgerMenuListAndVerifyScreen(string btnName, string screenName)
        {
            Thread.Sleep(500);
            AppHomeScreen.Click(AppHomeScreen.Button.HamburgerIcon);
            Thread.Sleep(1000);
            HamburgerMenu.Click(btnName);
            Thread.Sleep(5000);
            if (HamburgerMenu.WaitforCoCOScreen(screenName))
            {
                IapReport.Message("App is in " + screenName + " screen");
            }
            else
            {
                IapReport.Fail("App is not in " + screenName + " screen");
            }
        }

        [Then(@"I verify that user can navigate back to Product list view from product detail view")]
        public void ThenIVerifyThatUserCanNavigateBackToProductListViewFromProductDetailView()
        {
            RetailerScreen.Click(RetailerScreen.Button.Retailer_Back);
            try
            {
                bool Screen_Loaded = ProductScreen.WaitforProductScreen(); ;
                if (Screen_Loaded)
                {
                    IapReport.Message("Navigated to Product Catalog page");
                }
                else
                {
                    IapReport.Fail("Error: App not in Product Catalog screen.");
                }

            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed: Product Catalog page not loaded", e);
            }
        }




    }
}

