using OpenQA.Selenium;
using OpenQA.Selenium.Appium;
using OpenQA.Selenium.Appium.MultiTouch;
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

namespace Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS
{
    /// <summary>
    /// This class provides all the functionalities and constants for features related to Address.
    /// </summary>
    public class Address
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();

        /// <summary>
        /// Collection of all EditTexts
        /// </summary>
        public enum EditText
        {
            FirstName,
            LastName,
            Salutation,
            AddressLine1,
            AddressLine2,
            City,
            PostalCode,
            Country,
            State,
            Email,
            Phone
        }

        /// <summary>
        /// Collection of constant values Representing Swipe Direction
        /// </summary>
        public enum Direction
        {
            Up,
            Down
        }

        /// <summary>
        /// Provides collection of all the buttons
        /// </summary>
        public enum Button
        {
            Continue,
            Cancel,
            Save,
            UpButton,
            Switch
        }

        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }

        /// <summary>
        /// Enters text into the corresponding EditText field
        /// </summary>
        /// <param name="et">et represents the name of the EditText </param>
        /// <param name="txtValue">txtValue represents the string to be entered</param>
        public static void EnterText(EditText et, string txtValue)
        {
            string desiredText = string.Empty;

            switch (et)
            {
                case EditText.FirstName:
                    desiredText = Repository.iOS.AddressScreen.FirstName;
                    break;
                case EditText.LastName:
                    desiredText = Repository.iOS.AddressScreen.LastName;
                    break;
                case EditText.Salutation:
                    desiredText = Repository.iOS.AddressScreen.Salutation;
                    break;
                case EditText.AddressLine1:
                    desiredText = Repository.iOS.AddressScreen.AddressLine1;
                    break;
                case EditText.AddressLine2:
                    desiredText = Repository.iOS.AddressScreen.AddressLine2;
                    break;
                case EditText.City:
                    desiredText = Repository.iOS.AddressScreen.City;
                    break;
                case EditText.PostalCode:
                    desiredText = Repository.iOS.AddressScreen.PostalCode;
                    break;
                case EditText.Country:
                    desiredText = Repository.iOS.AddressScreen.Country;
                    break;
                case EditText.State:
                    desiredText = Repository.iOS.AddressScreen.State;
                    break;
                case EditText.Email:
                    desiredText = Repository.iOS.AddressScreen.AddrEmail;
                    break;
                case EditText.Phone:
                    desiredText = Repository.iOS.AddressScreen.Phone;
                    break;
            }
            if (et == EditText.Salutation)
            {
                _instance.GetElement(SearchBy.Xpath, desiredText).Click();
                int salutationCnt = _instance.GetElement(SearchBy.Xpath, Repository.iOS.AddressScreen.SalutationPopOver).ElementInstance.FindElements(ByIosUIAutomation.ClassName("UIAButton")).Count;
                IWebElement[] elements = new IWebElement[salutationCnt];
                _instance.GetElement(SearchBy.Xpath, Repository.iOS.AddressScreen.SalutationPopOver).ElementInstance.FindElements(ByIosUIAutomation.ClassName("UIAButton")).CopyTo(elements, 0);
                foreach (IWebElement salElement in elements)
                {
                    if (salElement.Text == txtValue)
                    {
                        salElement.Click();
                        return;
                    }
                }
            }
            else if (et == EditText.State)
            {
                _instance.GetElement(SearchBy.Xpath, desiredText).Click();

                int countOfStatesInUI = _instance.GetElement(SearchBy.Xpath, Repository.iOS.AddressScreen.StatePopOver).ElementInstance.FindElements(ByIosUIAutomation.ClassName("UIATableCell")).Count;

                IWebElement[] elements = new IWebElement[countOfStatesInUI];
                _instance.GetElement(SearchBy.Xpath, Repository.iOS.AddressScreen.StatePopOver).ElementInstance.FindElements(ByIosUIAutomation.ClassName("UIATableCell")).CopyTo(elements, 0);

                foreach (IWebElement stateElement in elements)
                {

                    if (stateElement.FindElement(ByIosUIAutomation.ClassName("UIAStaticText")).Text == txtValue)
                    {
                        stateElement.Click();
                        return;
                    }
                }
            }
            _instance.GetElement(SearchBy.Xpath, desiredText).SetText(txtValue);
        }

        /// <summary>
        /// Returns the text present in the EditText field
        /// </summary>
        /// <param name="et">et represents the name of the EditText, whose text value is required</param>
        /// <returns>a string value</returns>
        public static string GetText(EditText et)
        {
            string desiredText = string.Empty;

            switch (et)
            {
                case EditText.FirstName:
                    desiredText = Repository.iOS.AddressScreen.FirstName;
                    break;
                case EditText.LastName:
                    desiredText = Repository.iOS.AddressScreen.LastName;
                    break;
                case EditText.AddressLine1:
                    desiredText = Repository.iOS.AddressScreen.AddressLine1;
                    break;
                case EditText.AddressLine2:
                    desiredText = Repository.iOS.AddressScreen.AddressLine2;
                    break;
                case EditText.City:
                    desiredText = Repository.iOS.AddressScreen.City;
                    break;
                case EditText.PostalCode:
                    desiredText = Repository.iOS.AddressScreen.PostalCode;
                    break;
                case EditText.Country:
                    desiredText = Repository.iOS.AddressScreen.Country;
                    break;
                case EditText.Email:
                    desiredText = Repository.iOS.AddressScreen.AddrEmail;
                    break;
                case EditText.Phone:
                    desiredText = Repository.iOS.AddressScreen.Phone;
                    break;
                case EditText.Salutation:
                    desiredText = Repository.iOS.AddressScreen.Salutation;
                    break;
                case EditText.State:
                    desiredText = Repository.iOS.AddressScreen.State;
                    break;
            }
            return _instance.GetElement(SearchBy.Xpath, desiredText).Text;
        }

        /// <summary>
        /// Returns a string containing the Title of the Page
        /// </summary>
        /// <returns>a string value</returns>
        public static string GetHeader()
        {
            return _instance.GetElement(SearchBy.Xpath, Repository.iOS.AddressScreen.Title).Text;
        }

        /// <summary>
        /// Clicks the desired button
        /// </summary>
        /// <param name="btn">btn represents the name of the Button to be clicked</param>
        public static void Click(Button btn)
        {
            string desiredText = string.Empty;
            switch (btn)
            {
                case Button.Cancel:
                    desiredText = Repository.iOS.AddressScreen.Cancel;
                    break;
                case Button.UpButton:
                    desiredText = Repository.iOS.ShoppingCart.Back;
                    break;
                case Button.Save:
                    desiredText = Repository.iOS.AddressScreen.Save;
                    break;
                case Button.Continue:
                    desiredText = Repository.iOS.AddressScreen.ContinueButton;
                    break;
                case Button.Switch:
                    desiredText = Repository.iOS.AddressScreen.Switch;
                    break;
            }
            _instance.GetElement(SearchBy.Xpath, desiredText).Click();
        }

        /// <summary>
        /// Returns true of the desired button is present
        /// </summary>
        /// <param name="btn">btn represents the name of the button</param>
        /// <returns>a boolean value</returns>
        public static bool IsExist(Button btn)
        {
            bool bExist = false;
            string desiredText = string.Empty;
            switch (btn)
            {
                case Button.Cancel:
                    desiredText = Repository.iOS.AddressScreen.Cancel;
                    break;
                case Button.UpButton:
                    desiredText = Repository.iOS.ShoppingCart.Back;
                    break;
                case Button.Save:
                    desiredText = Repository.iOS.AddressScreen.Save;
                    break;
                case Button.Continue:
                    desiredText = Repository.iOS.AddressScreen.ContinueButton;
                    break;
                case Button.Switch:
                    desiredText = Repository.iOS.AddressScreen.Switch;
                    break;
            }
            if (_instance.GetElement(SearchBy.Xpath, desiredText) != null)
                bExist = true;
            else
                bExist = false;
            return bExist;
        }

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
                    desiredText = Repository.iOS.AddressScreen.Cancel;
                    break;
                case Button.UpButton:
                    desiredText = Repository.iOS.ShoppingCart.Back;
                    break;
                case Button.Save:
                    desiredText = Repository.iOS.AddressScreen.Save;
                    break;
                case Button.Continue:
                    desiredText = Repository.iOS.AddressScreen.ContinueButton;
                    break;
                case Button.Switch:
                    desiredText = Repository.iOS.AddressScreen.Switch;
                    break;
            }
            return _instance.GetElement(SearchBy.Xpath, desiredText).Enabled;
        }

        /// <summary>
        /// Returns true if the desired EditText field is Enabled
        /// </summary>
        /// <param name="et">btn represents the name of the EditText field</param>
        /// <returns>a boolean value</returns>
        public static bool IsEnabled(EditText et)
        {
            string desiredText = string.Empty;
            switch (et)
            {
                case EditText.FirstName:
                    desiredText = Repository.iOS.AddressScreen.FirstName;
                    break;
                case EditText.LastName:
                    desiredText = Repository.iOS.AddressScreen.LastName;
                    break;
                case EditText.AddressLine1:
                    desiredText = Repository.iOS.AddressScreen.AddressLine1;
                    break;
                case EditText.AddressLine2:
                    desiredText = Repository.iOS.AddressScreen.AddressLine2;
                    break;
                case EditText.City:
                    desiredText = Repository.iOS.AddressScreen.City;
                    break;
                case EditText.PostalCode:
                    desiredText = Repository.iOS.AddressScreen.PostalCode;
                    break;
                case EditText.Country:
                    desiredText = Repository.iOS.AddressScreen.Country;
                    break;
                case EditText.Email:
                    desiredText = Repository.iOS.AddressScreen.AddrEmail;
                    break;
                case EditText.Phone:
                    desiredText = Repository.iOS.AddressScreen.Phone;
                    break;
                case EditText.Salutation:
                    desiredText = Repository.iOS.AddressScreen.Salutation;
                    break;
                case EditText.State:
                    desiredText = Repository.iOS.AddressScreen.State;
                    break;
            }
            return _instance.GetElement(SearchBy.Xpath, desiredText).Enabled;

        }

        /// <summary>
        /// Returns true of the desired button is visible on the current screen
        /// </summary>
        /// <param name="btn">btn represents the name of the Button</param>
        /// <returns>a boolean value</returns>
        public static bool IsVisible(Button btn)
        {
            bool bVisible = false;
            switch (btn)
            {
                case Button.Cancel:
                    if ((_instance.GetElement(SearchBy.Xpath, Repository.iOS.AddressScreen.Cancel).Text) != null)
                        bVisible = true;
                    break;
                case Button.Save:
                    if ((_instance.GetElement(SearchBy.Xpath, Repository.iOS.AddressScreen.Save).Text) != null)
                        bVisible = true;
                    break;
                case Button.Continue:
                    if ((_instance.GetElement(SearchBy.Xpath, Repository.iOS.AddressScreen.ContinueButton).Text) != null)
                        bVisible = true;
                    break;
                case Button.UpButton:
                    if ((_instance.GetElement(SearchBy.Xpath, Repository.iOS.ShoppingCart.Back).Text) != null)
                        bVisible = true;
                    break;
                case Button.Switch:
                    if ((_instance.GetElement(SearchBy.Xpath, Repository.iOS.AddressScreen.Switch).Text) != null)
                        bVisible = true;
                    break;
            }
            return bVisible;
        }

        ///// <summary>
        ///// This Function Enters a Text and then captures the Validation Text.
        ///// The User is expected to send a "" in the argument.
        ///// User need not call EnterText Function.
        ///// User needs to send the next field, for the current text field to be de-focussed.
        ///// </summary>
        ///// <param name="txtFieldToEnterValidationText">txtFieldToEnterValidationText represents the field name whose validation needs to be performed</param>
        ///// <param name="nextTxtField">nextTxtField represent the name of any other field</param>
        ///// <param name="txtValue">txtValue represents the null string with which validation can be checked</param>
        ///// <returns></returns>
        //public static string ValidateTextFields(EditText txtFieldToEnterValidationText, EditText nextTxtField,string txtValue)
        //{
        //    EnterText(txtFieldToEnterValidationText, txtValue);
        //    TouchAction ta=new TouchAction();
        //    EnterText(nextTxtField,"");
        //    return _instance.GetElement(SearchBy.Xpath, Repository.iOS.AddressScreen.ErrorText).Text;
        //}

        /// <summary>
        /// Waits till the Address Screen appears
        /// </summary>
        /// <returns>a boolean value</returns>
        public static bool WaitforAddressScreen()
        {
            IMobilePageControl element = null;
            int loopcount = -0;
            while (element == null)
            {
                element = _instance.GetElement(SearchBy.Xpath, Repository.iOS.AddressScreen.Title);
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

    }
}
