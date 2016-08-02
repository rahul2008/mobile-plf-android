using OpenQA.Selenium;
using OpenQA.Selenium.Appium;
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

namespace Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS
{
    /// <summary>
    /// This class provides all the functionalities and constants for features related to Order Summary page.
    /// </summary>
    public class Summary
    {
        public static int n;

        public Summary()
        {
        }

        /// <summary>
        /// Provides collection of all the buttons in the Order Summary page.
        /// </summary>
        public  enum Button
        {
            UpButton,
            Pay_Now,
            Cancel
        }

        /// <summary>
        /// Provides collection of all textviews in the Order Summary page.
        /// </summary>
        public  enum Text
        {
            Shipping_Address,
            Billing_Address,
            DeliveryPrice,
            TotalPrice,
            TotalLabel
        }
       
        /// <summary>
        /// Provides colletion of constant values representing swipe direction in the Order Summary page.
        /// </summary>
        public enum Direction
        {
            up,
            down
        }

        /// <summary>
        /// Provides a construct for accessing products in the Order Summary page.
        /// </summary>
        public struct  ProductDetails
        {
           public string ProductName;
           public string IndividualPrice;
           public string Quantity;
        }


        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }

        /// <summary>
        /// Clicks the desired button in the Order Summary page.
        /// </summary>
        /// <param name="btn">btn represents the name of the button to be clicked.</param>
        public static void Click(Button btn)
        {
            string desiredText = string.Empty;
            switch(btn)
            {
                case Button.UpButton:
                    desiredText = Repository.iOS.ShoppingCart.Back;
                    break;
                case Button.Cancel:
                    desiredText = Repository.iOS.SummaryScreen.SummaryCancel;
                    break;
                case Button.Pay_Now:
                    desiredText = Repository.iOS.SummaryScreen.PayNow;
                    break;
            }
            _instance.GetElement(SearchBy.Xpath, desiredText).Click();
        }

        /// <summary>
        /// Returns the text from TextView in the Order Summary page.
        /// </summary>
        /// <param name="txt">txt represents the name of the textview for which the text needs to be retrieved.</param>
        /// <returns>string value representing the text in the text view control which was passed as parameter.</returns>
        public static string GetText(Text txt)
        {
            n = MobileDriver.iOSdriver.FindElements(By.ClassName("UIATableView"))[0].FindElements(By.ClassName("UIATableCell")).Count;

            IWebElement[] layouts = new IWebElement[n];
            MobileDriver.iOSdriver.FindElements(By.ClassName("UIATableView"))[0].FindElements(By.ClassName("UIATableCell")).CopyTo(layouts, 0);
            string desiredText = string.Empty;
            switch (txt)
            {
                case Text.Billing_Address:
                    desiredText =layouts[n-3].FindElements(By.ClassName("UIAStaticText"))[1].Text;
                    break;
                case Text.Shipping_Address:
                    desiredText = layouts[n - 4].FindElements(By.ClassName("UIAStaticText"))[1].Text;
                    break;
                case Text.TotalPrice:
                    desiredText = layouts[n - 1].FindElements(By.ClassName("UIAStaticText"))[3].Text;
                    break;
                case Text.DeliveryPrice:
                    desiredText = layouts[n - 2].FindElements(By.ClassName("UIAStaticText"))[1].Text;
                    break;
                case Text.TotalLabel:
                    desiredText = layouts[n - 1].FindElements(By.ClassName("UIAStaticText"))[1].Text;
                    break;
            }
            return desiredText;
        }

        /// <summary>
        /// Returns prodct details in the Order Summary Page, as a list.
        /// </summary>
        /// <returns>List of type ProductDetails representing the product details</returns>
        public static List<ProductDetails> GetText()
        {
            List<ProductDetails> ProductList = new List<ProductDetails>();
            n = MobileDriver.iOSdriver.FindElements(By.ClassName("UIATableView"))[0].FindElements(By.ClassName("UIATableCell")).Count;
            IWebElement[] layouts = new IWebElement[n];
            MobileDriver.iOSdriver.FindElements(By.ClassName("UIATableView"))[0].FindElements(By.ClassName("UIATableCell")).CopyTo(layouts, 0);
           for (int i = 0; i < n - 4; i++)
           {
               ProductDetails pdetails=new ProductDetails();
               pdetails.ProductName = layouts[i].FindElements(By.ClassName("UIAStaticText"))[0].Text;
               pdetails.Quantity = layouts[i].FindElements(By.ClassName("UIAStaticText"))[1].Text;
               pdetails.IndividualPrice = layouts[i].FindElements(By.ClassName("UIAStaticText"))[2].Text;
               ProductList.Add(pdetails);

           }
           return ProductList;
        }

        /// <summary>
        /// Waits for the Order Summary page to appear.
        /// </summary>
        /// <returns>Boolean value representing the screen appearance(true-appearance,false-nonappearance) </returns>
        public static bool WaitforSummaryScreen()
        {
            IMobilePageControl element = null;
            //AndroidElement element = null;
            int loopcount = -0;
            while (element == null)
            {
                element = _instance.GetElement(SearchBy.Xpath, Repository.iOS.SummaryScreen.PayNow);
                
                loopcount++;
                if (element != null)
                    break;
                if (loopcount > 2)
                    break;
                
            }
            if (element != null)
                return true;
            else
                return false;
        }
            

        /// <summary>
        /// Returns a string containing the screen title in the Order Summary Page.
        /// </summary>
        /// <returns>String value representing the screen title</returns>
        public static string GetScreenTitle()
        {
            return _instance.GetElement(SearchBy.Xpath, Repository.iOS.SummaryScreen.SummaryScreenTitle).Text;
        }

        /// <summary>
        /// Checks for existence of the text view, in the Order Summary Page.
        /// </summary>
        /// <param name="tv">tv represents the name of the textview</param>
        /// <returns>Boolean value representing the presence of the text view in the Order Summary Page.</returns>
        public static bool IsExist(Text tv)
        {
            n = MobileDriver.iOSdriver.FindElements(By.ClassName("UIATableView"))[0].FindElements(By.ClassName("UIATableCell")).Count;

            IWebElement[] layouts = new IWebElement[n];
            MobileDriver.iOSdriver.FindElements(By.ClassName("UIATableView"))[0].FindElements(By.ClassName("UIATableCell")).CopyTo(layouts, 0);
            bool bExist = false;
            string desiredText = string.Empty;
            switch (tv)
            {
                case Text.TotalPrice:
                    desiredText = layouts[n - 1].FindElements(By.ClassName("UIAStaticText"))[2].Text;
                    break;
                case Text.TotalLabel:
                    desiredText = layouts[n - 3].FindElements(By.ClassName("UIAStaticText"))[1].Text;
                    break;
                case Text.DeliveryPrice:
                    desiredText = layouts[n - 2].FindElements(By.ClassName("UIAStaticText"))[1].Text;
                    break;
                case Text.Billing_Address:
                    desiredText = layouts[n - 3].FindElements(By.ClassName("UIAStaticText"))[1].Text;
                    break;
                case Text.Shipping_Address:
                    desiredText = layouts[n - 4].FindElements(By.ClassName("UIAStaticText"))[1].Text;
                    break;
            }
            if (desiredText != null)
                bExist = true;
            else
                bExist = false;
            return bExist;
        }
    }
}
