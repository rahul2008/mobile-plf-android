using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;

namespace Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin
{
    public class TellUs
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
            Back_to_HomeScreen,
            Write_an_app_Review,
            Submit_Product_Review,
            Retry
        }

        /// <summary>
        /// header text
        /// </summary>
        /// <returns></returns>
        public static string GetHeaderTellUsPhilips()
        {
            return _instance.GetTextById(ConsumerCare.Android.TellUs.TellUsHeaderText);
        }
        public static bool IsVisible1()
        {
            bool bVisible = false;
            if (GetHeaderTellUsPhilips() == "Tell us what you think")
                bVisible = true;
            return bVisible;

        }

        /// <summary>
        /// waiting for Support screen to open
        /// </summary>
        /// <returns>boolean variable</returns>
        public static bool WaitforTellUsScreen()
        {
            int loopCount = 0;
            IMobilePageControl supportElement = null;
            while (supportElement == null)
            {
                supportElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.TellUs.TellUsHeaderText);
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

        /// <summary>
        /// Buttons visible or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsVisible(Button btn)
        {
            bool isVisible = false;
            if (btn == Button.Back_to_HomeScreen)
                isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.TellUs.BackToHomescreen).Displayed;
            else if (btn == Button.Write_an_app_Review)
                isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.TellUs.AppReview).Displayed;
            else if (btn == Button.Submit_Product_Review)
                isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.TellUs.ProductReview).Displayed;
            return isVisible;
        }

        /// <summary>
        /// Buttons Enable or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsEnable(Button btn)
        {
            bool isEnable = false;
            if (btn == Button.Back_to_HomeScreen)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.TellUs.BackToHomescreen).Enabled;
            else if (btn == Button.Write_an_app_Review)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.TellUs.AppReview).Enabled;
            else if (btn == Button.Submit_Product_Review)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.TellUs.ProductReview).Enabled;
            return isEnable;
        }

        public static void ClickTellUsButtons(Button btn)
        {
            if (btn == Button.Back_to_HomeScreen)
                _instance.ClickById(ConsumerCare.Android.TellUs.BackToHomescreen);
            else if (btn == Button.Write_an_app_Review)
                _instance.ClickById(ConsumerCare.Android.TellUs.AppReview);
            else if (btn == Button.Submit_Product_Review)
                _instance.ClickById(ConsumerCare.Android.TellUs.ProductReview);
        }

        public static bool ProductReviewWeb()
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.ClassName, "android.webkit.WebView");
            if (control != null)
                return true;
            else
                return false;
        }
        public static void Back()
        {
            MobileDriver.FireKeyEvent(4);
            TellUs.WaitforTellUsScreen();
        }

        public static bool ButtonPresent(Button btn)
        {
            bool isPresent = false;
            if(btn == Button.Retry)
            isPresent = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.TellUs.RetryButton).Displayed;
            return isPresent;
        }

        public static bool PlaySoreView()
        {
             IMobilePageControl supportElement = null;
            while (supportElement == null)
            {
                supportElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.TellUs.PlayStoreRetryButton);
                if (supportElement != null)
                    break;
            }
            if (supportElement != null)
                return true;
            else
                return false;
        }
        
    }
}
