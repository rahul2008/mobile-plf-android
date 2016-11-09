using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;

namespace Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin
{
    public class SelectedProductScreen
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
            Select_This_Produt
        }

        /// <summary>
        /// header text
        /// </summary>
        /// <returns></returns>
        public static string GetHeaderSelectedProductScreen()
        {
            return _instance.GetTextById(ConsumerCare.Android.SelectedProductScreen.TextHeader);
        }
        public static bool IsVisible1()
        {
            bool bVisible = false;
            if (GetHeaderSelectedProductScreen() == "Lumea Prestige IPL - Hair regrowth prevention")
                bVisible = true;
            return bVisible;
        }

        /// <summary>
        /// waiting for Product home screen
        /// </summary>
        /// <returns></returns>
        public static bool WaitforSelectedProductHomeScreen()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.SelectedProductScreen.TextHeader);
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

        public static bool IsVisible(Button btn)
        {
            bool isVisible = false;
            if (btn == Button.Backto_Home)
                isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.SelectedProductScreen.Backto_home).Displayed;
            else if (btn == Button.Backto_Home)
                isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.SelectedProductScreen.Select_Button).Displayed;
            return isVisible;
        }

        public static bool IsEnable(Button btn)
        {
            bool isEnable = false;
            if (btn == Button.Backto_Home)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.SelectedProductScreen.Backto_home).Enabled;
            else if (btn == Button.Backto_Home)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.SelectedProductScreen.Select_Button).Enabled;
            return isEnable;
        }

        public static void ClickSelectProductTest(Button btn)
        {
            if (btn == Button.Backto_Home)
                _instance.ClickById(ConsumerCare.Android.SelectedProductScreen.Backto_home);
            else if (btn == Button.Select_This_Produt)
                _instance.ClickById(ConsumerCare.Android.SelectedProductScreen.Select_Button);
        }

        public static string GetSelected_ProductName()
        {
            string str = _instance.GetTextById(ConsumerCare.Android.SelectedProductScreen.Selected_Productname);
            return str;
        }

        public static string GetSelected_ProductCTN()
        {
            string str = _instance.GetTextById(ConsumerCare.Android.SelectedProductScreen.Selected_ProductCTN);
            return str;
        }

    }

}
