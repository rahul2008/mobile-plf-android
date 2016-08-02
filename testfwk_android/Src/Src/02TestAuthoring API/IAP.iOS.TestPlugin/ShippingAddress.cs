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
using System.Linq;
using System.Net;
using System.Net.Security;
using System.Text;
using System.Threading.Tasks;

namespace Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS
{
    /// <summary>
    ///  This class provides all the functionalities and constants for features related to Shipping Address.
    /// </summary>
    public class ShippingAddress
    {
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }


        /// <summary>
        /// Collection of all the buttons in the Shipping Address.
        /// </summary>
        public enum Button
        {
            Cancel,
            BackButton,
            DeliverToThisAddr,
            AddNewAddr,
            Dots
        }

        public ShippingAddress()
        {

        }

        /// <summary>
        /// Waits till Shipping Address screen appears.
        /// Returns false if the screen fails to load after loopcount exceeds 10.
        /// </summary>
        /// <returns>boolean value representing the appearance of the screen.</returns>
        public static bool WaitforShippingAddressScreen()
        {
            IMobilePageControl element = null;
            int loopcount = -0;
            while (element == null)
            {
                element = _instance.GetElement(SearchBy.Xpath, Repository.iOS.ShippingAddressScreen.CancelButton);
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
        /// Clicks the desired button in the Shipping Address screen.
        /// </summary>
        /// <param name="btn">btn represents the name of the button.</param>
        public static void Click(Button btn)
        {
            string desiredText = string.Empty;
            switch (btn)
            {
                case Button.Cancel:
                    desiredText = Repository.iOS.ShippingAddressScreen.CancelButton;
                    break;
                case Button.BackButton:
                    desiredText = Repository.iOS.ShoppingCart.Back;
                    break;
            }
            _instance.GetElement(SearchBy.Xpath, desiredText).Click();

        }

        /// <summary>
        /// Returns a string containing the screen title.
        /// </summary>
        /// <returns>string value representing the page title</returns>
        public static string GetPageTitle()
        {
            return _instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.DemoTitle).Text;
        }

        /// <summary>
        /// Returns a string containing the Title of the Page
        /// </summary>
        /// <returns>a string value</returns>
        public static string GetHeader()
        {
            return _instance.GetElement(SearchBy.Xpath, Repository.iOS.ShippingAddressScreen.ShippingAddrTitle).Text;
        }

        /// <summary>
        /// Provides a list of Shipping Addresses present in the Shipping Address screen. 
        /// </summary>
        /// <returns>list of shipping addresses.</returns>
        public static List<Shipping_Address> GetListofAddress()
        {
            List<Shipping_Address> AddressList = new List<Shipping_Address>();
            int countOfAddrInUI = _instance.GetElement(SearchBy.Xpath, Repository.iOS.ShippingAddressScreen.ShippingAddress).ElementInstance.FindElements(ByIosUIAutomation.ClassName("UIATableCell")).Count;

            IWebElement[] elements = new IWebElement[countOfAddrInUI];
            _instance.GetElement(SearchBy.Xpath, Repository.iOS.ShippingAddressScreen.ShippingAddress).ElementInstance.FindElements(ByIosUIAutomation.ClassName("UIATableCell")).CopyTo(elements, 0);

            foreach (IWebElement addressElement in elements)
            {
                Shipping_Address address = new Shipping_Address();
                address.radiobutton = addressElement.FindElements(By.ClassName("UIAButton"))[0];
                address.dots = addressElement.FindElements(By.ClassName("UIAButton"))[3];
                address.completeAddress = addressElement.FindElements(By.ClassName("UIAStaticText"))[0].Text;
                address.deliverToThisAddr = addressElement.FindElements(By.ClassName("UIAButton"))[1];
                address.addNewAddr = addressElement.FindElements(By.ClassName("UIAButton"))[2];
                AddressList.Add(address);
            }
            return AddressList;
        }

        /// <summary>
        ///Provides a construct for accessing shipping address in the Address.
        /// </summary>
        public struct Shipping_Address
        {
            public IWebElement radiobutton;
            public IWebElement dots;
            public string completeAddress;
            public IWebElement deliverToThisAddr;
            public IWebElement addNewAddr;
        }

        /// <summary>
        /// Selects the shipping address based on the First Name of the logged in user.
        /// </summary>
        /// <param name="name">name represents first name of the user.</param>
        public static void SelectAddress(string addressText)
        {
            List<Shipping_Address> AddressList = GetListofAddress();
            AddressList.Find(p => p.completeAddress == addressText).radiobutton.Click();
        }

        /// <summary>
        /// Performs a click on the given button based on the address text passed as paramter.
        /// </summary>
        /// <param name="addressText">Entire address is passed as paramter.</param>
        /// <param name="btn">btn represents the name of the button.</param>
        public static void Click(string addressText, Button btn)
        {
            List<Shipping_Address> AddressList = GetListofAddress();
            switch (btn)
            {
                case Button.DeliverToThisAddr:
                    AddressList.Find(p => p.completeAddress == addressText).deliverToThisAddr.Click();
                    break;
                case Button.AddNewAddr:
                    AddressList.Find(p => p.completeAddress == addressText).addNewAddr.Click();
                    break;
                case Button.Dots:
                    AddressList.Find(p => p.completeAddress == addressText).dots.Click();
                    break;
            }
        }
    }
}

