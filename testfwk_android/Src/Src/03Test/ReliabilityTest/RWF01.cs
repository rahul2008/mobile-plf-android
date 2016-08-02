/*  ----------------------------------------------------------------------------
 *  SIG Automation : Philips Healthcare Bangalore Center of Competence
 *  ----------------------------------------------------------------------------
 *  IAP Reliability Automation
 *  ----------------------------------------------------------------------------
 *  File:       WF1.cs
 *  Author:     Ananya Ghosh & Sai Lokesh
 *  Creation Date: 3-3-2016
 *  ----------------------------------------------------------------------------
 */

using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Threading;
//using Microsoft.VisualStudio.TestTools.UnitTesting;

using Philips.MRAutomation.Foundation.FrameworkCore;
using Philips.MRAutomation.Foundation.FrameworkCore.Common;

using Philips.MRAutomation.Foundation.LoggingUtility;
using Philips.MRAutomation.Foundation.StatusIndicator;
//using Philips.MRAutomation.Foundation.TimerNotifier;
using Philips.CDPAutomation.Foundation.TestCaseDefinition;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using System.CodeDom.Compiler;
using System.Configuration;
//using Philips.MRAutomation.Utilities.Recoverer;

namespace Philips.SIG.Automation.IAP.Reliability.Workflows
{
    [Testcase(TestcaseState.GOOD, TestcaseType.RELIABILITY)]
    [VerificationType(VerificationIndicators.LOGIC)]
    [TestCaseInfo(Description = "Workflow from Login to Checkout page", Id = "TC01", Name = "RWF01", PreCondition = "IAP Application should be launched with user login", MainArea = "WellnessWorkflows", IsPartOfWorkflow = false)]
    [TestcaseProperties(ProductSpecifier.IAP, "", "")]
    [Product(ProductSpecifier.IAP, "CDP", "")]

    public class RWF01 : TestCase, ITestCase
    {
        #region Variables
        public static Dictionary<string, object> dataObjects = null;
        public string dataFile = "\\Data\\ReliabilityTestData.xml";
        private Report report = new Report();
        bool status = true;
        bool applicationCrashed = false;
        bool timeoutReached = false;
        string reportPath = GeneralConfiguration.executingAssemblyLocation + "\\Reports";
        List<string> tempTestResultMessages;
        static int _instance = 0;
       // HomeScreen hScreen = new HomeScreen();
       // ShoppingCart shop = new ShoppingCart();
        //ShippingAddress ship = new ShippingAddress();
       
        //static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        #endregion

        #region Constructors
        public RWF01()
        {
            dataObjects = (Dictionary<string, object>)TestDataUtility.TestDataInitializer(this.GetType().Name, dataFile);
           
        }
        #endregion

        #region ITestCase Members

      
        [TestcaseSetup]
      
        public override object Setup(params object[] parmaters)
        {
            status = true;
            report.Message("In Set up of " + this.GetType().Name);
            if (_instance == 0)
            {
               //IAPConfiguration.LoadConfiguration();
                IAPConfiguration.LoadConfigurationForDebug();
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
                _instance++;
                HomeScreen.WaitforHomeScreen();
            return true;
        }
        public static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        [TestcaseExecute]
        
        public override object ExecuteAction(params object[] parmaters)
        {
            List<string> testResultMessages = new List<string>();
            string dataStore = GeneralConfiguration.executingAssemblyLocation + dataFile;
            try
            {
                report.Message("In Execute of " + this.GetType().Name);
                bool isExist = false;

                // Step 1:    
                //User Journey-1
                try
                {
                    HomeScreen.ContextMenu.Select(HomeScreen.Fields.Country_Spinner, HomeScreen.ContextMenu.MenuItem.US);
                    Thread.Sleep(3000);
                    HomeScreen.Click(HomeScreen.Button.Shop_Now);
                    Thread.Sleep(6000);
                    ProductScreen.WaitforProductScreen();
                    Dictionary<int,string> dictionaryvalues=ProductScreen.listofProducts();
                   
                    List<string> productid = ProductScreen.GetProductListItems();
                    isExist = false;
                    while (!isExist)
                    {
                        try
                        {
                            for (int i = 0; i < dictionaryvalues.Count; i++)
                            {
                                productid = ProductScreen.GetProductListItems();
                                for (int j = 0; j < productid.Count; j++)
                                {
                                    try
                                    {
                                        if (dictionaryvalues[i].Equals(productid[j]))
                                        {

                                            isExist = true;
                                            ProductScreen.Click(dictionaryvalues[i], ProductScreen.Button.productScreen_InfoButton);
                                            string name = dictionaryvalues[i].ToString();
                                            break;
                                        }
                                        else
                                            Console.WriteLine("Product is out of stock");
                                    }catch(Exception e)
                                    {
                                        ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                                    }
                                        
                                    //ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                                }
                                if (isExist)
                                    break;
                            }
                        }
                        catch (Exception)
                        {

                            ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                        }
                    }

                    Thread.Sleep(4000);
                    isExist = false;
                    while (!isExist)
                    {
                        try
                        {
                            isExist = ShoppingCartItem.IsExist(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                            if (isExist == false)
                                ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                        }
                        catch (Exception)
                        {

                            ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                        }
                    }


                    ShoppingCartItem.Click(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                  
                }
                catch (Exception e)
                {
                    Logger.Fail("Exception occur in clicking buynow ");
                }
                report.Message("");
                

                //Step 2: Select 5min Liver Fat-Sat ExamCard, Liver as Anatomy and Posterior Coil for Scanning

                ShoppingCart.WaitforShoppingCartScreen();

               
               
                report.Message("");
                bool bSwiped = false;
                int qty = 0;
                List<String> ProdNames = ShoppingCart.GetProductNames();
               // List<CartList> values = ShoppingCart.getCartItems();
                //int a = values.Count();
                qty = Int32.Parse(ShoppingCart.GetText(ProdNames[0],ShoppingCart.ProductDetailsText.Quantity));
                if (qty != 1)
                {
                    int indx = qty - 1;
                    ShoppingCart.SwipeQuantity(ShoppingCart.Direction.Down,indx);
                    bSwiped = true;
                }
                ShoppingCart.Select(ProdNames[0], 3, bSwiped);
                bSwiped = false;

               // ShoppingCart.Click(ShoppingCart.Button.ClaimVoucherArrow);

                ShoppingCart.Click(ShoppingCart.Button.ContinueShopping);
                ProductScreen.WaitforProductScreen();
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                HomeScreen.WaitforHomeScreen();
                if (Device.GetConnectionType() == Device.ConnectionType.WifiOn)
                {
                    Device.SetConnectionType(Device.ConnectionType.WifiOff);

                }
                Device.SetConnectionType(Device.ConnectionType.WifiOn);
                Thread.Sleep(35000);
                HomeScreen.WaitforHomeScreen();
                try
                {
                    HomeScreen.ContextMenu.Select(HomeScreen.Fields.Country_Spinner, HomeScreen.ContextMenu.MenuItem.US);
                    Thread.Sleep(5000);
                    HomeScreen.Click(HomeScreen.Button.Shop_Now);
                    Thread.Sleep(6000);
                    ProductScreen.WaitforProductScreen();
                    Dictionary<int, string> dictionaryvalues = ProductScreen.listofProducts();

                    List<string> productid = ProductScreen.GetProductListItems();
                    isExist = false;
                    while (!isExist)
                    {
                        try
                        {
                            for (int i = 0; i < dictionaryvalues.Count; i++)
                            {
                                productid = ProductScreen.GetProductListItems();
                                for (int j = 0; j < productid.Count; j++)
                                {
                                    try
                                    {
                                        if (dictionaryvalues[i].Equals(productid[j]))
                                        {

                                            isExist = true;
                                            ProductScreen.Click(dictionaryvalues[i], ProductScreen.Button.productScreen_InfoButton);
                                            string name = dictionaryvalues[i].ToString();
                                            break;
                                        }
                                        else
                                            Console.WriteLine("Product is out of stock");
                                    }
                                    catch (Exception e)
                                    {
                                        ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                                    }
                                    //ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                                }
                                if (isExist)
                                    break;
                            }
                        }
                        catch (Exception)
                        {

                            ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                        }
                    }


                    Thread.Sleep(4000);
                    isExist = false;
                    while (!isExist)
                    {
                        try
                        {
                            isExist = ShoppingCartItem.IsExist(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                            if (isExist == false)
                                ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                        }
                        catch (Exception)
                        {

                            ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                        }
                    }


                    ShoppingCartItem.Click(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                }
                catch (Exception e)
                {
                    Logger.Fail("Exception occur in clicking buynow ");
                }
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.UpButton);
                ShoppingCartItem.WaitforShoppingCartInfoScreen();
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                ProductScreen.WaitforProductScreen();
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                HomeScreen.WaitforHomeScreen();
                
                try
                {
                    HomeScreen.ContextMenu.Select(HomeScreen.Fields.Country_Spinner, HomeScreen.ContextMenu.MenuItem.US);
                    Thread.Sleep(3000);
                    HomeScreen.Click(HomeScreen.Button.Shop_Now);
                    Thread.Sleep(6000);
                    ProductScreen.WaitforProductScreen();
                    Dictionary<int, string> dictionaryvalues = ProductScreen.listofProducts();

                    List<string> productid = ProductScreen.GetProductListItems();
                    isExist = false;
                    while (!isExist)
                    {
                        try
                        {
                            for (int i = 0; i < dictionaryvalues.Count; i++)
                            {
                                productid = ProductScreen.GetProductListItems();
                                for (int j = 0; j < productid.Count; j++)
                                {
                                    try
                                    {
                                        if (dictionaryvalues[i].Equals(productid[j]))
                                        {

                                            isExist = true;
                                            ProductScreen.Click(dictionaryvalues[i], ProductScreen.Button.productScreen_InfoButton);
                                            string name = dictionaryvalues[i].ToString();
                                            break;
                                        }
                                        else
                                            Console.WriteLine("Product is out of stock");
                                    }
                                    catch (Exception e)
                                    {
                                        ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                                    }
                                    //ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                                }
                                if (isExist)
                                    break;
                            }
                        }
                        catch (Exception)
                        {

                            ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                        }
                    }

                    Thread.Sleep(4000);
                     isExist = false;
                    
                    while (!isExist)
                    {
                        try
                        {
                            isExist = ShoppingCartItem.IsExist(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                            if (isExist == false)
                                ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                        }
                        catch (Exception)
                        {

                            ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                        }
                    }


                    ShoppingCartItem.Click(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                }
                catch (Exception e)
                {
                    Logger.Fail("Exception occur in clicking buynow ");
                }
                ShoppingCart.WaitforShoppingCartScreen();

                ShoppingCart.Click(ShoppingCart.Button.UpButton);
                ShoppingCartItem.WaitforShoppingCartInfoScreen();
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                ProductScreen.WaitforProductScreen();
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(HomeScreen.Button.CartImage);
                List<String> ProductNames = ShoppingCart.GetProductNames();
                int Quantity = Int32.Parse(ShoppingCart.GetText(ProductNames[0],ShoppingCart.ProductDetailsText.Quantity));
                Quantity += 1;

                ShoppingCart.Select(ProductNames[0], Quantity);

                ShoppingCart.Click(ShoppingCart.Button.Checkout);
                ShippingAddress.WaitforShippingAddressScreen();
                //ShippingAddress.Click(ShippingAddress.Button.Deliver_to_This_Address);
                ShippingAddress.Click(ShippingAddress.Button.Add_a_new_Address);
                Address.CreateAddress("abhi", "ram", Address.ContextMenu.MenuItem.Mr, "Manyata Tech Park", "Huntsville", "35630","US", false);
                Address.Click(Address.Button.Continue);
               
                Address.WaitforAddressScreen();
                isExist = false;
                while (!isExist)
                {
                    try
                    {
                        isExist = Address.IsExist(Address.Button.Continue);
                        if (isExist == false)
                            Address.Swipe(Address.Direction.Up, 2);
                    }
                    catch (Exception)
                    {

                        Address.Swipe(Address.Direction.Up, 2);
                    }
                }
                Address.Click(Address.Button.Continue);
                Summary.WaitforSummaryScreen();               
                Summary.Click(Summary.Button.Pay_Now);
                Thread.Sleep(30000);
                Payment.WaitforPaymentScreen();
                Payment.EnterPaymentDetails("4444333322221111", "08", "2020", "651");
                Confirmation.Click(Confirmation.Button.OK);
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                HomeScreen.WaitforHomeScreen();

                //User journey- 2
                try
                {
                    HomeScreen.ContextMenu.Select(HomeScreen.Fields.Country_Spinner, HomeScreen.ContextMenu.MenuItem.US);
                    Thread.Sleep(3000);
                    HomeScreen.Click(HomeScreen.Button.Shop_Now);
                    Thread.Sleep(6000);
                    ProductScreen.WaitforProductScreen();
                    Dictionary<int, string> dictionaryvalues = ProductScreen.listofProducts();

                    List<string> productid = ProductScreen.GetProductListItems();
                    isExist = false;
                    while (!isExist)
                    {
                        try
                        {
                            for (int i = 0; i < dictionaryvalues.Count; i++)
                            {
                                productid = ProductScreen.GetProductListItems();
                                for (int j = 0; j < productid.Count; j++)
                                {
                                    try
                                    {
                                        if (dictionaryvalues[i].Equals(productid[j]))
                                        {

                                            isExist = true;
                                            ProductScreen.Click(dictionaryvalues[i], ProductScreen.Button.productScreen_InfoButton);
                                            string name = dictionaryvalues[i].ToString();
                                            break;
                                        }
                                        else
                                            Console.WriteLine("Product is out of stock");
                                    }
                                    catch (Exception e)
                                    {
                                        ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                                    }
                                    //ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                                }
                                if (isExist)
                                    break;
                            }
                        }
                        catch (Exception)
                        {

                            ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                        }
                    }

                    Thread.Sleep(4000);
                     isExist = false;
                    while (!isExist)
                    {
                        try
                        {
                            isExist = ShoppingCartItem.IsExist(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                            if (isExist == false)
                                ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                        }
                        catch (Exception)
                        {

                            ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                        }
                    }


                    ShoppingCartItem.Click(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                }
                catch (Exception e)
                {
                    Logger.Fail("Exception occur in clicking buynow ");
                }
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.DeleteAllProducts();
                ShoppingCart.Click(ShoppingCart.Button.ContinueShopping_1);
                ProductScreen.WaitforProductScreen();
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                HomeScreen.WaitforHomeScreen();
                try
                {
                    HomeScreen.ContextMenu.Select(HomeScreen.Fields.Country_Spinner, HomeScreen.ContextMenu.MenuItem.US);
                    Thread.Sleep(3000);
                    HomeScreen.Click(HomeScreen.Button.Shop_Now);
                    Thread.Sleep(6000);
                    ProductScreen.WaitforProductScreen();
                    Dictionary<int, string> dictionaryvalues = ProductScreen.listofProducts();

                    List<string> productid = ProductScreen.GetProductListItems();
                    isExist = false;
                    while (!isExist)
                    {
                        try
                        {
                            for (int i = 0; i < dictionaryvalues.Count; i++)
                            {
                                productid = ProductScreen.GetProductListItems();
                                for (int j = 0; j < productid.Count; j++)
                                {
                                    try
                                    {
                                        if (dictionaryvalues[i].Equals(productid[j]))
                                        {

                                            isExist = true;
                                            ProductScreen.Click(dictionaryvalues[i], ProductScreen.Button.productScreen_InfoButton);
                                            string name = dictionaryvalues[i].ToString();
                                            break;
                                        }
                                        else
                                            Console.WriteLine("Product is out of stock");
                                    }
                                    catch (Exception e)
                                    {
                                        ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                                    }
                                    //ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                                }
                                if (isExist)
                                    break;
                            }
                        }
                        catch (Exception)
                        {

                            ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                        }
                    }
                    Thread.Sleep(4000);
                     isExist = false;
                    while (!isExist)
                    {
                        try
                        {
                            isExist = ShoppingCartItem.IsExist(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                            if (isExist == false)
                                ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                        }
                        catch (Exception)
                        {

                            ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                        }
                    }


                    ShoppingCartItem.Click(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                }
                catch (Exception e)
                {
                    Logger.Fail("Exception occur in clicking buynow ");
                }
               ShoppingCart.Click(ShoppingCart.Button.UpButton);
               ShoppingCartItem.WaitforShoppingCartInfoScreen();
               ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
               ProductScreen.WaitforProductScreen();
               ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
               HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(HomeScreen.Button.CartImage);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.Checkout);
                ShippingAddress.WaitforShippingAddressScreen();
                ShippingAddress.Click(ShippingAddress.Button.Deliver_to_This_Address);
                Address.WaitforAddressScreen();
                Address.Click(Address.Button.Switch);
                Thread.Sleep(2000);
                Address.CreateAddress("Mani", "Teja", Address.ContextMenu.MenuItem.Mr, "Manyata Tech Park", "Huntsville", "35801", "US", false);
                Address.Click(Address.Button.Continue);
                Summary.WaitforSummaryScreen();
                Summary.Click(Summary.Button.Pay_Now);
                Thread.Sleep(30000);
                Payment.WaitforPaymentScreen();
               
                Payment.EnterPaymentDetails("4444333322221111","08","2020","451");
                Thread.Sleep(3000);
                Confirmation.Click(Confirmation.Button.OK);
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                HomeScreen.WaitforHomeScreen();



                //USer journey-3

                try
                {
                    HomeScreen.ContextMenu.Select(HomeScreen.Fields.Country_Spinner, HomeScreen.ContextMenu.MenuItem.US);
                    Thread.Sleep(3000);
                    HomeScreen.Click(HomeScreen.Button.Shop_Now);
                    Thread.Sleep(6000);
                    ProductScreen.WaitforProductScreen();
                    Dictionary<int, string> dictionaryvalues = ProductScreen.listofProducts();

                    List<string> productid = ProductScreen.GetProductListItems();
                    isExist = false;
                    while (!isExist)
                    {
                        try
                        {
                            for (int i = 0; i < dictionaryvalues.Count; i++)
                            {
                                productid = ProductScreen.GetProductListItems();
                                for (int j = 0; j < productid.Count; j++)
                                {
                                    try
                                    {
                                        if (dictionaryvalues[i].Equals(productid[j]))
                                        {

                                            isExist = true;
                                            ProductScreen.Click(dictionaryvalues[i], ProductScreen.Button.productScreen_InfoButton);
                                            string name = dictionaryvalues[i].ToString();
                                            break;
                                        }
                                        else
                                            Console.WriteLine("Product is out of stock");
                                    }
                                    catch (Exception e)
                                    {
                                        ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                                    }
                                    //ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                                }
                                if (isExist)
                                    break;
                            }
                        }
                        catch (Exception)
                        {

                            ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                        }
                    }


                    Thread.Sleep(4000);
                     isExist = false;
                    while (!isExist)
                    {
                        try
                        {
                            isExist = ShoppingCartItem.IsExist(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                            if (isExist == false)
                                ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                        }
                        catch (Exception)
                        {

                            ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                        }
                    }


                    ShoppingCartItem.Click(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                }
                catch (Exception e)
                {
                    Logger.Fail("Exception occur in clicking buynow ");
                }
                ShoppingCart.WaitforShoppingCartScreen();
                ProdNames = ShoppingCart.GetProductNames();
                ShoppingCart.ContextMenu.Select(ProdNames[0], ShoppingCart.ContextMenu.MenuItem.Delete);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.ContinueShopping_1);
                ProductScreen.WaitforProductScreen();
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                HomeScreen.WaitforHomeScreen();
                try
                {
                    HomeScreen.ContextMenu.Select(HomeScreen.Fields.Country_Spinner, HomeScreen.ContextMenu.MenuItem.US);
                    Thread.Sleep(3000);
                    HomeScreen.Click(HomeScreen.Button.Shop_Now);
                    Thread.Sleep(6000);
                    ProductScreen.WaitforProductScreen();
                    Dictionary<int, string> dictionaryvalues = ProductScreen.listofProducts();

                    List<string> productid = ProductScreen.GetProductListItems();
                    isExist = false;
                    while (!isExist)
                    {
                        try
                        {
                            for (int i = 0; i < dictionaryvalues.Count; i++)
                            {
                                productid = ProductScreen.GetProductListItems();
                                for (int j = 0; j < productid.Count; j++)
                                {
                                    try
                                    {
                                        if (dictionaryvalues[i].Equals(productid[j]))
                                        {

                                            isExist = true;
                                            ProductScreen.Click(dictionaryvalues[i], ProductScreen.Button.productScreen_InfoButton);
                                            string name = dictionaryvalues[i].ToString();
                                            break;
                                        }
                                        else
                                            Console.WriteLine("Product is out of stock");
                                    }
                                    catch (Exception e)
                                    {
                                        ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                                    }
                                    //ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                                }
                                if (isExist)
                                    break;
                            }
                        }
                        catch (Exception)
                        {

                            ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                        }
                    }
                 
                    Thread.Sleep(4000);
                     isExist = false;
                    while (!isExist)
                    {
                        try
                        {
                            isExist = ShoppingCartItem.IsExist(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                            if (isExist == false)
                                ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                        }
                        catch (Exception)
                        {

                            ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                        }
                    }


                    ShoppingCartItem.Click(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                }
                catch (Exception e)
                {
                    Logger.Fail("Exception occur in clicking buynow ");
                }
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.Checkout);
                //user enters invalid address
                ShippingAddress.Click(ShippingAddress.Button.Add_a_new_Address);
                Address.CreateAddress("12345", "kumar", Address.ContextMenu.MenuItem.Mr, "2345566", "Huntsville", "35801", "US", false);
                Address.Click(Address.Button.Continue);
                Address.WaitforAddressScreen();
                isExist = false;
                while (!isExist)
                {
                    try
                    {
                        isExist = Address.IsExist(Address.Button.Cancel);
                        if (isExist == false)
                            Address.Swipe(Address.Direction.Up, 2);
                    }
                    catch (Exception)
                    {

                        Address.Swipe(Address.Direction.Up, 2);
                    }
                }
                ShippingAddress.Click(ShippingAddress.Button.Cancel);
                ShippingAddress.Click(ShippingAddress.Button.Cancel);
            ShippingAddress.Click(ShippingAddress.Button.Cancel);
            ShoppingCart.WaitforShoppingCartScreen();
            
            ShoppingCart.Click(ShoppingCart.Button.Checkout);
            ShippingAddress.WaitforShippingAddressScreen();
            ShippingAddress.Click(ShippingAddress.Button.Deliver_to_This_Address);
           
             isExist = false;
            while (!isExist)
            {
                try
                {
                    isExist = Address.IsExist(Address.Button.Cancel);
                    if (isExist == false)
                        Address.Swipe(Address.Direction.Up, 2);
                }
                catch (Exception)
                {

                    Address.Swipe(Address.Direction.Up, 2);
                }
            }
           
            Address.Click(Address.Button.Continue);
            Summary.WaitforSummaryScreen();
           
            Summary.Click(Summary.Button.Pay_Now);
          
            Thread.Sleep(30000);
            Payment.WaitforPaymentScreen();

            Payment.EnterPaymentDetails("4444333322221111", "08", "2020", "451");
            Confirmation.Click(Confirmation.Button.OK);
            ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
            HomeScreen.WaitforHomeScreen();



                //Workflow 4


                testResultMessages = new List<string>();
              
                    Indicator.SetTestStep("Step 3:");
                
                    report.Message("");

                    
            }
            catch (Exception e)
            {
                status = false;
               report.Message("Exception caught in " + this.GetType().Name + e.ToString());
               
                foreach (string msg in testResultMessages)
                {
                    report.Message(msg);
                }
               
                //timeoutReached = MyTimer.GetTimeoutFlag();

                UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell am force-stop com.philips.cdp.di.iapdemo ");
                //IAPConfiguration.CloseAll_Reliability();
                Logger.Info("Exception occur App is closed");
                UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell am  start -n com.philips.cdp.di.iapdemo/com.philips.cdp.di.iapdemo.DemoAppActivity ");
                //IAPConfiguration.LoadConfiguration_Reliability();
                //Logger.Info("App is Relaunched");
                //Thread.Sleep(4000);
                //if (Crash_Data.IsVisible())
                //{
                //    Crash_Data.Click(Crash_Data.Button.DISMISS);
                //    HomeScreen.WaitforHomeScreen();
                //}
            }
            
            return status;
        }

        [TestcaseVerify("VERIFY")]
        public override object VerifyAction(params object[] parameters)
        {
            report.Message("In Verify of " + this.GetType().Name);
            if (applicationCrashed)
            {
                report.Message("Test was aborted as application crashed");
            }
            else if (timeoutReached)
            {
                report.Message("Test was aborted as test timeout was reached");
            }
            else if (status)
            {
                report.Pass("Test case Passed");
                if (ConfigurationAccessor.Utilities.GetPerformanceMonitoringMode())
                {
                    report.Message("Performance Readings file: " + PerformanceMonitor.outputFile);
                }
                report.Pass("MTBC file: " + MTBCLog.mtbcLogFile.Split('\\')[1]);
            }
            else
            {
                report.Fail("Test case Failed");
                if (ConfigurationAccessor.Utilities.GetPerformanceMonitoringMode())
                {
                    report.Message("Performance Readings file: " + PerformanceMonitor.outputFile);
                }
                report.Fail("MTBC file: " + MTBCLog.mtbcLogFile.Split('\\')[1]);
            }
            return status;
        }

        [TestcaseTeardown]
        public override object Teardown(params object[] parmaters)
        {
            //Shaheen :: 17 March, 2016 :: Copying reliability test report file for dashboard
            if (_instance == Int32.Parse(ConfigurationManager.AppSettings["ReliabilityLoopCount"]))
            {
                IAPConfiguration.CloseAll();
                try
                {
                    string pathForReports = Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().Location) + "\\Reports";
                    var file = Directory.GetFiles(pathForReports, "*_TestReport.xml", SearchOption.TopDirectoryOnly)
                        .Select(x => new FileInfo(x))
                        .OrderByDescending(x => x.LastWriteTime)
                        .Take(1)
                        .First();
                    if (file.Length > 0 && File.Exists(file.ToString()))
                    {
                        File.Copy(file.FullName, ConfigurationManager.AppSettings["ReliabilityLogsPath"] + file.Name.ToString(), true);
                    }
                }
                catch (Exception ex)
                {
                    return false;
                }
            }
            //IAPConfiguration.LoadConfiguration_Reliability();
            //Logger.Info("App is Relaunched");

            return true;
        }

        [ReUsable]
        public override object ReusableFunction(params object[] parmaters)
        {
            return true;
        }
        #endregion


    }

    [ReUsable]
    public partial class ReusableClass
    {
       
        /// <summary>
        /// Delete .png files
        /// </summary>
        /// <param name="filePrintLocation">file print location</param>
        public static void DeleteFiles(string filePrintLocation)
        {
            string[] files = Directory.GetFiles(filePrintLocation, "*.png");
            for (int i = 0; i < files.Length; i++)
            {
                try
                {
                    File.Delete(files[i]);
                }
                catch
                {
                }
            }
        }
    }
}