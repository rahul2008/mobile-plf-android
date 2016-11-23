using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;

namespace Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin
{
    public class Question_and_Answer
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
            Back_to_FAQPage
        }
        /// <summary>
        /// getting header text
        /// </summary>
        /// <returns>boolean value</returns>
        public static string GetHeaderTextQuestionAnwer()
        {
            return _instance.GetTextById(ConsumerCare.Android.QuestionandAnswer.QAHeaderText);
        }
        public static bool format_QstnAns()
        {
            bool bVisible = false;
            if (GetHeaderTextQuestionAnwer() == "Question and answer")
                bVisible = true;
            return bVisible;

        }

        /// <summary>
        /// Waiting for QA home screen
        /// </summary>
        /// <returns></returns>
        public static bool WaitforQAScreen()
        {
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.QuestionandAnswer.QAHeaderText);
                if (homeScreenElement != null)
                    break;
            }
            if (homeScreenElement != null)
                return true;
            else
                return false;
        }

        /// <summary>
        /// checking whether button visible or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsVisible(Button btn)
        {
            bool isVisible = false;
            if (btn == Button.Back_to_FAQPage)
                isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.QuestionandAnswer.BackToFAQHomePage).Displayed;
            return isVisible;
        }

        /// <summary>
        /// checking whether button enable or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsEnable(Button btn)
        {
            bool isEnable = false;
            if (btn == Button.Back_to_FAQPage)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.QuestionandAnswer.BackToFAQHomePage).Enabled;
            return isEnable;
        }

        /// <summary>
        /// checking whether button clickable or not
        /// </summary>
        /// <param name="btn"></param>
        public static void ClickQABackbtn(Button btn)
        {
            if (btn == Button.Back_to_FAQPage)
                _instance.ClickById(ConsumerCare.Android.QuestionandAnswer.BackToFAQHomePage);
        }

        public static void Slide()
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.Id, "com.philips.cdp.sampledigitalcare:id/digitalcare_webview_scrollview");
            int[] coords = new int[2];
            coords = control.Coordinates;
            int[] sz = new int[2];
            sz = control.Size;
            int Srcx = coords[0] + sz[0] / 2;
            int Srcy = coords[1] + sz[1] / 2;
            double count = (double)Srcy;
            count = count - 400;
            MobileDriver.Swipe((double)Srcx, (double)Srcy, (double)Srcx, (count));
        }
    }
}

