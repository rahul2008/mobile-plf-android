using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using System.Collections.Generic;

namespace Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin
{
    public class GPSLocation
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }

        public enum SwitchButton
        {
            GPRS_ToggelButton
        }


        ///</Summary>
        /// Buttons Enabled or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool ISEnable1(SwitchButton btn)
        {
            bool isEnable = false;
            if (btn == SwitchButton.GPRS_ToggelButton)
                isEnable = _instance.GetElement(SearchBy.ClassName, "android.widget.Switch").Enabled;
            return isEnable;
        }

        public static void clickGPSBtn(SwitchButton btn)
        {
            if (btn == SwitchButton.GPRS_ToggelButton)
                _instance.ClickByName("OFF");
        }

        public static bool VerifyGPSOn()
        {
            string status = string.Empty;
            List<IMobilePageControl> elements = new List<IMobilePageControl>();
            elements = _instance.GetElements(SearchBy.ClassName, "android.widget.Switch");
            foreach (IMobilePageControl elem in elements)
            {
                status = elem.Text;
            }
            if (status == "ON")
                return true;
            else
                return false;
        }

        public static void Back()
        {
            MobileDriver.FireKeyEvent(4);
            ContactUs.WaitforContactUsHomeScreen();
        }

    }
}
