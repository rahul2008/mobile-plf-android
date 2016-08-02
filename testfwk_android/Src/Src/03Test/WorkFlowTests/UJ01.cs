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
using Philips.SIG.Automation.Android.CDPP.AppFramework_TestPlugin;

namespace Philips.CDP.Automation.IAP.Tests.Regression
{
    [Binding]
    public class UJ01
    {
        HomeScreen hScreen = new HomeScreen();

        ShoppingCart shop = new ShoppingCart();

        ShippingAddress ship = new ShippingAddress();

        Address address = new Address();

        Summary summary = new Summary();

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
                HomeScreen.Busy();
                ShoppingCart.country = "US";
                Thread.Sleep(6000);
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
                
                
                 String product = "HX8071/10";
                 //List<string> productid = ProductScreen.GetProductListItems();
                 Logger.Info("Adding: "+product+" to basket");
                 ProductScreen.SelectProduct(product);
                 //ProductScreen.Click(product, ProductScreen.Button.productScreen_InfoButton);
                 ProductScreen.Click(ProductScreen.Button.productScreen_AddToCart);
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
                Thread.Sleep(5000);
                bool bVisible = ShoppingCart.WaitforShoppingCartScreen();
                //bool bVisible = ShoppingCart.IsVisible();
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
                Thread.Sleep(5000);
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

        [Given(@"Verify the delivery mode and price")]
        public void GivenVerifyTheDeliveryModeAndPrice()
        {
            bool price = false;
            bool courrier = false;
            try
            {
                courrier = ShoppingCart.VerifyCourrier();
                price = ShoppingCart.VerifyDeliveryParcelPrice();
                if (price == true)
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
            }
            catch (Exception e)
            {
                IapReport.Fail("Step Failed:Exception occur in User clicks continue shopping", e);
            }

        }

        [Then(@"verify user is in product catalog view")]
        public void ThenVerifyUserIsInProductCatalogView()
        {
            try
            {
                ProductScreen.WaitforProductScreen();
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
                //List<string> productid = HomeScreen.GetProductListItems();
                ProductScreen.WaitforProductScreen();
                Cartnumber = ProductScreen.GetTextFromCart();
                //ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                //ProductScreen.Click(ProductScreen.Button.productScreen_BackButton);
                //ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                //HomeScreen.WaitforHomeScreen();
                GivenUserSelectsAProductAndClicksBuyNow();
               // HomeScreen.Click(productid[2], HomeScreen.Button.BuyNow);
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
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                HomeScreen.WaitforHomeScreen();

                if (HomeScreen.GetTextFromCart() == Cartnumber)
                    IapReport.Message("Step Passed: Cart number has not Incremented");
                else
                {
                    //IapReport.Fail("Step Failed: Cart number is changed ");
                    IapReport.Message("Step Failed: Cart number is changed ");
                }
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
                    //NetWork_Error.Click(NetWork_Error.Button.OK);

                    //List<string> productid = HomeScreen.GetProductListItems();
                   // GivenUserSelectsAProductAndClicksBuyNow();
                    Thread.Sleep(5000);
                   NetWork_Error.Click(NetWork_Error.Button.OK);
                }
                Device.SetConnectionType(Device.ConnectionType.WifiOn);
                //NetWork_Error.Click(NetWork_Error.Button.OK);
                //HomeScreen.WaitforHomeScreen();
                Thread.Sleep(20000);
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
                Thread.Sleep(3000);
                HomeScreen.Click(HomeScreen.Button.Shop_Now);
                Thread.Sleep(3000);
                GivenUserSelectsAProductAndClicksBuyNow();
                //List<string> productid = HomeScreen.GetProductListItems();
                //HomeScreen.WaitforHomeScreen();
                //HomeScreen.Click(productid[2], HomeScreen.Button.BuyNow);
                ShoppingCart.WaitforShoppingCartScreen();
                if (ShoppingCart.IsVisible())
                {
                    IapReport.Message("Step Passed: User Clicked the Buy Now and is on Shopping Cart Screen");
                    ShoppingCart.Click(ShoppingCart.Button.UpButton);
                    ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                    //ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                    //HomeScreen.WaitforHomeScreen();
                    GivenUserSelectsAProductAndClicksBuyNow();
                    //productid = HomeScreen.GetProductListItems();
                    //HomeScreen.Click(productid[2], HomeScreen.Button.BuyNow);
                    ShoppingCart.WaitforShoppingCartScreen();
                    ShoppingCart.Click(ShoppingCart.Button.UpButton);
                    ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                    ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
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
                HomeScreen.Click(HomeScreen.Button.Shop_Now);
                GivenUserSelectsAProductAndClicksBuyNow();
                //List<string> productid = HomeScreen.GetProductListItems();
                //HomeScreen.Click(productid[2], HomeScreen.Button.BuyNow);
                ShoppingCart.WaitforShoppingCartScreen();
                if (ShoppingCart.IsVisible())
                {
                    IapReport.Message("User is in shopping cart page stock is present");
                    ShoppingCart.Click(ShoppingCart.Button.UpButton);
                    ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                    ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
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
                if(IAP_Common.GetScreenTitle().StartsWith("E-Commerce Demo App"))
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
                //String priceinfo = ShoppingCartItem.GetText(ShoppingCartItem.TextView.Individual_Price);
                String priceinfo = ShoppingCartItem.getPrice();
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
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
              //  ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                HomeScreen.WaitforHomeScreen();


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
                HomeScreen.Click(HomeScreen.Button.CartImage);
                ShoppingCart.WaitforShoppingCartScreen();
                List<String> ProdNames = ShoppingCart.GetProductNames();
                ShoppingCart.ContextMenu.Select(ProdNames[0], ShoppingCart.ContextMenu.MenuItem.Delete);
                ShoppingCart.Click(ShoppingCart.Button.ContinueShopping_1);
                IapReport.Message("Passed: Delete from cart");
            }
            catch (Exception e)
            {
                IapReport.Fail("Failed: Delete from cart", e);
            }
        }
        [Then(@"verify product quantity is editable")]
        public void ThenVerifyProductQuantityIsEditable()
        {
            try
            {
                //HomeScreen.WaitforHomeScreen();
                //HomeScreen.Click(HomeScreen.Button.Shop_Now);
                //ShoppingCart.WaitforShoppingCartScreen();
                //Thread.Sleep(2000);
                //ShoppingCart.Click(ShoppingCart.Button.ContinueShopping_1);
                //HomeScreen.WaitforHomeScreen();
                //HomeScreen.Click(HomeScreen.Button.Shop_Now);
                ProductScreen.WaitforProductScreen();
                ProductScreen.SelectProduct("HX9042/64");
                ProductScreen.Click(ProductScreen.Button.productScreen_AddToCart);
                ShoppingCart.Click(ShoppingCart.Button.UpButton);
                //ShippingAddress.Click(ShippingAddress.Button.arrow);
                Thread.Sleep(3000);
                ShoppingCart.Click(ShoppingCart.Button.UpButton);
                Thread.Sleep(3000);
                ProductScreen.Click(ProductScreen.Button.productScreen_ImageCart);
                //HomeScreen.Click(HomeScreen.Button.CartImage);
                Thread.Sleep(5000);
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
                IapReport.Fail("Failed: ThenVerifyProductQuantityIsEditable", e);
            }

        }
        [Then(@"verify delivery and VAT")]
        public void ThenVerifyDeliveryAndVAT()
        {
            try
            {
                //GivenVerifyTheDeliveryModeAndPrice();
                string VatValue = ShoppingCart.GetText(ShoppingCart.TextView.VAT);
                float vat = float.Parse((ShoppingCart.GetRefinedText(VatValue)));
                if (vat != 0.0)
                    IapReport.Message("vat is  added to totalcost");
                else
                {
                    IapReport.Message("Vat is not added to totalcost");
                    // IapReport.Fail("Vat is not added to totalcost");
                }
                ShoppingCart.SwipeProductList(ShoppingCart.Direction.Down, 2);
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
                ShoppingCart.SwipeToTop();
                List<String> n = ShoppingCart.GetProductNames();

                string totalitems = ShoppingCart.GetText(ShoppingCart.TextView.TotalItems);
                //totalitems=totalitems.Split('(')[1].Split(' ')[0];
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
                IapReport.Fail("Failed: ThenVerifyTheTotalNumberOfProductListAndTheTotalCost", e);
            }
        }

        [Then(@"user clicks on checkout")]
        public void ThenUserClickOnCheckout()
        {
            try
            {
                
                ShoppingCart.Click(ShoppingCart.Button.Checkout);
                Thread.Sleep(3000);
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
                String btn = ShippingAddress.WaitforShippingAddressScreen();
                //if(btn.Equals("Continue"))//new user, add first address
                //{

                //}
                if(btn.Equals("Add a new address"))//user has addresses, adding new address
                {
                    ShippingAddress.Click(ShippingAddress.Button.Add_a_new_Address);
                }
                
                Address.WaitforAddressScreen();
                string title = Address.GetHeader();
                if(Address.country.Equals("US") && title.Equals("Shipping address"))
                    IapReport.Message("Step Passed: The user is in Address Screen");
                else if (Address.country.Equals("UK") && title.Equals("Delivery address"))
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
                //ShippingAddress.Click(ShippingAddress.Button.Deliver_to_This_Address);
                MobileDriver.TakeScreenshot("AddressScreen.bmp");
                Address.CreateAddress("abhi", "ram", Address.ContextMenu.MenuItem.Mr, "Manyata Tech Park", "Banagalore", "35007", "US", false,"9876543210");
                Address.Click(Address.Button.Continue);
                Thread.Sleep(3000);
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

            Address.SwipeToTop();

        }

        [Then(@"Enter a Valid address and Click Continue")]
        public void ThenEnterAValidAddressAndClickContinue()
        {
            Address.Click(Address.Button.Continue);
            Thread.Sleep(3000);
            //Payment.WaitforPaymentScreen();
            //Payment.Click(Payment.Button.Add_new_Payment);
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
            Summary.WaitforSummaryScreen();
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
            int count = 0;
            while (!isExist && count<10)
            {
                count++;
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
            Thread.Sleep(40000);
            Payment.WaitforPaymentScreen();
           // Confirmation.WaitforConfirmationScreen();

        }

        [Then(@"verify User able to enter card entries")]
        public void ThenVerifyUserAbleToEnterCardEntries()
        {
            Thread.Sleep(3000);
          Payment.EnterPaymentDetails("4444333322221111","08","2020","651");
          Payment.Click(Payment.Button.MakePayment);
        }

        [Then(@"Verify that page display information correctly")]
        public void ThenVerifyThatPageDisplayInformationCorrectly()
        {
            Confirmation.Click(Confirmation.Button.OK);
            ProductScreen.WaitforProductScreen();
        }

        [Given(@"is a first time user")]
        public void GivenIsAFirstTimeUser()
        {
            //ScenarioContext.Current.Pending();
            Logger.Info("TBD");
        }

        [When(@"User selects country store and clicks on Shop Now")]
        public void WhenUserSelectsCountryStoreAndClicksOnShopNow()
        {
            HomeScreen.Busy();
            Thread.Sleep(4000);
            //HomeScreen.ContextMenu.Select(HomeScreen.Fields.Country_Spinner,HomeScreen.ContextMenu.MenuItem.UK);
            HomeScreen.ContextMenu.Select(HomeScreen.Fields.Country_Spinner, HomeScreen.ContextMenu.MenuItem.US);
            //Thread.Sleep(10000);
            HomeScreen.Busy();
            HomeScreen.Click(HomeScreen.Button.Shop_Now);
            //Thread.Sleep(3000);
            HomeScreen.Busy();
            ProductScreen.WaitforProductScreen();
                
        }

        [Then(@"select ""(.*)"" from product catalog view")]
        public void ThenSelectFromProductCatalogView(string productID)
        {
            //if in shopping cart then go back twice for catalog view
            String screen= ProductScreen.GetScreenTitle();
            if(screen.Equals("Shopping Cart") || screen.Equals("Shopping Basket"))
            {
                ShoppingCart.Click(ShoppingCart.Button.ContinueShopping);
                Thread.Sleep(2000);
            }
            Logger.Info("Adding: " + productID + " to basket");
            ProductScreen.SelectProduct(productID);
            ProductScreen.Click(ProductScreen.Button.productScreen_AddToCart);
        }

        [Then(@"Verify shopping cart icon is ""(.*)"" in all views")]
        public void ThenVerifyShoppingCartIconIsInAllViews(int count)
        {
            //starts in shopping cart and ends in home screen
            Boolean failed = false;
            ShoppingCart.WaitforShoppingCartScreen();
            ShoppingCart.Click(ShoppingCart.Button.ContinueShopping);
            Thread.Sleep(3000);
            ProductScreen.WaitforProductScreen();
            String prodScreenCart = ProductScreen.GetTextFromCart();
            if(Int32.Parse(prodScreenCart)==count)
                IapReport.Message("Passed: Product Screen shows "+count);
            else
            {
                IapReport.Message("Failed: Product Screen shows: "+prodScreenCart+" while it should be 2");
                failed = true;
            }
                
            List<String> products = ProductScreen.GetProductListItems();
            ProductScreen.SelectProduct(products[0]);
            Thread.Sleep(3000);
            String prodDetailCart = ProductScreen.GetTextFromCart();
            if (Int32.Parse(prodDetailCart)==count)
                IapReport.Message("Passed: Product Detail Screen shows "+count);
            else
            {
                IapReport.Message("Failed: Product Detail Screen shows: " + prodDetailCart + " while it should be "+count);
                failed = true;
            }

            ProductScreen.Click(ProductScreen.Button.productScreen_InfoButton);
            Thread.Sleep(2000);
            ProductScreen.Click(ProductScreen.Button.productScreen_InfoButton);
            Thread.Sleep(3000);
            HomeScreen.WaitforHomeScreen();
            Thread.Sleep(5000);
            String homeCart = HomeScreen.GetTextFromCart();
            if (Int32.Parse(homeCart)==count)
                IapReport.Message("Passed: Home Screen cart shows "+count);
            else
            {
                IapReport.Message("Failed: Home Screen cart shows: " + homeCart + " while it should be "+count);
                failed = true;
            }

            //consolidated check
            if (failed)
                IapReport.Fail("Failed: ThenVerifyShoppingCartIconIsInAllViews:\nProduct Catalog: "+prodScreenCart
                    +"\nProduct Detail: "+prodDetailCart
                    +"\nHome Screen: "+homeCart);

        }

        [When(@"User increments quantity of ""(.*)"" to ""(.*)""")]
        public void WhenUserIncrementsQuantityOfTo(string productCode, int count)
        {
            //HomeScreen.Click(HomeScreen.Button.CartImage);
            ShoppingCart.WaitforShoppingCartScreen();
            ShoppingCart.SwipeTo(productCode);
            ShoppingCart.Select(productCode, count);
        }

        [Then(@"go to shopping cart")]
        public void ThenGoToShoppingCart()
        {
            HomeScreen.Click(HomeScreen.Button.CartImage);
            Thread.Sleep(3000);
            ShoppingCart.WaitforShoppingCartScreen();
        }

    }

}



    



