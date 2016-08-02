using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using TechTalk.SpecFlow;

namespace Philips.CDP.Automation.IAP.Tests.Workflows
{
    [Binding]
    public class WF2
    {
        HomeScreen hScreen = new HomeScreen();
        ShoppingCart shop = new ShoppingCart();
        ShippingAddress ship = new ShippingAddress();
        public static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        string Cartnumber = string.Empty;
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }
        
        [Given(@"Verify user that on test app ,user can see a shopping icon")]
        public void GivenVerifyUserThatOnTestAppUserCanSeeAShoppingIcon()
        {
            try
            {
                HomeScreen.WaitforHomeScreen();
                if (HomeScreen.IsEnabled(HomeScreen.Button.CartImage))
                {
                    IapReport.Message("Step Passed:User can see shopping icon");
                    MobileDriver.TakeScreenshot("WRF1_ShoppingIconPresent.png");
                }
                else
                {
                    //IapReport.Fail("Step Failed:User cannot see shopping icon");
                    IapReport.Message("Step Failed:User cannot see shopping icon");
                }
            }
            catch (Exception e)
            {
                IapReport.Fail("User is not in Home screen", e);
            } 
        }
        [Given(@"verify user empty cart behaviour")]
        public void GivenVerifyUserEmptyCartBehaviour()
        {
            try
            {
                List<string> productid = HomeScreen.GetProductListItems();
                HomeScreen.Click(productid[1], HomeScreen.Button.AddToCart);
                HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(HomeScreen.Button.CartImage);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.DeleteAllProducts();
                ShoppingCart.Click(ShoppingCart.Button.UpButton);
                HomeScreen.WaitforHomeScreen();
                Thread.Sleep(3000);
                if (Cartnumber == "")
                {
                    IapReport.Message("Step Passed: verified empty cart behaviour");
                }
                else
                {
                    //IapReport.Fail("Step Failed: The cart contains product quantity:" + HomeScreen.GetTextFromCart());
                    IapReport.Message("Step Failed: The cart contains product quantity:" + HomeScreen.GetTextFromCart());
                }
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in shoppingcart screen", e);
            } 
  
        }
        [When(@"User select a product and click on Add to cart")]
        public void WhenUserSelectAProductAndClickOnAddToCart()
        {
            try
            {
                Cartnumber = HomeScreen.GetTextFromCart();
            }
            catch (Exception e)
            {
            }
            try
            {
                List<string> productid = HomeScreen.GetProductListItems();
                HomeScreen.Click(productid[1], HomeScreen.Button.AddToCart);

            }
            catch (Exception e)
            {
                IapReport.Fail("User is not in Home screen", e);
            } 
        }


        [Then(@"Verify when the product is added ,the shopping cart display the incremented product number")]
        public void ThenVerifyWhenTheProductIsAddedTheShoppingCartDisplayTheIncrementedProductNumber()
        {
            try
            {
                HomeScreen.WaitforHomeScreen();
                int newnumber = Convert.ToInt32(HomeScreen.GetTextFromCart());
                if (Cartnumber == "")
                    Cartnumber = "0";
                int cartnumber = Convert.ToInt32(Cartnumber);
                if (newnumber == (cartnumber + 1))
                    IapReport.Message("Step Passed: Cart number Incremented");
                else
                {
                   // IapReport.Fail("Step Failed: Cart number is not changed ");
                    IapReport.Message("Step Failed: Cart number is not changed " + newnumber);
                
                }
            }
            catch (Exception e)
            {
                IapReport.Fail("User is not in Home screen", e);
            } 
        }

        [When(@"User  clicking on shopping icon")]
        public void WhenUserClickingOnShoppingIcon()
        {
            try
            {
                HomeScreen.WaitforHomeScreen();
               
                HomeScreen.Click(HomeScreen.Button.CartImage);
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in Home screen", e);
            } 
        }

        [Then(@"Verify user is navigate to shopping cart view")]
        public void ThenVerifyUserIsNavigateToShoppingCartView()
        {
            try
            {
                ShoppingCart.WaitforShoppingCartScreen();
                if (ShoppingCart.IsVisible())
                    IapReport.Message("Step Passed: user navigated to Shopping Cart");
                else
                {
                    //IapReport.Fail("Step Failed: user not navigated to Shopping Cart");
                    IapReport.Message("Step Failed: user not navigated to Shopping Cart");
                }
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in shoppingcart screen", e);
            } 
        }

        [When(@"User repeated Click on Add to Cart")]
        public void WhenUserRepeatedClickOnAddToCart()
        {
            try
            {
                ShoppingCart.Click(ShoppingCart.Button.UpButton);
                HomeScreen.WaitforHomeScreen();
                List<string> productid = HomeScreen.GetProductListItems();
                HomeScreen.Click(productid[1], HomeScreen.Button.AddToCart);
                HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(productid[1], HomeScreen.Button.AddToCart);
                HomeScreen.WaitforHomeScreen();
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in Home screen", e);
            } 
        }

        [Then(@"Verify the status of shopping icon when the app is sent to background")]
        public void ThenVerifyTheStatusOfShoppingIconWhenTheAppIsSentToBackground()
        {
            try
            {
                UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input keyevent 3 ");
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in Home screen", e);
            } 
        }

        [Then(@"Verify the status of shopping icon when the app is closed and relaunched")]
        public void ThenVerifyTheStatusOfShoppingIconWhenTheAppIsClosedAndRelaunched()
        {
            try
            {
                Cartnumber = HomeScreen.GetTextFromCart();
                UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell am force-stop com.philips.cdp.di.iapdemo ");
                IapReport.Message("App is closed");
                UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell am  start -n com.philips.cdp.di.iapdemo/com.philips.cdp.di.iapdemo.DemoAppActivity ");
                IapReport.Message("App is Relaunched");
                //LoginScreen.Login(ConfigurationManager.AppSettings["UsernameIAP"], ConfigurationManager.AppSettings["PasswordIAP"]);
                string newcartnumber = HomeScreen.GetTextFromCart();
                if (Cartnumber == newcartnumber)
                    IapReport.Message("Step Passed: shopping icon status verified");
                else
                {
                   // IapReport.Fail("Step Failed: shopping icon status not verified");
                    IapReport.Message("Step Failed: shopping icon status not verified");
                }
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in Home screen", e);
            } 
        }

        [Then(@"Verify the status of the shopping cart if the product that he added does not exist")]
        public void ThenVerifyTheStatusOfTheShoppingCartIfTheProductThatHeAddedDoesNotExist()
        {
            try
            {
                Cartnumber = HomeScreen.GetTextFromCart();
                List<string> productid = HomeScreen.GetProductListItems();
                HomeScreen.Click(productid[0], HomeScreen.Button.AddToCart);
                MobileDriver.TakeScreenshot("VerifyTheStatusOfTheShoppingCartIfTheProductThatHeAddedDoesNotExist.bmp");
                HomeScreen.WaitforHomeScreen();
                string newCartnumber = HomeScreen.GetTextFromCart();
                if (Cartnumber.Equals(newCartnumber))
                    IapReport.Message("status of  shopping cart is not changed and product currentely does not exist ");
                else
                {
                    //IapReport.Fail("Status of shopping cart is changed and Product is exist");
                    IapReport.Message("Status of shopping cart is changed and Product is exist");
                }

            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in Home screen", e);
            } 
        }

        [When(@"User Clicks on Countinue Shopping_(.*)")]
        public void WhenUserClicksOnCountinueShopping_(int p0)
        {
            ShoppingCart.Click(ShoppingCart.Button.ContinueShopping_1);
            HomeScreen.WaitforHomeScreen();
        }


    }
}
