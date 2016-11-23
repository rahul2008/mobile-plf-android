using OpenQA.Selenium.Appium.MultiTouch;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Diagnostics;


namespace UserRegistration_TestPlugin
{
    public class UR_DialogScreen
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
            Continue,
            OK,
            Coppa_OK,
            Coppa_Cancel
        }

        public enum EditBox
        {
            Age,
            Year
        }

        /// <summary>
        /// waiting for dialog
        /// </summary>
        /// <returns></returns>
        public static bool WaitForResentDialog()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, UserRegistration.Android.UR_Dialogs.Resent_Title);
                loopCount++;
                if (homeScreenElement != null)
                    break;
                if (loopCount > 3)
                    break;
            }
            if (homeScreenElement != null)
                return true;
            else
                return false;
        }

        /// <summary>
        /// get dialog header
        /// </summary>
        /// <returns></returns>
        public static string GetResentHeader()
        {
            string str = _instance.GetElement(SearchBy.Id, UserRegistration.Android.UR_Dialogs.Resent_Title).Text;
            return str;
        }


        /// <summary>
        /// Button clicking
        /// </summary>
        /// <param name="btn"></param>
        public static void Click(Button btn)
        {
            if (btn == Button.Continue)
                _instance.ClickById(UserRegistration.Android.UR_Dialogs.Continue_Button);
            if (btn == Button.OK)
                _instance.ClickById(UserRegistration.Android.UR_Dialogs.OK);
            if (btn == Button.Coppa_OK)
                _instance.ClickById(UserRegistration.Android.UR_Dialogs.Coppa_Ok);
            if (btn == Button.Coppa_Cancel)
                _instance.ClickById(UserRegistration.Android.UR_Dialogs.Coppa_Cancel);
        }

        /// <summary>
        /// Waiting for password reset dialog screen
        /// </summary>
        /// <returns></returns>
        public static bool WaitForResetPasswordDialog()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, UserRegistration.Android.UR_Dialogs.Reset_Password);
                loopCount++;
                if (homeScreenElement != null)
                    break;
                if (loopCount > 5)
                    break;
            }
            if (homeScreenElement != null)
                return true;
            else
                return false;
        }

        /// <summary>
        /// Waiting for parent dialog
        /// </summary>
        /// <returns></returns>
        public static bool WaitforParentalDialog()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, UserRegistration.Android.UR_Dialogs.ParentalMessage);
                loopCount++;
                if (homeScreenElement != null)
                    break;
                if (loopCount > 5)
                    break;
            }
            if (homeScreenElement != null)
                return true;
            else
                return false;
        }

        /// <summary>
        /// Getting parental dialog msg
        /// </summary>
        /// <returns></returns>
        public static string Parental_ErrorMsg()
        {
            string str = _instance.GetElement(SearchBy.Id, UserRegistration.Android.UR_Dialogs.ParentalMessage).Text;
            return str;
        }


        /// <summary>
        /// waiting for picker dialog
        /// </summary>
        /// <returns></returns>
        public static bool WaitforPickerDialog()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, UserRegistration.Android.UR_Dialogs.Enter_Digit);
                loopCount++;
                if (homeScreenElement != null)
                    break;
                if (loopCount > 5)
                    break;
            }
            if (homeScreenElement != null)
                return true;
            else
                return false;
        }

        /// <summary>
        /// entering values to age and year edit box
        /// </summary>
        /// <param name="edt"></param>
        /// <param name="digit"></param>
        public static void Enter_Picker(EditBox edt, string digit)
        {
            if (edt == EditBox.Age)
            {
                IMobilePageControl control = _instance.GetElement(SearchBy.Id, UserRegistration.Android.UR_Dialogs.Enter_Digit);
                control.Clear();
                control.SetText(digit);
            }
            else if (edt == EditBox.Year)
            {
                IMobilePageControl control = _instance.GetElement(SearchBy.Id, UserRegistration.Android.UR_Dialogs.Enter_Digit);
                control.Clear();
                control.SetText(digit);
            }

        }


        /// <summary>
        /// verifying entered Age and year
        /// </summary>
        /// <param name="edt"></param>
        /// <param name="digit"></param>
        /// <returns></returns>
        public static bool Verify_EnteredDigit(EditBox edt, string digit)
        {
            bool isTrue = false;
            if (edt == EditBox.Age)
            {
                string age = _instance.GetElement(SearchBy.Id, UserRegistration.Android.UR_Dialogs.Picker_Text).Text;
                if (age == digit)
                {
                    isTrue = true;
                }
            }
            else
            {
                string age = _instance.GetElement(SearchBy.Id, UserRegistration.Android.UR_Dialogs.Picker_text_birth).Text;
                if (age == digit)
                {
                    isTrue = true;
                }
            }
            return isTrue;
        }

        /// <summary>
        /// waiting for picker dialog
        /// </summary>
        /// <returns></returns>
        public static bool WaitforAge_yearMismatchDialog()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, UserRegistration.Android.UR_Dialogs.Age_YearMismatch);
                loopCount++;
                if (homeScreenElement != null)
                    break;
                if (loopCount > 5)
                    break;
            }
            if (homeScreenElement != null)
                return true;
            else
                return false;
        }
    }
}
