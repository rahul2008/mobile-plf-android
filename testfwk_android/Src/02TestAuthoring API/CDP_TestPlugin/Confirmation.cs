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


namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin
{
    /// <summary>
    /// This class provides all the functionalities and constants for features related to Confirmation page.
    /// </summary>
    public class Confirmation : IAP_Common
    {

        /// <summary>
        /// Collection of all Buttons
        /// </summary>
        public enum Button
        {
            OK,
            CONTINUESHOPPING_OK,
            CONTINUESHOPPING_CANCEL
        }

        /// <summary>
        /// Collection of all TextViews
        /// </summary>
        public enum TextView
        {
            ThankYou,
            YourOrderNumber,
            OrderNumber,
            Confirm_Email
        }

        /// <summary>
        /// Clicks the desired button
        /// </summary>
        /// <param name="btn">btn represents the name of the Button</param>
        public static void Click(Button btn)
        {
            string desiredText = string.Empty;
            switch (btn)
            {
                case Button.OK:
                    desiredText = ObjectRepository.OKButton;
                    break;
                case Button.CONTINUESHOPPING_OK:
                    desiredText = "com.philips.cdp.di.iapdemo:id/dialogButtonOk";
                    break;
                case Button.CONTINUESHOPPING_CANCEL:
                    desiredText = "com.philips.cdp.di.iapdemo:id/dialogButtonCancel";
                    break;
            }
            _instance.ClickById(desiredText);
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
                    desiredID = ObjectRepository.Thankyou_TV;
                    break;
                case TextView.OrderNumber:
                    desiredID = ObjectRepository.OrderNumber;
                    break;
                case TextView.YourOrderNumber:
                    desiredID = ObjectRepository.YourOrderNumber;
                    break;
                case TextView.Confirm_Email:
                    desiredID = ObjectRepository.TotalItems;
                    break;
            }
            return _instance.GetElement(SearchBy.Id, desiredID).Text;
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
                element = _instance.GetElement(SearchBy.Id, ObjectRepository.ScreenTitle);
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
