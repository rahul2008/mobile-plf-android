using OpenQA.Selenium;
using OpenQA.Selenium.Appium;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;
using System.Threading;

namespace Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS
{
    /// <summary>
    /// This class provides all the functionalities and constants for features related to Payment Screen.
    /// </summary>
    public class Payment
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        /// <summary>
        /// Collection of all EditBoxes
        /// </summary>
        public enum EditBox
        {
            CardNumber,
            CardHolderName,
            SecurityCode
        }
        /// <summary>
        /// Collection of all Buttons
        /// </summary>
        public enum Button
        {
           
            MakePayment,
            CancelPayment,
        }
        /// <summary>
        /// Provides colletion of constant values representing swipe direction in the Order Summary page.
        /// </summary>
       
        /// <summary>
        /// Collection of all DropDowns
        /// </summary>
        public enum DropDown
        {
            ExpiryMonth,
            ExpiryYear
        }

        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }

        /// <summary>
        /// Clicks the desired Button
        /// </summary>
        /// <param name="btn">btn represents the name of the Button</param>
        public static void Click(Button btn)
        {
            string desiredText = string.Empty;
            switch (btn)
            {
                case Button.MakePayment:
                    desiredText = Repository.iOS.PaymentScreen.makePaymemt;
                    break;
                case Button.CancelPayment:
                    desiredText = Repository.iOS.PaymentScreen.cancelPayment;
                    break;
            }

            _instance.GetElement(SearchBy.Xpath, desiredText).Click();

        }

        /// <summary>
        /// Enters text in the desired EditText field
        /// </summary>
        /// <param name="ed">ed represents the name of the EditText field</param>
        /// <param name="txtValue">txtValue represents the string value that the user wants to enter</param>
        public static void EnterText(EditBox ed, string txtValue)
        {
            string desiredText = string.Empty;
            

            switch (ed)
            {
                case EditBox.CardNumber:
                    desiredText = Repository.iOS.PaymentScreen.cardNumber;
                    Thread.Sleep(1000);
                    break;
                case EditBox.CardHolderName:
                    desiredText = Repository.iOS.PaymentScreen.cardholderName;
                    break;
                case EditBox.SecurityCode:
                    desiredText = Repository.iOS.PaymentScreen.securityCode;
                    break;
            }
            _instance.GetElement(SearchBy.Xpath, desiredText).Click();
            _instance.GetElement(SearchBy.Xpath, desiredText).SetText(txtValue);

        }

     
       

       /// <summary>
        /// Clicks on the DropDown and selects the specified value
        /// </summary>
        /// <param name="dropDown">dropDown represent the name of the dropdown</param>
        /// <param name="index">index represents the value which needs to be selected in the dropdown</param>
        public static void Select(DropDown dropDown, string index)
        {
            string desiredText = string.Empty;

            switch (dropDown)
            {
                case DropDown.ExpiryMonth:
                    desiredText = Repository.iOS.PaymentScreen.expiryMonth;
                    break;
                case DropDown.ExpiryYear:
                    desiredText = Repository.iOS.PaymentScreen.expiryYear;
                    break;
            }
            _instance.GetElement(SearchBy.Xpath, desiredText).Click();
            //_instance.GetElement(SearchBy.Xpath, desiredText).SetText("04");  
            //MobileDriver.iOSdriver.FindElement(By.XPath(desiredText)).SendKeys(index);
        }

       
        /// <summary>
        /// Returns a string containing Screen Title
        /// </summary>
        /// <returns>a string value</returns>
        public static string GetPageTitle()
        {
            return _instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.DemoTitle).Text;
        }

        /// <summary>
        /// Waits for the Shopping Cart Screen
        /// Returns false if the Shopping Cart Screen doesn't appear after loopcount exceeds 10
        /// </summary>
        /// <returns>a boolean value</returns>
        public static bool WaitforPaymentScreen()
        {
            IMobilePageControl element = null;
            int loopcount = -0;
            while (element == null)
            {
                element = _instance.GetElement(SearchBy.Xpath, Repository.iOS.PaymentScreen.cancelPayment);
                loopcount++;
                if (element != null)
                    break;
                if (loopcount > 10)
                    break;
            }
            Thread.Sleep(3000);
            if (element != null)
                return true;
            else
                return false;
        }

        /// <summary>
        /// Returns a string containing the Header of the page
        /// </summary>
        /// <returns>string value representing the header value</returns>
        public static string GetHeader()
        {
            string text = "";
            try
            {
                text = _instance.GetElement(SearchBy.Xpath, Repository.iOS.HomeScreen.DemoTitle).Text;
            }
            catch (Exception e)
            {
            }
            return text;
        }

       /// <summary>
       /// This method checkes if the button passed as a parameter is enabled.
       /// </summary>
       /// <param name="btn">btn represents the desired button for which the enabled property is being checked.</param>
       /// <returns></returns>
        public static bool IsEnabled(Button btn)
        {
            bool isEnabled = false;
            if (btn == Button.MakePayment)
                isEnabled = _instance.GetElement(SearchBy.Xpath, Repository.iOS.PaymentScreen.makePaymemt).Enabled;
            if (btn == Button.CancelPayment)
                isEnabled = _instance.GetElement(SearchBy.Xpath, Repository.iOS.PaymentScreen.cancelPayment).Enabled;
            return isEnabled;
        }

        /// <summary>
        /// User enters the Payment Details
        /// </summary>
        public static void EnterPaymentDetails(string cardnumber, string expirymonth, string expiryYear,string securitycode)
        {
            Thread.Sleep(5000);
            Payment.EnterText(Payment.EditBox.CardNumber, cardnumber);
            Payment.Select(Payment.DropDown.ExpiryMonth, expirymonth);
            Payment.Select(Payment.DropDown.ExpiryYear, expiryYear);
            Payment.EnterText(Payment.EditBox.SecurityCode, securitycode);
            Payment.Click(Payment.Button.MakePayment);

        }
      }

    /// <summary>
    /// This class provides all the functionalities and constants forPayment Cancellation alert. 
    /// </summary>
    public class CancelPayment
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        
        /// <summary>
        /// Collection of all Buttons
        /// </summary>
        public enum Button
        {
            Ok,
            Cancel
        }

        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }

        /// <summary>
        /// This method can be used to click on the desired button in the Cancel Payment alert.
        /// </summary>
        /// <param name="btn">btn represents the desired Button</param>
        public static void Click(Button btn)
        {
            string desiredText = null;
            switch (btn)
            {
                case Button.Ok:
                    desiredText = Repository.iOS.PaymentScreen.okWarning;
                    break;
                case Button.Cancel:
                    desiredText = Repository.iOS.PaymentScreen.cancelWarning;
                    break;
            }
            _instance.GetElement(SearchBy.Xpath, desiredText).Click();
        }

        /// <summary>
        /// This method returns a boolean value, based on existence of Cancel Payment alert.
        /// </summary>
        /// <returns>It returns a boolean true if the Cancel Payment alert is present and false otherwise.</returns>
        public static bool IsExist()
        {
            bool bExist = false;
            if (_instance.GetElement(SearchBy.Xpath, Repository.iOS.PaymentScreen.cancelWarning)!=null)
                bExist = true;
            return bExist;
        }

    }
}
