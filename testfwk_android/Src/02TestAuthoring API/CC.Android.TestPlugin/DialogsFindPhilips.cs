using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;

namespace Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin
{
    public class DialogsFindPhilips1
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }

        public enum TextButton
        {
            GoTo_Contact_Page,
            FindOk
        }

        /// <summary>
        /// header text
        /// </summary>
        /// <returns></returns>
        public static string GetDialogHeader()
        {
            return _instance.GetTextById(ConsumerCare.Android.DialogsFindPhilips.TextDesc);
        }
        public static bool ErrorMsg()
        {
            bool bVisible = false;
            var str = GetDialogHeader();
            var ns = "No service centres available in your country'";
            if (str.Contains(ns))
                bVisible = true;
            return bVisible;
        }

        public static bool WaitforFindPhilipsDialog1HomeScreen()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.DialogsFindPhilips.TextDesc);
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

        public static void clickTest(TextButton tBtn)
        {
            if (tBtn == TextButton.GoTo_Contact_Page)
                _instance.ClickById(ConsumerCare.Android.DialogsFindPhilips.GoToContactPge);
            else if (tBtn == TextButton.FindOk)
                _instance.ClickById(ConsumerCare.Android.DialogsFindPhilips.TextOK);
        }
    }


    public class DialogsFindPhilips2
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
            NoData_Ok
        }

        /// <summary>
        /// header text
        /// </summary>
        /// <returns></returns>
        public static string GetDialogHeader()
        {
            return _instance.GetTextById(ConsumerCare.Android.DialogsFindPhilips1.ErrorData);
        }
        public static bool DataErrorMsg()
        {
            bool bVisible = false;
            var str = GetDialogHeader();
            var ns = "No data available";
            if (str.Contains(ns))
                bVisible = true;
            return bVisible;
        }

        public static bool WaitforFindPhilipsDialog2HomeScreen()
        {
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.DialogsFindPhilips1.ErrorData);
                if (homeScreenElement != null)
                    break;
            }
            if (homeScreenElement != null)
                return true;
            else
                return false;
        }

        public static void ClickErrorData(Button btn)
        {
            if (btn == Button.NoData_Ok)
                _instance.ClickById(ConsumerCare.Android.DialogsFindPhilips1.DataOK);
        }
    }


    public class DialogsFindPhilips3
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
            GPS_Ok,
            GPS_Cancel
        }

        /// <summary>
        /// header text
        /// </summary>
        /// <returns></returns>
        public static string GetDialogHeader()
        {
            return _instance.GetTextById(ConsumerCare.Android.DialogsFindPhilips2.GPSErrorData);
        }
        public static bool GPSErrorMsg()
        {
            bool bVisible = false;
            var str = GetDialogHeader();
            var ns = "GPS is disabled on your device. Would you like to enable it";
            if (str.Contains(ns))
                bVisible = true;
            return bVisible;
        }

        public static bool WaitforFindPhilipsDialog3HomeScreen()
        {
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.DialogsFindPhilips2.GPSErrorData);
                if (homeScreenElement != null)
                    break;
            }
            if (homeScreenElement != null)
                return true;
            else
                return false;
        }

        public static bool WaitForDialog3()
        {
            IMobilePageControl homeScreenElement = null;
            if (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.DialogsFindPhilips2.GPSErrorData);
            }
            if (homeScreenElement != null)
                return true;
            else
                return false;

        }

        public static void ClickErrorData(Button btn)
        {
            if (btn == Button.GPS_Ok)
                _instance.ClickById(ConsumerCare.Android.DialogsFindPhilips2.GPS_OK);
            else if (btn == Button.GPS_Cancel)
                _instance.ClickById(ConsumerCare.Android.DialogsFindPhilips2.GPS_Cancel);
        }

        
    }

    public class DialogsFindPhilips4
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
            ShareLocationToPhilipsOKButton,
            NoServiceAvailableOKButton,
        }
        public static bool WaitForDialog3()
        {
            IMobilePageControl homeScreenElement = null;
          
            if (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.DialogsFindPhilips4.ShareLocationToPhilipsOKButton);
            }
            if (homeScreenElement != null)
                return true;
            else
                return false;
        }

        public static bool VerifyNoServiceAvailableDialog()
        {
            IMobilePageControl homeScreenElement = null;
            if (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.DialogsFindPhilips4.NoServiceAvailableOKButton);
            }
            if (homeScreenElement != null)
                return true;
            else
                return false;
        }

        public static void ClickMakeSure(Button btn)
        {
            if (btn == Button.ShareLocationToPhilipsOKButton)
                _instance.ClickById(ConsumerCare.Android.DialogsFindPhilips4.ShareLocationToPhilipsOKButton);
            if (btn == Button.NoServiceAvailableOKButton)
                _instance.ClickById(ConsumerCare.Android.DialogsFindPhilips4.NoServiceAvailableOKButton);
        }
    }
}
