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

namespace Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS
{
    /// <summary>
    /// This class provides all the functionalities and constants for features related to Shopping Cart page.
    /// </summary>
    public class ShoppingCart
    {
       
        static MobilePageInstance Instance;
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        
        /// <summary>
        /// An object of the type ObjectRepository
        /// </summary>
        public  ObjectRepository objRep = new ObjectRepository();
		 
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }

        /// <summary>
        /// A variable to store the total cost of all the products
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
            Down,
            Left,
            Right
        }

        /// <summary>
        /// Provides collection of all the buttons in the Shopping Cart page.
        /// </summary>
        public enum Button
        {
            Checkout,
            ContinueShopping,
            UpButton,
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
            UPS_Parcel,
            Tax,
            TotalCost,
            TotalItems
        }

        /// <summary>
        /// Verifies the price of UPS Delivery Parcel when the total cost is specified.
        /// </summary>
        /// <returns>Booelan value representing the correctness of price value</returns>
        public static bool VerifyDeliveryParcelPrice()
        {
            string UPSValue = null;
            bool bRetVal = false;
            double totalcost = 0, tax = 0;
          
            int i = MobileDriver.iOSdriver.FindElements(By.ClassName("UIATableCell")).Count;
            IWebElement[] items = new IWebElement[i];
            MobileDriver.iOSdriver.FindElements(By.ClassName("UIATableCell")).CopyTo(items, 0);
            foreach (IWebElement item in items)
            {
                try
                {
                    if (item.FindElements(By.ClassName("UIAStaticText"))[0].Text.Contains("Delivery"))
                    {
                        UPSValue = GetRefinedText(item.FindElements(By.ClassName("UIAStaticText"))[1].Text);
                    }
                    else if ((item.FindElements(By.ClassName("UIAStaticText"))[0].Text.Contains("Tax")) &&
                        (item.FindElements(By.ClassName("UIAStaticText"))[1].Text.Contains("Total")))
                    {
                        tax = Convert.ToDouble(GetRefinedText(item.FindElements(By.ClassName("UIAStaticText"))[3].Text));
                        totalcost = Convert.ToDouble(GetRefinedText(item.FindElements(By.ClassName("UIAStaticText"))[2].Text));
                    }
                }
                catch (Exception) { }
            }

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
            if (tv == ProductDetailsText.Price)
                retVal = pDetails.Find(p => p.Prod_Description == productName).price;
            if (tv == ProductDetailsText.Quantity)
                retVal = pDetails.Find(p => p.Prod_Description == productName).quantity;
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
            string UPSValue = string.Empty, t = string.Empty;
            double totalcost = 0, tax = 0;
          
            int i = MobileDriver.iOSdriver.FindElements(By.ClassName("UIATableCell")).Count;
            IWebElement[] items = new IWebElement[i];
            MobileDriver.iOSdriver.FindElements(By.ClassName("UIATableCell")).CopyTo(items, 0);
            foreach (IWebElement item in items)
            {
                try
                {
                    if (item.FindElements(By.ClassName("UIAStaticText"))[0].Text.Contains("Delivery"))
                    {
                        UPSValue = GetRefinedText(item.FindElements(By.ClassName("UIAStaticText"))[1].Text);
                    }
                    else if ((item.FindElements(By.ClassName("UIAStaticText"))[0].Text.Contains("Tax")) &&
                        (item.FindElements(By.ClassName("UIAStaticText"))[1].Text.Contains("Total")))
                    {
                        tax = Convert.ToDouble(GetRefinedText(item.FindElements(By.ClassName("UIAStaticText"))[3].Text));
                        totalcost = Convert.ToDouble(GetRefinedText(item.FindElements(By.ClassName("UIAStaticText"))[2].Text));
                        t = item.FindElements(By.ClassName("UIAStaticText"))[1].Text.Split('(')[1].Split(' ')[0];
                    }
                }
                catch (Exception) { }
            }
            switch (tv)
            {
                case TextView.Tax:
                    retVal = tax.ToString();
                    break;
                case TextView.TotalCost:
                    retVal = totalcost.ToString();
                    break;
                case TextView.UPS_Parcel:
                    retVal = UPSValue.ToString();
                    break;
                case TextView.TotalItems:
                    retVal = t;
                    break;
            }
            return retVal;
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
                Instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.Back).Click();
            else if (btn == Button.Checkout)
                Instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.Checkout).Click();
            else if (btn == Button.ContinueShopping)
                Instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.ContinueShopping).Click();
            else if (btn == Button.ContinueShopping_1)
                Instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.Checkout).Click();
            else
                Console.WriteLine("Not yet Implemented");
        }

        /// <summary>
        /// This method clicks on the cart item, whose name has been passed as a parameter.
        /// </summary>
        /// <param name="productName">productName repreents the product name, as displayed in the app.</param>
        public static void ClickCartItem(String productName)
        {
            Instance = _instance;
            try
            {
                _getproductList().Find(p => p.Prod_Description == productName).clickableProperty.Click();
            }
            catch (Exception)
            {
                throw;
            }
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
                isEnabled = _instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.Checkout).Enabled;
            else if (btn == Button.ContinueShopping)
                isEnabled = _instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.ContinueShopping).Enabled;
            else if (btn == Button.UpButton)
                isEnabled = _instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.Back).Enabled;
            else if (btn == Button.ContinueShopping_1)
                isEnabled = _instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.Checkout).Enabled;

            return isEnabled;
        }

         /// <summary>
        /// Returns a string containing the title of the screen.
        /// </summary>
        /// <returns>string value containing the title</returns>
        public static string GetScreenTitle()
        {
            return _instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.DemoTitle).Text;
        }

          /// <summary>
        /// Returns true if the Screen Title "Shopping Cart" is visible.
        /// </summary>
        /// <returns>boolean value representing the screen visibility.</returns>
        public static bool IsVisible()
        {
            bool bVisible = false;
            if (_instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.DemoTitle).Text == "Shopping Cart")
                bVisible = true;

            return bVisible;

        }

         /// <summary>
        /// To select the quantity of a given product from dropdown in Shopping Cart.
        /// </summary>
        /// <param name="qty">qty represents the number to be selected in the dropdown.</param>
        public static void Select(string productName, int qty, bool swiped = false)
        {
            List<ProductDetails> pDetails;
            if (swiped == false)
            {
                pDetails = _getproductList();
                pDetails.Find(p => p.Prod_Description == productName).qtyDropdown.Click();
            }
            IWebElement cartTable = MobileDriver.iOSdriver.FindElement(By.ClassName("UIAPopover")).FindElement(By.ClassName("UIATableView"));
            int i = cartTable.FindElements(By.ClassName("UIATableCell")).Count;
            IWebElement[] items = new IWebElement[i];
            cartTable.FindElements(By.ClassName("UIATableCell")).CopyTo(items, 0);
            foreach (IWebElement item in items)
            {
                try
                {
                    string qtyVal = item.FindElements(By.ClassName("UIAStaticText"))[0].Text;

                    if (int.Parse(qtyVal) == qty)
                    {
                        item.FindElements(By.ClassName("UIAStaticText"))[0].Click();
                        break;
                    }
                }
                catch (Exception)
                {

                }
            }
    }

         /// <summary>
        /// Waits for the Shopping Cart screen
        /// Returns false if the Shopping Cart screen doesn't appear after loopcount exceeds 10.
        /// </summary>
        /// <returns>boolean value representing the appearance of the screen.</returns>
        public static bool WaitforShoppingCartScreen()
        {
            IMobilePageControl element = null;
            int loopcount = -0;
            while (element == null)
            {
                element = _instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.Checkout);
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
       /// Returns true if the desired product exists.
       /// </summary>
       /// <param name="productName">productName represents the name of the product.</param>
       /// <returns>boolean value representing the existence of the product.</returns>
        public static bool IsProductExist(string productName)
        {
            bool isExist = false;
            List<String> items = GetProductNames();
            if (items.Exists(d => d.Equals(productName)))
            {
                isExist = true;
            }
            else
            {
                isExist = false;
            }
            return isExist;
        }

         /// <summary>
        /// Returns a list containing all the individual prices of the products present in the Shopping Cart screen.
        /// </summary>
        /// <returns>a list of strings representing the product prices.</returns>
        public static List<string> GetAllProductPrice()
        {
            List<ProductDetails> items = ShoppingCart._getproductList();

            List<String> PriceList = new List<string>();
            

            foreach (ProductDetails item in items)
            {
                if (item.price.Contains('$'))
                    PriceList.Add(item.price.Split('$')[1]);
                if (item.price.Contains("USD"))
                    PriceList.Add(item.price.Split(' ')[1]);
                if (item.price.Contains("£"))
                    PriceList.Add(item.price.Split('£')[1]);
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
            List<string> ProductNames = new List<string>();
            foreach (ProductDetails item in items)
                ProductNames.Add(item.Prod_Description);
            return ProductNames;

        }

      
         /// <summary>
        /// Returns a list containing ProductDetails.
        /// </summary>
        /// <returns>list of ProductDetails from the Shopping Cart screen.</returns>
        public static List<ProductDetails> _getproductList()
        {
            List<ProductDetails> pDetails = new List<ProductDetails>();
            int i = 0;
            try
            {
                i = MobileDriver.iOSdriver.FindElements(By.ClassName("UIATableCell")).Count;

                if (i > 0)
                {
                    try
                    {
                        IWebElement[] items = new IWebElement[i];
                        MobileDriver.iOSdriver.FindElements(By.ClassName("UIATableCell")).CopyTo(items, 0);
                        foreach (IWebElement item in items)
                        {
                            ProductDetails pd = new ProductDetails();
                            pd.Prod_Description = item.FindElements(By.ClassName("UIAStaticText"))[0].Text;
                            pd.qtyDropdown = item.FindElements(By.ClassName("UIAButton"))[0];
                            pd.quantity = item.FindElements(By.ClassName("UIAStaticText"))[1].Text.Trim().Split(':')[1];
                            pd.price = item.FindElements(By.ClassName("UIAStaticText"))[2].Text;
                            pd.clickableProperty = item.FindElements(By.ClassName("UIAStaticText"))[0];
                            pDetails.Add(pd);
                        }
                    }
                    catch (Exception)
                    {
                        return pDetails;
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine("Shopping Cart is empty. No element found");
            }

            return pDetails;
        }

        /// <summary>
        /// Provides a construct for accessing product details in the Shopping Cart page.
        /// </summary>
        public struct ProductDetails
        {
            public string Prod_Description;
            public IWebElement qtyDropdown;
            public string price;
            public IWebElement clickableProperty;
            public string quantity;
        }
        
        /// <summary>
        /// Swipes through the Shopping Cart page when scrollable is enabled.
        /// </summary>
        /// <param name="dirn">dirn represents the direction in which the dropdown should be swiped.</param>
        /// <param name="indx">indx represents the amount by which swipe should happen.</param>
        public static void SwipeShoppingCartList(Direction dirn, int indx)
        {
            int[] sz = _instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.ProductList).Size;
            int[] pt = _instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.ProductList).Coordinates;

            if (dirn == Direction.Up)
            {
                int x1 = pt[0] + sz[1] / 2;
                int y1 = pt[1] + sz[0] / 2;
                int x2 = x1;
                int y2 = y1 - 50 * indx;
                if (y2 >= pt[1])
                    MobileDriver.swipeForiOS(x1, y1, x2, y2);

            }
            else if (dirn == Direction.Down)
            {
                int x1 = pt[0] + sz[1] / 2;
                int y1 = pt[1] + sz[0] / 2;
                int x2 = x1;
                int y2 = y1 + 50 * indx;

                if (y2 <= (pt[1] + sz[0]))
                    MobileDriver.Swipe(x1, y1, x2, y2);
            }
        }

        /// <summary>
        /// This method can be used to swipe in left and right direction (for delete option)
        /// </summary>
        /// <param name="productName">productName is the product description</param>
        /// <param name="dirn">it refers to swipe direction</param>
        /// <param name="indx">arbitrary value for swipe</param>
        public static void SwipeShoppingCartItem(string productName, Direction dirn,int indx)
        {
            List<ProductDetails> pDetails = _getproductList();
            string retVal = string.Empty;
            Size size = pDetails.Find(p => p.Prod_Description == productName).clickableProperty.Size;
            Point coordinates = pDetails.Find(p => p.Prod_Description == productName).clickableProperty.Location;

            if (dirn == Direction.Left)
            {
                int x1 = coordinates.X + (3*size.Width/4);
                int y1 = coordinates.Y + size.Height/2;
                int x2 = x1 - 50 * indx;
                int y2 = y1;
                if (x1 >= x2)
                    MobileDriver.swipeForiOS(x1,y1,x2,y2);
            }
            else if (dirn == Direction.Right)
            {
                int x1 = coordinates.X + (size.Width / 4);
                int y1 = coordinates.Y + size.Height / 2;
                int x2 = x1 + 50 * indx;
                int y2 = y1;
                if (x2 >= x1)
                    MobileDriver.swipeForiOS(x1, y1, x2, y2);
            }
        }
    }

    /// <summary>
    /// This class provides all the functionalities and constants for features related to Shopping Cart Item.
    /// </summary>
    public class ShoppingCartItem
    {
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }

         /// <summary>
        /// Provides collection of all the buttons in the Shopping Cart page.
        /// </summary>
        public enum Button
        {
            UpButton,
            AddToCart,
            BuyFromRetailer,
            CartImage,
            Delete,
            Ok,
            Cancel,
            DeleteCartItem
        }

         /// <summary>
        /// Provides colletion of constant values representing swipe direction in the Shopping Cart page.
        /// </summary>
        public enum Direction
        {
            Up,
            Down,
            Left,
            Right
        }

         /// <summary>
        /// Provides collection of all textviews in the Shopping Cart page.
        /// </summary>
        public enum TextView
        {
            ShoppingCartItemTitle,
            ProductDescription,
            CTN,
            OriginalPrice,
            NewPrice,
            ProductOverview
        }

        /// <summary>
        /// Clicks the desired button in the Shopping Cart page.
        /// </summary>
        /// <param name="btn">btn represents the name of the button to be clicked.</param>
        public static void Click(Button btn)
        {
            if (btn == Button.UpButton)
                _instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.Back).Click();
            else if (btn == Button.AddToCart)
                _instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.AddToCart).Click();
            else if (btn == Button.BuyFromRetailer)
                _instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.BuyFromRetailer).Click();
            else if (btn == Button.CartImage)
                _instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.ImageCart).Click();
            else if (btn == Button.Delete)
                _instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.Delete).Click();
            else if (btn == Button.Ok)
                _instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.DeleteOkButton).Click();
            else if (btn == Button.Cancel)
                _instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.DeleteCancelButton).Click();
            else if(btn == Button.DeleteCartItem)
                _instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.DeleteCartItem).Click();

        }

         /// <summary>
        /// Waits for the Shopping Cart Info screen to appear
        /// Returns false if the Shopping Cart Info screen fails to appear after loopcount exceeds 5
        /// </summary>
        /// <returns>boolean value</returns>
        public static bool WaitforShoppingCartInfoScreen()
        {
            IMobilePageControl element = null;
            int loopcount = -0;
            while (element == null)
            {
                try
                {
                    element = _instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.Delete);
                }
                catch (Exception)
                {
                    element = _instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.AddToCart);
                }
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
        /// Returns true if the Screen title is 'Shopping Cart Item'.
        /// </summary>
        /// <returns>boolean value representing the visibility of Shopping Cart Item</returns>
        public static bool IsVisible()
        {
            bool bVisible = false;

            if (_instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.ProductDescription).Enabled)
                bVisible = true;
            return bVisible;
        }

        /// <summary>
        /// Returns string contaning text from the desired TextView.
        /// </summary>
        /// <param name="tv">tv represent the name of TextView.</param>
        /// <returns>string value containing the text value of the desired TextView.</returns>
        public static string GetText(TextView tv)
        {
            string desiredText = string.Empty;
            switch (tv)
            {
                case TextView.CTN:
                    desiredText = Repository.iOS.ShoppingCart.CTN;
                    break;
                case TextView.ProductDescription:
                    desiredText = Repository.iOS.ShoppingCart.ProductDescription;
                    break;
                case TextView.ProductOverview:
                    desiredText = Repository.iOS.ShoppingCart.ProductOverview;
                    break;
                case TextView.OriginalPrice:
                    desiredText = Repository.iOS.ShoppingCart.OriginalPrice;
                    break;
                case TextView.NewPrice:
                    desiredText = Repository.iOS.ShoppingCart.NewPrice;
                    break;
                case TextView.ShoppingCartItemTitle:
                    desiredText = Repository.iOS.HomeScreen.DemoTitle;
                    break;
            }
            return _instance.GetElement(SearchBy.Xpath, desiredText).Text;
        }
    }
}
