using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;

namespace Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin
{
    public class ChatWithPhilips
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
            Backto_ContactUs,
            Chat_Now,
            Cancel
        }

        /// <summary>
        /// Getting Text Header
        /// </summary>
        /// <returns></returns>
        public static string GetHeaderTextFAQ()
        {
            return _instance.GetTextById(ConsumerCare.Android.ChatWithPhilips.HeaderText);
        }
        public static bool IsVisible1()
        {
            bool bVisible = false;
            if (GetHeaderTextFAQ() == "Chat with Philips")
                bVisible = true;
            return bVisible;

        }

        /// <summary>
        /// Waiting for screen
        /// </summary>
        /// <returns></returns>
        public static bool WaitforChatwithPhilipsHomeScreen()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ChatWithPhilips.HeaderText);
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
        /// Checking Whether Buttons are visible or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsVisible(Button btn)
        {
            bool isVisble = false;
            if (btn == Button.Backto_ContactUs)
                isVisble = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ChatWithPhilips.BacktoContactUs).Displayed;
            if (btn == Button.Chat_Now)
                isVisble = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ChatWithPhilips.ChatNow).Displayed;
            if (btn == Button.Cancel)
                isVisble = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ChatWithPhilips.Cancel).Displayed;
            return isVisble;
        }

        /// <summary>
        /// Buttons are Enabled or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsEnable(Button btn)
        {
            bool isEnable = false;
            if (btn == Button.Backto_ContactUs)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ChatWithPhilips.BacktoContactUs).Enabled;
            if (btn == Button.Chat_Now)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ChatWithPhilips.ChatNow).Enabled;
            if (btn == Button.Cancel)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.ChatWithPhilips.Cancel).Enabled;
            return isEnable;
        }

        /// <summary>
        /// Click Test
        /// </summary>
        /// <param name="btn"></param>
        public static void ClickChatTest(Button btn)
        {
            if (btn == Button.Backto_ContactUs)
                _instance.ClickById(ConsumerCare.Android.ChatWithPhilips.BacktoContactUs);
            if (btn == Button.Chat_Now)
                _instance.ClickById(ConsumerCare.Android.ChatWithPhilips.ChatNow);
            if (btn == Button.Cancel)
                _instance.ClickById(ConsumerCare.Android.ChatWithPhilips.Cancel);
        }

    }
}
