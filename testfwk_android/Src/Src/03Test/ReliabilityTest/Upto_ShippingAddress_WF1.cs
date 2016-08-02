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
    [TestCaseInfo(Description = "Workflow from Login to Checkout page", Id = "TC01", Name = "Upto_ShippingAddress_WF1", PreCondition = "IAP Application should be launched with user login", MainArea = "WellnessWorkflows", IsPartOfWorkflow = false)]
    [TestcaseProperties(ProductSpecifier.IAP, "", "")]
    [Product(ProductSpecifier.IAP, "CDP", "")]
    
    public class Upto_ShippingAddress_WF1 : TestCase, ITestCase
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
        HomeScreen hScreen = new HomeScreen();
        ShoppingCart shop = new ShoppingCart();
        ShippingAddress ship = new ShippingAddress();
       
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        #endregion

        #region Constructors
        public Upto_ShippingAddress_WF1()
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
                IAPConfiguration.LoadConfiguration();
                HomeScreen.Click(HomeScreen.Button.Register);
                LoginScreen.WaitforLoginScreen();
                LoginScreen.LoginUser(ConfigurationManager.AppSettings["UsernameIAP"], ConfigurationManager.AppSettings["PasswordIAP"]);
                HomeScreen.WaitforHomeScreen();
            }
                _instance++;
                HomeScreen.WaitforHomeScreen();
            return true;
        }

        [TestcaseExecute]
       
        public override object ExecuteAction(params object[] parmaters)
        {
            List<string> testResultMessages = new List<string>();
            string dataStore = GeneralConfiguration.executingAssemblyLocation + dataFile;
            try
            {
                report.Message("In Execute of " + this.GetType().Name);

                // Step 1:    
                //User Journey-1

                List<string> productid = HomeScreen.GetProductListItems();
                HomeScreen.Click(productid[2], HomeScreen.Button.BuyNow);
               
                report.Message("");
                

                //Step 2: Select 5min Liver Fat-Sat ExamCard, Liver as Anatomy and Posterior Coil for Scanning

                ShoppingCart.WaitforShoppingCartScreen();
                
                report.Message("");
                bool bSwiped = false;
                int qty = 0;
                List<String> ProdNames = ShoppingCart.GetProductNames();

                qty = Int32.Parse(ShoppingCart.GetText(ProdNames[0],ShoppingCart.ProductDetailsText.Quantity));
                if (qty != 1)
                {
                    int indx = qty - 1;
                    ShoppingCart.SwipeQuantity(ShoppingCart.Direction.Down,indx);
                    bSwiped = true;
                }
                ShoppingCart.Select(ProdNames[0], 3, bSwiped);
                bSwiped = false;

                ShoppingCart.Click(ShoppingCart.Button.ClaimVoucherArrow);

                ShoppingCart.Click(ShoppingCart.Button.ContinueShopping);
                HomeScreen.WaitforHomeScreen();
                if (Device.GetConnectionType() == Device.ConnectionType.WifiOn)
                {
                    Device.SetConnectionType(Device.ConnectionType.WifiOff);
                    productid = HomeScreen.GetProductListItems();
                    HomeScreen.Click(productid[2], HomeScreen.Button.BuyNow);
                    NetWork_Error.Click(NetWork_Error.Button.OK);
                }
                Device.SetConnectionType(Device.ConnectionType.WifiOn);
                Thread.Sleep(3000);
                HomeScreen.WaitforHomeScreen();
                productid = HomeScreen.GetProductListItems();
                HomeScreen.Click(productid[2], HomeScreen.Button.BuyNow);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.UpButton);
                productid = HomeScreen.GetProductListItems();
                HomeScreen.Click(productid[2], HomeScreen.Button.BuyNow);
                ShoppingCart.WaitforShoppingCartScreen();

                ShoppingCart.Click(ShoppingCart.Button.UpButton);
                HomeScreen.Click(HomeScreen.Button.CartImage);
                List<String> ProductNames = ShoppingCart.GetProductNames();
                int Quantity = Int32.Parse(ShoppingCart.GetText(ProductNames[0],ShoppingCart.ProductDetailsText.Quantity));
                Quantity += 1;

                ShoppingCart.Select(ProductNames[0], Quantity);

                ShoppingCart.Click(ShoppingCart.Button.Checkout);
                ShippingAddress.WaitforShippingAddressScreen();
                //ShippingAddress.Click(ShippingAddress.Button.Deliver_to_This_Address);
                ShippingAddress.Click(ShippingAddress.Button.Add_a_new_Address);
                Address.CreateAddress("abhi", "ram", Address.ContextMenu.MenuItem.Mr, "Manyata Tech Park", "Banagalore", "560045", "US", "9876543210", false);
               
                Payment.WaitforPaymentScreen();
                Payment.Click(Payment.Button.Add_new_Payment);
                Address.WaitforAddressScreen();
                bool isExist = false;
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
                HomeScreen.WaitforHomeScreen();

                //User journey- 2
                productid = HomeScreen.GetProductListItems();
                HomeScreen.Click(productid[2], HomeScreen.Button.AddToCart);
                HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(HomeScreen.Button.CartImage);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.DeleteAllProducts();
                ShoppingCart.Click(ShoppingCart.Button.ContinueShopping_1);
                productid = HomeScreen.GetProductListItems();
                HomeScreen.Click(productid[2], HomeScreen.Button.AddToCart);
                HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(productid[2], HomeScreen.Button.AddToCart);
                HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(HomeScreen.Button.CartImage);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.Checkout);
                ShippingAddress.WaitforShippingAddressScreen();
                ShippingAddress.Click(ShippingAddress.Button.Deliver_to_This_Address);
                Payment.WaitforPaymentScreen();
                Payment.Click(Payment.Button.Add_new_Payment);
                Address.WaitforAddressScreen();
                Address.Click(Address.Button.Switch);
                Thread.Sleep(2000);
                Address.CreateAddress("Mani", "Teja", Address.ContextMenu.MenuItem.Mr, "Manyata Tech Park", "Banagalore", "560045", "US", "9876543210", true);
                Summary.WaitforSummaryScreen();
                Summary.Click(Summary.Button.Pay_Now);
                Thread.Sleep(30000);
                Payment.WaitforPaymentScreen();
                //User entering invalid payment details
                //Payment.EnterPaymentDetails("9999888877776666", "08", "2023", "451");
                //Thread.Sleep(3000);
                Payment.EnterPaymentDetails("4444333322221111","08","2020","451");
                Thread.Sleep(3000);
                Confirmation.Click(Confirmation.Button.OK);
                HomeScreen.WaitforHomeScreen();


                //USer journey-3
              
                productid = HomeScreen.GetProductListItems();
                HomeScreen.Click(productid[2], HomeScreen.Button.BuyNow);
                ShoppingCart.WaitforShoppingCartScreen();
                ProdNames = ShoppingCart.GetProductNames();
                ShoppingCart.ContextMenu.Select(ProdNames[0], ShoppingCart.ContextMenu.MenuItem.Delete);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.ContinueShopping_1);
                HomeScreen.WaitforHomeScreen();
                productid = HomeScreen.GetProductListItems();
                HomeScreen.Click(productid[2], HomeScreen.Button.BuyNow);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.Checkout);
                //user enters invalid address
                ShippingAddress.Click(ShippingAddress.Button.Add_a_new_Address);
                Address.CreateAddress("12345","kumar",Address.ContextMenu.MenuItem.Mr,"2345566","Hyderabad","780909","US","8990900854",false);
                ShippingAddress.Click(ShippingAddress.Button.Cancel);
            ShoppingCart.WaitforShoppingCartScreen();
            ShoppingCart.Click(ShoppingCart.Button.Checkout);
            Address.WaitforAddressScreen();
            ShippingAddress.Click(ShippingAddress.Button.Deliver_to_This_Address);
            Payment.WaitforPaymentScreen();
            Payment.Click(Payment.Button.Add_new_Payment);
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
            Payment.WaitforPaymentScreen();
            Payment.Click(Payment.Button.Use_this_Payment);
            Summary.WaitforSummaryScreen();
           
            Summary.Click(Summary.Button.Pay_Now);
            Thread.Sleep(30000);
            Payment.WaitforPaymentScreen();
            Payment.EnterPaymentDetails("4444333322221111", "08", "2020", "651");
            Confirmation.Click(Confirmation.Button.OK);
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
                    string[] files = Directory.GetFiles(Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().Location) + "\\Reports", "*_TestReport.xml", SearchOption.TopDirectoryOnly);
                    if (files.Length > 0 && File.Exists(files[0]))
                    {
                        File.Copy(files[0], ConfigurationManager.AppSettings["ReliabilityLogsPath"] + Path.GetFileName(files[0]), true);
                    }
                }
                catch (Exception ex)
                {
                    return false;
                }
            }

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