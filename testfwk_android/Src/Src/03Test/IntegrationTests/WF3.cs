using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using TechTalk.SpecFlow;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System.Threading;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using System.Configuration;

namespace Philips.CDP.Automation.IAP.Tests.Workflows
{
    [Binding]
    public class WF3
    {
       
        
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }
        public static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        [Given(@"User is on the HomeScreen")]
        public void GivenUserIsOnTheHomeScreen()
        {
            try
            {
                Thread.Sleep(5000);
                string title = HomeScreen.GetScreenTitle();
                if (title.Equals("E-Commerce Demo App"))
                {
                    IapReport.Message("User is in  Launch page");

                }
            }
            catch (Exception e)
            {
                IapReport.Message("User is not in Launch page ");

                UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell am force-stop com.philips.cdp.di.iapdemo ");
                UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell am  start -n com.philips.cdp.di.iapdemo/com.philips.cdp.di.iapdemo.DemoAppActivity ");
            }
            try
            {
                HomeScreen.Click(HomeScreen.Button.Register);
                bool title1 = AccountSettings.IsScreenVisible();
                if (title1)
                    AccountSettings.Click(AccountSettings.Button.LogOut);
                Thread.Sleep(2000);
                LoginScreen.LoginUser(ConfigurationManager.AppSettings["UsernameIAP1"], ConfigurationManager.AppSettings["PasswordIAP"]);
                 HomeScreen.WaitforHomeScreen();
            }
            catch(Exception e)
            {
                IapReport.Fail("Exception occur in Home Page", e);
            }
        }
        string Cartnumber = string.Empty;
        [When(@"User is first time buyer ,select a product and click on buy now")]
        public void WhenUserIsFirstTimeBuyerSelectAProductAndClickOnBuyNow()
        {
            try
            {

                  IapReport.Message("user is first time buyer");
                List<string> productid = HomeScreen.GetProductListItems();
                HomeScreen.Click(productid[1], HomeScreen.Button.BuyNow);
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in Home screen", e);
            } 
          
        }
        [Then(@"Verify the user is navigated to shopping cart")]
        public void ThenVerifyTheUserIsNavigatedToShoppingCart()
        {
            try
            {
                
                ShoppingCart.WaitforShoppingCartScreen();
                if (ShoppingCart.IsVisible())
                    IapReport.Message("User successfully navigated to shopping cart screen");
                else
                    IapReport.Fail("User did not navigate to shopping cart screen");
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in shoppingcart screen", e);
            } 
        }
        [Then(@"Verify the User is able to edit the quantity of the product")]
        public void ThenVerifyTheUserIsAbleToEditTheQuantityOfTheProduct()
        {
            try
            {
                ShoppingCart.WaitforShoppingCartScreen();
                    List<String> ProdNames = ShoppingCart.GetProductNames();
                    string prdo1 = ProdNames[0];
                    int Quantity = Int32.Parse(ShoppingCart.GetText(ProdNames[0], ShoppingCart.ProductDetailsText.Quantity));
                    Quantity += 2;

                    ShoppingCart.Select(ProdNames[0], Quantity);

                    ProdNames = ShoppingCart.GetProductNames();

                    int newQuantity = Int32.Parse(ShoppingCart.GetText(ProdNames[0], ShoppingCart.ProductDetailsText.Quantity));

                    if (newQuantity == Quantity)
                        IapReport.Message("Step Passed: quantity is editable");
                    else
                        IapReport.Fail("Step Failed: quantity is not editable");
                

               
                }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in shoppingcart screen", e);
            } 
        }
        [Then(@"Verify that max quantity is based on available stock of the product in hybris")]
        public void ThenVerifyThatMaxQuantityIsBasedOnAvailableStockOfTheProductInHybris()
        {
            try
            {
                IapReport.Message("not implemented");
            }
             catch (Exception e)
            {
                IapReport.Fail("Exception occur in shoppingcart screen", e);
            } 
        }
        [When(@"Click on menu button next to the product")]
        public void WhenClickOnMenuButtonNextToTheProduct()
        {
           // shop.Click(ShoppingCart.Button.Dots);
        }

        [Then(@"Verify the user is able to see the detail info of the product")]
        public void ThenVerifyTheUserIsAbleToSeeTheDetailInfoOfTheProduct()
        {
            try
            {
                List<String> ProdNames = ShoppingCart.GetProductNames();
                ShoppingCart.ContextMenu.Select(ProdNames[0], ShoppingCart.ContextMenu.MenuItem.Info);
                ShoppingCartItem.WaitforShoppingCartInfoScreen();
                bool value = ShoppingCartItem.IsVisible();
                if (value)
                {
                    MobileDriver.TakeScreenshot("WF3_ShoppingCartInfoScreen.png");
                    IapReport.Message("User navigate to info page");
                    ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);

                }
                else
                    IapReport.Fail("Not yet implemented");
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in shoppingcart screen", e);
            } 
        }

        [Then(@"Verify that the user is able to delete the product that he does not choose to buy from cart using delete button")]
        public void ThenVerifyThatTheUserIsAbleToDeleteTheProductThatHeDoesNotChooseToBuyFromCartUsingDeleteButton()
        {
            try
            {
                ShoppingCart.WaitforShoppingCartScreen();
                List<String> ProdNames = ShoppingCart.GetProductNames();
                ShoppingCart.ContextMenu.Select(ProdNames[0], ShoppingCart.ContextMenu.MenuItem.Delete);
                ShoppingCart.WaitforShoppingCartScreen();
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in shoppingcart screen", e);
            } 
        }
        [When(@"User clicks on continue shopping")]
        public void WhenUserClicksOnContinueShopping()
        {
            try
            {
                List<String> ProdNames = ShoppingCart.GetProductNames();
                if (ProdNames.Count != 0)
                    ShoppingCart.Click(ShoppingCart.Button.ContinueShopping);
                else
                    ShoppingCart.Click(ShoppingCart.Button.ContinueShopping_1);
                HomeScreen.WaitforHomeScreen();
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in shoppingcart screen", e);
            } 
           
        }
        [Then(@"verify that it will navigate to test app")]
        public void ThenVerifyThatItWillNavigateToTestApp()
        {
            try
            {
                if (HomeScreen.IsVisible())
                    IapReport.Message("Step Passed: navigated to home screen");
                else
                    IapReport.Fail("Step Failed: did not navigate to home screen");
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in Home screen", e);
            } 

        }
        [When(@"User selects different  product and clicks on buy now")]
        public void WhenUserSelectsDifferentProductAndClicksOnBuyNow()
        {
            try
            {
                
                List<string> productid = HomeScreen.GetProductListItems();
                HomeScreen.Click(productid[1], HomeScreen.Button.BuyNow);
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in Home screen", e);
            } 
           
        }
        [Then(@"User clicks on checkout")]
        public void ThenUserClicksOnCheckout()
        {
            try
            {

                ShoppingCart.Click(ShoppingCart.Button.Checkout);
                ShippingAddress.WaitforShippingAddressScreen();
                ShippingAddress.Click(ShippingAddress.Button.arrow);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.UpButton);
                HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(HomeScreen.Button.Register);
                Thread.Sleep(2000);
                AccountSettings.Click(AccountSettings.Button.LogOut);
                LoginScreen.Click(LoginScreen.Button.LoginBackButton);
                HomeScreen.WaitforHomeScreen();
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in shoppingcart screen", e);
            } 
        }


    }
}
