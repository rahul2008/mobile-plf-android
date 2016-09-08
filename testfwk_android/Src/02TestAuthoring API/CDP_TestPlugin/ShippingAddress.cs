using OpenQA.Selenium;
using OpenQA.Selenium.Appium;
using OpenQA.Selenium.Appium.Android;
using OpenQA.Selenium.Appium.MultiTouch;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Collections.Generic;
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
    ///  This class provides all the functionalities and constants for features related to Shipping Address.
    /// </summary>
    public class ShippingAddress: IAP_Common
    {


        
        /// <summary>
        /// Collection of all the buttons in the Shipping Address.
        /// </summary>
        public  enum Button
        {
            arrow,
            Deliver_to_This_Address,
            Add_a_new_Address,
            Cancel,
            Continue
        }

        /// <summary>
        /// Collection of all textboxes in the Shipping Address.
        /// </summary>
        public  enum TextBox
        {
            Address
        }
        /// <summary>
        /// Collection of constant values Representing Swipe Direction
        /// </summary>
        public enum Direction
        {
            Up,
            Down
        }
        public ShippingAddress()
        {
            
        }
        public enum TextView
        {
            Edit,
            Delete
        }

        /// <summary>
        /// Selects a field in the menu item
        /// </summary>
        /// <param name="tv">tv represents the textview of dots</param>
     
        public static void Select(TextView tv)
        {
            
               
                if (tv == TextView.Delete)
                {
                    _instance.ClickByText("Delete");
                }
                if (tv == TextView.Edit)
                {
                    _instance.ClickByText("Edit");
                }

        }

        public static void deleteAllAddress(string store)
        {
           
            List<Shipping_Address> address = GetListofAddress(store);
          
            int addressCount = address.Count;
            List<Shipping_Address> addressList = new List<Shipping_Address>(addressCount);
           for(int i=0;i<addressCount-1;i++)
           {
               address[0].dots.Click();
                ShippingAddress.Select(ShippingAddress.TextView.Delete);
               
            }
           
        }

        public static void DeleteAllNonDefaultAddresses()
        {
            //keeps deleting the second address till only one is left
            while(true)
            {
                Thread.Sleep(3000);
                List<IMobilePageControl> dots = _instance.GetElements(SearchBy.Id, ObjectRepository.Address_ContextMenu_Dots);
                if (dots.Count < 2) break;
                dots[1].Click();
                Thread.Sleep(1000);
                _instance.ClickByText("Delete");
            }
        }
        /// <summary>
        /// Waits till Shipping Address screen appears.
        /// Returns false if the screen fails to load after loopcount exceeds 10.
        /// </summary>
        /// <returns>boolean value representing the appearance of the screen.</returns>
        public static String WaitforShippingAddressScreen()
        {
            String element = "";
            int loopcount = 0;
            for (loopcount = 0; !element.Equals("Address") && loopcount < 40; loopcount++ )
            {
                Thread.Sleep(500);
                element = GetScreenTitle();
            }
            if (loopcount == 40)
                Logger.Info("Failed: Address screen not loaded");
            else
                Logger.Info("Pass: Address screen title seen");

            for (; loopcount < 40 ; loopcount++)
            {
                Thread.Sleep(500);
                //GetPageTitle();
                IMobilePageControl salute = _instance.GetElementNoWait(null, SearchBy.Id, "com.philips.cdp.di.iapdemo:id/et_salutation");
                if(null!=salute)
                {
                    Logger.Info("First time user address entry: "+salute.Text);
                    return salute.Text;
                }
                IMobilePageControl addNewAddr = _instance.GetElementNoWait(null, SearchBy.Id, "com.philips.cdp.di.iapdemo:id/btn_new_address");
                if(null!=addNewAddr)
                {
                    Logger.Info("Address exists: found "+addNewAddr.Text);
                    return addNewAddr.Text;
                }
            }
            Logger.Info("Not in Select address OR New address screen");
            return "";
        }

        /// <summary>
        /// Clicks the desired button in the Shipping Address screen.
        /// </summary>
        /// <param name="btn">btn represents the name of the button.</param>
        public static void Click(Button btn)
        {
            string desiredText = string.Empty;
            switch (btn)
            {
                case Button.arrow:
                    desiredText = ObjectRepository.ArrowButton;
                    break;
                case Button.Add_a_new_Address:
                    desiredText = ObjectRepository.NewAddressButton;
                    break;
                case Button.Deliver_to_This_Address:
                    desiredText = ObjectRepository.DeliverButton;
                    break;
                case Button.Cancel:
                    desiredText = ObjectRepository.CancelButton;
                    break;
                case Button.Continue:
                    desiredText = ObjectRepository.ContinueButton;
                    break;

            }
            //all buttons except NewAddressButton are at bottom
            if(btn != Button.Add_a_new_Address)
            {
                SwipeToBottom();
            }
            _instance.ClickById(desiredText);
        }

        public static void SwipeProductList2(int x1, int y1, int x2, int y2)
        {
            MobileDriver.Swipe(x1, y1, x2, y2);
        }

        public static void SwipeToBottom()
        {
            bool swipe = true;
            int check = 0;
            while (swipe && check < 10)
            {
                SwipeProductList2(400, 600, 400, 200);
                if (_instance.GetElement(SearchBy.Id, ObjectRepository.ContinueButton) != null) swipe = false;
                check++;
                Thread.Sleep(1000);
            }
        }
        /// <summary>
        /// Swipes across the Screen by the desired amount
        /// </summary>
        /// <param name="direction">direction represents the Direction of Swipe</param>
        /// <param name="value">value represents the amount by which the screen needs to be swiped</param>
        public static void Swipe(Direction direction, int value)
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.Id, ObjectRepository.ShippingAddressesScroll);
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
        /// Returns a string containing the screen title.
        /// </summary>
        /// <returns>string value representing the page title</returns>
        public static string GetPageTitle()
        {
            return _instance.GetElement(SearchBy.Id, ObjectRepository.ScreenTitle).Text;
        }

        /// <summary>
        /// Provides a list of Shipping Addresses present in the Shipping Address screen. 
        /// </summary>
        /// <returns>list of shipping addresses.</returns>
        public static List<Shipping_Address> GetListofAddress(string store)
        {
            
           
            AddressList list=RestApiInvoker.GetAddresses("inapptest@mailinator.com",store);
            
            int addressCount=list.addresses.Count;
            List<Shipping_Address> AddressList = new List<Shipping_Address>(addressCount);
            bool needsSwipe = true;
            while (_instance.GetElement(SearchBy.Id, ObjectRepository.ShippingAddressesScroll).GetAttribute("scrollable") == "true")
            {
                try
                {
                    if (addressCount > 0)
                    {
                        int count = _instance.GetElement(SearchBy.Id, ObjectRepository.ShippingAddressesScroll).ElementInstance.FindElements(ByAndroidUIAutomator.ClassName("android.widget.RelativeLayout")).Count;
                        IWebElement[] elements = new IWebElement[count];
                        _instance.GetElement(SearchBy.Id, ObjectRepository.ShippingAddressesScroll).ElementInstance.FindElements(ByAndroidUIAutomator.ClassName("android.widget.RelativeLayout")).CopyTo(elements, 0);

                        foreach (IWebElement item in elements)
                        {
                            needsSwipe = true;
                            Shipping_Address address = new Shipping_Address();
                            address.name = item.FindElement(By.Id(ObjectRepository.Address_Name)).Text;
                            if (AddressList.Find(p => p.name == address.name).name != null)
                                continue;
                            address.radiobutton = item.FindElement(By.Id(ObjectRepository.RadioButton));
                            address.dots = item.FindElement(By.Id(ObjectRepository.Address_ContextMenu_Dots));
                            address.Address = item.FindElement(By.Id(ObjectRepository.Address_Address)).Text;
                            AddressList.Add(address);
                            addressCount--;
                            needsSwipe = false;
                        }

                        if (addressCount > 0 && needsSwipe && elements.Count() != 0)
                          Swipe(Direction.Up, 2);
                    }
                    
                    
                    else
                    {
                        if (AddressList == null)
                            return null;

                        for (int i = AddressList.Count; i > 0; i--)
                        {
                            if (i == 0)
                                break;
                            else
                                Swipe(Direction.Down, 1);
                        }
                        return AddressList;
                    }
                }
                    catch(ArgumentException)
                {
                    return AddressList;
                }
                catch (Exception e)
                {
                    Swipe(Direction.Up, 2);
                }
            }
            return AddressList;
        }
        /// <summary>
        ///Provides a construct for accessing shipping address in the Address.
        /// </summary>
        public  struct Shipping_Address
        {
           public IWebElement radiobutton;
           public IWebElement dots;
           public string name;
           public string Address;
        }

        /// <summary>
        /// Selects the shipping address based on the First Name of the logged in user.
        /// </summary>
        /// <param name="name">name represents first name of the user.</param>
        public static void SelectAddress(string name,string store)
        {
            List<Shipping_Address> AddressList = GetListofAddress(store);
            int addressCount = AddressList.Count;
            if (addressCount != null)
            {
                bool isExist = false;
                while (!isExist)
                {
                    try
                    {
                        addressCount--;
                        if (addressCount == 0)
                            break;
                        AddressList.Find(p => p.name == name).radiobutton.Click();
                        isExist = true;
                    }
                    catch (Exception e)
                    {
                        Swipe(Direction.Up, 1);
                    }
                }
                addressCount = AddressList.Count;
                for (int i = addressCount; i > 0; i--)
                {

                    Swipe(Direction.Down, 1);
                }
            }
           
        }

        /// <summary>
        /// Creates a list of Shipping Addresses which are present.
        /// </summary>
        public static List<Shipping_Address> GetList()
        {
            //written only for testing purpose
            List<Shipping_Address> AddressList = new List<Shipping_Address>();
            int count = _instance.GetElement(SearchBy.Id, ObjectRepository.ShippingAddressesScroll).ElementInstance.FindElements(ByAndroidUIAutomator.ClassName("android.widget.RelativeLayout")).Count;
            IWebElement[] elements = new IWebElement[count];
            bool isExist = false;
            //while (!isExist)
            {
                _instance.GetElement(SearchBy.Id, ObjectRepository.ShippingAddressesScroll).ElementInstance.FindElements(ByAndroidUIAutomator.ClassName("android.widget.RelativeLayout")).CopyTo(elements, 0);
                for (int i = 0; i < count; i++)
                {
                    Shipping_Address address = new Shipping_Address();
                    address.radiobutton = elements[i].FindElement(By.Id(ObjectRepository.RadioButton));
                    address.dots = elements[i].FindElement(By.Id(ObjectRepository.Address_ContextMenu_Dots));
                    address.name = elements[i].FindElement(By.Id(ObjectRepository.Address_Name)).Text;
                    address.Address = elements[i].FindElement(By.Id(ObjectRepository.Address_Address)).Text;
                    AddressList.Add(address);
                }
            }

            return AddressList;
        }
        
        /// <summary>
        /// This class provides all the functinoalities related to the Context Menu in Shipping Adddress screen.
        /// </summary>
        public class ContextMenu
        {
            /// <summary>
            /// Collection of all Menu Items in Shipping Adddress screen.
            /// </summary>
            public enum MenuItem
            {
              
                Delete,
                Edit
            }

            /// <summary>
            /// Selects the desired Context Menu button in Shipping Adddress screen.
            /// </summary>
            /// <param name="Name">Name represents the name of the product.</param>
            /// <param name="mI">mI represents the name of Menu Item.</param>
            public static void Select(string Name, MenuItem mI,string store)
            {
                List<Shipping_Address> pDetails = ShippingAddress.GetListofAddress(store);
                pDetails.Find(p => p.name == Name).dots.Click();
                if (mI == MenuItem.Delete)
                {
                    _instance.GetElement(SearchBy.Name, "Delete").Click();
                }
                else if (mI == MenuItem.Edit)
                {
                    _instance.GetElement(SearchBy.Name, "Edit").Click();
                }
            }
        }

        /// <summary>
        /// Returns a string containing billing address choice
        /// Same or different from shipping
        /// </summary>
        /// <returns>a string value</returns>
        public static string GetBillingAddress()
        {
            return _instance.GetElement(SearchBy.Id, "com.philips.cdp.di.iapdemo:id/billing_address_tv").Text;
        }

        public static bool DeleteAddress()
        {
            WaitforShippingAddressScreen();
            Thread.Sleep(3000);
            List<IMobilePageControl> dots = _instance.GetElements(SearchBy.Id, ObjectRepository.Address_ContextMenu_Dots);
            if (dots.Count < 2) return false;

            else
            {
                dots[1].Click();
                Thread.Sleep(2000);
                try
                {
                    _instance.ClickByText("Delete");
                    return true;
                }
                catch(Exception e)
                {
                    return false;
                }
            }
        }

        public static void DAA2()
        {
            List<Shipping_Address> address = GetListofAddress("US");

            int addressCount = address.Count;
            List<Shipping_Address> addressList = new List<Shipping_Address>(addressCount);
            for (int i = 0; i < addressCount - 1; i++)
            {
                //address[0].dots.Click();
                //ShippingAddress.Select(ShippingAddress.TextView.Delete);
                Logger.Info(i+" : "+addressList[i].Address);
            }
        }

        /// <summary>
        /// <summary>
        /// Returns true if the desired button is Enabled
        /// </summary>
        /// <param name="btn">btn represents the name of the Button</param>
        /// <returns>a boolean value</returns>
        public static bool IsEnabled(Button btn)
        {
            string desiredText = string.Empty;
            switch (btn)
            {
                case Button.Cancel:
                    desiredText = ObjectRepository.CancelButton;
                    break;
                case Button.Deliver_to_This_Address:
                    desiredText = ObjectRepository.DeliverButton;
                    break;
                case Button.Continue:
                    desiredText = ObjectRepository.ContinueButton;
                    break;
                case Button.Add_a_new_Address:
                    desiredText = ObjectRepository.NewAddressButton;
                    break;
            }
            return _instance.GetElement(SearchBy.Id, desiredText).Enabled;

        } 

    }
}

