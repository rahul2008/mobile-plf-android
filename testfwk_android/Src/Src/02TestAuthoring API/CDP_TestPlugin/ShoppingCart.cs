using OpenQA.Selenium.Appium;
using OpenQA.Selenium.Appium.Android;
using OpenQA.Selenium.Appium.MultiTouch;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Drawing;
using OpenQA.Selenium;
using Philips.MRAutomation.Foundation.Common.ImageCompareLibrary;
using System.Configuration;
using System.Net;
using System.Net.Security;
using System.Threading;
using System.Xml;
using System.IO;

namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin
{
    /// <summary>
    /// This class provides all the functionalities and constants for features related to Shopping Cart page.
    /// </summary>
    public class ShoppingCart : IAP_Common
    {

        

        /// <summary>
        /// A variable to store the 
        /// 
        /// cost of all the products
        /// </summary>
        public static int totalcost = 0;

        /// <summary>
        /// A variable to store the VAT value in the Shopping Cart page
        /// </summary>
        public int vat = 0;
        
        public ShoppingCart()
        {

        }

        /// <summary>
        /// Provides colletion of constant values representing swipe direction in the Shopping Cart page.
        /// </summary>
        public enum Direction
        {
            Up,
            Down
        }

        /// <summary>
        /// Provides collection of all the buttons in the Shopping Cart page.
        /// </summary>
        public enum Button
        {
            Checkout,
            ContinueShopping,
            UpButton,
            Dots,
            ClaimVoucherArrow,
            ContinueShopping_1
        }

        /// <summary>
        /// Provides a collection for accessing products in the Shopping Cart page.
        /// </summary
        public enum ProductDetailsText
        {
            Price,
            Quantity,
            Prod_Description
        }

        /// <summary>
        /// Provides details/values for 'Delivery Via UPS parcel' in the Shopping Cart page.
        /// </summary>
        public enum TextView
        {
            UPS_PARCEL,
            VAT,
            TotalCost,
            Title,
            TotalItems
        }

        /// <summary>
        /// This class contains methods to select the Menu Items in the Context Menu.
        /// </summary>
        public static class ContextMenu
        {
            /// <summary>
            /// Collection of MenuItems
            /// </summary>
            public enum MenuItem
            {
                Delete,
                Info
            }

            public static XmlDocument Adjustments;

            /// <summary>
            /// Clicks the dotted menu button of the desired product and selects the desired options.
            /// </summary>
            /// <param name="productName">productName represents the name of the product.</param>
            /// <param name="mI">mI represents the operation that the user wants to perform. </param>
            public static void Select(string productName, MenuItem mI)
            {
                List<ProductDetails> pDetails = ShoppingCart._getproductList();
                pDetails.Find(p => p.Prod_Description == productName).dots.Click();

                System.Drawing.Size wSize = _instance.GetScreenSize();
                Logger.Info("X: " + wSize.Width + "\nY: " + wSize.Height);

                string runTimeLoc = Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location);
                Logger.Info(runTimeLoc);

                Adjustments = new XmlDocument();
                Adjustments.Load(runTimeLoc+"\\adjustments.xml");
                Logger.Info(Adjustments.InnerText);

                if (mI == MenuItem.Delete)
                {
                    int[] pt = _instance.GetElement(SearchBy.Id, ObjectRepository.Dots).Coordinates;
                    int x = pt[0] - 100;

                    String correction = Adjustments.SelectSingleNode("//y" + wSize.Height + "/delete").InnerText;
                    Logger.Info("Delete: correction: " + correction);
                    int y = pt[1] + Int16.Parse(correction);
                    
                    //int y = pt[1] + 220;//+375
                    // int y = pt[1] + 450;
                    //UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input tap " + "1019 572"); 
                    UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input tap " + x + " " + y);
                }
                else if (mI == MenuItem.Info)
                {
                    int[] pt = _instance.GetElement(SearchBy.Id, ObjectRepository.Dots).Coordinates;
                    int x = pt[0] - 100;

                    String correction = Adjustments.SelectSingleNode("//y" + wSize.Height + "/info").InnerText;
                    Logger.Info("Info: correction: " + correction);
                    int y = pt[1] + Int16.Parse(correction);

                    //int y = pt[1] + 275;//+575
                    //  int y = pt[1] + 650;
                    UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input tap " + x + " " + y);

                    //UtilityAuto.ExecuteProcess("/c adb -s " + mobileDriverConfig.DEVICE_ID + " shell input tap " + "1019 772"); 

                }
            }
        }

        /// <summary>
        /// Verifies the price of UPS Delivery Parcel when the total cost is specified.
        /// </summary>
        /// <returns>Booelan value representing the correctness of price value</returns>
        public static bool VerifyDeliveryParcelPrice()
        {
            string UPSValue;
            bool bRetVal = false;

            string sValue = ShoppingCart.GetText(ShoppingCart.TextView.TotalCost);
            double totalcost = Convert.ToDouble(ShoppingCart.GetRefinedText(sValue));
            UPSValue = ShoppingCart.GetText(ShoppingCart.TextView.UPS_PARCEL);
            UPSValue = (ShoppingCart.GetRefinedText(UPSValue));
            if (totalcost >= 100)
            {
                if (float.Parse(UPSValue) > 0.0)
                {
                    Logger.Info("Fail:UPS Delivery value is greater than 0, where as if the total cost is more than $100, the UPS Parcel Service Value Should be 0 ");
                }
                else
                {
                    bRetVal = true;
                    Logger.Info("Pass:UPS Delivery is not charged and is 0, which is a correct behavior");
                }
            }

            else
            {
                if (float.Parse(UPSValue) == 0.0)
                    Logger.Info(" Fail: UPS Delivery is not charged even when the total cost is less than 100");
                else
                {
                    bRetVal = true;
                    Logger.Info("Pass: UPS Delivery is  charged for total cost < 100 which is a correct behavior");
                }

            }

            Console.WriteLine("Delivery via USP Parcel price Present");
            return bRetVal;
        }

        /// <summary>
        /// Fetches a string containing text from the desired TextView of the desired product in the Shopping Cart page.
        /// </summary>
        /// <param name="productName">productName represents the name of the product.</param>
        /// <param name="tv">tv represents the TextView name.</param>
        /// <returns>string value representing the text in the text view control for the product which was passed as parameter.</returns>
        public static string GetText(string productName, ProductDetailsText tv)
        {
            List<ProductDetails> pDetails = _getproductList();
            string retVal = string.Empty;
            bool isExist = false;
            while (!isExist)
            {
                try
                {
                    if (tv == ProductDetailsText.Price)
                    {
                        retVal = pDetails.Find(p => p.Prod_Description == productName).price;
                        isExist = true;
                    }
                    if (tv == ProductDetailsText.Quantity)
                    {
                        retVal = pDetails.Find(p => p.Prod_Description == productName).Qty.Text;
                        isExist = true;
                    }
                }
                catch (Exception e)
                {
                    SwipeProductList(Direction.Up, 1);
                }
            }
            for (int i = pDetails.Count; i > 0; i--)
            {
                if (i == 0)
                    break;
                else
                    ShoppingCart.SwipeProductList(Direction.Down, 2);
            }
            return retVal;

        }

        /// <summary>
        /// Returns the text from TextView in the Shopping Cart page. 
        /// If the screen is scrollable, it will call the Swipe function internally till the desired TextView is found.
        /// </summary>
        /// <param name="tv">txt represents the name of the textview for which the text needs to be retrieved.</param>
        /// <returns>string value representing the text in the text view control which was passed as parameter.</returns>
        public static string GetText(TextView tv)
        {
            string retVal = string.Empty;
            string desiredID = string.Empty;
            //string retValue = _instance.GetElement(SearchBy.Id, "com.philips.cdp.di.iapdemo:id/shopping_cart_recycler_view").GetAttribute("scrollable");

            switch (tv)
            {
                case TextView.VAT:
                    desiredID = ObjectRepository.VAT;
                    //retVal = _instance.GetElements(SearchBy.Id, desiredID)[0].Text;
                    break;
                case TextView.TotalCost:
                    desiredID = ObjectRepository.TotalCost;
                    break;

                case TextView.UPS_PARCEL:
                    desiredID = ObjectRepository.UPSParcel;
                    //retVal = _instance.GetElements(SearchBy.Id, desiredID)[0].Text;
                    break;

                case TextView.TotalItems:
                    desiredID = ObjectRepository.TotalItems;
                    break;
            }

            //since all these will be at the bottom, swipe down
            if ((tv == TextView.VAT) || (tv == TextView.TotalCost) || (tv == TextView.UPS_PARCEL) || (tv == TextView.TotalItems))
            {
                Logger.Info("GetText: SwipeToBottom");
                SwipeToBottom();
            }

            if (tv == TextView.UPS_PARCEL)
            {
                try
                { //it is not available before the address present
                    retVal = _instance.GetTextById(desiredID);
                }
                catch (Exception e)
                {
                    return "0";
                }
            } 

            //total item needs to be split
            if(tv == TextView.TotalItems)
            {
                retVal = _instance.GetElement(SearchBy.Id, desiredID).Text;
                retVal = retVal.Split('(')[1].Split(' ')[0];
            }

            else if ((tv == TextView.VAT) || (tv == TextView.TotalCost))
            {
                string val = _instance.GetElement(SearchBy.Id, desiredID).Text;
                retVal = ShoppingCart.GetRefinedText(val);
                //retVal = _instance.GetElement(SearchBy.Id, desiredID).Text.Split('$')[1];
            }

            if (retVal == string.Empty)
                retVal = _instance.GetTextById(desiredID);

            return retVal;
        }

        public static void SwipeToBottom()
        {
            bool swipe = true;
            int check = 0;
            while(swipe && check<10)
            {
                SwipeProductList(Direction.Up, 3);
                if (_instance.GetElement(SearchBy.Id, ObjectRepository.TotalItems) != null) swipe = false;
                check++;
                Thread.Sleep(1000);
            }
        }

        public static void SwipeToTop()
        {
            int check = 0;
            String firstProduct = String.Empty;
            String currentFirst = String.Empty;
            if (_instance.GetElementNoWait(null, SearchBy.Id, "com.philips.cdp.di.iapdemo:id/empty_cart") != null)return;
            do
            {
                SwipeProductList(Direction.Down, 3);
                System.Collections.ObjectModel.ReadOnlyCollection<IWebElement> productItems;
                System.Collections.ObjectModel.ReadOnlyCollection<IWebElement> cfElem = _instance.GetElement(SearchBy.ClassName, ObjectRepository.RecyclerView).ElementInstance.FindElements(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/text1Name"));
                if(cfElem.Count>0)
                {
                    currentFirst = cfElem.First().Text;
                    if (firstProduct.Equals(currentFirst))
                        break;
                    firstProduct = currentFirst;
                }
                check++;
                Thread.Sleep(1000);
            } while (check < 10);
        }

        public static List<CartList> getCartItems()
        {
            ServicePointManager.ServerCertificateValidationCallback = new RemoteCertificateValidationCallback
              (
                  delegate { return true; }
              );
            CartList list = RestApiInvoker.GetCart("inapptest@mailinator.com", "UK");

            List<CartList> value = new List<CartList>();
            value.Add(list);
            return value;
        }
        /// <summary>
        /// This Function gives back the actual value in String removing all the prefixes.
        /// Useful in getting the Price Value like $800 => 800
        /// USD 800 => 800
        /// </summary>
        /// <param name="rawValue">rawValue represents the string which needs to be refined to get the numeric value</param>
        /// <example>
        /// string VatValue = ShoppingCart.GetText(ShoppingCart.TextView.VAT);
        /// ShoppingCart.GetRefinedText(VatValue);
        /// </example>
        /// <returns>a string value</returns>
        public static string GetRefinedText(string rawValue)
        {
            if (rawValue.Contains("USD"))
            {
                rawValue = rawValue.Split(' ')[1];
            }
            else if (rawValue.Contains("$"))
            {
                rawValue = rawValue.Split('$')[1];
            }
            else if (rawValue.Contains("£"))
            {
                rawValue = rawValue.Split('£')[1];
            }
            return rawValue;
        }

        /// <summary>
        /// Clicks the desired button in the Shopping Cart page.
        /// </summary>
        /// <param name="btn">btn represents the name of the button to be clicked.</param>
        public static void Click(Button btn)
        {
            Instance = _instance;
            if (btn == Button.UpButton)
                _instance.ClickById(ObjectRepository.UpButton);
            else if (btn == Button.Checkout)
                _instance.ClickById(ObjectRepository.Checkout);
            else if (btn == Button.ContinueShopping)
                _instance.ClickById(ObjectRepository.ContinueShopping);
            else if (btn == Button.ClaimVoucherArrow)
                _instance.ClickById(ObjectRepository.Voucher);
            else if (btn == Button.Dots)
                Instance.ClickById(ObjectRepository.Dots);
            else if (btn == Button.ContinueShopping_1)

                Instance.ClickById(ObjectRepository.Continues);
            
            //else
            //    Console.WriteLine("Not yet Implemented");
            Busy();
        }

        /// <summary>
        /// Returns true if the desired button is enabled.
        /// </summary>
        /// <param name="btn">btn represents the name of the button.</param>
        /// <returns>Returns a Boolean value</returns>
        public static bool IsEnabled(Button btn)
        {
            bool isEnabled = false;
            if (btn == Button.Checkout)
                isEnabled = _instance.GetElement(SearchBy.Id, ObjectRepository.Checkout).Enabled;
            else if (btn == Button.ContinueShopping)
                isEnabled = _instance.GetElement(SearchBy.Id, ObjectRepository.ContinueShopping).Enabled;
            else if (btn == Button.UpButton)
                isEnabled = _instance.GetElement(SearchBy.Id, ObjectRepository.UpButton).Enabled;
            else if (btn == Button.ClaimVoucherArrow)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.Voucher).Enabled;
            else if (btn == Button.ContinueShopping_1)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.Continues).Enabled;

            return isEnabled;
        }

        /// <summary>
        /// Returns a string containing the title of the screen.
        /// </summary>
        /// <returns>string value containing the title</returns>
        public static string GetScreenTitle()
        {
            return _instance.GetTextById(ObjectRepository.ScreenTitle);
        }

        /// <summary>
        /// Returns true if the Screen Title "Shopping Cart" is visible.
        /// </summary>
        /// <returns>boolean value representing the screen visibility.</returns>
        public static bool IsVisible()
        {
            String screenTitle = _instance.GetTextById(ObjectRepository.ScreenTitle);
            if (country.Equals("US") && screenTitle == "Shopping Cart") return true;
            if (country.Equals("UK") && screenTitle == "Shopping Basket") return true;
            return false;
        }

        /// <summary>
        /// To select the quantity of a given product from dropdown in Shopping Cart.
        /// </summary>
        /// <param name="qty">qty represents the number to be selected in the dropdown.</param>
        public static void Select(string productName, int qty, bool swiped = false)
        {
            if (swiped == false)
            {
                List<ProductDetails> pDetails = _getproductList();
                bool isExist = false;
                while (!isExist)
                {
                    try
                    {
                        //pDetails.Find(p => p.Prod_Description == productName).Qty.Click();
                        foreach (ProductDetails items in pDetails)
                        {
                            if (items.Prod_Description.Equals(productName))
                            {
                                items.Qty.Click();
                                Busy();
                                isExist = true;
                                break;
                            }
                            else
                            {
                                SwipeProductList(Direction.Up, 1);
                            }
                        }


                    }
                    catch (Exception e)
                    {
                        SwipeProductList(Direction.Up, 1);
                    }
                }

            }


            List<IMobilePageControl> txtList = _instance.GetElements(SearchBy.Id, ObjectRepository.QuantitydropdownMenuItem);
            foreach (IMobilePageControl ctrl in txtList)
            {
                if (ctrl.Text == qty.ToString())
                {
                    ctrl.Click();
                    break;
                }
            }
        }


        public static String SelectMax(string productName)
        {
            List<ProductDetails> pDetails = _getproductList();
            bool isExist = false;
            while (!isExist)
            {
                try
                {
                    foreach (ProductDetails items in pDetails)
                    {
                        if (items.Prod_Description.Equals(productName))
                        {
                            items.Qty.Click();
                            Thread.Sleep(2000);
                            isExist = true;
                            break;
                        }
                        else
                        {
                            SwipeProductList(Direction.Down, 1);
                        }
                    }


                }
                catch (Exception e)
                {
                    SwipeProductList(Direction.Up, 1);
                }
            }

            String LastQTY = "";
            String CurLast = "";
            List<IMobilePageControl> txtList;
            do{
                LastQTY = CurLast;
                SwipeQuantity(Direction.Up, 4);
                txtList = _instance.GetElements(SearchBy.Id, ObjectRepository.QuantitydropdownMenuItem);
                CurLast = txtList.Last().Text;
            }
            while(!CurLast.Equals(LastQTY));
            String ret = txtList.Last().Text;
            txtList.Last().Click();
            return ret;
        }


        /// <summary>
        /// Swipes through the Shopping Cart page when scrollable is enabled.
        /// </summary>
        /// <param name="dirn">dirn represents the direction in which the dropdown should be swiped.</param>
        /// <param name="indx">indx represents the amount by which swipe should happen.</param>
        public static void SwipeProductList(Direction dirn, int indx)
        {
            if (dirn == Direction.Up)
            {
                int[] sz = _instance.GetElement(SearchBy.ClassName, ObjectRepository.RecyclerView).Size;
                int height = sz[0];
                int[] pt = _instance.GetElement(SearchBy.ClassName, ObjectRepository.RecyclerView).Coordinates;
                int x1 = pt[0] + sz[1] / 2;
                int y1 = pt[1] + sz[0] / 2;
                int x2 = x1;
                int y2 = y1 - 200 * indx;
                if (y2 >= pt[1])
                    MobileDriver.Swipe(x1, y1-10, x2, y2);
            }
            else if (dirn == Direction.Down)
            {
                int[] sz = _instance.GetElement(SearchBy.ClassName, ObjectRepository.RecyclerView).Size;
                int height = sz[0];
                int[] pt = _instance.GetElement(SearchBy.ClassName, ObjectRepository.RecyclerView).Coordinates;
                int x1 = pt[0] + sz[1] / 2;
                int y1 = pt[1] + sz[0] / 2;
                int x2 = x1;
                int y2 = y1 + 200 * indx;
                if (y2 <= (pt[1] + sz[0]))
                    MobileDriver.Swipe(x1, y1, x2, y2);
            }
        }

        public static void SwipeProductList2(int x1, int y1, int x2, int y2)
        {
            MobileDriver.Swipe(x1, y1, x2, y2);
        }


        /// <summary>
        /// Swipe through the quantity dropdown in Shopping Cart screen.
        /// </summary>
        /// <param name="dirn">dirn represents the direction in which swipe should be performed.</param>
        /// <param name="indx">indx represents the amount by which swipe must be performed.</param>
        public static void SwipeQuantity(Direction dirn, int indx)
        {
            if (dirn == Direction.Up)
                SwipeUp(indx);
            else
                SwipeDown(indx);
        }

        private static void SwipeDown(int index)
        {
            int height;
            _instance.ClickById(ObjectRepository.Quantity);
            int[] sz = _instance.GetElement(SearchBy.ClassName, ObjectRepository.QuantityDropDown).Size;
            List<IMobilePageControl> txtList = _instance.GetElements(SearchBy.Id, ObjectRepository.QuantitydropdownMenuItem);
            height = sz[0] / txtList.Count;
            int[] pt = _instance.GetElement(SearchBy.ClassName, ObjectRepository.QuantityDropDown).Coordinates;
            int x1 = pt[0] + sz[1] / 2;
            int y1 = pt[1];// +sz[0] / 2;
            int x2 = x1;
            int y2 = y1 + height * index;
            if (y2 <= (pt[1] + sz[0]))
                MobileDriver.Swipe(x1, y1, x2, y2);
        }

        private static void SwipeUp(int index)
        {
            int height;
            _instance.ClickById(ObjectRepository.Quantity);
            int[] sz = _instance.GetElement(SearchBy.ClassName, ObjectRepository.QuantityDropDown).Size;
            List<IMobilePageControl> txtList = _instance.GetElements(SearchBy.Id, ObjectRepository.QuantitydropdownMenuItem);
            height = sz[0] / txtList.Count;
            int[] pt = _instance.GetElement(SearchBy.ClassName, ObjectRepository.QuantityDropDown).Coordinates;
            int x1 = pt[0] + sz[1] / 2;
            int y1 = pt[1] + sz[0]-100;
            int x2 = x1;
            int y2 = y1 - height * index;
            if (y2 >= pt[1])
                MobileDriver.Swipe(x1, y1, x2, y2);
        }


        /// <summary>
        /// Waits for the Shopping Cart screen
        /// Returns false if the Shopping Cart screen doesn't appear after loopcount exceeds 10.
        /// </summary>
        /// <returns>boolean value representing the appearance of the screen.</returns>
        public static bool WaitforShoppingCartScreen()
        {
            Logger.Info("WaitForShoppingCartScreen: start");
            int i; int retry;

            for (retry = 0; retry < 3; retry++)
            {
                for (i = 0; i < 60; i++)
                {
                    string title = GetScreenTitle();
                    if (title.Equals("Shopping Cart") || title.Equals("Shopping Basket"))
                    {
                        Logger.Info("Seeing: Shopping Cart title: " + title + " try: " + retry);
                        break;
                    }
                    Thread.Sleep(500);
                }
                if (i == 60)
                    Logger.Info("Product screen title is not shown" + " try: " + retry);
                for (; i < 60; i++)
                {
                    if (_instance.GetElementNoWait(null, SearchBy.Id, ObjectRepository.CartItem) != null)
                    {
                        Logger.Info("Cart items are loaded" + " try: " + retry);
                        return true;
                    }
                    IMobilePageControl empty = _instance.GetElementNoWait(null, SearchBy.Id, "com.philips.cdp.di.iapdemo:id/empty_cart");
                    if (empty != null)
                    {
                        Logger.Info("Cart is empty: try: " + retry);
                        return true;
                    }
                }
                Click(Button.UpButton);
                String title_s = GetScreenTitle();
                switch (title_s)
                {
                    case "E-Commerce Demo App":
                        HomeScreen.Click(HomeScreen.Button.CartImage);
                        break;
                    case "Products":
                        ProductScreen.Click(ProductScreen.Button.productScreen_ImageCart);
                        break;
                    default:
                        Logger.Info("In product detail screen " + title_s);
                        ProductScreen.Click(ProductScreen.Button.productScreen_ImageCart);
                        break;
                }
            }
            Logger.Info("Shopping Cart items not loaded after 3 tries");
            return false;
        }

        /*
        public static bool WaitforShoppingCartScreen()
        {
            Logger.Info("WaitForShoppingCartScreen: start");
            int i;
            for (i = 0; i < 40; i++)
            {
                string title = GetScreenTitle();
                if(title.Equals("Shopping Cart") || title.Equals("Shopping Basket"))
                {
                    Logger.Info("Seeing: Shopping Cart title: "+title);
                    break;
                }
                Thread.Sleep(500);
            }
            if (i == 40)
                Logger.Info("Product screen title is not shown");
            for (; i < 40; i++)
            {
                if (_instance.GetElementNoWait(null, SearchBy.Id, ObjectRepository.CartItem) != null)
                {
                    Logger.Info("Cart items are loaded");
                    return true;
                }
                IMobilePageControl empty = _instance.GetElementNoWait(null, SearchBy.Id, "com.philips.cdp.di.iapdemo:id/empty_cart");
                if (empty !=null)
                {
                    Logger.Info("Cart is empty");
                    return true;
                }
            }
            Logger.Info("Cart items not loaded");
            return false;
        }
        */
        /// <summary>
        /// Returns true if the desired product exists.
        /// </summary>
        /// <param name="productName">productName represents the name of the product.</param>
        /// <returns>boolean value representing the existence of the product.</returns>
        public static bool IsProductExist(string productName)
        {
            bool isExist = false;
            Logger.Info("IsProductExist: start:" + productName);
            List<String> items = GetProductNames();
            if (items.Exists(d => d.Equals(productName)))
            {
                isExist = true;
            }
            else
            {
                isExist = false;
            }
            Logger.Info("IsProductExist: end:" + productName);
            return isExist;
        }

        /// <summary>
        /// Returns a list containing all the individual prices of the products present in the Shopping Cart screen.
        /// </summary>
        /// <returns>a list of strings representing the product prices.</returns>
        public static List<string> GetAllProductPrice()
        {
            SwipeToTop();
            List<ProductDetails> items = ShoppingCart._getproductList();

            List<String> PriceList = new List<string>();
            //_instance.

            foreach (ProductDetails item in items)
            {
                if (item.price.Contains('$'))
                    PriceList.Add(item.price.Split('$')[1]);
                if (item.price.Contains("USD"))
                    PriceList.Add(item.price.Split(' ')[1]);
            }
            return PriceList;

        }

        /// <summary>
        /// Returns a list of all the products present in the Shopping Cart screen.
        /// </summary>
        /// <returns>a list of strings representing the product names.</returns>
        public static List<String> GetProductNames()
        {
            List<ProductDetails> items = ShoppingCart._getproductList();
            List<String> ProductNames = null;
            if (items != null)
            {
                ProductNames = new List<string>();
                foreach (ProductDetails item in items)
                    ProductNames.Add(item.Prod_Description);
            }
            String pNames = "---product names---\n";
            foreach (var p in items)
            {
                pNames += p.Prod_Description + "\n";
            }
            Logger.Info(pNames);

            return ProductNames;
        }
        /// <summary>
        /// Returns list of distinct elements in the list
        /// </summary>
        /// <param name="items"></param>
        /// <returns></returns>
        private static List<ProductDetails> getFilteredProducts(List<ProductDetails> items)
        {
            List<ProductDetails> tempItems = null;
            foreach (ProductDetails item in items)
            {
                if (!tempItems.Contains(item))
                {
                    tempItems.Add(item);
                }
            }
            return tempItems;
        }

        /// <summary>
        /// Deletes all the products present in the Shopping Cart screen.
        /// </summary>
        public static void DeleteAllProducts()
        {
            try
            {
                List<ProductDetails> items = ShoppingCart._getproductList();


                foreach (ProductDetails item in items)
                {
                    ShoppingCart.ContextMenu.Select(item.Prod_Description, ShoppingCart.ContextMenu.MenuItem.Delete);
                    ShoppingCart.WaitforShoppingCartScreen();
                }


            }
            catch (Exception e)
            {
                Logger.Info("Shopping cart is empty" + e.Message);
            }
        }

        /// <summary>
        /// Returns a list containing ProductDetails.
        /// </summary>
        /// <returns>list of ProductDetails from the Shopping Cart screen.</returns>
        public static List<ProductDetails> _getproductList()
        {
            bool flagDeliverByUPS = false;
            Thread.Sleep(3000);
            int yTop = 0;
            if (_instance.GetElementNoWait(null, SearchBy.Id, "com.philips.cdp.di.iapdemo:id/empty_cart") != null)
            {
                Logger.Info("Shopping cart empty");
                return null;
            }
            Logger.Info("swipe to top");
            String firstProduct;
            String currentFirstProduct = "";
            System.Collections.ObjectModel.ReadOnlyCollection<IWebElement> productItems;
            firstProduct = _instance.GetElement(SearchBy.ClassName, ObjectRepository.RecyclerView).ElementInstance.FindElements(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/text1Name")).First().Text;
            while(!firstProduct.Equals(currentFirstProduct))
            {
                ShoppingCart.SwipeProductList2(100, 400, 100, 900);
                productItems = _instance.GetElement(SearchBy.ClassName, ObjectRepository.RecyclerView).ElementInstance.FindElements(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/ratingtheme"));
                currentFirstProduct = _instance.GetElement(SearchBy.ClassName, ObjectRepository.RecyclerView).ElementInstance.FindElements(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/text1Name")).First().Text;
                if(firstProduct.Equals(currentFirstProduct))
                {
                    Logger.Info("Stop scrolling");
                }
                else
                {
                    firstProduct = currentFirstProduct;
                    currentFirstProduct = "";
                }
            }
            List<ProductDetails> pDetails = new List<ProductDetails>();
            int indx = 0;
            bool needsSwipe = true;
            
            int x = 100, y = 0;
            while (needsSwipe)
            {
                Thread.Sleep(2000);
                productItems = _instance.GetElement(SearchBy.ClassName, ObjectRepository.RecyclerView).ElementInstance.FindElements(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/ratingtheme"));
                if (productItems.Count == 0) break;
                y = ((AppiumWebElement)productItems.First()).Location.Y;
                foreach (AppiumWebElement item in productItems)
                {
                    ProductDetails pd = new ProductDetails();
                    try
                    {
                        Logger.Info("location: " + item.Location);
                        Logger.Info("coords: " + item.Coordinates);
                        Logger.Info("size: " + item.Size);
                        yTop = item.Location.Y;//on failure scroll to this point
                        pd.image = item.FindElement(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/image"));
                        pd.Prod_Description = item.FindElement(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/text1Name")).Text;
                        if (pDetails.Find(p => p.Prod_Description == pd.Prod_Description).Prod_Description != null)
                            continue;
                        pd.Qty = item.FindElement(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/text2value"));
                        pd.iQty = int.Parse(pd.Qty.Text);
                        pd.price = item.FindElement(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/price")).Text;
                        pd.dots = item.FindElement(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/dots"));
                        pDetails.Add(pd);
                        yTop = item.Location.Y + item.Size.Height;//success, so increase scroll point
                        Logger.Info("ProductDetails added: " + pd.Prod_Description + " : " + pd.iQty + " : " + pd.price);
                    }
                    catch
                    {
                        //todo
                        //half data scrolled out, so we dont at any point get a full view of one element - might be taken care if the scroll is done accordingly
                        Logger.Info("ProductDetails failed for: " + pd.Prod_Description);
                        break;
                    }
                }
                try
                {
                    if (_instance.GetElementNoWait(null, SearchBy.Id, "com.philips.cdp.di.iapdemo:id/iap_tv_totalcost") != null)
                    {
                        needsSwipe = false;
                        Logger.Info("Stop swiping");
                    }
                    else
                        ShoppingCart.SwipeProductList2(x, yTop, x, y);
                }
                catch (Exception e)
                {
                    ShoppingCart.SwipeProductList2(x, yTop, x, y);
                    Logger.Info("Keep swiping");
                }
            }

            String logging = "";
            foreach (ProductDetails pd in pDetails)
            {
                logging += pd.Prod_Description + " : " + pd.price + " : " + pd.iQty + "\n";
            }
            Logger.Info(logging);
            SwipeToTop();
            return pDetails;
        }


        /// <summary>
        /// Provides a construct for accessing product details in the Shopping Cart page.
        /// </summary>
        public struct ProductDetails
        {
            public IWebElement image;
            public string Prod_Description;
            public IWebElement Qty;
            public int iQty;
            public string price;
            public IWebElement dots;

        }


        public static bool VerifyCourrier()
        {
            String CourrierText = GetText(TextView.UPS_PARCEL);
            //String CourrierText =_instance.GetElement(SearchBy.Id, "com.philips.cdp.di.iapdemo:id/iap_tv_delivery_via_ups").Text;
            if (CourrierText.Equals("Delivery via FedEx") && country.Equals("US")) return true;
            else if (CourrierText.Equals("Delivery via UPS") && country.Equals("UK")) return true;
            else return false;
        }

        public static string GetTextFromCart()
        {
            return _instance.GetElement(SearchBy.Id, "com.philips.cdp.di.iapdemo:id/item_count").Text;
        }

        public static void SwipeTo(String ShortProductDesc)
        {
            
            bool flagDeliverByUPS = false;
            int yTop = 0;
            if (_instance.GetElementNoWait(null, SearchBy.Id, "com.philips.cdp.di.iapdemo:id/empty_cart") != null)
            {
                Logger.Info("Shopping cart empty");
                return;
            }
            Logger.Info("swipe to top");
            String firstProduct;
            String currentFirstProduct = "";
            System.Collections.ObjectModel.ReadOnlyCollection<IWebElement> productItems;
            firstProduct = _instance.GetElement(SearchBy.ClassName, ObjectRepository.RecyclerView).ElementInstance.FindElements(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/text1Name")).First().Text;
            while (!firstProduct.Equals(currentFirstProduct))
            {
                ShoppingCart.SwipeProductList2(100, 400, 100, 600);
                productItems = _instance.GetElement(SearchBy.ClassName, ObjectRepository.RecyclerView).ElementInstance.FindElements(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/ratingtheme"));
                currentFirstProduct = _instance.GetElement(SearchBy.ClassName, ObjectRepository.RecyclerView).ElementInstance.FindElements(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/text1Name")).First().Text;
                if (firstProduct.Equals(currentFirstProduct))
                {
                    Logger.Info("Stop scrolling");
                }
                else
                {
                    firstProduct = currentFirstProduct;
                    currentFirstProduct = "";
                }
            }
            List<Philips.SIG.Automation.Android.CDP.IAPTestPlugin.ShoppingCart.ProductDetails> pDetails = new List<Philips.SIG.Automation.Android.CDP.IAPTestPlugin.ShoppingCart.ProductDetails>();
            int indx = 0;
            bool needsSwipe = true;

            int x = 100, y = 0;
            while (needsSwipe)
            {
                productItems = _instance.GetElement(SearchBy.ClassName, ObjectRepository.RecyclerView).ElementInstance.FindElements(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/ratingtheme"));
                y = ((AppiumWebElement)productItems.First()).Location.Y;
                foreach (AppiumWebElement item in productItems)
                {
                    Philips.SIG.Automation.Android.CDP.IAPTestPlugin.ShoppingCart.ProductDetails pd = new Philips.SIG.Automation.Android.CDP.IAPTestPlugin.ShoppingCart.ProductDetails();
                    try
                    {
                        Logger.Info("location: " + item.Location);
                        Logger.Info("coords: " + item.Coordinates);
                        Logger.Info("size: " + item.Size);
                        yTop = item.Location.Y;//on failure scroll to this point
                        IWebElement wElem = item.FindElement(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/text1Name"));
                        pd.Prod_Description = wElem.Text;
                        if (pDetails.Find(p => p.Prod_Description == pd.Prod_Description).Prod_Description != null)
                            continue;
                        pd.Qty = item.FindElement(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/text2value"));
                        pd.iQty = int.Parse(pd.Qty.Text);
                        yTop = item.Location.Y + item.Size.Height;//success, so increase scroll point
                        Logger.Info("ProductDetails: " + pd.Prod_Description + " : " + pd.iQty);
                        if(pd.Prod_Description.Equals(ShortProductDesc))
                        {
                            return;
                        }
                    }
                    catch
                    {
                        //todo
                        //half data scrolled out, so we dont at any point get a full view of one element - might be taken care if the scroll is done accordingly
                        break;
                    }
                }
                try
                {
                    if (_instance.GetElementNoWait(null, SearchBy.Id, "com.philips.cdp.di.iapdemo:id/iap_tv_delivery_via_ups") != null)
                    {
                        needsSwipe = false;
                        Logger.Info("Stop swiping");
                    }
                    else
                        ShoppingCart.SwipeProductList2(x, yTop, x, y);
                }
                catch (Exception e)
                {
                    ShoppingCart.SwipeProductList2(x, yTop, x, y);
                    Logger.Info("Keep swiping");
                }
            }
            return;
        }

        public static String GetMax(string productName)
        {
            List<ProductDetails> pDetails = _getproductList();
            bool isExist = false;
            while (!isExist)
            {
                try
                {
                    foreach (ProductDetails items in pDetails)
                    {
                        if (items.Prod_Description.Equals(productName))
                        {
                            items.Qty.Click();
                            Thread.Sleep(2000);
                            isExist = true;
                            break;
                        }
                        else
                        {
                            SwipeProductList(Direction.Down, 1);
                        }
                    }


                }
                catch (Exception e)
                {
                    SwipeProductList(Direction.Up, 1);
                }
            }

            String LastQTY = "";
            String CurLast = "";
            List<IMobilePageControl> txtList;
            do
            {
                LastQTY = CurLast;
                SwipeQuantity(Direction.Up, 4);
                txtList = _instance.GetElements(SearchBy.Id, ObjectRepository.QuantitydropdownMenuItem);
                CurLast = txtList.Last().Text;
            }
            while (!CurLast.Equals(LastQTY));
            String ret = txtList.Last().Text;
            Back();
            //txtList.Last().Click();
            return ret;
        }
    }

   
}
