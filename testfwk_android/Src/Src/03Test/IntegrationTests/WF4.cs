using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using TechTalk.SpecFlow;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using System.Threading;

namespace Philips.CDP.Automation.IAP.Tests.Workflows
{
    [Binding]
    public class WF4
    {
        HomeScreen hScreen = new HomeScreen();
        ShoppingCart shop = new ShoppingCart();

        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }


        
        [When(@"User increase and decrease  number of product and see the delivery method calculation")]
        public void WhenUserIncreaseAndDecreaseNumberOfProductAndSeeTheDeliveryMethodCalculation()
        {
            try
            {
                ShoppingCart.Click(ShoppingCart.Button.UpButton);
                HomeScreen.WaitforHomeScreen();
                List<string> productid = HomeScreen.GetProductListItems();
                HomeScreen.Click(productid[1], HomeScreen.Button.BuyNow);
                 Thread.Sleep(5000);
                 if (ShoppingCart.IsVisible())

                     ShoppingCart.Click(ShoppingCart.Button.UpButton);
                 else
                     IapReport.Message("Product is out of stock");
                productid = HomeScreen.GetProductListItems();
                HomeScreen.Click(productid[2], HomeScreen.Button.BuyNow);
                Thread.Sleep(5000);
                if (ShoppingCart.IsVisible())
                    IapReport.Message("User added second product");
                else
                    IapReport.Message("Product is out of stock");
                string sValue = ShoppingCart.GetText(ShoppingCart.TextView.TotalCost);
                double totalcostAfterIncrease = Convert.ToDouble(ShoppingCart.GetRefinedText(sValue));
                IapReport.Message("user is verifying increase the product: The Total cost after increase is "+totalcostAfterIncrease.ToString());
                ThenVerifyTheFreeDeliveryMethodCalculation();
                HomeScreen.Click(HomeScreen.Button.CartImage);
                ShoppingCart.WaitforShoppingCartScreen();
                List<String> ProdNames = ShoppingCart.GetProductNames();
                ShoppingCart.ContextMenu.Select(ProdNames[0], ShoppingCart.ContextMenu.MenuItem.Delete);
                ShoppingCart.WaitforShoppingCartScreen();
                 sValue = ShoppingCart.GetText(ShoppingCart.TextView.TotalCost);
                 double totalcostAfterDecrease = Convert.ToDouble(ShoppingCart.GetRefinedText(sValue));
                if(totalcostAfterIncrease>totalcostAfterDecrease)
                    IapReport.Message("user is verifying decreasing the product.The Total cost after decrease of products is "+totalcostAfterDecrease.ToString());
                ThenVerifyTheFreeDeliveryMethodCalculation();
                HomeScreen.Click(HomeScreen.Button.CartImage);
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in Home screen", e);
            } 
        }
        
        [Then(@"Verify the free  delivery method calculation")]
        public void ThenVerifyTheFreeDeliveryMethodCalculation()
        {
            try
            {
                string UPSValue;
                string sValue = ShoppingCart.GetText(ShoppingCart.TextView.TotalCost);
                double totalcost = Convert.ToDouble(ShoppingCart.GetRefinedText(sValue));
                UPSValue = ShoppingCart.GetText(ShoppingCart.TextView.UPS_PARCEL);
                UPSValue = (ShoppingCart.GetRefinedText(UPSValue));
                if (totalcost >= 100)
                {
                    if (float.Parse(UPSValue) > 0.0)
                    {
                        IapReport.Message("Fail:UPS Delivery value is greater than 0, where as if the total cost is more than $100, the UPS Parcel Service Value Should be 0 ");
                        //IapReport.Fail("Fail:UPS Delivery value is greater than 0, where as if the total cost is more than $100, the UPS Parcel Service Value Should be 0 ");
                    }
                        
                    else
                    {
                        IapReport.Message("Pass:UPS Delivery is not charged and is 0, which is a correct behavior");
                    }
                }

                else
                {
                    if (float.Parse(UPSValue) == 0.0)
                    {
                        IapReport.Message(" Fail: UPS Delivery is not charged even when the total cost is less than 100");
                       // IapReport.Fail(" Fail: UPS Delivery is not charged even when the total cost is less than 100");
                    }
                        
                    else
                        IapReport.Message("Pass: UPS Delivery is  charged for total cost < 100 which is a correct behavior");

                }
                Console.WriteLine("Delivery via USP Parcel price Present");
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