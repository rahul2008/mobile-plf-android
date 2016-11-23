using OpenQA.Selenium;
using OpenQA.Selenium.Appium.Android;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin
{
    public class ProductList
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }

        public enum Button
        {
            Back_to_Homescreen
        }

        public enum TextView
        {
            Product_Name,
            Product_Id,
        }

        /// <summary>
        /// header text
        /// </summary>
        /// <returns></returns>
        public static string GetHeaderProductScreen()
        {
            return _instance.GetTextById(ConsumerCare.Android.ProductScreen.ProductHeaderText);
        }
        public static bool IsVisible1()
        {
            bool bVisible = false;
            if (GetHeaderProductScreen() == "Products")
                bVisible = true;
            return bVisible;
        }

        /// <summary>
        /// waiting for Product home screen
        /// </summary>
        /// <returns></returns>
        public static bool WaitforProductListHomeScreen()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ProductScreen.Back_To_home);
                loopCount++;
                if (homeScreenElement != null)
                    break;
                else
                    homeScreenElement = _instance.GetElement(SearchBy.ClassName, "android.widget.LinearLayout");
                break;
                if (loopCount > 10)
                    break;
            }
            if (homeScreenElement != null)
                return true;
            else
                return false;
        }

        public static bool IsVisible(Button btn)
        {
            bool isVisible = false;
            if (btn == Button.Back_to_Homescreen)
                isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ProductScreen.Back_To_home).Displayed;
            return isVisible;
        }

        public static bool IsEnable(Button btn)
        {
            bool isEnable = false;
            if (btn == Button.Back_to_Homescreen)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ProductScreen.Back_To_home).Enabled;
            return isEnable;
        }

        public static void ClickBack(Button btn)
        {
            if (btn == Button.Back_to_Homescreen)
                _instance.ClickById(ConsumerCare.Android.ProductScreen.Back_To_home);
        }

        /// <summary>
        /// Return list of all list of productts in the screen(Checking list of products peresent oer what)
        /// </summary>
        /// <returns></returns>
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
        private static List<AndroidElement> _getProductListed()
        {
            List<AndroidElement> elementList = new List<AndroidElement>();
            System.Collections.ObjectModel.ReadOnlyCollection<IWebElement> welementList = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ProductScreen.Product_Screen).ElementInstance.FindElements(By.Id(ConsumerCare.Android.ProductScreen.Product_Id));
            int indx = welementList.Count();
            AndroidElement[] items = new AndroidElement[indx];
            welementList.CopyTo(items, 0);
            foreach (AndroidElement elem in items)
            {
                elementList.Add(elem);
            }
            return elementList;
        }

        /// <summary>
        /// Clicking on Mobile driver back
        /// </summary>
        public static void RefreshPage()
        {
            for (int i = 0; i < 3; i++)
            {
                MobileDriver.RefreshPage();
            }
            MobileDriver.FireKeyEvent(4);
        }

        /// <summary>
        /// Selecting product
        /// </summary>
        public static void Swipe()
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.ClassName, "android.widget.ScrollView");
            int[] coords = new int[2];
            coords = control.Coordinates;
            int[] sz = new int[2];
            sz = control.Size;
            int Srcx = coords[0] + sz[0] / 2;
            int Srcy = coords[1] + sz[1] / 2;
            double count = (double)Srcy;
            count = count - 200;
            MobileDriver.Swipe((double)Srcx, (double)Srcy, (double)Srcx, (count));
        }
        public static void SelectProduct(string productID) 
        {
            int count = 0;
            List<string> str = ProductList.GetProductListItems();
            foreach (string str1 in str)
            {
                if (str1 == productID)
                {
                    _instance.ClickByText(str1);
                    count = 1;
                    break;
                }               
            }  
            if(count != 1)
            {
                Swipe();
                SelectProduct(productID);
            }
        }
    }
}
