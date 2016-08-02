using OpenQA.Selenium.Appium;
using OpenQA.Selenium.Appium.Android;
using OpenQA.Selenium.Remote;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using OpenQA.Selenium;

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading;
using System.Configuration;
using System.Threading.Tasks;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System.Runtime.InteropServices;
using System.Reflection;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using System.Net.Security;

namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin
{
    /// <summary>
    /// This class provides all the functionalities and constants for features related to Home Screen page.
    /// </summary>
    public class HomeScreen: IAP_Common
    {


        /// <summary>
        /// Represents the object of DesiredCapabilities class.
        /// </summary>
        //public DesiredCapabilities cap = new DesiredCapabilities();

        /// <summary>
        /// Represents the static variable consisting the login id
        /// </summary>
        public static string loginId;

        /// <summary>
        /// Collection of all Buttons
        /// </summary>
        public enum Button
        {
            BuyNow,
            AddToCart,
            CartImage,
            Submit,
            Register,
            Shop_Now,

        }

        /// <summary>
        /// Collection of all TextBoxes
        /// </summary>
        public enum TextBox
        {
            UserName,
            Password
        }
        /// <summary>
        /// Collection of all Fields
        /// </summary>
        public enum Fields
        {
            Country_Spinner

        }
        /// <summary>
        /// Collection of all Spinner
        /// </summary>
        public enum Spinner
        {
            Select_Country
        }

        public HomeScreen()
        {

        }

        /// <summary>
        /// Clicks the desired button
        /// </summary>
        /// <param name="btn">btn represents one of the buttons present on the screen</param>
        public static void Click(Button btn)
        {
            Instance = _instance;

            if (btn == Button.BuyNow)
                Instance.ClickById(ObjectRepository.BuyNow);
            else if (btn == Button.AddToCart)
                Instance.ClickById(ObjectRepository.AddToCart);
            else if (btn == Button.CartImage)
                Instance.ClickById(ObjectRepository.ImageCart);
            else if (btn == Button.Submit)
                Instance.ClickById(ObjectRepository.HomeScreen_Submit);
            else if (btn == Button.Register)
                Instance.ClickById(ObjectRepository.Register);
            else if (btn == Button.Shop_Now)
                Instance.ClickById(ObjectRepository.Shop_Now);
            Busy();
        }

        /// <summary>
        /// Checks if a button is Enabled or not
        /// </summary>
        /// <param name="btn">btn represents one of the buttons present on the screen</param>
        /// <returns>a boolean value</returns>
        public static bool IsEnabled(Button btn)
        {
            bool isEnabled = false;
            if (btn == Button.BuyNow)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.BuyNow).Enabled;
            if (btn == Button.AddToCart)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.AddToCart).Enabled;
            if (btn == Button.CartImage)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.ImageCart).Enabled;
            if (btn == Button.Register)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.Register).Enabled;

            return isEnabled;
        }

        /// <summary>
        /// Returns the number of product items appearing in the HomeScreen
        /// </summary>
        /// <returns>integer value</returns>
        public static int GetProductCount()
        {
            return _getProductListed().Count;
        }


        /// <summary>
        /// Returns a string containing the name of the product the user wants to select
        /// </summary>
        /// <param name="indx">indx represents the index of the product the user wants to select</param>
        /// <returns>string value</returns>
        public static string GetProductName(int indx)
        {
            return _getProductListed()[indx].Text;
        }

        /// <summary>
        /// Clicks the BuyNow button of the product that user wants to buy
        /// </summary>
        /// <param name="indx">indx reprensents the index of the product the user wants to buy</param>
        public static void SelectProduct(int indx)
        {
            _getProductListed()[indx].Click();
            Busy();
        }
        /// <summary>
        /// This method retrieve list of productid's from ProductList
        /// </summary>
        /// <returns>List of product id's:HX089/16,HX067/10</returns>

        private static List<AndroidElement> _getProductListed()
        {
            //IMobilePageControl  element = _instance.GetElement(SearchBy.Id,ObjectRepository.ProductList);
            List<AndroidElement> elementList = new List<AndroidElement>();

            int indx = _instance.GetElement(SearchBy.Id, ObjectRepository.ProductList).ElementInstance.FindElements(By.Id(ObjectRepository.TextView)).Count;

            AndroidElement[] items = new AndroidElement[indx];

            _instance.GetElement(SearchBy.Id, ObjectRepository.ProductList).ElementInstance.FindElements(By.Id(ObjectRepository.TextView)).CopyTo(items, 0);

            //element.ElementInstance.FindElements(By.Id(ObjectRepository.TextView)).CopyTo()
            foreach (AndroidElement elem in items)
            {
                //string s = elem.Text;
                elementList.Add(elem);
            }
            return elementList;
        }

        /// <summary>
        /// Returns the number from the Shopping Cart Icon
        /// </summary>
        /// <returns>a string value</returns>
        public static string GetTextFromCart()
        {
            string retVal = string.Empty;
            try
            {
                retVal = _instance.GetTextById(ObjectRepository.CartNumber);
            }
            catch (Exception e)
            {
                retVal = "";
            }
            return retVal;
        }

        /// <summary>
        /// Returns a list of IMobilePageControls, containing all the Products present in the HomeScreen
        /// </summary>
        /// <returns>a list of IMobilePageControls</returns>
        public static List<IMobilePageControl> getProductList()
        {
            return _instance.GetElements(null, SearchBy.Id, ObjectRepository.ProductList);
        }

        /// <summary>
        /// Returns a list of Strings, containing all the product names present in the HomeScreen
        /// </summary>
        /// <returns>a list of strings</returns>
        public static List<String> GetProductListItems()
        {
            List<AndroidElement> items = _getProductListed();
            List<String> productItems = new List<string>();
            foreach (AndroidElement item in items)
            {
                productItems.Add(item.Text);
            }
            return productItems;
        }

        /// <summary>
        /// Waits for the HomeScreen page to appear
        /// Returns false if the Shopping Cart Screen doesn't appear after loopcount exceeds 10
        /// </summary>
        /// <returns>a boolean value</returns>
        public static bool WaitforHomeScreen()
        {
            /*IMobilePageControl element = null;
            //AndroidElement element = null;
            int loopcount = -0;
            while (element == null)
            {
                element = _instance.GetElement(SearchBy.Id, ObjectRepository.DemoTitle);
                //element = (AndroidElement)_launchdriver.FindElementById("com.philips.cdp.di.iapdemo:id/demo_title");
                loopcount++;
                if (element != null)
                    break;
                if (loopcount > 10)
                    break;

            }
            if (element != null)
                return true;
            else
                return false;
            */

            for (int i = 0; i < 40;i++)
            {
                if (GetScreenTitle().Equals("E-Commerce Demo App"))
                {
                    Logger.Info("Seeing: E-Commerce Demo App");
                    return true;
                }
                  
                Thread.Sleep(500);
            }
            return false;
        }


        /// <summary>
        /// Checks if the Screen Title is "Demo App"
        /// </summary>
        /// <returns>a boolean value</returns>
        public static bool IsVisible()
        {
            bool bVisible = false;
            if (GetScreenTitle() == "E-Commerce Demo App")
                bVisible = true;
            return bVisible;

        }

        
        ///// <summary>
        ///// Enters the Username and Password
        ///// </summary>
        ///// <param name="tb">tb must contain the textbox name</param>
        ///// <param name="name">name must contain the string to be entered</param>
        //private static void EnterText(TextBox tb,string name)
        //{
        //    string desiredtxtBox=string.Empty;
        //    if (tb == TextBox.UserName)
        //        desiredtxtBox=ObjectRepository.HomeScreen_UserName;
        //     if (tb == TextBox.Password)
        //        desiredtxtBox=ObjectRepository.HomeScreen_Password;
        //     _instance.GetElement(SearchBy.Id,desiredtxtBox ).SetText(name);
        //}

        ///// <summary>
        ///// Enters username and password and clicks on Submit Button
        ///// </summary>
        ///// <param name="usrname">usrname must contain the Username</param>
        ///// <param name="pwd">pwd must contain the password</param>
        //public static void Login(string usrname, string pwd)
        //{
        //    loginId = usrname;
        //    EnterText(TextBox.UserName, usrname);
        //    EnterText(TextBox.Password, pwd);
        //    UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input keyevent" + " 111");
        //    Click(Button.Submit);
        //    HomeScreen.WaitforHomeScreen();
        //}

        /// <summary>
        /// Returns a list of IMobilePageControls, containing all desired buttons
        /// </summary>
        /// <param name="btn">btn reprents the name of the button </param>
        /// <returns>a list of IMobilePageControls</returns>
        public static List<IMobilePageControl> GetButtons(Button btn)
        {
            List<IMobilePageControl> items = new List<IMobilePageControl>();
            if (btn == Button.BuyNow)
                items = _instance.GetElements(null, SearchBy.Id, ObjectRepository.BuyNow);
            else if (btn == Button.AddToCart)
                items = _instance.GetElements(null, SearchBy.Id, ObjectRepository.AddToCart);

            return items;
        }


       
        /// <summary>
        /// Clicks the desired button of the corresponding Product name
        /// </summary>
        /// <param name="productname">productname must contain a string containing the name of the product</param>
        /// <param name="btn">btn represents the name of the button to be clicked</param>
        public static void Click(string productname, Button btn)
        {
            List<Product> ProdList = _getButtonSet();
            if (btn == Button.BuyNow)
                ProdList.Find(item => item.ProdName == productname).buyNowButton.Click();

            else if (btn == Button.AddToCart)
                ProdList.Find(item => item.ProdName == productname).addtoCartButton.Click();
            Busy();
        }

        /// <summary>
        /// This structure provides details/attributes related to Product.
        /// </summary>
        public struct Product
        {
            public string ProdName;
            public AndroidElement buyNowButton;
            public AndroidElement addtoCartButton;
        }
        /// <summary>
        /// Returns List of Buttons in the Home Screen
        /// </summary>
        /// <returns></returns>
        private static List<Product> _getButtonSet()
        {
            List<Product> productList = new List<Product>();


            IWebElement Elem = _instance.GetElement(SearchBy.Id, ObjectRepository.ProductList).ElementInstance;
            int i = Elem.FindElements(ByAndroidUIAutomator.ClassName("android.widget.LinearLayout")).Count;
            AndroidElement[] items = new AndroidElement[i];
            Elem.FindElements(ByAndroidUIAutomator.ClassName("android.widget.LinearLayout")).CopyTo(items, 0);
            foreach (AndroidElement item in items)
            {
                AndroidElement buyNowBtn = null;

                try
                {
                    buyNowBtn = item.FindElement(ByAndroidUIAutomator.Id(ObjectRepository.BuyNow)) as AndroidElement;
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.InnerException);
                }
                if (buyNowBtn != null)
                {
                    Product p = new Product();
                    p.ProdName = item.FindElement(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/name")).Text;
                    p.buyNowButton = buyNowBtn;
                    p.addtoCartButton = item.FindElement(ByAndroidUIAutomator.Id(ObjectRepository.AddToCart)) as AndroidElement;
                    productList.Add(p);
                }
                else
                    continue;
            }
            return productList;
        }

        /// <summary>
        /// Returns the stock level by calling the REST API to get cart entries
        /// </summary>
        /// <returns>Integer value representing stock level</returns>
        public static int stockLevel()
        {
            ServicePointManager.ServerCertificateValidationCallback = new RemoteCertificateValidationCallback
            (
                delegate { return true; }
            );
            String endpoint = "https://tst.pl.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/" + loginId;
            String contentType = "application/json";

            HttpVerb method = HttpVerb.GET;
            String postdata = "";
            RestClientExtended rest = new RestClientExtended(endpoint, method, contentType, postdata);
            string response = rest.MakeRequest("/carts/current/entries");

            RootObject root = JsonConvert.DeserializeObject<RootObject>(response);
            int level = root.orderEntries[0].product.stock.stockLevel;

            return level;
        }

        ///// <summary>
        ///// Returns the addresses for every user, by calling the REST API to get addresses
        ///// </summary>
        ///// <returns>String value representing the addresses</returns>
        //public static string getAddress()
        //{
        //    String endpoint = "https://tst.pl.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/" + loginId;
        //    String contentType = "application/json";

        //    HttpVerb method = HttpVerb.GET;
        //    String postdata = "";
        //    RestClientExtended rest = new RestClientExtended(endpoint, method, contentType, postdata);
        //    string response = rest.MakeRequest("/addresses");
        //    // RootObjectAddress root = JsonConvert.DeserializeObject<RootObjectAddress>(response);

        //    return response;
        //}
        /// <summary>
        /// This class provides all the functionalities and constants for features related to Context menu.
        /// </summary>
 
        public static class ContextMenu
        {
            static MobilePageInstance Instance;

            private static MobilePageInstance _instance
            {
                get
                {
                    return Application.MobilePageInstanceInstance;
                }
            }
            /// <summary>
            /// Provides collection of all menu items.
            /// </summary>
            public enum MenuItem
            {
                US,
                UK

            }

            /// <summary>
            /// Selects a field in the menu item
            /// </summary>
            /// <param name="flds">flds represents the context menu for state and salutation</param>
            /// <param name="mI">mI represents the menu item</param>

            public static void Select(Fields flds, MenuItem mI)
            {
                if (flds == Fields.Country_Spinner)
                {
                    _instance.ClickById(ObjectRepository.Country_Spinner);
                    if (mI == MenuItem.UK)
                    {
                        _instance.ClickByName("UK");
                    }
                    if (mI == MenuItem.US)
                    {
                        _instance.ClickByName("US");
                    }
                    Busy();
                 }
                
            }

        }
    }

        /// <summary>
        /// This class represents the network error related functionalities under test.
        /// </summary>
        public static class NetWork_Error
        {

            public static MobilePageInstance _instance
            {
                get
                {
                    return Application.MobilePageInstanceInstance;
                }
            }
            
            /// <summary>
            /// Collection of all Buttons
            /// </summary>
            public enum Button
            {
                OK
            }

            /// <summary>
            /// Returns true if the 'Network Error' pop up shows
            /// </summary>
            /// <returns>boolean value</returns>
            public static bool IsVisible()
            {
                bool bVisible = false;
                if (_instance.GetElementByText("You are offline") != null)
                    bVisible = true;
                return bVisible;

            }

            /// <summary>
            /// Clicks the 'ok' button when the 'Network Error' pop up appears
            /// </summary>
            /// <param name="btn">btn represent the name of the Button</param>
            public static void Click(Button btn)
            {
                if (IsVisible())
                    _instance.ClickById(ObjectRepository.GetData("NetworkError_OK"));
                IAP_Common.Busy();
            }


        }

        /// <summary>
        /// This class represents the timeout error related functionalities under test.
        /// </summary>
        public static class Timeout_Error
        {
            public static MobilePageInstance _instance
            {
                get
                {
                    return Application.MobilePageInstanceInstance;
                }
            }

            /// <summary>
            /// Collection of all Buttons
            /// </summary>
            public enum Button
            {
                OK
            }

            /// <summary>
            /// Returns true if "Network Error" text is visible
            /// </summary>
            /// <returns>a boolean value</returns>
            public static bool IsVisible()
            {
                bool bVisible = false;
                if (_instance.GetElementByText("Network Error") != null)
                    bVisible = true;
                return bVisible;
            }

            /// <summary>
            /// Clicks the desired button
            /// </summary>
            /// <param name="btn">btn represents the name of the Button</param>
            public static void Click(Button btn)
            {
                if (IsVisible())
                    _instance.ClickById("com.philips.cdp.di.iapdemo:id/dialogButtonCancel");
                IAP_Common.Busy();
            }
        }
        /// <summary>
        /// This class provides all the functionalities and constants for features related to Crash Data page.
        /// </summary>
        public static class Crash_Data
        {
            public static MobilePageInstance _instance
            {
                get
                {
                    return Application.MobilePageInstanceInstance;
                }
            }
            /// <summary>
            /// Collection of all buttons
            /// </summary>
            public enum Button
            {
                ALWAYS_SEND,
                DISMISS,
                SEND
            }
            /// <summary>
            /// Returns true if "Crashed Data" text is visible
            /// </summary>
            /// <returns>a boolean value</returns>
            public static bool IsVisible()
            {
                bool bVisible = false;
                if (_instance.GetElementByText("Crash Data") != null)
                    bVisible = true;
                return bVisible;
            }
            /// <summary>
            /// Clicks the desired button
            /// </summary>
            /// <param name="btn">btn represents the name of the Button</param>

            public static void Click(Button btn)
            {
                if (IsVisible() && btn == Button.ALWAYS_SEND)
                    _instance.ClickById(ObjectRepository.ALWAYS_SEND);
                if (IsVisible() && btn == Button.DISMISS)
                    _instance.ClickById(ObjectRepository.DISMISS);
                if (IsVisible() && btn == Button.SEND)
                    _instance.ClickById(ObjectRepository.SEND);
                IAP_Common.Busy();

            }
            /// <summary>
            /// Waits for the Crash Screen page to appear
            /// Returns false if the Crash Screen doesn't appear after loopcount exceeds 10
            /// </summary>
            /// <returns>a boolean value</returns>
            public static bool WaitforCrashScreen()
            {
                IMobilePageControl element = null;
                //AndroidElement element = null;
                int loopcount = -0;
                while (element == null)
                {
                    element = _instance.GetElement(SearchBy.Id, ObjectRepository.Crash_Title);
                    //element = (AndroidElement)_launchdriver.FindElementById("com.philips.cdp.di.iapdemo:id/demo_title");
                    loopcount++;
                    if (element != null)
                        break;
                    if (loopcount > 10)
                        break;

                }
                if (element != null)
                    return true;
                else
                    return false;
            }
        }
    }

