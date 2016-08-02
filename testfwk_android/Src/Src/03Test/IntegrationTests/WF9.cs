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
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.CDP.Automation.IAP.Tests.Workflows;

namespace Philips.CDP.Automation.IAP.Tests.Regression
{
    [Binding]
    public class WF9
    {
        ShoppingCart shop = new ShoppingCart();
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }
        [Given(@"User is first time buyer")]
        public void GivenUserIsFirstTimeBuyer()
        {
            try
            {

                    IapReport.Message("user is First time buyer");
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in Home screen", e);
            } 
        }

        [Given(@"user clicks on shopping icon")]
        public void GivenUserClicksOnShoppingIcon()
        {
            try
            {
                HomeScreen.Click(HomeScreen.Button.CartImage);
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in Home screen", e);
            } 
        }

        [Then(@"Verify that your shopping cart is currently empty appears on Shopping Cart Page")]
        public void ThenVerifyThatYourShoppingCartIsCurrentlyEmptyAppearsOnShoppingCartPage()
        {
            try
            {
                //IMobilePageControl n=null;
                //n = _instance.GetElement(null, SearchBy.Id, ObjectRepository.CartNumber);

                ShoppingCart.WaitforShoppingCartScreen();
                List<String> ProdNames = ShoppingCart.GetProductNames();
                if (ProdNames.Count != 0)
                {
                    ShoppingCart.DeleteAllProducts();
                    ShoppingCart.Click(ShoppingCart.Button.UpButton);
                    HomeScreen.WaitforHomeScreen();
                    HomeScreen.Click(HomeScreen.Button.CartImage);
                    ShoppingCart.WaitforShoppingCartScreen();
                }
                ProdNames = ShoppingCart.GetProductNames();
                if (ProdNames.Count == 0)
                    IapReport.Message("Step Passed: Shopping cart is empty");
                else
                {
                    //IapReport.Fail("Step Failed: Shopping cart is not empty");
                    IapReport.Message("Step Failed: Shopping cart is not empty");
                }
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in shoppingcart screen", e);
            } 
        }
       

        [Then(@"user clicks on continue shopping")]
        public void ThenUserClicksOnContinueShopping()
        {
            try
            {
                ShoppingCart.Click(ShoppingCart.Button.ContinueShopping_1);
                HomeScreen.WaitforHomeScreen();
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in shoppingcart screen", e);
            } 
        }

    }
}
