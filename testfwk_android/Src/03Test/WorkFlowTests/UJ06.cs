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
    public class UJ06
    {
        HomeScreen hScreen = new HomeScreen();

        ShoppingCart shop = new ShoppingCart();

        ShippingAddress ship = new ShippingAddress();

        Address address = new Address();

        Summary summary = new Summary();

        public static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();

        string Cartnumber = string.Empty;

        [Then(@"verify previous shipping address is selected")]
        public void ThenVerifyPreviousShippingAddressIsSelected()
        {
            ShoppingCart.country = "US";
            ShippingAddress.WaitforShippingAddressScreen();
            //verify there is an address already selected maybe look at the button deliver to this
        }

        [Then(@"verify each address is editable")]
        public void ThenVerifyEachAddressIsEditable()
        {
            ShippingAddress.ContextMenu contextMenu = new ShippingAddress.ContextMenu();
            ShippingAddress.ContextMenu.Select("shipToabhi shipToram", ShippingAddress.ContextMenu.MenuItem.Edit, ShoppingCart.country);
            Address.Click(Address.Button.Cancel);
        }


        /*[Then(@"click on ""(.*)""")]
        public void ThenClickOn(string p0)
        {
            ShippingAddress.WaitforShippingAddressScreen();
            Thread.Sleep(3000);
            ShippingAddress.Click(ShippingAddress.Button.Deliver_to_This_Address);
            Thread.Sleep(5000);
            if(Payment.GetPageTitle().Equals("Payment"))
            {
                Payment.Click(Payment.Button.Use_this_Payment);
            }
            else
            {
                Address.Click(Address.Button.Continue);
            }
            Summary.WaitforSummaryScreen();
        }*/

        

        [Given(@"User is a second time user")]
        public void GivenUserIsASecondTimeUser()
        {
            Logger.Info("coming in as a second time user");
        }

        [Then(@"verify user is able to delete another address")]
        public void ThenVerifyUserIsAbleToDeleteAnotherAddress()
        {
            ShippingAddress.WaitforShippingAddressScreen();
            ShippingAddress.DeleteAddress();
            Logger.Info("Pass: was able to delete one shipping address");
        }

        [Then(@"verify user cannot delete if only one address is present")]
        public void ThenVerifyUserCannotDeleteIfOnlyOneAddressIsPresent()
        {
            ShippingAddress.DeleteAllNonDefaultAddresses();
            if(ShippingAddress.DeleteAddress()==false)
            {
                Logger.Info("Pass: unable to delete last remaining address");
            }
            else
                Logger.Info("Failed: able to delete last remaining address");
        }


        [Given(@"test delete address REST")]
        public void GivenTestDeleteAddressREST()
        {     
            //ShippingAddress.DAA2();
            Product p1 = RestApiInvoker.GetProduct("HX8331/11","US");
            Logger.Info(p1.stock.ToString());
            
        }

        [Then(@"verify max quantity of ""(.*)"" matches hybris")]
        public void ThenVerifyMaxQuantityOfMatchesHybris(string ProductID)
        {
            //get maximum quantity available in hybris and compare
            ProductPRX pFromPRX = RestApiInvoker.GetProductDescriptionFromPRX(ProductID);

            Product p = RestApiInvoker.GetProduct(ProductID, "US");
            int maxStock = p.stock.stockLevel;

            //int maxUI = Int32.Parse(ShoppingCart.SelectMax(pFromPRX.data.productTitle));
            int maxUI = Int32.Parse(ShoppingCart.GetMax(pFromPRX.data.productTitle));
            if (maxUI == maxStock)
                Logger.Info("Pass: max values match: " + maxUI);
            else
                Logger.Info("Failed: max values do not match: ui: " + maxUI + " store: " + maxStock);
        }

        [Then(@"delete ""(.*)"" from cart")]
        public void ThenDeleteFromCart(string ProductDescription)
        {
            //delete product with the given description from the shopping cart
            Summary.Click(Summary.Button.Cancel);
            Thread.Sleep(3000);
            ShoppingCart.WaitforShoppingCartScreen();
            ShoppingCart.ContextMenu.Select(ProductDescription, ShoppingCart.ContextMenu.MenuItem.Delete);
            Thread.Sleep(3000);
        }

        [Then(@"press continue shopping")]
        public void ThenPressContinueShopping()
        {
            ShoppingCart.Click(ShoppingCart.Button.ContinueShopping_1);
            ProductScreen.WaitforProductScreen();
        }

        [Then(@"click on continue")]
        public void ThenClickOnContinue()
        {
            ShippingAddress.Click(ShippingAddress.Button.Continue);
            Summary.WaitforSummaryScreen();
        }

        [Then(@"click cancel and return to shopping cart")]
        public void ThenClickCancelAndReturnToShoppingCart()
        {
            Summary.WaitforSummaryScreen();
            Summary.Click(Summary.Button.Cancel);
            Thread.Sleep(3000);
            ShoppingCart.WaitforShoppingCartScreen();
        }

        [Then(@"add new payment method")]
        public void ThenAddNewPaymentMethod()
        {
            Payment.WaitforPaymentScreen();
            Payment.Click(Payment.Button.Add_new_Payment);
            Thread.Sleep(3000);
        }

        [Then(@"go to shopping cart from product catalog")]
        public void ThenGoToShoppingCartFromProductCatalog()
        {
            ProductScreen.Click(ProductScreen.Button.productScreen_ImageCart);
            ShoppingCart.WaitforShoppingCartScreen();
            Thread.Sleep(3000);
        }

        [Then(@"add product to cart")]
        public void ThenAddProductToCart()
        {
            ProductScreen.Click(ProductScreen.Button.productScreen_AddToCart);
            Thread.Sleep(3000);
            ShoppingCart.WaitforShoppingCartScreen();
        }

        [Then(@"click on Use this payment method")]
        public void ThenClickOnUseThisPaymentMethod()
        {
            Payment.Click(Payment.Button.Use_this_Payment);
            Thread.Sleep(3000);
        }


    }
}
