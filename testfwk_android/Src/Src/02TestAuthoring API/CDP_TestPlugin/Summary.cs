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

namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin
{
    /// <summary>
    /// This class provides all the functionalities and constants for features related to Order Summary page.
    /// </summary>
    public class Summary:IAP_Common
    {
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
            Shipping_FirstName,
            Shipping_Address,
            Billing_FirstName,
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
                    desiredText = ObjectRepository.ArrowButton;
                    break;
                case Button.Cancel:
                    desiredText = ObjectRepository.CancelButton;
                    break;
                case Button.Pay_Now:
                    desiredText = ObjectRepository.PayNow;
                    break;
            }
            _instance.ClickById(desiredText);
        }

        /// <summary>
        /// Returns the text from TextView in the Order Summary page.
        /// </summary>
        /// <param name="txt">txt represents the name of the textview for which the text needs to be retrieved.</param>
        /// <returns>string value representing the text in the text view control which was passed as parameter.</returns>
        public static string GetText(Text txt)
        {
            string desiredText = string.Empty;
            switch (txt)
            {
                case Text.Billing_Address:
                    desiredText = ObjectRepository.Billing_Address;
                    break;
                case Text.Billing_FirstName:
                    desiredText = ObjectRepository.Billing_Name;
                    break;
                case Text.Shipping_Address:
                    desiredText = ObjectRepository.Shipping_Address;
                    break;
                case Text.Shipping_FirstName:
                    desiredText = ObjectRepository.Shipping_Name;
                    break;
                case Text.TotalPrice:
                    desiredText = ObjectRepository.TotalPrice;
                    break;
                case Text.DeliveryPrice:
                    desiredText = ObjectRepository.DeliveryPrice;
                    break;
                case Text.TotalLabel:
                    desiredText = ObjectRepository.TotalLabel;
                    break;
            }
            string retText=_instance.GetElement(SearchBy.Id, desiredText).Text;

            if(txt==Text.TotalPrice)
            {
               List<IMobilePageControl> totalprices= _instance.GetElements(SearchBy.Id, desiredText);
               retText = totalprices.Last().Text;
            }

            return retText;
        }

        /// <summary>
        /// Returns prodct details in the Order Summary Page, as a list.
        /// </summary>
        /// <returns>List of type ProductDetails representing the product details</returns>
        public static List<ProductDetails> GetText()
        {
            List<ProductDetails> ProductList = new List<ProductDetails>();
            int count = _instance.GetElement(SearchBy.ClassName, ObjectRepository.RecyclerView).ElementInstance.FindElements(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/rl_list_item")).Count;
            //int count= _instance.GetElement(SearchBy.Id, ObjectRepository.OrderSummary).ElementInstance.FindElements(By.ClassName("android.widget.LinearLayout")).Count;
           IWebElement[] layouts = new IWebElement[count];
           _instance.GetElement(SearchBy.ClassName, ObjectRepository.RecyclerView).ElementInstance.FindElements(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/rl_list_item")).CopyTo(layouts, 0);
            //_instance.GetElement(SearchBy.Id, ObjectRepository.OrderSummary).ElementInstance.FindElements(By.ClassName("android.widget.LinearLayout")).CopyTo(layouts, 0);
           for (int i = 0; i < count - 1; i++)
           {
               ProductDetails pdetails=new ProductDetails();
               pdetails.ProductName = layouts[i].FindElement(By.Id(ObjectRepository.ProductName)).Text;
               pdetails.Quantity = layouts[i].FindElement(By.Id(ObjectRepository.SummaryQuantity)).Text;
               pdetails.IndividualPrice = layouts[i].FindElement(By.Id(ObjectRepository.TotalPrice)).Text;
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
                element = _instance.GetElement(SearchBy.Id, ObjectRepository.ScreenTitle);
                
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
        /// Returns a string containing the screen title in the Order Summary Page.
        /// </summary>
        /// <returns>String value representing the screen title</returns>
        public static string GetScreenTitle()
        {
            return _instance.GetTextById(ObjectRepository.ScreenTitle);
        }

        /// <summary>
        /// Swipes the screen with specified magnitude in the direction provided.
        /// </summary>
        /// <param name="direction">Specifies the direction of swipe</param>
        /// <param name="value">Magnitude of swipe, to be performed.</param>
        public static void Swipe(Direction direction, int value)
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.Id, ObjectRepository.SummaryScroll);
            if (null == control) return;
            Point pt = new Point(control.Coordinates[0], control.Coordinates[1]);
            Size sz = new Size(control.Size[0], control.Size[1]);
            Point srcPoint = new Point((pt.X + sz.Width / 2), (pt.Y + sz.Height / 2));
            Point dstPoint;
            if (direction == Direction.up)
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

        /// <summary>
        /// Checks for existence of the text view, in the Order Summary Page.
        /// </summary>
        /// <param name="tv">tv represents the name of the textview</param>
        /// <returns>Boolean value representing the presence of the text view in the Order Summary Page.</returns>
        public static bool IsExist(Text tv)
        {
            bool bExist = false;
            string desiredText = string.Empty;
            switch (tv)
            {
                case Text.TotalPrice:
                    desiredText = ObjectRepository.TotalPrice;
                    break;
                case Text.TotalLabel:
                    desiredText = ObjectRepository.TotalLabel;
                    break;
                case Text.DeliveryPrice:
                    desiredText = ObjectRepository.DeliveryPrice;
                    break;
                case Text.Billing_Address:
                    desiredText = ObjectRepository.Billing_Address;
                    break;
            }
            if (_instance.GetElement(SearchBy.Id, desiredText) != null)
                bExist = true;
            else
                bExist = false;
            return bExist;
        }
    }
}
