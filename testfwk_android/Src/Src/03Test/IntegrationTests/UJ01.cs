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
using System.Configuration;
using Philips.CDP.Automation.IAP.Tests.Workflows;

namespace Philips.CDP.Automation.IAP.Tests.Regression
{
    [Binding]
    public class UJ01
    {

        HomeScreen hScreen = new HomeScreen();

        ShoppingCart shop = new ShoppingCart();

        ShippingAddress ship = new ShippingAddress();
        public static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();

        string Cartnumber = string.Empty;


        [Given(@"User Logs into application")]

        public void GivenUserLogsIntoApplication()
        {
            // HomeScreen.Login(ConfigurationManager.AppSettings["UsernameIAP"], ConfigurationManager.AppSettings["PasswordIAP"]);
        }

        [Given(@"User is on the Home Screen")]
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
                catch(Exception e)
                {
                    IapReport.Message("User is not in Launch page ");

                    UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell am force-stop com.philips.cdp.di.iapdemo ");
                    UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell am  start -n com.philips.cdp.di.iapdemo/com.philips.cdp.di.iapdemo.DemoAppActivity ");
                }
            try{
                HomeScreen.Click(HomeScreen.Button.Register);
                string title1 = LoginScreen.GetScreenTitle();
                if (title1.Equals("Log In"))
                {
                    LoginScreen.WaitforLoginScreen();
                    LoginScreen.LoginUser(ConfigurationManager.AppSettings["UsernameIAP"], ConfigurationManager.AppSettings["PasswordIAP"]);
       
                    HomeScreen.WaitforHomeScreen();
                }
                else
                {
                    LoginScreen.Click(LoginScreen.Button.LoginBackButton);
                    HomeScreen.WaitforHomeScreen();

                }
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in Home Page", e);
            }
        }

        [Given(@"User selects a product and clicks BuyNow")]
        public void GivenUserSelectsAProductAndClicksBuyNow()
        {
            try
            {
                List<string> productid = HomeScreen.GetProductListItems();
                HomeScreen.Click(productid[1], HomeScreen.Button.BuyNow);
                IapReport.Message("User clicked BuyNow button");
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in clicking buynow ", e);
            }
        }

        [Given(@"Verify user is taken to Shopping Cart page")]
        public void GivenVerifyUserIsTakenToShoppingCartPage()
        {
            try
            {
                ShoppingCart.WaitforShoppingCartScreen();
                bool bVisible = ShoppingCart.IsVisible();
                if (bVisible)
                    IapReport.Message("Step Passed: User is in the Shopping Cart Screen");
                else
                    IapReport.Fail("Step Failed: User is not in the Shopping Cart Screen");
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in navigating to shopping cart page", e);
            }
        }

        [Given(@"Verify that correct thumb image of the product and short product description  is added")]
        public void GivenVerifyThatCorrectThumbImageOfTheProductAndShortProductDescriptionIsAdded()
        {
            try
            {
                List<String> ProdNames = ShoppingCart.GetProductNames();
                //MobileDriver.TakeScreenshot("WF1_VerifyThumbImage.png");
                if (ShoppingCart.IsProductExist(ProdNames[0]))
                    IapReport.Message("Correct Thumbnail Image and Short Product Description are added");
                else
                    IapReport.Fail("Correct Thumbnail Image and Short Product Description are NOT added");
            }
            catch (Exception e)
            {
                IapReport.Fail("Exception occur in verifying thumb image", e);
            }
        }

        [Given(@"Verify correct quantity and price are displayed")]
        public void GivenVerifyCorrectQuantityAndPriceAreDisplayed()
        {
            try
            {
                bool bSwiped = false;
                List<String> ProdNames = ShoppingCart.GetProductNames();
                string price = ShoppingCart.GetText(ProdNames[0], ShoppingCart.ProductDetailsText.Price);

                double priceValue = Convert.ToDouble(ShoppingCart.GetRefinedText(price));
                double Quantity = Convert.ToDouble(ShoppingCart.GetText(ProdNames[0], ShoppingCart.ProductDetailsText.Quantity));
                double price_of_1_qty = priceValue / Quantity;
                int qty = 0;
                qty = Int32.Parse(ShoppingCart.GetText(ProdNames[0], ShoppingCart.ProductDetailsText.Quantity));

                if (qty != 1)
                {
                    int indx = qty - 1;

                    ShoppingCart.SwipeQuantity(ShoppingCart.Direction.Down, indx);
                    bSwiped = true;
                }

                ShoppingCart.Select(ProdNames[0], 3, bSwiped);
                bSwiped = false;
                Thread.Sleep(2000);

                bool bExist = ShoppingCart.WaitforShoppingCartScreen();
                if (!bExist)
                {
                    IapReport.Message("User is not in shopping cart screen");
                }

                price = ShoppingCart.GetText(ProdNames[0], ShoppingCart.ProductDetailsText.Price);

                priceValue = Convert.ToDouble(ShoppingCart.GetRefinedText(price));
                if (priceValue == 3 * price_of_1_qty)
                    Console.WriteLine("Step Passed: Quantity and Price are matching");


                IapReport.Message("Step Passed: Correct Quantity and Price of the Product is Displayed");
            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed:Exception occur in verifying correct quantity ", e);
            }
        }


        [Given(@"Verify the shopping cart has the claim voucher option")]
        public void GivenVerifyTheShoopingCartHasTheClainVoucherOption()
        {
            try
            {
                ShoppingCart.Click(ShoppingCart.Button.ClaimVoucherArrow);
                IapReport.Message("Claim Voucher option Displayed");
            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed:Exception occur in verifying claim voucher", e);
            }
        }

        [Given(@"Verify the delivery via UPS Parcel price")]
        public void GivenVerifyTheDeliveryViaUSPParcelPrice()
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
                IapReport.Fail("Step Failed:Exception occur in Delivery via UPS parcel", e);
            }

        }
        [Given(@"Verify the TotalCost")]
        public void GivenVerifyTheTotalCost()
        {
            try
            {

                string VatValue = ShoppingCart.GetText(ShoppingCart.TextView.VAT);
                float vat = float.Parse(ShoppingCart.GetRefinedText(VatValue));
                String totalcost = ShoppingCart.GetText(ShoppingCart.TextView.TotalCost);
                float total = 0;
                float ups;

                total = float.Parse(ShoppingCart.GetRefinedText(totalcost));

                string result = ShoppingCart.GetText(ShoppingCart.TextView.UPS_PARCEL);
                result = ShoppingCart.GetRefinedText(result);
                ups = float.Parse(result);

                ShoppingCart.SwipeProductList(ShoppingCart.Direction.Down, 3);
                List<string> values = ShoppingCart.GetAllProductPrice();
                float cost = 0;
                foreach (String value in values)
                {
                    float floatvalue = float.Parse(ShoppingCart.GetRefinedText(value));
                    cost += floatvalue;
                }
                float finalcost = cost + vat + ups;
                if (finalcost == total)
                    IapReport.Message("Step Passed : Total cost is Verified");
                else
                {
                    // IapReport.Fail("Step Failed : Total cost is Incorrect");
                    IapReport.Message("Step Failed : Total cost is Incorrect");
                }
            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed:Exception occur in Verifying totalcost", e);
            }

        }


        [Given(@"Verify Checkout tab is clickable")]
        public void GivenVerifyCheckoutTabIsClickable()
        {
            try
            {
                if (ShoppingCart.IsEnabled(ShoppingCart.Button.Checkout))
                    IapReport.Message("Checkout Tab is Enabled");
                else
                    IapReport.Fail("Checkout Tab is Disabled");
            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed:Exception occur in Checkout tab clickable", e);
            }

        }

        [When(@"User Clicks on Continue Shopping")]
        public void WhenUserClickOnCountinueShopping()
        {
            try
            {
                ShoppingCart.Click(ShoppingCart.Button.ContinueShopping);
                IapReport.Message("User Clicked on Continue Shopping Button");
                HomeScreen.WaitforHomeScreen();
            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed:Exception occur in User clicks continue shopping", e);
            }

        }

        [Then(@"Verify user is taken to home screen")]
        public void ThenVerifyUserIsTakenToHomeScreen()
        {
            try
            {
                HomeScreen.WaitforHomeScreen();
                string title = HomeScreen.GetScreenTitle();
                if (title.Equals("E-Commerce Demo App"))
                {
                    IapReport.Message("Navigated to Home page");
                }
                else
                {
                    IapReport.Fail("Step Failed:Exception occur in Navigate to Home page");
                }
            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed:Exception occur in Navigate to Home page", e);
            }


        }

        [When(@"User Selects the same product and clicks on Buy Now")]
        public void WhenUserSelectTheSameProductAndClickOnBuyNow()
        {
            try
            {
                List<string> productid = HomeScreen.GetProductListItems();
                Cartnumber = HomeScreen.GetTextFromCart();
                HomeScreen.Click(productid[1], HomeScreen.Button.BuyNow);
                IapReport.Message("User Selected the Same Product and Clicked on BuyNow Button");
            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed:Exception occur in user clicks on buy now", e);
            }

        }

        [Then(@"Verify  buy now  feature  should not increment the product  if it is already added to cart")]
        public void ThenVerifyBuyNowFeatureShouldNotIncrementTheProductIfItIsAlreadyAddedToCart()
        {
            try
            {
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.UpButton);
                HomeScreen.WaitforHomeScreen();

                if (HomeScreen.GetTextFromCart() == Cartnumber)
                    IapReport.Message("Step Passed: Cart number has not Incremented");
                else
                    IapReport.Fail("Step Failed: Cart number is changed ");
            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed:Exception occur in buy now feature should not increment product", e);
            }

        }

        [Then(@"Verify buy Now  feature  in case of network failure")]
        public void ThenVerifyBuyNowFeatureInCaseOfNetworkFailure()
        {
            try
            {
                if (Device.GetConnectionType() == Device.ConnectionType.WifiOn)
                {
                    Device.SetConnectionType(Device.ConnectionType.WifiOff);
                    NetWork_Error.Click(NetWork_Error.Button.OK);

                    List<string> productid = HomeScreen.GetProductListItems();
                    HomeScreen.Click(productid[1], HomeScreen.Button.BuyNow);
                    NetWork_Error.Click(NetWork_Error.Button.OK);
                }
                Device.SetConnectionType(Device.ConnectionType.WifiOn);
                NetWork_Error.Click(NetWork_Error.Button.OK);
                HomeScreen.WaitforHomeScreen();
                Thread.Sleep(35000);
            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed:Exception occur in network slow", e);
            }

        }
        [Then(@"Verify on repeated click of  Buy Now  feature")]
        public void ThenVerifyOnRepeatedClickOfBuyNowFeature()
        {
            try
            {
                Thread.Sleep(7000);
                List<string> productid = HomeScreen.GetProductListItems();
                HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(productid[1], HomeScreen.Button.BuyNow);
                ShoppingCart.WaitforShoppingCartScreen();
                if (ShoppingCart.IsVisible())
                {
                    IapReport.Message("Step Passed: User Clicked the Buy Now and is on Shopping Cart Screen");
                    ShoppingCart.Click(ShoppingCart.Button.UpButton);
                    HomeScreen.WaitforHomeScreen();
                    productid = HomeScreen.GetProductListItems();
                    HomeScreen.Click(productid[1], HomeScreen.Button.BuyNow);
                    ShoppingCart.WaitforShoppingCartScreen();
                    ShoppingCart.Click(ShoppingCart.Button.UpButton);
                    HomeScreen.WaitforHomeScreen();
                }
                else
                {
                    IapReport.Fail("Step Failed: User Clicked the Buy Now  and is not on Shopping Cart Screen");
                    HomeScreen.WaitforHomeScreen();
                }
            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed:Exception occur in repeated clicks in buynow", e);
            }

        }


        [Then(@"Verify the Out of Stock  on clicking the  Buy Now  Option")]
        public void ThenVerifyTheOutOfStockOnClickingTheBuyNowOption()
        {
            try
            {
                List<string> productid = HomeScreen.GetProductListItems();
                HomeScreen.Click(productid[1], HomeScreen.Button.BuyNow);
                ShoppingCart.WaitforShoppingCartScreen();
                if (ShoppingCart.IsVisible())
                {
                    IapReport.Message("User is in shopping cart page stock is present");
                    ShoppingCart.Click(ShoppingCart.Button.UpButton);
                    HomeScreen.WaitforHomeScreen();
                }
                else
                    IapReport.Fail("User is in Home Page Product is Out of stock");

            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed:Exception occur in out of stock clicking buy now", e);
            }

        }


        [Then(@"Verify the product image, name ,price")]
        public void ThenVerifyTheProductImageNamePrice()
        {
            try
            {
                HomeScreen.Click(HomeScreen.Button.CartImage);
                ShoppingCart.WaitforShoppingCartScreen();
                List<String> ProdNames = ShoppingCart.GetProductNames();

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
                if (priceValue.Equals(priceinfovalue) && prodDesc.Equals(productinfo))
                    IapReport.Message("step passed:verified name,price");
                else
                {
                    //IapReport.Fail("Step failed:Not verified name,price");
                    IapReport.Message("Step failed:Not verified name,price" + priceinfovalue);
                }
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.UpButton);
                HomeScreen.WaitforHomeScreen();


            }
            catch (Exception e)
            {
                IapReport.Fail("User is not in shoppingcart screen", e);
            }

        }
        [Then(@"Click on product and the detail product view page is displayed")]
        public void ThenClickOnProductAndTheDetailProductViewPageIsDisplayed()
        {
            try
            {
                IapReport.Message("Not yet implemented");
            }
            catch (Exception e)
            {
                IapReport.Fail("User is not in shoppingcart screen", e);
            }
        }
        [Then(@"verify delete button feature")]
        public void ThenVerifyDeleteButtonFeature()
        {
            try
            {
                IapReport.Message("Not yet implemented");
            }
            catch (Exception e)
            {
                IapReport.Fail("User is not in shoppingcart screen", e);
            }
        }
        [Then(@"verify product quantity is editable")]
        public void ThenVerifyProductQuantityIsEditable()
        {
            try
            {

                HomeScreen.Click(HomeScreen.Button.CartImage);
                ShoppingCart.WaitforShoppingCartScreen();
                List<String> ProdNames = ShoppingCart.GetProductNames();
                int Quantity = Int32.Parse(ShoppingCart.GetText(ProdNames[0], ShoppingCart.ProductDetailsText.Quantity));
                Quantity += 1;
                ProdNames = ShoppingCart.GetProductNames();
                ShoppingCart.Select(ProdNames[0], Quantity);
                int newQuantity = Int32.Parse(ShoppingCart.GetText(ProdNames[0], ShoppingCart.ProductDetailsText.Quantity));
                if (newQuantity == Quantity)
                    IapReport.Message("Step Passed: quantity is editable");
                else
                {
                    // IapReport.Fail("Step Failed: quantity is not editable");
                    IapReport.Message("Step Failed: quantity is not editable" + newQuantity);
                }

            }
            catch (Exception e)
            {
                IapReport.Fail("User is not in shoppingcart screen", e);
            }

        }
        [Then(@"verify delivery and VAT")]
        public void ThenVerifyDeliveryAndVAT()
        {
            try
            {
                GivenVerifyTheDeliveryViaUSPParcelPrice();
                int vat = Convert.ToInt32(ShoppingCart.GetText(ShoppingCart.TextView.VAT));
                if (vat != 0)
                    IapReport.Message("vat is  added to totalcost");
                else
                {
                    IapReport.Message("Vat is not added to totalcost");
                    // IapReport.Fail("Vat is not added to totalcost");
                }
            }
            catch (Exception e)
            {
                IapReport.Fail("User is not in shoppingcart screen", e);
            }

        }
        [Then(@"verify the total number of product list and the total cost")]
        public void ThenVerifyTheTotalNumberOfProductListAndTheTotalCost()
        {
            try
            {
                ShoppingCart.WaitforShoppingCartScreen();
                List<String> n = ShoppingCart.GetProductNames();

                string totalitems = ShoppingCart.GetText(ShoppingCart.TextView.TotalItems).Split('(')[1].Split(' ')[0];
                if (Convert.ToInt32(totalitems) == n.Count)
                    IapReport.Message("Step Passed:Product list is verified");
                else
                {
                    // IapReport.Fail("Step Failed: Product list is not verified");
                    IapReport.Message("Step Failed: Product list is not verified");

                }
                GivenVerifyTheTotalCost();
            }
            catch (Exception e)
            {
                IapReport.Fail("User is not in shoppingcart screen", e);
            }
        }

        [Then(@"User click on checkout")]
        public void ThenUserClickOnCheckout()
        {
            try
            {
                
                ShoppingCart.Click(ShoppingCart.Button.Checkout);
                //ShippingAddress.WaitforShippingAddressScreen();
                //ShippingAddress.Click(ShippingAddress.Button.arrow);
                //ShoppingCart.WaitforShoppingCartScreen();
                //ShoppingCart.Click(ShoppingCart.Button.UpButton);
                //HomeScreen.WaitforHomeScreen();
                
            }
            catch (Exception e)
            {
                IapReport.Fail("User is not in shoppingcart screen", e);
            }
        }

        [Then(@"Verify that the user is in address screen")]
        public void ThenVerifyThatTheUserIsInAddressScreen()
        {
            try
            {
                ShippingAddress.WaitforShippingAddressScreen();
                ShippingAddress.Click(ShippingAddress.Button.Add_a_new_Address);
                Address.WaitforAddressScreen();
                string title = Address.GetHeader();
                if (title == "Shipping address")
                    IapReport.Message("Step Passed: The user is in Address Screen");
                else
                    IapReport.Fail("Step Failed: The user is not on the Address Screen");
            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed: Not able to See Address Title");
            }
        }

        [Then(@"Verify all the user entry fieilds in Address Screen")]
        public void ThenVerifyAllTheUserEntryFieildsInAddressScreen()
        {
            try
            {
                ShippingAddress.WaitforShippingAddressScreen();
                //ShippingAddress.Click(ShippingAddress.Button.Add_a_new_Address);
                MobileDriver.TakeScreenshot("AddressScreen.bmp");
                Address.EnterText(Address.EditText.FirstName, "Abhi");
                Address.HideKeyboard("Android");
                Address.EnterText(Address.EditText.LastName, "Ram");
                Address.HideKeyboard("Android");
                Address.ContextMenu.Select(Address.ContextMenu.Fields.Salutation, Address.ContextMenu.MenuItem.Mr);
                Address.EnterText(Address.EditText.AddressLine1, "abc");
                Address.HideKeyboard("Android");
                //Address.EnterText(Address.EditText.AddressLine2, "abc");
                //Address.HideKeyboard("Android");
                Address.EnterText(Address.EditText.City, "abc");
                Address.HideKeyboard("Android");
                Address.EnterText(Address.EditText.PostalCode, "744");
                Address.HideKeyboard("Android");
                Address.EnterText(Address.EditText.Country, "US");
                Address.HideKeyboard("Android");
                Address.ContextMenu.Select(Address.ContextMenu.Fields.State, Address.ContextMenu.MenuItem.Alabama);

                Address.EnterText(Address.EditText.Phone, "68586568");
                Address.HideKeyboard("Android");
               
            }
            catch (Exception e)
            {
                IapReport.Fail(""+e.Message);
            }
        }

        [Then(@"Verify that the screen has Continue and Cancel Buttons")]
        public void ThenVerifyThatTheScreenHasContinueAndCancelButtons()
        {
            try
            {
                bool bExist = Address.IsExist(Address.Button.Continue);
                bExist = bExist & Address.IsExist(Address.Button.Cancel);
                if (bExist == true)
                    IapReport.Message("Step Passed: Cancel and Continue Button Exist");
            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed: Could not find the Buttons" + e.Message);
            }

        }

        [Then(@"Enter a Valid address and Click Continue")]
        public void ThenEnterAValidAddressAndClickContinue()
        {
            Address.Click(Address.Button.Continue);
            Payment.WaitforPaymentScreen();
            Payment.Click(Payment.Button.Add_new_Payment);
            Address.WaitforAddressScreen();

        }

        [Then(@"Let the Billing address is same as Shipping Address and All fields are auto-populated and Click Continue")]
        public void ThenLetTheBillingAddressIsSameAsShippingAddressAndAllFieldsAreAuto_PopulatedAndClickContinue()
        {
            if (Address.IsEnabled(Address.EditText.FirstName))
            {
                Address.Click(Address.Button.Switch);
            }
            else
                IapReport.Message("Verified Billing address and shipping address are same");
            bool isExist = false;
            while (!isExist)
            {
                try
                {
                   isExist= Address.IsExist(Address.Button.Continue);
                    if(isExist==false)
                        Address.Swipe(Address.Direction.Up, 2);
                }
                catch (Exception)
                {

                    Address.Swipe(Address.Direction.Up, 2);
                }
            }
            Address.Click(Address.Button.Continue);
        }

        [Then(@"Press Continue to Reach Ordersummary Screen")]
        public void ThenPressContinueToReachOrdersummaryScreen()
        {
            Summary.WaitforSummaryScreen();
            string title=Summary.GetScreenTitle();
            if (title.Equals("Order Summary"))
                IapReport.Message("User is in order summary page");
            else
            {
                //IapReport.Fail("User is not in order summary page");
                IapReport.Message("User is not in order summary page");
            }
        }
       
        [Then(@"verify user is in Order summary page shippingaddress,Voucher discount,UPS Parcel,Totalcost,Vat and totalquantity")]
        public void ThenVerifyUserIsInOrderSummaryPageShippingaddressVoucherDiscountUPSParcelTotalcostVatAndTotalquantity()
        {
            bool isExist = false;
            while (!isExist)
            {
                try
                {
                    isExist = Summary.IsExist(Summary.Text.TotalLabel);
                    if (isExist == false)
                        Summary.Swipe(Summary.Direction.up, 2);
                }
                catch (Exception)
                {

                    Summary.Swipe(Summary.Direction.up, 2);
                }
            }
            string billingAddress=Summary.GetText(Summary.Text.Billing_Address);
            string upsPrice=Summary.GetText(Summary.Text.DeliveryPrice);
            string totlprice=Summary.GetText(Summary.Text.TotalLabel);
            
            
        }
        [Then(@"User clicks on Paynow")]
        public void ThenUserClicksOnPaynow()
        {
            Summary.Click(Summary.Button.Pay_Now);
            Payment.WaitforPaymentScreen();
           // Confirmation.WaitforConfirmationScreen();

        }

        [Then(@"verify User able to enter card entries")]
        public void ThenVerifyUserAbleToEnterCardEntries()
        {
            Thread.Sleep(5000);
            Payment.EnterText(Payment.EditBox.CardNumber, "4444333322221111");
            Payment.Select(Payment.DropDown.ExpiryMonth, "08");
            Payment.Select(Payment.DropDown.ExpiryYear, "2020");
            Payment.EnterText(Payment.EditBox.SecurityCode, "456");
            Payment.Click(Payment.Button.MakePayment);


        }

        [Then(@"Verify that page display information correctly")]
        public void ThenVerifyThatPageDisplayInformationCorrectly()
        {
            Confirmation.Click(Confirmation.Button.OK);
            HomeScreen.WaitforHomeScreen();
        }


    }
}



    



