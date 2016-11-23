using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;

namespace Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin
{
    public class AddCTN
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
        /// This class provides all the functionalities related to Add product screen.
        /// </summary>
        public enum Button
        {
            Submit
        }

        public enum TextBox
        {
            Add_CTN,
            Add_Category,
            Add_Catalog
        }

        /// <summary>
        /// wiating for Addproduct screen
        /// </summary>
        /// <returns>bool value</returns>
        public static bool WaitforAddProductScreen()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.AddProduct.Submit);
                loopCount++;
                if (homeScreenElement != null)
                    break;
                if (loopCount > 5)
                    break;
            }
            if (homeScreenElement != null)
                return true;
            else
                return false;
        }

        /// <summary>
        /// checking whether button is visible or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsVisible(Button btn)
        {
            bool isVisible = false;
            try
            {
                if (btn == Button.Submit)
                isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.AddProduct.Submit).Displayed;
            }
            catch(Exception e)
            {
                isVisible = false;
            }
            return isVisible;
        }

        /// <summary>
        /// checking whether button is enabled or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsEnable(Button btn)
        {
            bool isEnable = false;
            try
            {
                if (btn == Button.Submit)
                    isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.AddProduct.Submit).Enabled;
            }
            catch (Exception e)
            {
                isEnable = false;
            }
            return isEnable;
        }

        /// <summary>
        /// checking whether edit boxes are visible or not
        /// </summary>
        /// <param name="textbox"></param>
        /// <returns></returns>
        public static bool IsEditboxVisible(TextBox textbox)
        {
            bool isEditboxVisible = false;
            try
            {
                if (textbox == TextBox.Add_CTN)
                    isEditboxVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.AddProduct.AddCTN).Displayed;
                if (textbox == TextBox.Add_Category)
                    isEditboxVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.AddProduct.AddCategory).Displayed;
                if (textbox == TextBox.Add_Catalog)
                    isEditboxVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.AddProduct.AddCatalog).Displayed;
            }
            catch(Exception e)
            {
                isEditboxVisible = false;
            }
            return isEditboxVisible;
        }

        public static void ClickSubmit(Button btn)
        {
            if(btn == Button.Submit)
                _instance.ClickById(ConsumerCare.Android.AddProduct.Submit);
        }

        /// <summary>
        /// Checking whether Edit box is enabled or not
        /// </summary>
        /// <param name="txtbox"></param>
        /// <returns></returns>
        public static bool IsEditable(TextBox txtbox)
        {
            string desiredText = string.Empty;
            switch (txtbox)
            {
                case TextBox.Add_CTN:
                    desiredText = ConsumerCare.Android.AddProduct.AddCTN;
                    break;
                case TextBox.Add_Category:
                    desiredText = ConsumerCare.Android.AddProduct.AddCategory;
                    break;
                case TextBox.Add_Catalog:
                    desiredText = ConsumerCare.Android.AddProduct.AddCatalog;
                    break;
            }
            return _instance.GetElement(SearchBy.Id, desiredText).Enabled;

        }
        /// <summary>
        /// Adding CTN's
        /// </summary>
        /// <param name="str"></param>
        public static void Add_CTN(string str)
        {
            EnterText(TextBox.Add_CTN, str);
            _instance.ClickById(ConsumerCare.Android.AddProduct.Submit);
        }
        public static void EnterText(TextBox txtBox, string str)
        {
            string desiredtxtBox = string.Empty;
            if (txtBox == TextBox.Add_CTN)
                desiredtxtBox = ConsumerCare.Android.AddProduct.AddCTN;
            _instance.GetElement(SearchBy.Id, desiredtxtBox).SetText(str);
        }

        
    }
}
