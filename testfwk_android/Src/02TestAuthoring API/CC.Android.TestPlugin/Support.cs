using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Collections.Generic;
using System.Threading;

namespace Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin
{
    public class Support
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }

        /// <summary>
        /// This class provides all the functionalities related to Product page.
        /// </summary>
        public enum Button
        {
            View_Product_Information,
            Read_FAQ,
            Contact_Us,
            Find_Philips_near_You,
            Tell_Us,
            My_Philips_Account,
            Change_Selected_Product,
            Return_To_HomeScreen
        }

        public static bool IsVisible(Button btn)
        {
            bool isVisible = false;
            try
            {
                if (btn == Button.View_Product_Information)
                    isVisible = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.SupportScreen.ViewProductInfo).Displayed;
                else if (btn == Button.Read_FAQ)
                    isVisible = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.SupportScreen.ReadFAQ).Displayed;
                else if (btn == Button.Contact_Us)
                    isVisible = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.SupportScreen.ContactUs).Displayed;
                else if (btn == Button.Find_Philips_near_You)
                    isVisible = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.SupportScreen.FindPhilips).Displayed;
                else if (btn == Button.Tell_Us)
                    isVisible = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.SupportScreen.TellUs).Displayed;
                else if (btn == Button.My_Philips_Account)
                    isVisible = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.SupportScreen.MyPhilipsAccount).Displayed;
                else if (btn == Button.Change_Selected_Product)
                    isVisible = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.SupportScreen.ChangeSelectedProduct).Displayed;
                else if (btn == Button.Return_To_HomeScreen)
                    isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.SupportScreen.ReturnToHomeScreen).Displayed;
            }
            catch(Exception e)
            {
                isVisible = false;
            }
            return isVisible;
        }

        /// <summary>
        /// Checks if a button is Enabled or not
        /// </summary>
        public static bool IsEnabled(Button btn)
        {
            bool isEnabled = false;
            if (btn == Button.View_Product_Information)
                isEnabled = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.SupportScreen.ViewProductInfo).Enabled;
            else if (btn == Button.Read_FAQ)
                isEnabled = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.SupportScreen.ReadFAQ).Enabled;
            else if (btn == Button.Contact_Us)
                isEnabled = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.SupportScreen.ContactUs).Enabled;
            else if (btn == Button.Find_Philips_near_You)
                isEnabled = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.SupportScreen.FindPhilips).Enabled;
            else if (btn == Button.Tell_Us)
                isEnabled = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.SupportScreen.TellUs).Enabled;
            else if (btn == Button.My_Philips_Account)
                isEnabled = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.SupportScreen.MyPhilipsAccount).Enabled;
            else if (btn == Button.Change_Selected_Product)
                isEnabled = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.SupportScreen.ChangeSelectedProduct).Enabled;
            else if (btn == Button.Return_To_HomeScreen)
                isEnabled = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.SupportScreen.ReturnToHomeScreen).Enabled;
            return isEnabled;
        }

        /// <summary>
        /// Checks if a button is clickable or not
        /// </summary>
        /// <param name="btn"></param>
        public static void Click(Button btn)
        {
            if (btn == Button.View_Product_Information)
                _instance.ClickByName(ConsumerCare.Android.SupportScreen.ViewProductInfo);
            else if (btn == Button.Read_FAQ)
                _instance.ClickByName(ConsumerCare.Android.SupportScreen.ReadFAQ);
            else if (btn == Button.Contact_Us)
                _instance.ClickByName(ConsumerCare.Android.SupportScreen.ContactUs);
            else if (btn == Button.Find_Philips_near_You)
                _instance.ClickByName(ConsumerCare.Android.SupportScreen.FindPhilips);
            else if (btn == Button.Tell_Us)
                _instance.ClickByName(ConsumerCare.Android.SupportScreen.TellUs);
            else if (btn == Button.My_Philips_Account)
                _instance.ClickByName(ConsumerCare.Android.SupportScreen.MyPhilipsAccount);
            else if (btn == Button.Change_Selected_Product)
                _instance.ClickByName(ConsumerCare.Android.SupportScreen.ChangeSelectedProduct);
            else if (btn == Button.Return_To_HomeScreen)
                _instance.ClickById(ConsumerCare.Android.SupportScreen.ReturnToHomeScreen);
        }

        /// <summary>
        /// waiting for Support screen to open
        /// </summary>
        /// <returns>boolean variable</returns>
        public static bool WaitforSupportScreen()
        {
            IMobilePageControl supportElement = null;
            int loopCount = 0;
            while(supportElement == null)
            {
                supportElement = _instance.GetElement(SearchBy.Name, ConsumerCare.Android.SupportScreen.ContactUs);
                if (supportElement != null)
                    break;
                loopCount++;
                if (loopCount > 20)
                    break;
                Thread.Sleep(500);
            }
            if (supportElement != null)
                return true;
            else
                return false;
        }

        /// <summary>
        /// Getting SupportScreen title
        /// </summary>
        /// <returns>boolean variable</returns>
        public static string GetHeaderText()
        {
            return _instance.GetTextById(AppFrameWork.Android.HomeScreen.SupportTitle);
        }
        public static bool IsVisible1()
        {
            bool bVisible = false;
            if (GetHeaderText() == "Support")
                bVisible = true;
            return bVisible;

        }
       
    }
}
