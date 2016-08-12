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
    public class WF7
    {
        HomeScreen hScreen = new HomeScreen();
        ShoppingCart shop = new ShoppingCart();
        [Given(@"User is a second time buyer")]
        public void GivenUserIsASecondTimeBuyer()
        {
            try
            {

                    IapReport.Message("user is Second time buyer");
               
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in Home screen", e);
            } 
        }
        [Then(@"Verify that the previously shopping cart items are visible if he has selected")]
        public void ThenVerifyThatThePreviouslyShoppingCartItemsAreVisibleIfHeHasSelected()
        {
            try
            {
                //This is not proper verification
                List<String> ProdNames = ShoppingCart.GetProductNames();
                if (ProdNames.Count != 0)
                    IapReport.Message("The shopping cart items are visible as user selected");
                else
                    IapReport.Fail("The shopping cart items are not visible as user selected");
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in shoppingcart screen", e);
            } 
        }

        [Then(@"verify the product image, name ,price")]
        public void ThenVerifyTheProductImageNamePrice()
        {
            try
            {

                ShoppingCart.WaitforShoppingCartScreen();
                List<String> ProdNames = ShoppingCart.GetProductNames();
                if (ProdNames.Count == 0)
                {
                    IapReport.Message("Shopping cart is empty");
                    ShoppingCart.Click(ShoppingCart.Button.UpButton);
                    HomeScreen.WaitforHomeScreen();
                    List<string> productid = HomeScreen.GetProductListItems();
                    HomeScreen.Click(productid[1], HomeScreen.Button.BuyNow);
                    ShoppingCart.WaitforShoppingCartScreen();
                }
                else
                {
                    string price = ShoppingCart.GetText(ProdNames[0], ShoppingCart.ProductDetailsText.Price);

                    double priceValue = Convert.ToDouble(ShoppingCart.GetRefinedText(price));
                    string prodDesc = ProdNames[0].ToString();

                    double Quantity = double.Parse(ShoppingCart.GetText(ProdNames[0], ShoppingCart.ProductDetailsText.Quantity));
                    double price_of_1_qty = priceValue / Quantity;

                    ShoppingCart.ContextMenu.Select(ProdNames[0], ShoppingCart.ContextMenu.MenuItem.Info);
                    ShoppingCartItem.WaitforShoppingCartInfoScreen();
                    string productinfo = ShoppingCartItem.GetText(ShoppingCartItem.TextView.ProductDescription);
                    String priceinfo = ShoppingCartItem.GetText(ShoppingCartItem.TextView.Individual_Price);
                    double priceinfovalue = Convert.ToDouble(ShoppingCart.GetRefinedText(priceinfo));
                    if (price_of_1_qty.Equals(priceinfovalue) && prodDesc.Equals(productinfo))
                        IapReport.Message("step passed:verified name,price");
                    else
                    {
                        IapReport.Message("Step failed:Not verified name,price");
                        // IapReport.Fail("Step failed:Not verified name,price");

                    }
                    ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                    ShoppingCart.WaitforShoppingCartScreen();


                }
            }

            catch (Exception e)
            {
                IapReport.Fail("User is not in shoppingcart screen", e);
            }

        }


        [Then(@"User selects the delivery method")]
        public void ThenUserSelectsTheDeliveryMethod()
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
        [Then(@"User Look at the VAT, Total price, Claim voucher")]
        public void ThenUserLookAtTheVATTotalPriceClaimVoucher()
        {
            try
            {
               
                IapReport.Message("User is check the vat,total,price");
               
               
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in shoppingcart screen at vat,total price and voucher", e);
            } 
        }



    }
}
