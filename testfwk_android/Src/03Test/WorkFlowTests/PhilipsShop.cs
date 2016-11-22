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
using Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin;
using System.Xml;
using System.Reflection;
using System.IO;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using Philips.CDP.Automation.IAP.Tests.Workflows;
using Philips.SIG.Automation.Android.CDPP.AppFramework_TestPlugin;

namespace Philips.CDP.Automation.IAP.Tests.Workflows
{
    [Binding]

    public class PhilipsShop
    {
        [Then(@"I launch Philips Shop from hamburger menu")]
        public void ThenILaunchPhilipsShopFromHamburgerMenu()
        {
            try
            {
                AppHomeScreen.Click(AppHomeScreen.Button.PhilipShop);
            }
            catch(Exception e)
            {
                IapReport.Fail("Step Failed: Exception: Philip Shop could not be clicked", e);
            }
        }

        [Then(@"I verify that the product list view is displayed")]
        public void ThenIVerifyThatTheProductListViewIsDisplayed()
        {
            try
            {
                bool Screen_Loaded = ProductScreen.WaitforProductScreen(); ;
                if (!Screen_Loaded)
                {
                    IapReport.Fail("Error: The Product List View is not loaded.");
                }
                
                string title = ProductScreen.GetScreenTitle();
                if (title.Equals("Products"))
                {
                    IapReport.Message("Navigated to Product Catalog page");
                }
                else
                {
                    IapReport.Fail("Step Failed: Not landed in Product Catalog Page");
                }
            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed: Exception: Not landed in Product Catalog Page", e);
            }


        }

        [Then(@"I select ""(.*)"" from the product list view")]
        public void ThenISelectFromTheProductListView(string p0)
        {
            try
            {
                ProductScreen.SelectProduct(p0);
                
            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed: Exception: The product could not be selected", e);
            }
        }


        [Then(@"I verify that user is navigated to the detail view of product ""(.*)""")]
        public void ThenIVerifyThatUserIsNavigatedToTheDetailViewOfProduct(string p0)
        {
            try
            {
                ShoppingCartItem.WaitforShoppingCartInfoScreen();
                bool IsPresent = ShoppingCartItem.IsVisible(p0);
                if(!IsPresent)
                {
                    IapReport.Fail("Step Failed: Exception: The product could not be visible : " +p0);
                }
                
                string Product_description = ShoppingCartItem.GetText(ShoppingCartItem.TextView.ProductDescription);
                string ShoppingCartItemTitle = ShoppingCartItem.GetText(ShoppingCartItem.TextView.ShoppingCartItemTitle);
                if((Product_description != "Sonicare DiamondClean Standard sonic toothbrush heads") && (ShoppingCartItemTitle != "Sonicare DiamondClean Standard sonic toothbrush heads"))
                 {
                IapReport.Fail("Step Failed: Exception: The user is not in product detail view page");
                 }
                //ShoppingCartItem.GetProductImage();
                ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 1);
                


            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed: Exception: The user is not navigated to product detail view page", e);
            }
        }

        [Then(@"I click on Buy Now and verify that user is navigated to list of retailer list view")]
        public void ThenIClickOnBuyNowAndVerifyThatUserIsNavigatedToListOfRetailerListView()
        {
            try
            {
                ShoppingCartItem.Click(ShoppingCartItem.Button.BuyFromRetailer);
                string Retailer_ScreenTitle = RetailerScreen.GetScreenTitle();
                if (Retailer_ScreenTitle != "Retailer")
                {
                    Logger.Fail("Error: The user is not in the Retailer List Screen.");
                }
                int Retailer_List_Count = RetailerScreen.GetRetailerListItems().Count;
                Logger.Info("The Total number of Retailer is : " +Retailer_List_Count);

            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed: Exception: The user is not navigated to retailer view page", e);
            }


        }

        [Then(@"I select ""(.*)"" from the retailer list view")]
        public void ThenISelectFromTheRetailerListView(string p0)
        {
            try
            {
                RetailerScreen.SelectRetailer(p0);
                Thread.Sleep(30000);
            }
            catch (Exception)
            {
                IapReport.Fail("Step Failed: Exception: The user is could not launch the retailer :" +p0 );
            }
            
        }

        [Then(@"I verify that ""(.*)"" webview is launched")]
        public void ThenIVerifyThatWebviewIsLaunched(string p0)
        {
           string Retailer_Webview = RetailerScreen.GetScreenTitle();
           if (Retailer_Webview != p0)
            {
                Logger.Fail("Error: The webview of the selected retailer is not launched.");
            }
        }

        [Then(@"I verify that user can navigate back from webview to retailer list view")]
        public void ThenIVerifyThatUserCanNavigateBackFromWebviewToRetailerListView()
        {
            try
            {

                RetailerScreen.Click(RetailerScreen.Button.Retailer_Back);
                RetailerScreen.WaitforRetailerScreen();
                string Retailer_ScreenTitle = RetailerScreen.GetScreenTitle();
                if (Retailer_ScreenTitle != "Retailer")
                {
                    Logger.Fail("Error: The user is not in the Retailer List Screen.");
                }
                
            }
            catch(Exception e)
            {
                IapReport.Fail("Step Failed: Exception: The user is not navigated to retailer view page", e);
            }

        }

        [Then(@"I verify that user can navigate back to product detail view from retailer list view")]
        public void ThenIVerifyThatUserCanNavigateBackToProductDetailViewFromRetailerListView()
        {
            RetailerScreen.Click(RetailerScreen.Button.Retailer_Back);
            ShoppingCartItem.WaitforShoppingCartInfoScreen();
            bool IsPresent = ShoppingCartItem.IsVisible("HX6064/33");
            if (!IsPresent)
            {
                IapReport.Fail("Step Failed: Exception: The user is not back to product detail screen : ");
            }
           
            string Product_description = ShoppingCartItem.GetText(ShoppingCartItem.TextView.ProductDescription);
            string ShoppingCartItemTitle = ShoppingCartItem.GetText(ShoppingCartItem.TextView.ShoppingCartItemTitle);
            if((Product_description != "Sonicare DiamondClean Standard sonic toothbrush heads") && (ShoppingCartItemTitle != "Sonicare DiamondClean Standard sonic toothbrush heads"))
            {
                IapReport.Fail("Step Failed: Exception: The user is not in product detail view page");
            }

        }

        [Then(@"I click on Buy Now")]
        public void ThenIClickOnBuyNow()
        {
            try
            {

                ShoppingCartItem.Click(ShoppingCartItem.Button.BuyFromRetailer);
            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed: Exception: The user could not click on Buy Now", e);
            }
        }

       

    }
}
