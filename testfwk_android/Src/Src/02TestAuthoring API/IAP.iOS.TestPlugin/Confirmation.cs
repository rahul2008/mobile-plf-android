using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


namespace Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS
{
    /// <summary>
    /// This class provides all the functionalities and constants for features related to Confirmation page.
    /// </summary>
    public class Confirmation
    {
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }

        /// <summary>
        /// Collection of all Buttons
        /// </summary>
        public enum Button
        {
            OK
        }

        /// <summary>
        /// Collection of all TextViews
        /// </summary>
        public enum TextView
        {
            ThankYou,
            
            OrderNumber,
            Confirm_Email
        }

        /// <summary>
        /// This method is used to automate click operation on any buttons in Confirmation screen.
        /// </summary>
        /// <param name="btn">btn represents the name of the Button</param>
        public static void Click(Button btn)
        {
            string desiredText = string.Empty;
            switch (btn)
            {
                case Button.OK:
                    desiredText = Repository.iOS.ConfirmationScreen.OKButton;
                    break;
            }
            _instance.GetElement(SearchBy.Xpath, desiredText).Click();
        }

        /// <summary>
        /// Returns a string containing the content of the TextView
        /// </summary>
        /// <param name="tv">tv represents the TextView name</param>
        /// <returns>a string value</returns>
        public static string GetText(TextView tv)
        {
            string desiredID = string.Empty;
            switch (tv)
            {
                case TextView.ThankYou:
                    desiredID = Repository.iOS.ConfirmationScreen.Thankyou_TV;
                    break;
                case TextView.OrderNumber:
                    desiredID = Repository.iOS.ConfirmationScreen.OrderNumber;
                    break;
                
                case TextView.Confirm_Email:
                    desiredID = Repository.iOS.ConfirmationScreen.ConfirmEmail;
                    break;
            }
            return _instance.GetElement(SearchBy.Xpath, desiredID).Text;
        }

        /// <summary>
        /// Waits for the Shopping Cart Screen
        /// Returns false if the Shopping Cart Screen doesn't appear after loopcount exceeds 10
        /// </summary>
        /// <returns>a boolean value</returns>
        public static bool WaitforConfirmationScreen()
        {
            IMobilePageControl element = null;
            int loopcount = -0;
            while (element == null)
            {
                element = _instance.GetElement(SearchBy.Xpath,Repository.iOS.ConfirmationScreen.OKButton);
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
