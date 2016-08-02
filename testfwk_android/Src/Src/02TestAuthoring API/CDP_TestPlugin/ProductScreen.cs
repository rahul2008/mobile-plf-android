using OpenQA.Selenium;
using OpenQA.Selenium.Appium;
using OpenQA.Selenium.Appium.Android;
using OpenQA.Selenium.Remote;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Drawing;
using System.Linq;
using System.Net;
using System.Net.Security;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin
{
    /// <summary>
    /// This class provides all the functionalities and constants for features related to Product Screen page.
    /// </summary>
    public  class ProductScreen: IAP_Common
    { 
        /// <summary>
        /// Represents the object of DesiredCapabilities class.
        /// </summary>
        //public DesiredCapabilities cap = new DesiredCapabilities();

        /// <summary>
        /// Provides a construct for accessing product details in the Product Screen.
        /// </summary>
   public struct Product
        {
            public string ProdName;
            public AndroidElement info;
           
        }

   /// <summary>
   /// Provides colletion of constant values representing swipe direction in the Product Screen page.
   /// </summary>
   public enum Direction
   {
       Up,
       Down,
       Left,
       Right
   }
   /// <summary>
   /// Collection of all Buttons
   /// </summary>
        public enum Button
        {
            productScreen_BackButton,
            productScreen_ImageCart,
            productScreen_InfoButton,
            productScreen_AddToCart
        }
        /// <summary>
        /// Collection of all TextViews
        /// </summary>
        public enum TextView
        {
            productScreen_ProductId,
            productScreen_title
        }
          /// <summary>
        /// Clicks the desired button
        /// </summary>
        /// <param name="btn">btn represents one of the buttons present on the screen</param>
        public static void Click(Button btn)
        {
            Instance = _instance;

            if (btn == Button.productScreen_BackButton)
                Instance.ClickById(ObjectRepository.productScreen_BackButton);
            else if (btn == Button.productScreen_ImageCart)
                Instance.ClickById(ObjectRepository.productScreen_ImageCart);
            else if (btn == Button.productScreen_InfoButton)
                Instance.ClickById(ObjectRepository.productScreen_InfoButton);
            else if (btn == Button.productScreen_AddToCart) AddToCart();
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
            if (btn == Button.productScreen_BackButton)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.productScreen_BackButton).Enabled;
            if (btn == Button.productScreen_ImageCart)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.productScreen_ImageCart).Enabled;
            if (btn == Button.productScreen_InfoButton)
                isEnabled = Instance.GetElement(SearchBy.Id, ObjectRepository.productScreen_InfoButton).Enabled;
            return isEnabled;
        }
        /// <summary>
        /// Returns a list of IMobilePageControls, containing all desired buttons and Textviews
        /// </summary>
        /// <param name="btn">btn reprents the name of the button </param>
         /// <param name="tv">btn reprents the name of the button </param>
        /// <returns>a list of IMobilePageControls</returns>
        private static List<Product> _getButtonSet()
        {
            List<Product> productList = new List<Product>();

            IWebElement Elem = _instance.GetElement(SearchBy.Id, ObjectRepository.productScreen_ProductList).ElementInstance;
            int i = Elem.FindElements(ByAndroidUIAutomator.ClassName("android.widget.RelativeLayout")).Count;
            AndroidElement[] items = new AndroidElement[i];
            Elem.FindElements(ByAndroidUIAutomator.ClassName("android.widget.RelativeLayout")).CopyTo(items, 0);
            foreach (AndroidElement item in items)
            {
                AndroidElement productScreen_InfoButton = null;

                try
                {
                    productScreen_InfoButton = item.FindElement(ByAndroidUIAutomator.Id(ObjectRepository.productScreen_InfoButton)) as AndroidElement;
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.InnerException);
                }
                if (productScreen_InfoButton != null)
                {
                    Product p = new Product();
                    p.ProdName = item.FindElement(ByAndroidUIAutomator.Id("com.philips.cdp.di.iapdemo:id/tv_ctn")).Text;
                    p.info = productScreen_InfoButton;

                    productList.Add(p);
                }
                else
                    continue;
            }
            return productList;
        }
        /// <summary>
        /// Slides the Product image in left or right direction.
        /// </summary>
        /// <param name="direction">direction represents the direction </param>
        public static void Swipe(Direction direction, int value)
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.Id, ObjectRepository.productScreen_ProductList);
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

        public static Dictionary<int,string> listofProducts()
        {
            ServicePointManager.ServerCertificateValidationCallback = new RemoteCertificateValidationCallback
              (
                  delegate { return true; }
              );
            SearchCategories list = RestApiInvoker.GetSearchCategories("","US");
            int a=list.products.Count();
            Dictionary<int, string> dictionary = new Dictionary<int, string>();
            for (int i = 0; i < a;i++ )
            {
                string value=list.products[i].stock.stockLevelStatus;
                string code = list.products[i].code;
                if(value.Equals("inStock"))
                {
                    
                    dictionary.Add(i,code);
                   
                }
                
            }
                return dictionary;
        }
      
        /// <summary>
        /// Waits for the ProductScreen page to appear
        /// Returns false if the Product Screen doesn't appear after loopcount exceeds 10
        /// </summary>
        /// <returns>a boolean value</returns>
        public static bool WaitforProductScreen()
        {
            int i; int retry;
            for (retry = 0; retry < 3; retry++)
            {
                for (i = 0; i < 60; i++)
                {
                    if (GetScreenTitle().Equals("Products"))
                    {
                        Logger.Info("Seeing: Products title:  try: " + retry);
                        break;
                    }
                    Thread.Sleep(500);
                }
                if (i == 60)
                    Logger.Info("Product screen title is not shown:  try: " + retry);
                for (; i < 60; i++)
                {
                    if (_instance.GetElementNoWait(null, SearchBy.Id, ObjectRepository.ProductDetailsArrow) != null)
                    {
                        int count = _getProductListed().Count;
                        if (count == 3)
                        {
                            Logger.Info("Products are loaded: count:" + count + " try: " + retry);
                            return true;
                        }
                        else
                            Logger.Info("Products are loaded: count mismatch:" + count + " try: " + retry);
                    }
                }
                Logger.Info("Product items are not loaded:  try: " + retry);
                Click(Button.productScreen_BackButton);
                String title = GetScreenTitle();
                switch (title)
                {
                    case "E-Commerce Demo App":
                        HomeScreen.Click(HomeScreen.Button.Shop_Now);
                        break;
                    default:
                        Logger.Info("ProductScreen retry: this is not implemented: " + title);
                        break;
                }

            }
            Logger.Info("ProductScreen retry: failed to load products after 3 tries");
            return false;
        }

        /*
        public static bool WaitforProductScreen()
        {
            int i;
            for (i = 0; i < 40; i++)
            {
                if (GetScreenTitle().Equals("Products"))
                {
                    Logger.Info("Seeing: Products title");
                    break;
                }
                Thread.Sleep(500);
            }
            if (i == 40)
                Logger.Info("Product screen title is not shown");
            for (; i < 40; i++)
            {
                if (_instance.GetElementNoWait(null, SearchBy.Id, ObjectRepository.ProductDetailsArrow) != null)
                {
                    Logger.Info("Products are loaded");
                    return true;
                }
            }
            Logger.Info("Product items are not loaded");
            return false;
        }
        */
        /// <summary>
        /// Checks if the Screen Title is "Products"
        /// </summary>
        /// <returns>a boolean value</returns>
        public static bool IsVisible()
        {
            bool bVisible = false;
            if (GetScreenTitle() == "Products")
                bVisible = true;
            return bVisible;

        }
        /// <summary>
        /// Clicks the desired button of the corresponding Product name
        /// </summary>
        /// <param name="productname">productname must contain a string containing the name of the product</param>
        /// <param name="btn">btn represents the name of the button to be clicked</param>
        public static void Click(string productname, Button btn)
        {

            List<AndroidElement> ProdList = _getProductListed();
            List<Product> prod = _getButtonSet();
            String plist = "\n";
            foreach (var p in prod)
            {
                plist += p.ProdName + "\n";
            }
            Logger.Info(plist);
            prod.Find(item => item.ProdName == productname).info.Click();
            Busy();
        }
        
        /// <summary>
        /// Returns a list of all the products present in the Product screen.
        /// </summary>
        /// <returns>a list of strings representing the product names.</returns>
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
        /// Returns a list of all the products present in the Product Screen.
        /// </summary>
        /// <returns>a list of strings representing the product id's</returns>
        private static List<AndroidElement> _getProductListed()
        {
            //IMobilePageControl  element = _instance.GetElement(SearchBy.Id,ObjectRepository.ProductList);
            List<AndroidElement> elementList = new List<AndroidElement>();
            System.Collections.ObjectModel.ReadOnlyCollection<IWebElement> welementList = _instance.GetElement(SearchBy.Id, ObjectRepository.productScreen_ProductList).ElementInstance.FindElements(By.Id(ObjectRepository.productScreen_ProductId));
            int indx = welementList.Count();

            AndroidElement[] items = new AndroidElement[indx];

            welementList.CopyTo(items, 0);

           //element.ElementInstance.FindElements(By.Id(ObjectRepository.TextView)).CopyTo()
            foreach (AndroidElement elem in items)
            {
                //string s = elem.Text;
                elementList.Add(elem);
            }
            return elementList;
        }
        /// <summary>
        /// It returns bool value if button exist or not
        /// </summary>
        /// <param name="btn">check desired button is exist or not</param>
        /// <returns>if exist return true else false</returns>
        public static bool IsExist(Button btn)
        {
            bool bExist = false;
            string desiredText = string.Empty;
            switch (btn)
            {
                case Button.productScreen_BackButton:
                    desiredText = ObjectRepository.productScreen_BackButton;
                    break;
                case Button.productScreen_ImageCart:
                    desiredText = ObjectRepository.productScreen_ImageCart;
                    break;
                case Button.productScreen_InfoButton:
                    desiredText = ObjectRepository.productScreen_InfoButton;
                    break;
              
            }
            if (_instance.GetElement(SearchBy.Id, desiredText) != null)
                bExist = true;
            else
                bExist = false;
            return bExist;
        }

        /// <summary>
        /// Finds and selects a product based on the product id
        /// </summary>
        /// <returns>nothing</returns>
        public static void SelectProduct(String productID)
        {
            Logger.Info("SelectProduct: start");
            List<AndroidElement> items;
            Boolean eos = false;
            Boolean found = false;
            String previousLast="";
            Thread.Sleep(3000);
            while(!eos && !found)
            {
                items = _getProductListed();
                Logger.Info("SelectProduct: "+items.Last());
                Logger.Info("previousLast: " + previousLast);
                if(previousLast.Equals(items.Last().Text))
                {
                    Logger.Info("SelectProduct: reached eos");
                    break;
                }
                Logger.Info("SelectProduct: in list: "+items);
                foreach (AndroidElement item in items)
                {
                    if(item.Text.Equals(productID))
                    {
                        Logger.Info("SelectProduct: "+productID+" found, selecting");
                        item.Click();
                        found = true;
                        break;
                    }
                }
                if(!found && !eos)
                {
                    Swipe(Direction.Up, 5);
                }
            }
            Logger.Info("SelectProduct: end");
            
        }

        private static void AddToCart()
        {
            bool isExist = false;
            int count = 0;
            while (!isExist && count < 10)
            {
                count++;
                try
                {
                    isExist = ShoppingCartItem.IsExist(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                    if (isExist == false)
                        ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                }
                catch (Exception)
                {

                    ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                }
            }

            // List<string> productid = HomeScreen.GetProductListItems();
            // HomeScreen.Click(productid[2], HomeScreen.Button.BuyNow);
            //IapReport.Message("User clicked BuyNow button");
            ShoppingCartItem.Click(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
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
                retVal = _instance.GetTextById("com.philips.cdp.di.iapdemo:id/item_count");
            }
            catch (Exception e)
            {
                retVal = "";
            }
            return retVal;
        }
    }
}
