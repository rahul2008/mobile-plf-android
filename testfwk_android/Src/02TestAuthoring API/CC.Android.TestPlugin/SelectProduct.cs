using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;


namespace Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin
{
    public class SelectProduct
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }

        public enum Button
        {
            Backto_Home,
            Search
        }

        
        /// <summary>
        /// waiting for SelectProduct screen to open
        /// </summary>
        /// <returns>boolean variable</returns>
        public static bool WaitforSelectProductScreen()
        {
            int loopCount = 0;
            IMobilePageControl supportElement = null;
            while (supportElement == null)
            {
                supportElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.SelectProduct.BackTo_Home);
                loopCount++;
                if (supportElement != null)
                    break;
                if (loopCount > 10)
                    break;
            }
            if (supportElement != null)
                return true;
            else
                return false;
        }

        public static bool SelectproductPage()
        {
            IMobilePageControl supportElement = null;
            if (supportElement == null)
            {
                supportElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.SelectProduct.BackTo_Home);
            }
            if (supportElement != null)
                return true;
            else
                return false;
        }

        /// <summary>
        /// Checking whether Buttons visble or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsVisibleSelectPrdct(Button btn)
        {
            bool isVisible = false;
            try
            {
                if (btn == Button.Backto_Home)
                    isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.SelectProduct.BackTo_Home).Displayed;
                if (btn == Button.Search)
                    isVisible = _instance.GetElement(SearchBy.ClassName, "android.widget.TextView").Displayed;
            }
            catch
            {
                isVisible = false;
            }
            return isVisible;
        }

        /// <summary>
        /// Checking whether Buttons enabled or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsEnableSelectPrdct(Button btn)
        {
            bool isEnable = false;
            if (btn == Button.Backto_Home)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.SelectProduct.BackTo_Home).Enabled;
            else if (btn == Button.Search)
                isEnable = _instance.GetElement(SearchBy.ClassName, "android.widget.TextView").Enabled;
            return isEnable;
        }

        /// <summary>
        /// Checking whether buttons clickable or not
        /// </summary>
        /// <param name="btn"></param>
        public static void ClicktestProductScreen(Button btn)
        {
            if (btn == Button.Backto_Home)
                _instance.ClickById(ConsumerCare.Android.SelectProduct.BackTo_Home);
            else if (btn == Button.Search)
                _instance.ClickByText("Find product");
        }

        /// <summary>
        /// Image cropping
        /// </summary>
        public static void GetProductImage()
        {
            //IMobilePageControl control = _instance.GetElement(SearchBy.Xpath, ConsumerCareiOS.iOS.SelectProduct.Image);
            //MobileDriver.GetImage(control, "croppedProductImage");
        }
    }
}
