using OpenQA.Selenium;
using OpenQA.Selenium.Appium;
using OpenQA.Selenium.Appium.Android;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin
{
    public class RetailerScreen : IAP_Common
    {
        
        /// <summary>
        /// Provides collection of all the buttons in the Retailer Screen.
        /// </summary>
        public enum Button
        {
           CrossButton,
            RetailerInfo
        }
        /// <summary>
        /// Provides colletion of constant values representing swipe direction in the Retailer Screen.
        /// </summary>
        public enum Direction
        {
            Up,
            Down,
            Left,
            Right
        }

        /// <summary>
        /// Provides collection of all textviews in the Retailer Screen.
        /// </summary>
        public enum TextView
        {
            RetailerScreenTitle
        }
        /// <summary>
        /// Clicks the desired button in the Retailer Screen.
        /// </summary>
        /// <param name="btn">btn represents the name of the button to be clicked.</param>
        public static void Click(Button btn)
        {
            if (btn == Button.CrossButton)
                _instance.ClickById(ObjectRepository.CrossButton);
            else if (btn == Button.RetailerInfo)
                _instance.ClickById(ObjectRepository.RetailerInfo);
          
        }
        /// <summary>
        /// Provides a construct for accessing product details in the Retailer Screen.
        /// </summary>
        public struct Retailer
        {
            public string RetailerName;
            public AndroidElement RetailerInfo;

        }
        /// <summary>
        /// Returns a list of all the retailers present in the Retailer screen.
        /// </summary>
        /// <returns>a list of strings representing the retailer names.</returns>
        public static List<String> GetRetailerListItems()
        {
            List<AndroidElement> items = _getRetailorListed();
            List<String> productItems = new List<string>();
            foreach (AndroidElement item in items)
            {
                productItems.Add(item.Text);
            }
            return productItems;
        }
        /// <summary>
        /// Clicks the desired button of the corresponding Retailer name
        /// </summary>
        /// <param name="retailername">retailername must contain a string containing the name of the retailer</param>
        /// <param name="btn">btn represents the name of the button to be clicked</param>
        public static void Click(string retailername, Button btn)
        {
            List<AndroidElement> ProdList = _getRetailorListed();
            List<Retailer> prod = _getButtonSet();
            prod.Find(item => item.RetailerName == retailername).RetailerInfo.Click();

        }
        /// <summary>
        /// Returns a list of all the Retailer present in the Retailer Screen.
        /// </summary>
        /// <returns>a list of strings representing the product names</returns>
        private static List<AndroidElement> _getRetailorListed()
        {
            //IMobilePageControl  element = _instance.GetElement(SearchBy.Id,ObjectRepository.ProductList);
            List<AndroidElement> elementList = new List<AndroidElement>();

            
            int indx = _instance.GetElement(SearchBy.Id, ObjectRepository.RetailerList).ElementInstance.FindElements(By.Id(ObjectRepository.RetailerProductName)).Count;

            AndroidElement[] items = new AndroidElement[indx];

            _instance.GetElement(SearchBy.Id, ObjectRepository.RetailerList).ElementInstance.FindElements(By.Id(ObjectRepository.RetailerProductName)).CopyTo(items, 0);

            //element.ElementInstance.FindElements(By.Id(ObjectRepository.TextView)).CopyTo()
            foreach (AndroidElement elem in items)
            {
                //string s = elem.Text;
                elementList.Add(elem);
            }
            return elementList;


        }
        /// <summary>
        /// Returns a list of IMobilePageControls, containing all desired buttons and Textviews
        /// </summary>
        /// <param name="btn">btn reprents the name of the button </param>
        /// <param name="tv">btn reprents the name of the button </param>
        /// <returns>a list of IMobilePageControls</returns>
        private static List<Retailer> _getButtonSet()
        {
            List<Retailer> productList = new List<Retailer>();

            IWebElement Elem = _instance.GetElement(SearchBy.Id, ObjectRepository.RetailerList).ElementInstance;
            int i = Elem.FindElements(ByAndroidUIAutomator.ClassName("android.widget.RelativeLayout")).Count;
            AndroidElement[] items = new AndroidElement[i];
            Elem.FindElements(ByAndroidUIAutomator.ClassName("android.widget.RelativeLayout")).CopyTo(items, 0);
            foreach (AndroidElement item in items)
            {
                AndroidElement retailerScreen_InfoButton = null;

                try
                {
                    retailerScreen_InfoButton = item.FindElement(ByAndroidUIAutomator.Id(ObjectRepository.RetailerInfo)) as AndroidElement;
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.InnerException);
                }
                if (retailerScreen_InfoButton != null)
                {
                    Retailer p = new Retailer();
                    p.RetailerName = item.FindElement(ByAndroidUIAutomator.Id(ObjectRepository.RetailerProductName)).Text;
                    p.RetailerInfo = retailerScreen_InfoButton;

                    productList.Add(p);
                }
                else
                    continue;
            }
            return productList;
        }

        /// <summary>
        /// Waits for the RetailerScreen page to appear
        /// Returns false if the Retailer Screen doesn't appear after loopcount exceeds 10
        /// </summary>
        /// <returns>a boolean value</returns>
        public static bool WaitforRetailerScreen()
        {
            IMobilePageControl element = null;
            //AndroidElement element = null;
            int loopcount = -0;
            while (element == null)
            {
                element = _instance.GetElement(SearchBy.Id, ObjectRepository.RetailerScreenTitle);
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
        /// Swipes across the Screen by the desired amount
        /// </summary>
        /// <param name="direction">direction represents the Direction of Swipe</param>
        /// <param name="value">value represents the amount by which the screen needs to be swiped</param>
        public static void Swipe(Direction direction, int value)
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.Id, ObjectRepository.RetailerList);
            Point pt = new Point(control.Coordinates[0], control.Coordinates[1]);
            Size sz = new Size(control.Size[0], control.Size[1]);
            Point srcPoint = new Point((pt.X + sz.Width / 2), (pt.Y + sz.Height / 2));
            Point dstPoint;
            if (direction == Direction.Up)
            {
                dstPoint = new Point(srcPoint.X, (srcPoint.Y - 400));
                MobileDriver.Swipe(srcPoint.X, srcPoint.Y, dstPoint.X, dstPoint.Y);
            }
            else
            {
                dstPoint = new Point(srcPoint.X, (srcPoint.Y + 400));
                MobileDriver.Swipe(srcPoint.X, srcPoint.Y, dstPoint.X, dstPoint.Y);
            }
        }
    }
}
