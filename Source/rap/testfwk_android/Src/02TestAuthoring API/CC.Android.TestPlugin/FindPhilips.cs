using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;

namespace Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin
{
    public class FindPhilips
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
            Backto_HomeScrn,
            SearchButton,
            Allow
        }

        public enum EditBox
        {
            Search
        }

        /// <summary>
        /// header text
        /// </summary>
        /// <returns></returns>
        public static string GetHeaderTextFindPhilips()
        {
            return _instance.GetTextById(ConsumerCare.Android.FindPhilips.FindHeaderText);
        }
        public static bool IsVisible1()
        {
            bool bVisible = false;
            if (GetHeaderTextFindPhilips() == "Find Philips near you")
                bVisible = true;
            return bVisible;

        }

        /// <summary>
        /// waiting for FAQ home screen
        /// </summary>
        /// <returns></returns>
        public static bool WaitforFindPhilipsHomeScreen()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.FindPhilips.FindHeaderText);
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
        /// Buttons visible or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsVisible(Button btn)
        {
            bool isVisible = false;
            try
            {
                if (btn == Button.SearchButton)
                    isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.FindPhilips.searchButton).Displayed;
                else if (btn == Button.Backto_HomeScrn)
                    isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.FindPhilips.BackToHome).Displayed;
                else if (btn == Button.Allow)
                    isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.FindPhilips.Allow).Displayed;

            }

            catch
            {
                
            }

            return isVisible;
        }

        /// <summary>
        /// Buttons Enabled or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool ISEnable1(Button btn)
        {
            bool isEnable = false;
            if (btn == Button.SearchButton)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.FindPhilips.searchButton).Enabled;
            else if (btn == Button.Backto_HomeScrn)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.FindPhilips.BackToHome).Enabled;
            return isEnable;
        }
        
        /// <summary>
        /// Text box editable or not
        /// </summary>
        /// <param name="srch"></param>
        /// <returns></returns>
        public static bool Editable(EditBox srch)
        {
            bool isEditable = false;
            if (srch == EditBox.Search)
                isEditable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.FindPhilips.Search).Enabled;
            return isEditable;
        }

        /// <summary>
        /// buttons clickable or not
        /// </summary>
        /// <param name="btn"></param>
        public static void ClickTst1(Button btn)
        {
            if (btn == Button.Backto_HomeScrn)
                _instance.ClickById(ConsumerCare.Android.FindPhilips.BackToHome);
            else if (btn == Button.SearchButton)
                _instance.ClickById(ConsumerCare.Android.FindPhilips.searchButton);
            else if (btn == Button.Allow)
                _instance.ClickById(ConsumerCare.Android.FindPhilips.Allow);
        }

        /// <summary>
        /// Searchinh with other country
        /// </summary>
        /// <param name="str"></param>
        public static void Add_Country(string str)
        {
             EnterText(EditBox.Search, str);
            _instance.ClickById(ConsumerCare.Android.FindPhilips.searchButton);
        }

        public static void EnterText(EditBox txt, string str)
        {
            string desiredtxtBox = string.Empty;
            if (txt == EditBox.Search)
                desiredtxtBox = ConsumerCare.Android.FindPhilips.Search;
            _instance.GetElement(SearchBy.Id, desiredtxtBox).SetText(str);
        }
    }
}
