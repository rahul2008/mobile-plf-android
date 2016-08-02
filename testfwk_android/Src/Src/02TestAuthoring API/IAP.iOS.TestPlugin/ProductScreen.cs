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
using System.Drawing;


namespace Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS
{
    /// <summary>
    /// This class provides all the functionalities and constants for features related to Product Screen.
    /// </summary>
    public class ProductScreen
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        static MobilePageInstance Instance;

        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }

        /// <summary>
        /// Collection of all buttons in Product Screen.
        /// </summary>
         public enum Button
        {
            Back,
            CartImage
        }

        /// <summary>
        /// Product details are listed in the structure, to enable features like click.
        /// </summary>
        public struct Product
        {
            public string name;
            public string productID;
            public string originalPrice;
            public string newPrice;
            public IWebElement clickableProperty;

        }

        /// <summary>
        /// Collection of constant values representing Swipe Direction
        /// </summary>
        public enum Direction
        {
            up,
            down
        }

        /// <summary>
        /// This method gets the list of products as listed in the Product screen.
        /// </summary>
        /// <returns></returns>
        public static List<Product> _getListOfProducts()
        {
            List<Product> productList = new List<Product>();
            int i = MobileDriver.iOSdriver.FindElements(By.ClassName("UIATableCell")).Count;
            IWebElement[] items = new IWebElement[i];
            MobileDriver.iOSdriver.FindElements(By.ClassName("UIATableCell")).CopyTo(items, 0);
            foreach (IWebElement item in items)
            {
                Product p = new Product();

                int cntOfDetails = item.FindElements(By.ClassName("UIAStaticText")).Count;
                p.name = item.FindElements(By.ClassName("UIAStaticText"))[0].Text;
                p.productID = item.FindElements(By.ClassName("UIAStaticText"))[1].Text;
                p.originalPrice = item.FindElements(By.ClassName("UIAStaticText"))[2].Text;
                if (cntOfDetails == 4)
                    p.newPrice = item.FindElements(By.ClassName("UIAStaticText"))[3].Text;
                p.clickableProperty = item.FindElements(By.ClassName("UIAStaticText"))[0];
                productList.Add(p);
            }
            return productList;
        }

        /// <summary>
        /// This method selects a given product, whose name matches the product name in the parameter.
        /// </summary>
        /// <param name="prodName">prodName represents the name of the product, as displayed in the UI.</param>
        public static void SelectProduct(string prodName)
        {
            List<Product> productList = new  List<Product>();
            productList = _getListOfProducts();
            productList.Find(p => p.name == prodName).clickableProperty.Click();
        }

        /// <summary>
        /// This method can be used to click on the desired button in the Product screen
        /// </summary>
        /// <param name="btn">btn represents the name of the button.</param>
        public static void Click(Button btn)
        {
            Instance = _instance;

            if (btn == Button.CartImage)
                Instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.ImageCart).Click();
            else if (btn == Button.Back)
                Instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.Back).Click();
        }

        /// <summary>
        /// This method waits for the Product screen page to appear.
        /// </summary>
        /// <returns>It returns true if the screen is loaded and false otherwise.</returns>
        public static bool WaitforProductScreen()
        {
            IMobilePageControl element = null;
            int loopcount = -0;
            while (element == null)
            {
                element = _instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.ImageCart);
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
        /// This method can be used to swipe the product list in up or down directions
        /// </summary>
        /// <param name="direction">Is enum value : Direction.up, Direction.down</param>
        /// <param name="value">This is the arbitrary value for amount of swipe(value*50)</param>
        public static void SwipeForiOS(Direction direction, int value)
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.ProductList);
            Point pt = new Point(control.Coordinates[0], control.Coordinates[1]);
            Size sz = new Size(control.Size[0], control.Size[1]);
            Point srcPoint = new Point((pt.X + sz.Width / 2), (pt.Y + sz.Height / 2));
            Point dstPoint;
            if (direction == Direction.up)
            {
                dstPoint = new Point(srcPoint.X, (srcPoint.Y - 50*value));
                MobileDriver.swipeForiOS(srcPoint.X, srcPoint.Y, dstPoint.X, dstPoint.Y);
            }
            else
            {
                dstPoint = new Point(srcPoint.X, (srcPoint.Y + 50*value));
                MobileDriver.swipeForiOS(srcPoint.X, srcPoint.Y, dstPoint.X, dstPoint.Y);
            }
        }
    }
}
