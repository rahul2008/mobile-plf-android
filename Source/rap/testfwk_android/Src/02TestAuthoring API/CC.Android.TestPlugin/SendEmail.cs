using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;


namespace Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin
{
    public class SendEmail
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
            Backto_HomeScrn
        }

        /// <summary>
        /// header text
        /// </summary>
        /// <returns></returns>
        public static string GetHeaderTextSendEmail()
        {
            return _instance.GetTextById(ConsumerCare.Android.SendEmail.HeaderTxt);
        }
        public static bool Sen_EmailScreen()
        {
            bool bVisible = false;
            if (GetHeaderTextSendEmail() == "Send us an email")
                bVisible = true;
            return bVisible;

        }

        /// <summary>
        /// waiting for FAQ home screen
        /// </summary>
        /// <returns></returns>
        public static bool WaitforSendEmailHomeScreen()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.SendEmail.HeaderTxt);
                loopCount++;
                if (homeScreenElement != null)
                    break;
                if (loopCount > 10)
                    break;
            }
            if (homeScreenElement != null)
                return true;
            else
                return false;
        }

        /// <summary>
        /// checking whether btn visible or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsVisible(Button btn)
        {
            bool isVisble = false;
            if (btn == Button.Backto_HomeScrn)
                isVisble = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.SendEmail.BacktoHomeScrn).Displayed;
            return isVisble;
        }

        /// <summary>
        /// checking whether button is enabled or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsEnable(Button btn)
        {
            bool isEnable = false;
            if (btn == Button.Backto_HomeScrn)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.SendEmail.BacktoHomeScrn).Enabled;
            return isEnable;
        }

        /// <summary>
        /// checking whether button or clickable or not
        /// </summary>
        /// <param name="btn"></param>
        public static void ClickBackbtn(Button btn)
        {
            if (btn == Button.Backto_HomeScrn)
                _instance.ClickById(ConsumerCare.Android.SendEmail.BacktoHomeScrn);
        }
    }
}
