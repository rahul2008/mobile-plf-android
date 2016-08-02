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
using Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS;
//using Newtonsoft.Json.Linq;
//using Newtonsoft.Json;
using System.Net.Security;

namespace Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS
{
    
    /// <summary>
    /// This class provides all the functionalities and constants for features related to Home Screen page.
    /// </summary>
    public class HomeScreen
    {  
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
     
        /// <summary>
        /// Collection of all Buttons
        /// </summary>
        public enum Button
        {
            ShopNow,
            SelectStore,
            CartImage,
            LogOut,
            Back,
            Register
        }
       
        static MobilePageInstance Instance;

        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
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

            if (btn == Button.CartImage)
                Instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.ImageCart).Click();
            else if (btn == Button.LogOut)
                Instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.LogOut).Click();
            else if (btn == Button.Back)
                Instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.Back).Click();
            else if (btn == Button.ShopNow)
                Instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.ShopNow).Click();
            else if (btn == Button.SelectStore)
                Instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.SelectStore).Click();
            else if (btn == Button.Register)
                Instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.Register).Click();
        }

        /// <summary>
        /// Checks if a button is enabled or not
        /// </summary>
        /// <param name="btn">btn represents one of the buttons present on the screen</param>
        /// <returns>a boolean value</returns>
        public static bool IsEnabled(Button btn)
        {
            bool isEnabled = false;
            if (btn == Button.LogOut)
                isEnabled = _instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.LogOut).Enabled;
            else if (btn == Button.CartImage)
                isEnabled = _instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.ImageCart).Enabled;
            else if (btn == Button.Back)
                isEnabled = _instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.Back).Enabled;
            else if (btn == Button.ShopNow)
                isEnabled = _instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.ShopNow).Enabled;
            else if (btn == Button.SelectStore)
                isEnabled = _instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.SelectStore).Enabled;
            else if (btn == Button.Register)
                isEnabled = _instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.Register).Enabled;

            return isEnabled;
        }

        public struct Country
        {
            public string name;
            public IWebElement button;
        
        }

        private static List<Country> _getCountryListForSelectStore()
        {
            List<Country> countryList = new List<Country>();
            int i = MobileDriver.iOSdriver.FindElements(By.ClassName("UIACollectionCell")).Count;
            IWebElement[] items = new IWebElement[i];
            MobileDriver.iOSdriver.FindElements(By.ClassName("UIACollectionCell")).CopyTo(items, 0);
            foreach (IWebElement item in items)
            {
                Country c = new Country();
                c.name = item.FindElements(By.ClassName("UIAButton"))[0].Text;
                c.button =item.FindElements(By.ClassName("UIAButton"))[0];
                countryList.Add(c);   
            }
            return countryList;
        }

        /// <summary>
        /// This method selects the store location(either US or UK).
        /// </summary>
        /// <param name="name">name represents the store location, (country name)either US or UK</param>
        public static void SelectCountry(string name)
        {

            _instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.SelectStore).Click();
            List<Country> c = _getCountryListForSelectStore();
            c.Find(p => p.name == name).button.Click();
            //_instance.GetElements(SearchBy.Xpath, "//UIAApplication[1]/UIAWindow[1]/UIAAlert[1]/UIACollectionView[1]").Find(p => p.Text == name).Click();
        }


        /// <summary>
        /// Waits for the HomeScreen page to appear
        /// Returns false if the Shopping Cart Screen doesn't appear after loopcount exceeds 10
        /// </summary>
        /// <returns>a boolean value</returns>
        public static bool WaitforHomeScreen()
        {
            IMobilePageControl element = null;
            //AndroidElement element = null;
            int loopcount = -0;
            while (element == null)
            {
                element = _instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.Register);
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

        /// <summary>
        /// Checks if the Screen Title is "Build Number"
        /// </summary>
        /// <returns>a boolean value</returns>
        public static bool IsVisible()
        {
            bool bVisible = false;
            IMobilePageControl element = _instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.ImageCart);
            if (element!=null) 
                bVisible = true;
            return bVisible;

        }
       
        /// <summary>
        /// Returns a string containing the Screen Title
        /// </summary>
        /// <returns>a string value</returns>
        public static string GetScreenTitle()
        {
            return _instance.GetElement(SearchBy.Xpath,Repository.iOS.HomeScreen.DemoTitle).Text;
        }
}
}
