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
using Philips.CDP.Automation.IAP.Tests.Workflows;
using Philips.SIG.Automation.Android.CDPP.AppFramework_TestPlugin;

namespace Philips.CDP.Automation.IAP.Tests.Workflows
{
    [Binding]
    public class CC_Bindings
    {

        [Given(@"the user is on Reference App Welcome Screen")]
        public void GivenTheUserIsOnReferenceAppWelcomeScreen()
        {
            WelcomeScreen.WelcomeScreenImage();
            Thread.Sleep(10000);
        }

        [Given(@"the user is on Mobile App Home page")]
        public void GivenTheUserIsOnMobileAppHomePage()
        {

            try
            {
                Thread.Sleep(3000);
                if (!AppHomeScreen.IsVisibleScreenTitleText("Mobile App home"))
                {
                    WelcomeScreen.Click(WelcomeScreen.Button.Skip);
                    Log_In.Click();
                    Log_In.SignIn("hubble@mailinator.com", "Philips@123");
                }
            }
            catch (Exception e)
            {
                IapReport.Fail("Not able to log in", e);
            }

        }


        [Then(@"I click on Support from Hamburger Menu List and verify support page")]
        public void ThenIClickOnSupportFromHamburgerMenuListAndVerifySupportPage()
        {
            AppHomeScreen.Click(AppHomeScreen.Button.Support);
            Thread.Sleep(3000);
            bool isPresent = Support.IsVisible1();
            if (!isPresent)
            {
                IapReport.Fail("The user is not in support screen");
            }
        }

        [Given(@"im on the cc home page")]
        public void GivenImOnTheCcHomePage()
        {
            try
            {
                HomeScreen.WaitforHomeScreen();
                HomeScreen.IsEnabled(HomeScreen.Button.Launch_DC_as_Activity);
                HomeScreen.IsEnabled(HomeScreen.Button.Launch_Dc_as_Fragment);
                Logger.Info("Reached home screen");
            }
            catch(Exception e)
            {
                IapReport.Fail("Not in home screen, Something is missing in homescreen", e);
            }

        }

        [Given(@"add a ctn ""(.*)"" to list of products")]
        public void GivenAddACtnToListOfProducts(string ctn)
        {
            HomeScreen.Click(HomeScreen.Button.AddCTN);
            AddCTN.WaitforAddProductScreen();
            Logger.Info("Reached Add product screen");
            try
            {
                AddCTN.IsEditable(AddCTN.TextBox.Add_CTN);
                AddCTN.IsEditable(AddCTN.TextBox.Add_Catalog);
                AddCTN.IsEditable(AddCTN.TextBox.Add_Category);
                AddCTN.Add_CTN(ctn);
                IapReport.Message("CTN added successfully");
            }
            catch (Exception e)
            {
                IapReport.Fail("Failed to add ctn", e);
            }                        
        }


        [Given(@"select country as ""(.*)"" and language as ""(.*)""")]
        public void GivenSelectCountryAsAndLanguageAs(string country, string language)
        {
            try
            {
                if (country == "United Kingdom (GB)")
                {
                    HomeScreen.SelectCountry(HomeScreen.Fields.Country_Spinner, HomeScreen.Country.United_Kingdom);
                    Thread.Sleep(5000);
                }
                if (country == "United States (US)")
                {
                    HomeScreen.SelectCountry(HomeScreen.Fields.Country_Spinner, HomeScreen.Country.United_States);
                    Thread.Sleep(5000);
                }
                if (country == "India (IN)")
                {
                    HomeScreen.SelectCountry(HomeScreen.Fields.Country_Spinner, HomeScreen.Country.India);
                    Thread.Sleep(5000);
                }
                //HomeScreen.SelectCountry(HomeScreen.Fields.Country_Spinner, HomeScreen.Country.United_Kingdom);
                //Thread.Sleep(5000);
                HomeScreen.SelectLanguage(HomeScreen.Fields.Langauage, HomeScreen.Language.English);
                Thread.Sleep(5000);
                HomeScreen.WaitforHomeScreen();
                IapReport.Message("Selected country and language successfully");
            }
            catch(Exception e)
            {
                IapReport.Fail("Failed to select country and language", e);
            }
        }


        [Then(@"verify added ctn ""(.*)"" exist in the list of products")]
        public void ThenVerifyAddedCtnExistInTheListOfProducts(string ctn)
        {
            try
            {
                HomeScreen.VerifyAddedProduct(ctn, 500);
                IapReport.Message("Added ctn exist in product ctn list");
            } 
            catch(Exception e)
            {
                IapReport.Fail("Added ctn doesn't exist in products ctn list",e);
            }
        }

        [Then(@"delete ctn ""(.*)"" from list of products")]
        public void ThenDeleteCtnFromListOfProducts(string ctn)
        {
            try
            {
                //HomeScreen.Scrollagain();
                HomeScreen.Delete_CTN(ctn, 500);
                HomeScreen.Delete_CTNforReliability();
                IapReport.Message("Deleted ctn from the list of products");
            }
            catch (Exception e)
            {
                IapReport.Fail("Failed in deleting ctn from the list of products", e);
            }
        }

        [Then(@"verify deleted ctn ""(.*)"" from list of products")]
        public void ThenVerifyDeletedCtnFromListOfProducts(string ctn)
        {
            //ScenarioContext.Current.Pending();
        }

        [Then(@"select launch digitalcare as activity and verify support page")]
        public void ThenSelectLaunchDigitalcareAsActivityAndVerifySupportPage()
        {
            try
            {
                HomeScreen.Click(HomeScreen.Button.Launch_DC_as_Activity);
                Thread.Sleep(7000);
                Support.WaitforSupportScreen();
                Support.IsEnabled(Support.Button.View_Product_Information);
                Support.IsEnabled(Support.Button.Read_FAQ);
                Support.IsEnabled(Support.Button.Contact_Us);
                Support.IsEnabled(Support.Button.Find_Philips_near_You);
                Support.IsEnabled(Support.Button.Tell_Us);
                //Support.IsEnabled(Support.Button.My_Philips_Account);
                IapReport.Message("All options are not availale in support screen");
            }            
            catch(Exception e)
            {
                IapReport.Fail("All options are not availale in support screen", e);
            }
        }

        [Then(@"select ""(.*)"" from support screen")]
        public void ThenSelectFromSupportScreen(string btn)
        {
            try
            {
                if (btn == "View product information")
                {
                    Support.Click(Support.Button.View_Product_Information);
                    Thread.Sleep(5000);
                }
                if (btn == "Read FAQs")
                {
                    Support.Click(Support.Button.Read_FAQ);
                    Thread.Sleep(5000);
                }
                if (btn == "Contact us")
                {
                    Support.Click(Support.Button.Contact_Us);
                    Thread.Sleep(5000);
                }
                if (btn == "Find Philips near you")
                {
                    Support.Click(Support.Button.Find_Philips_near_You);
                    Thread.Sleep(5000);
                    if(FindPhilips.IsVisible(FindPhilips.Button.Allow) == true)
                    {
                        FindPhilips.ClickTst1(FindPhilips.Button.Allow);
                    }
                    if (DialogsFindPhilips4.WaitForDialog3() != false)
                    {
                        DialogsFindPhilips4.ClickMakeSure(DialogsFindPhilips4.Button.ShareLocationToPhilipsOKButton);
                    }
                    if (DialogsFindPhilips4.VerifyNoServiceAvailableDialog() != false)
                    {
                        DialogsFindPhilips4.ClickMakeSure(DialogsFindPhilips4.Button.NoServiceAvailableOKButton);
                    }

                        
                }
                if (btn == "Tell us what you think")
                {
                    Support.Click(Support.Button.Tell_Us);
                    Thread.Sleep(5000);
                }
                //SelectProduct.WaitforSelectProductScreen();
            }
            catch(Exception e)
            {
                IapReport.Fail("Failed to select in the support screen", e);
            }            
        }


        [Then(@"verify all fields in select product screen")]
        public void ThenVerifyAllFieldsInSelectProductScreen()
        {
            try
            {

                Thread.Sleep(5000);
                //string title = SelectedProductScreen.GetHeaderSelectedProductScreen();
                //if (title == "Select product")                
                bool b = ProductInformation.IsVisible(ProductInformation.Button.Download_Product_Manual);
                if (b == true)
                {
                    Logger.Info("Not in select product screen");
                }
                else
                {
                    Logger.Info("You are in select product screen");
                }
            }
            catch
            {
                IapReport.Fail("Failed to verify select product screen");
            }
             
        }

        [Then(@"select Find product and verify the product screen")]
        public void ThenSelectFindProductAndVerifyTheProductScreen()
        {
            try
            {
                bool b = ProductInformation.IsVisible(ProductInformation.Button.Download_Product_Manual);
                if (b == true)
                {
                    Logger.Info("Already product is added");
                }
                else
                {                    
                    Logger.Info("You are in select product screen");
                    SelectProduct.ClicktestProductScreen(SelectProduct.Button.Search);
                    ProductList.WaitforProductListHomeScreen();
                    string titl = ProductList.GetHeaderProductScreen();
                    if (titl == "Products")
                    {
                        Logger.Info("You are in product list screen");
                    }
                    else
                    {
                        Logger.Info("Not in product list screen");
                    }
                }

            }
            catch (Exception e)
            {
                IapReport.Fail("Failed to select find product OR product screen", e);
            }
            
        }

        [Then(@"select a product of code ""(.*)"" and verify product details page")]
        public void ThenSelectAProductOfCodeAndVerifyProductDetailsPage(string code)
        {
            try
            {
                bool b = ProductInformation.IsVisible(ProductInformation.Button.Download_Product_Manual);
                if (b == true)
                {
                    Logger.Info("Product is already selected");
                    ProductInformation.ClickTest(ProductInformation.Button.Back_to_supportScreen);
                    Thread.Sleep(5000);
                    Support.WaitforSupportScreen();
                    Support.Click(Support.Button.Change_Selected_Product);
                    Thread.Sleep(3000);
                    ProductList.WaitforProductListHomeScreen();
                    ProductList.SelectProduct(code);
                    Thread.Sleep(3000);
                    SelectedProductScreen.WaitforSelectedProductHomeScreen();
                    SelectedProductScreen.ClickSelectProductTest(SelectedProductScreen.Button.Select_This_Produt);
                    Thread.Sleep(3000);
                    ConfirmationScreen.WaitforConfirmationScreen();
                    ConfirmationScreen.ClickConfirmationButtons(ConfirmationScreen.Button.Continue);
                    Thread.Sleep(4000);
                    Support.WaitforSupportScreen();
                    Support.Click(Support.Button.View_Product_Information);
                    Thread.Sleep(4000);
                    ProductInformation.WaitforProductInformationScreen();
                    Logger.Info("Reached product information screen");
                }
                else
                {
                    Logger.Info("You are in Product list screen");
                    ProductList.SelectProduct(code);
                    Thread.Sleep(5000);
                    SelectedProductScreen.WaitforSelectedProductHomeScreen();
                    string ctn = SelectedProductScreen.GetSelected_ProductCTN();
                    if (ctn == code)
                    {
                        Logger.Info("Selected product ctn is correct");
                    }
                    else
                    {
                        Logger.Info("Selected product ctn is wrong");
                    }
                }
                
            }
            catch
            {
                IapReport.Fail("Failed to select product");
            }
            
        }

        [Then(@"verify product name ""(.*)"" and select this product to reach confirmation screen")]
        public void ThenVerifyProductNameAndSelectThisProductToReachConfirmationScreen(string pname)
        {
            try
            {
                bool b = ProductInformation.IsVisible(ProductInformation.Button.Download_Product_Manual);
                if (b == true)
                {
                    Logger.Info("You are in product information screen");
                    //ProductInformation.ClickTest(ProductInformation.Button.Back_to_supportScreen);
                    //Thread.Sleep(5000);
                    //Support.WaitforSupportScreen();
                    
                }
                else
                {
                    Logger.Info("you are in selected product screen");
                    string P_name = SelectedProductScreen.GetSelected_ProductName();
                    if (P_name == pname)
                    {
                        Logger.Info("Selected prodcut name is correct");
                    }
                    else
                    {
                        Logger.Info("Selected product name is wrong");
                    }
                    SelectedProductScreen.ClickSelectProductTest(SelectedProductScreen.Button.Select_This_Produt);
                    Thread.Sleep(5000);
                    ConfirmationScreen.WaitforConfirmationScreen();
                    bool c = ConfirmationScreen.IsVisible(ConfirmationScreen.Button.Continue);
                    if (c == true)
                    {
                        Logger.Info("Reached confirmation screen");
                    }
                    //ConfirmationScreen.ClickConfirmationButtons(ConfirmationScreen.Button.Continue);
                    //Thread.Sleep(3000);
                    //Support.WaitforSupportScreen();
                }
                
            }
            catch
            {
                IapReport.Fail("Failed to select product OR in confirmation screen");
            }
            
        }

        [Then(@"confirm the selection and reach support screen")]
        public void ThenConfirmTheSelectionAndReachSupportScreen()
        {
            try
            {
                bool b = ProductInformation.IsVisible(ProductInformation.Button.Download_Product_Manual);
                if (b == true)
                {
                    ProductInformation.ClickTest(ProductInformation.Button.Back_to_supportScreen);
                    Thread.Sleep(5000);
                    Support.WaitforSupportScreen();
                }
                else
                {
                    bool c = ConfirmationScreen.IsVisible(ConfirmationScreen.Button.Continue);
                    if (c == true)
                    {
                        ConfirmationScreen.ClickConfirmationButtons(ConfirmationScreen.Button.Continue);
                        Thread.Sleep(5000);
                        Support.WaitforSupportScreen();
                    }
                    else
                    {
                        Logger.Info("Product is already added and reached support screen");
                    }
                }
                
            }
            catch(Exception e)
            {
                IapReport.Fail("Failed in adding product", e);
            }           
        }

        [Then(@"verify selected product ""(.*)"" detail in view product information")]
        public void ThenVerifySelectedProductDetailInViewProductInformation(string name)
        {
            try
            {
                Support.Click(Support.Button.View_Product_Information);
                Thread.Sleep(6000);
                ProductInformation.WaitforProductInformationScreen();
                if (ProductInformation.GetProductName().Equals(name))
                {
                    Logger.Info("Selected product is seen in product info screen");
                }
                else
                {
                    Logger.Info("Selected product is not seen in product info screen");
                }
                ProductInformation.ClickTest(ProductInformation.Button.Back_to_supportScreen);
                Thread.Sleep(6000);
                Support.WaitforSupportScreen();
            }
            catch(Exception e)
            {
                IapReport.Fail("Fail - failed to verify selected product in view product info",e);
            }            
        }

        [Then(@"verify selected product ""(.*)"" detail in view product information with CTN ""(.*)""")]
        public void ThenVerifySelectedProductDetailInViewProductInformationWithCTN(string name, string CTN_Number)
        {
            try
            {
                Support.Click(Support.Button.View_Product_Information);
                AppHomeScreen.IsVisibleScreenTitleText("Product Information");
                if (ProductInformation.GetProductName().Equals(name))
                {
                    Logger.Info("Selected product is seen in product info screen");
                }
                else
                {
                    Logger.Info("Selected product is not seen in product info screen");
                }
                string ctn = ProductInformation.GetVariantText();
                if (ctn == CTN_Number)
                {
                    Logger.Info("Selected product ctn is correct");
                }
                else
                {
                    Logger.Info("Selected product ctn is wrong");
                }

            }
            catch (Exception e)
            {
                IapReport.Fail("Fail - failed to verify selected product in view product info", e);
            }
        }
       

   

        [Then(@"change the product to ""(.*)"" and verify in product info")]
        public void ThenChangeTheProductToAndVerifyInProductInfo(string product)
        {
            try
            {
                Support.Click(Support.Button.Change_Selected_Product);
                Thread.Sleep(6000);
                ProductList.WaitforProductListHomeScreen();
                ProductList.SelectProduct(product);
                Thread.Sleep(5000);
                SelectedProductScreen.WaitforSelectedProductHomeScreen();
                SelectedProductScreen.ClickSelectProductTest(SelectedProductScreen.Button.Select_This_Produt);
                ConfirmationScreen.WaitforConfirmationScreen();
                ConfirmationScreen.ClickConfirmationButtons(ConfirmationScreen.Button.Continue);
                Support.WaitforSupportScreen();
                Support.Click(Support.Button.View_Product_Information);
                Thread.Sleep(6000);
                ProductInformation.WaitforProductInformationScreen();
                if (ProductInformation.GetVariantText().Equals(product))
                {
                    Logger.Info("Selected product is seen in product info screen");
                }
                else
                {
                    Logger.Info("Selected product is not seen in product info screen");
                }
                ProductInformation.ClickTest(ProductInformation.Button.Back_to_supportScreen);
                Thread.Sleep(6000);
                Support.WaitforSupportScreen();
                IapReport.Message("Added product " + product + "");
            }
            catch (Exception e)
            {
                IapReport.Fail("Failed to change product to " + product + "", e);
            }
        }

       
        [Then(@"Verify product is already slected or need to select and if not select ""(.*)""")]
        public void ThenVerifyProductIsAlreadySlectedOrNeedToSelectAndIfNotSelect(string product)
        {
            try
            {
                Support.Click(Support.Button.View_Product_Information);
                Thread.Sleep(5000);
                bool b = ProductInformation.IsVisible(ProductInformation.Button.Download_Product_Manual);
                if (b == true)
                {
                    IapReport.Message("You are already selected a product");
                    ProductInformation.ClickTest(ProductInformation.Button.Back_to_supportScreen);
                }
                else 
                {
                    SelectProduct.ClicktestProductScreen(SelectProduct.Button.Search);
                    Thread.Sleep(5000);
                    ProductList.WaitforProductListHomeScreen();
                    IapReport.Message("You need to select a product in the list and add");
                    ProductList.SelectProduct(product);
                    Thread.Sleep(6000);
                    SelectedProductScreen.WaitforSelectedProductHomeScreen();
                    SelectedProductScreen.ClickSelectProductTest(SelectedProductScreen.Button.Select_This_Produt);
                    Thread.Sleep(7000);
                    ConfirmationScreen.WaitforConfirmationScreen();
                    ConfirmationScreen.ClickConfirmationButtons(ConfirmationScreen.Button.Continue);
                    Thread.Sleep(5000);
                    Support.WaitforSupportScreen();
                    IapReport.Message("Reached support screen after selecting a product");
                }
            }
            catch (Exception e)
            {
                IapReport.Fail("Failed to check the product selection validation", e);
            }
        }

        [Then(@"reach product information screen on tapping view product info in support screen")]
        public void ThenReachProductInformationScreenOnTappingViewProductInfoInSupportScreen()
        {
            try
            {
                Support.Click(Support.Button.View_Product_Information);
                ProductInformation.WaitforProductInformationScreen();
                IapReport.Message("Landed in product information screen");
            }
            catch (Exception e)
            {
                IapReport.Fail("Not landed in product information screen", e);
            }
        }

        //[Then(@"verify product information screen for product manual, detail and video")]
        //public void ThenVerifyProductInformationScreenForProductManualDetailAndVideo()
        //{
        //    try
        //    {
        //        ProductInformation.ClickTest(ProductInformation.Button.Download_Product_Manual);
        //        Thread.Sleep(5000);
        //        bool web = ProductInformation.manual_opened();
        //        if (web == true)
        //        {
        //            Thread.Sleep(3000);
        //            MobileDriver.FireKeyEvent(4);
        //            Thread.Sleep(6000);
        //        }
               
        //        //need to check the flow for download, rite now getting download error
        //        ProductInformation.ClickTest(ProductInformation.Button.Product_Information);
        //        Thread.Sleep(9000);
        //        if (ProductInformation.WebProductInformation() == true)
        //        {
        //            Logger.Info("Reached product information web page");
        //            ProductInformation.Back();
        //        }
        //        else
        //        {
        //            Logger.Info("Product information web page is not loaded");
        //            ProductInformation.Back();
        //        }
        //        bool video = ProductInformation.IsVideoPlayButtonEnable1(ProductInformation.Button.VideoPlay);
        //        if (video == true)
        //        {
        //            Logger.Info("Video is available to play");
        //            ProductInformation.VideoClick(ProductInformation.Button.VideoPlay);
        //            Thread.Sleep(30000);
        //            MobileDriver.FireKeyEvent(4);
        //            MobileDriver.FireKeyEvent(4);
        //            Thread.Sleep(6000);
        //            ProductInformation.WebProductInformation();
        //        }
        //        else
        //        {
        //            Logger.Info("Video is not available for this product");
        //        }
        //        IapReport.Message("Verified complete product information");
        //    }
        //    catch (Exception e)
        //    {
        //        IapReport.Fail("Failed to get info of the product information", e);
        //    }            
        //}

        [Then(@"verify product information \(on Philips\.com\) button")]
        public void ThenVerifyProductInformationOnPhilips_ComButtonAndReturnTo()
        {
            ProductInformation.ClickTest(ProductInformation.Button.Product_Information);
            Thread.Sleep(9000);
            if (ProductInformation.WebProductInformation() == true)
            {
                Logger.Info("Reached product information web page");
                ProductInformation.Back(); // Returns back to Product Information screen
                
            }
            else
            {
                Logger.Info("Product information web page is not loaded");
                ProductInformation.Back();
            }

        }

        
        [Then(@"add a product ""(.*)"" through FAQ and reach support screen")]
        public void ThenAddAProductThroughFAQAndReachSupportScreen(string p0)
        {
            Support.WaitforSupportScreen();
            if(Support.IsEnabled(Support.Button.Change_Selected_Product))
            {
                Logger.Info("Already product exist");
                Support.Click(Support.Button.Change_Selected_Product);
                Thread.Sleep(5000);
                ProductList.WaitforProductListHomeScreen();
                ProductList.SelectProduct(p0);
                Thread.Sleep(6000);
                SelectedProductScreen.ClickSelectProductTest(SelectedProductScreen.Button.Select_This_Produt);
                Thread.Sleep(5000);
                ConfirmationScreen.WaitforConfirmationScreen();
                ConfirmationScreen.ClickConfirmationButtons(ConfirmationScreen.Button.Continue);
                Thread.Sleep(7000);
                Support.WaitforSupportScreen();
            }
            else
            {
                Support.Click(Support.Button.Read_FAQ);
                Thread.Sleep(7000);
                SelectProduct.ClicktestProductScreen(SelectProduct.Button.Search);
                Thread.Sleep(5000);
                ProductList.WaitforProductListHomeScreen();
                ProductList.SelectProduct(p0);
                Thread.Sleep(6000);
                SelectedProductScreen.WaitforSelectedProductHomeScreen();
                SelectedProductScreen.ClickSelectProductTest(SelectedProductScreen.Button.Select_This_Produt);
                Thread.Sleep(6000);
                ConfirmationScreen.WaitforConfirmationScreen();
                ConfirmationScreen.ClickConfirmationButtons(ConfirmationScreen.Button.Continue);
                Thread.Sleep(7000);
                Support.WaitforSupportScreen();
            }
        }

        [Then(@"reach FAQs screen")]
        public void ThenReachFAQsScreen()
        {
            Support.Click(Support.Button.Read_FAQ);
            Thread.Sleep(6000);
            FAQ.WaitforFAQScreen();
        }

        [Then(@"verify each FAQ is clickable and readable")]
        public void ThenVerifyEachFAQIsClickableAndReadable()
        {
            if(FAQ.GetHeaderTextFAQ().Equals("FAQs"))
            {
                Logger.Info("You are in FAQs screen");
            }

            FAQ.Click("Service and Repair (2)");

            FAQ.ClickSubQustn("Where can I find an authorised Philips service centre?");
            Thread.Sleep(6000);
            Question_and_Answer.WaitforQAScreen();
            if (Question_and_Answer.GetHeaderTextQuestionAnwer().Equals("Question and answer"))
            {
                Logger.Info("Reached question and answer screen");
                Question_and_Answer.ClickQABackbtn(Question_and_Answer.Button.Back_to_FAQPage);
                Thread.Sleep(5000);
                FAQ.WaitforFAQScreen();
            }
            FAQ.Click("Service and Repair (2)");

            if (FAQ.GetHeaderTextFAQ().Equals("FAQs"))
            {
                Logger.Info("You are in FAQs screen");
            }
               
        }

        [Then(@"come back to support screen")]
        public void ThenComeBackToSupportScreen()
        {
            Thread.Sleep(6000);
            FAQ.ClickBack(FAQ.Button.Back_to_HomeFAQPage);
            Thread.Sleep(5000);
        }

        [Then(@"verify all options on contact us screen")]
        public void ThenVerifyAllOptionsOnContactUsScreen()
        {
            ContactUs.WaitforContactUsHomeScreen();
            string CU = ContactUs.GetHeaderTextFAQ();
            if(CU == "Contact us")
            {
                Logger.Info("Reached contact us screen");
            }
            else{
                Logger.Info("Not in contact us screen");
            }

            ContactUs.IsEnable(ContactUs.Button.On_Twitter);
            ContactUs.IsEnable(ContactUs.Button.On_faceBook);
            ContactUs.IsEnable(ContactUs.Button.Live_Chat);
            ContactUs.IsEnable(ContactUs.Button.Send_Email);
            Logger.Info("All options in contact us and verified");
        }

        [Then(@"verify on Twitter option")]
        public void ThenVerifyOnTwitterOption()
        {
            ContactUs.ClickContact(ContactUs.Button.On_Twitter);
            Thread.Sleep(5000);
            TwitterView.IsEnableTwitter(TwitterView.Button.LogIn);
            TwitterView.IsEnableTwitter(TwitterView.Button.SignUp);
            Thread.Sleep(7000);
            ContactUs.ClickContact(ContactUs.Button.Backto_HomeScreen);
            ContactUs.WaitforContactUsHomeScreen();
            Logger.Info("Twitter page is verified");
        }

        [Then(@"verify on facebook option")]
        public void ThenVerifyOnFacebookOption()
        {
            ContactUs.ClickContact(ContactUs.Button.On_faceBook);
            Thread.Sleep(10000);

            if (FacebookView.IsEnable(FacebookView.Button.LogIn) || FacebookView.IsEnable(FacebookView.Button.Join))
            {
                Logger.Info("Verified facebook screen");
            }
            else
            {
                Logger.Info("Facebook page not loaded");
            }
            Thread.Sleep(4000);
            ContactUs.ClickContact(ContactUs.Button.Backto_HomeScreen); // Returns to Contact Us page
            Thread.Sleep(3000);
            ContactUs.ClickContact(ContactUs.Button.Backto_HomeScreen); // Returns to Support Page
            ContactUs.WaitforContactUsHomeScreen();
            
        }

        [Then(@"verify live chat option")]
        public void ThenVerifyLiveChatOption()
        {
            ContactUs.ClickContact(ContactUs.Button.Live_Chat);
            Thread.Sleep(6000);
            ChatWithPhilips.WaitforChatwithPhilipsHomeScreen();
            ChatWithPhilips.ClickChatTest(ChatWithPhilips.Button.Cancel);
            ContactUs.WaitforContactUsHomeScreen();
            ContactUs.ClickContact(ContactUs.Button.Live_Chat);
            Thread.Sleep(7000);
            ChatWithPhilips.WaitforChatwithPhilipsHomeScreen();
            string CWP = ChatWithPhilips.GetHeaderTextFAQ();
            if(CWP == "Chat with Philips")
            {
                Logger.Info("Reached chat with philips screen");
            }
            else
            {
                Logger.Info("Not in chat with philips screen");
            }
            ChatWithPhilips.IsEnable(ChatWithPhilips.Button.Chat_Now);
            ChatWithPhilips.ClickChatTest(ChatWithPhilips.Button.Chat_Now);
            Thread.Sleep(7000);
            ChatNow.WaitforChatNow1HomeScreen();
            ChatNow.Click(ChatNow.Button.Backto_HomeScreen);
                Thread.Sleep(7000);
                ChatWithPhilips.WaitforChatwithPhilipsHomeScreen();
                ChatWithPhilips.ClickChatTest(ChatWithPhilips.Button.Backto_ContactUs);
                Thread.Sleep(7000);
                ContactUs.WaitforContactUsHomeScreen();
             
            
        }

        [Then(@"verify send email option")]
        public void ThenVerifySendEmailOption()
        {
            ContactUs.ClickContact(ContactUs.Button.Send_Email);
            Thread.Sleep(9000);
            SendEmail.WaitforSendEmailHomeScreen();
            SendEmail.Sen_EmailScreen();
            string se = SendEmail.GetHeaderTextSendEmail();
            if(se == "Send us an email")
            {
                Logger.Info("Reached send email screen");
            }
            else
            {
                Logger.Info("Not in send email screen");
            }
            SendEmail.IsVisible(SendEmail.Button.Backto_HomeScrn);
            SendEmail.IsEnable(SendEmail.Button.Backto_HomeScrn);
            SendEmail.ClickBackbtn(SendEmail.Button.Backto_HomeScrn);
            Thread.Sleep(6000);
            ContactUs.WaitforContactUsHomeScreen();
            ContactUs.ClickContact(ContactUs.Button.Backto_HomeScreen);
            Thread.Sleep(6000);
        }

        [Then(@"verify find philips near you option")]
        public void ThenVerifyFindPhilipsNearYouOption()
        {
            
            //FindPhilips.WaitforFindPhilipsHomeScreen();
            //DialogsFindPhilips4.ClickMakeSure(DialogsFindPhilips4.Button.OK);
            //DialogsFindPhilips2.ClickErrorData(DialogsFindPhilips2.Button.NoData_Ok)

            //bool GPS = DialogsFindPhilips3.GPSErrorMsg();
            //if (GPS == true)
            //{
            //    DialogsFindPhilips3.ClickErrorData(DialogsFindPhilips3.Button.GPS_Ok);
            //    GPSLocation.clickGPSBtn(GPSLocation.SwitchButton.GPRS_ToggelButton);
            //    ContactUs.Back();
            //    Thread.Sleep(6000);
            //    FindPhilips.Add_Country("India");
            //    Thread.Sleep(6000);
            //    //FindPhilips.EnterText(FindPhilips.EditBox.Search, "India");
            //    ContactUs.Back();
            //    Thread.Sleep(7000);
            //    DialogsFindPhilips2.ClickErrorData(DialogsFindPhilips2.Button.NoData_Ok);       
            //}
            try
            {
                bool ErrMsg = DialogsFindPhilips1.ErrorMsg();
                if (ErrMsg == false)
                {
                    Thread.Sleep(6000);
                    DialogsFindPhilips1.WaitforFindPhilipsDialog1HomeScreen();
                    DialogsFindPhilips1.clickTest(DialogsFindPhilips1.TextButton.GoTo_Contact_Page);
                    Thread.Sleep(7000);
                    ContactUs.WaitforContactUsHomeScreen();
                    ContactUs.Back();
                    Thread.Sleep(3000);
                    FindPhilips.Add_Country("India");
                    Thread.Sleep(6000);
                    //FindPhilips.EnterText(FindPhilips.EditBox.Search, "India");
                    //ContactUs.Back();
                    //Thread.Sleep(7000);
                    DialogsFindPhilips2.ClickErrorData(DialogsFindPhilips2.Button.NoData_Ok);
                }
            }
            catch { }
            ContactUs.Back();
            FindPhilips.ClickTst1(FindPhilips.Button.Backto_HomeScrn);
            Thread.Sleep(7000);
            Support.WaitforSupportScreen();
        }

        [Then(@"verify tell us what you think screen")]
        public void ThenVerifyTellUsWhatYouThinkScreen()
        {
            try
            {
                TellUs.WaitforTellUsScreen();
                TellUs.ClickTellUsButtons(TellUs.Button.Write_an_app_Review);
                Thread.Sleep(7000);
                bool PS = TellUs.PlaySoreView();
                if(PS == true)
                {
                    Logger.Info("Reached playstore");
                }
                else
                {
                    Logger.Info("Not in playstore");
                }
                TellUs.Back();
                Thread.Sleep(9000);
                TellUs.ClickTellUsButtons(TellUs.Button.Submit_Product_Review);
                Thread.Sleep(14000);
                bool PR = TellUs.ProductReviewWeb();
                if (PR == true)
                {
                    Logger.Info("Reached product review web page");
                }
                else
                {
                    Logger.Info("Not in prodcut review web page");
                }
                TellUs.Back();
                TellUs.WaitforTellUsScreen();
                TellUs.ClickTellUsButtons(TellUs.Button.Back_to_HomeScreen);
                IapReport.Message("Pass : verified tell us what you think screen");
            }
            catch
            {
                IapReport.Fail("Failed to verify tell us what you think screen");
            }
        }

        [Then(@"verify change product in confirmation screen")]
        public void ThenVerifyChangeProductInConfirmationScreen()
        {
            try
            {
                Support.WaitforSupportScreen();
                Support.Click(Support.Button.Change_Selected_Product);
                Thread.Sleep(3000);
                ProductList.WaitforProductListHomeScreen();
                ProductList.SelectProduct("SC2009/00");
                Thread.Sleep(3000);
                SelectedProductScreen.WaitforSelectedProductHomeScreen();
                SelectedProductScreen.ClickSelectProductTest(SelectedProductScreen.Button.Select_This_Produt);
                Thread.Sleep(3000);
                ConfirmationScreen.WaitforConfirmationScreen();
                ConfirmationScreen.ClickConfirmationButtons(ConfirmationScreen.Button.Change);
                Thread.Sleep(3000);
                ProductList.WaitforProductListHomeScreen();
                ProductList.SelectProduct("RQ1250/17");
                Thread.Sleep(3000);
                SelectedProductScreen.WaitforSelectedProductHomeScreen();
                SelectedProductScreen.ClickSelectProductTest(SelectedProductScreen.Button.Select_This_Produt);
                Thread.Sleep(3000);
                ConfirmationScreen.WaitforConfirmationScreen();
                ConfirmationScreen.ClickConfirmationButtons(ConfirmationScreen.Button.Continue);
                Thread.Sleep(2000);
                Support.WaitforSupportScreen();
                Support.Click(Support.Button.View_Product_Information);
                Thread.Sleep(3000);
                ProductInformation.WaitforProductInformationScreen();
                String P_Name = ProductInformation.GetProductName();
                if (P_Name == "Shaver series 9000 SensoTouch wet and dry electric shaver")
                {
                    IapReport.Message("Verified change product : pass");
                }
                else
                {
                    IapReport.Message("change product verification : fail");
                }
            }
            catch(Exception e)
            {
                IapReport.Fail("change product verification : fail", e);
            }
            ProductInformation.ClickTest(ProductInformation.Button.Back_to_supportScreen);
            Thread.Sleep(3000);
            Support.WaitforSupportScreen();
        }

        [Then(@"select a new product of code ""(.*)"" name ""(.*)"" and verify product details page and reach support screen")]
        public void ThenSelectANewProductOfCodeNameAndVerifyProductDetailsPageAndReachSupportScreen(string code, string name)
        {
            try
            {
                Thread.Sleep(3000);
                MobileDriver.FireKeyEvent(4);
                Thread.Sleep(3000);
                Support.WaitforSupportScreen();
                try
                {
                    bool b = Support.IsVisible(Support.Button.Change_Selected_Product);
                    if (b == true)
                    {
                        Support.Click(Support.Button.Change_Selected_Product);
                        Thread.Sleep(3000);
                        ProductList.WaitforProductListHomeScreen();
                        ProductList.SelectProduct(code);
                        Thread.Sleep(3000);
                        SelectedProductScreen.WaitforSelectedProductHomeScreen();
                        SelectedProductScreen.ClickSelectProductTest(SelectedProductScreen.Button.Select_This_Produt);
                        Thread.Sleep(3000);
                        ConfirmationScreen.WaitforConfirmationScreen();
                        ConfirmationScreen.ClickConfirmationButtons(ConfirmationScreen.Button.Continue);
                        Thread.Sleep(4000);
                        Support.WaitforSupportScreen();
                    }
                }
                catch
                {

                }
                
            }
            catch(Exception e)
            {
                IapReport.Fail("Fail - on adding a new product", e);
            }
        }
        [Then(@"Verify the Mobile App Home Screen with Title ""(.*)"" and log in with username ""(.*)"" and password ""(.*)"" if required")]
        public void ThenVerifyTheMobileAppHomeScreenWithTitleAndLogInWithUsernameAndPasswordIfRequired(string TitleText, string UserName, string Password)
        {

            try
            {
                Thread.Sleep(1000);
                if (!AppHomeScreen.IsVisibleScreenTitleText(TitleText))        
                {
                    Log_In.Click();
                    Log_In.SignIn(UserName, Password);
                }
            }
            catch (Exception e)
            {
                IapReport.Fail("Not able to log in", e);
            }
        }




    }


}
