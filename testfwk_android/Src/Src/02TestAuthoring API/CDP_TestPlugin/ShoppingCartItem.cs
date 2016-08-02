using OpenQA.Selenium.Appium.Android;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.MRAutomation.Foundation.Common.ImageCompareLibrary;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin
{
    /// <summary>
    /// This class provides all the functionalities and constants for features related to Shopping Cart Item.
    /// </summary>
    public class ShoppingCartItem:IAP_Common
    {
        
        /// <summary>
        /// Provides collection of all the buttons in the Shopping Cart page.
        /// </summary>
        public enum Button
        {
            UpButton,
            Delete,
            ShoppingCartItem_AddtoCart,
            BuyFromRetailer
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
            Individual_Price,
            ProductOverview,
            DiscountPrice
        }

        /// <summary>
        /// Clicks the desired button in the Shopping Cart page.
        /// </summary>
        /// <param name="btn">btn represents the name of the button to be clicked.</param>
        public static void Click(Button btn)
        {
            if (btn == Button.UpButton)
                _instance.ClickById(ObjectRepository.UpButton);
            else if (btn == Button.ShoppingCartItem_AddtoCart)
                _instance.ClickById(ObjectRepository.ShoppingCartItem_AddtoCart);
            else if (btn == Button.BuyFromRetailer)
                _instance.ClickById(ObjectRepository.BuyFromRetailer);
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
                element = _instance.GetElement(SearchBy.Id, ObjectRepository.ScreenTitle);
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
        public static bool IsVisible(string productName)
        {
            bool bVisible = false;

            if (_instance.GetTextById(ObjectRepository.CTN) == productName)
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
                    desiredText = ObjectRepository.CTN;
                    break;
                case TextView.ProductDescription:
                    desiredText = ObjectRepository.ProductDescription;
                    break;
                case TextView.ProductOverview:
                    desiredText = ObjectRepository.ProductOverview;
                    break;
                case TextView.Individual_Price:
                    desiredText = ObjectRepository.IndividualPrice;
                    break;
                case TextView.DiscountPrice:
                    desiredText = ObjectRepository.DiscountPrice;
                    break;
                case TextView.ShoppingCartItemTitle:
                    desiredText = ObjectRepository.ScreenTitle;
                    break;
            }
            return _instance.GetElement(SearchBy.Id, desiredText).Text;
        }

        /// <summary>
        /// Takes screenshot of the page, crops the product image from the Screenshot and returns its Bitmap.
        /// </summary>
        /// <returns>a Bitmap object representing the product image.</returns>
        public static Bitmap GetProductImage()
        {
            AndroidElement elem = _instance.GetElement(SearchBy.Id, ObjectRepository.ViewPager) as AndroidElement;
            MobileDriver.TakeScreenshot("ShoppingCartItemImage.bmp");
            Bitmap bmp = ImageResizer.CropImage(new Bitmap("ShoppingCartItemImage.bmp"), elem.Location.X, elem.Location.Y, elem.Size.Width, elem.Size.Height);
            return bmp;
        }

        /// <summary>
        /// Returns true if the Slideshow Navigation Button at the desired index is selected.
        /// </summary>
        /// <param name="indx">indx represents the index of item.</param>
        /// <returns>boolean value representing the selection of the item which has been passed as a parameter.</returns>
        public static bool IsSelected(int indx)
        {
            List<IMobilePageControl> controls = _instance.GetElements(SearchBy.ClassName, ObjectRepository.View);
            return controls[indx].Selected;
        }

        /// <summary>
        /// Returns the index of the Slideshow button which is currently selected.
        /// </summary>
        /// <returns>integer value representing the index of the selected image.</returns>
        public static int GetSelectedImageIndex()
        {
            List<IMobilePageControl> controls = _instance.GetElements(SearchBy.ClassName, ObjectRepository.View);
            int indx = 0;
            for (int i = 0; i < controls.Count; i++)
            {
                if (controls[i].Selected)
                {
                    indx = i;
                    break;
                }
            }
            return indx;
        }

        /// <summary>
        /// Returns the number of images in the slide show.
        /// </summary>
        /// <returns>Integer value representing the count of images.</returns>
        public static int GetCountofImages()
        {
            return _instance.GetElements(SearchBy.ClassName, ObjectRepository.View).Count;
        }

        /// <summary>
        /// Slides the Product image in left or right direction.
        /// </summary>
        /// <param name="direction">direction represents the direction </param>
        public static void Swipe(Direction direction, int value)
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.Id, ObjectRepository.ShoppingCartItemScroll);
            if (null == control) return;
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
                case Button.ShoppingCartItem_AddtoCart:
                    desiredText = ObjectRepository.ShoppingCartItem_AddtoCart;
                    break;
                case Button.BuyFromRetailer:
                    desiredText = ObjectRepository.BuyFromRetailer;
                    break;

            }
            if (_instance.GetElement(SearchBy.Id, desiredText) != null)
                bExist = true;
            else
                bExist = false;
            return bExist;
        }

        public static String getPrice()
        {
            return _instance.GetElement(SearchBy.Id, "com.philips.cdp.di.iapdemo:id/tv_discounted_price").Text;
        }
    }
}


