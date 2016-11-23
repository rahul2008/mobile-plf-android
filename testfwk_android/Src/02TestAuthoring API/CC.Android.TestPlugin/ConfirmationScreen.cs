using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;

namespace Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin
{
    public class ConfirmationScreen
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
            Backto_home,
            Change,
            Continue
        }

        /// <summary>
        /// header text
        /// </summary>
        /// <returns></returns>
        public static string GetHeaderConfirmation()
        {
            return _instance.GetTextById(ConsumerCare.Android.ConfirmationScreen.TextHeader);
        }
        
        public static bool IsVisible1()
        {
            bool bVisible = false;
            if (GetHeaderConfirmation() == "Confirmation")
                bVisible = true;
            return bVisible;

        }
        

        /// <summary>
        /// waiting for Support screen to open
        /// </summary>
        /// <returns>boolean variable</returns>
        public static bool WaitforConfirmationScreen()
        {
            int loopCount = 0;
            IMobilePageControl supportElement = null;
            while (supportElement == null)
            {
                supportElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ConfirmationScreen.TextHeader);
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
            try
            {
                if (btn == Button.Backto_home)
                    isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ConfirmationScreen.Bact_To_Home).Displayed;
                else if (btn == Button.Change)
                    isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ConfirmationScreen.Change).Displayed;
                else if (btn == Button.Continue)
                    isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ConfirmationScreen.Continue).Displayed;
            }
            catch 
            {
                isVisible = false;
            }
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
            if (btn == Button.Backto_home)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ConfirmationScreen.Bact_To_Home).Enabled;
            else if (btn == Button.Change)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ConfirmationScreen.Change).Enabled;
            else if (btn == Button.Continue)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ConfirmationScreen.Continue).Enabled;
            return isEnable;
        }

        public static void ClickConfirmationButtons(Button btn)
        {
            if (btn == Button.Backto_home)
                _instance.ClickById(ConsumerCare.Android.ConfirmationScreen.Bact_To_Home);
            else if (btn == Button.Change)
                _instance.ClickById(ConsumerCare.Android.ConfirmationScreen.Change);
            else if (btn == Button.Continue)
                _instance.ClickById(ConsumerCare.Android.ConfirmationScreen.Continue);
        }
        

        public static string GetProductVariant()
        {
            string prdctVariant = _instance.GetTextById(ConsumerCare.Android.ConfirmationScreen.ProductVrnt);
            return prdctVariant;
        }
    }
}
