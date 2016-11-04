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
using Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin;

namespace ProdReg.Android.TestPlugin
{
    public class PRHome
    {
        static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();
        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }


        }

        /// <summary>
        /// This class provides all the functionalities related to HomeScreen.
        /// </summary>
        public enum Button
        {
            PR_shopicon,
            PR_Hamburgericon,
            PRRegister,
            DatePurchase
        }

        public static bool IsVisible(Button btn)
        {
            bool isVisble = false;
            try
            {
                if (btn == Button.PRRegister)
                    isVisble = _instance.GetElement(SearchBy.Name, ProductRegistration.Android.HomeScreen.PRRegister).Displayed;
                else if (btn == Button.PR_Hamburgericon)
                    isVisble = _instance.GetElement(SearchBy.Name, ProductRegistration.Android.HomeScreen.PR_Hamburgericon).Displayed;
                else if (btn == Button.PR_shopicon)
                    isVisble = _instance.GetElement(SearchBy.Name, ProductRegistration.Android.HomeScreen.PR_shopicon).Displayed;
                else if (btn == Button.DatePurchase)
                    isVisble = _instance.GetElement(SearchBy.Name, ProductRegistration.Android.HomeScreen.DatePurchase).Displayed;

            }
            catch (Exception e)
            {
                isVisble = false;
            }
            return isVisble;
        }

        public static void Click(Button btn)
        {
            if (btn == Button.PR_Hamburgericon)
                _instance.ClickById(ProductRegistration.Android.HomeScreen.PR_Hamburgericon);
            else if (btn == Button.PR_shopicon)
                _instance.ClickById(ProductRegistration.Android.HomeScreen.PR_shopicon);
            else if (btn == Button.PRRegister)
                _instance.ClickById(ProductRegistration.Android.HomeScreen.PRRegister);
            else if (btn == Button.DatePurchase)
            {
                IMobilePageControl cnt = _instance.GetElement(SearchBy.ClassName, "android.widget.EditText");
                cnt.Click();
                cnt.Click();
            }
        }


        public static string GetHeaderTextPR()
        {
            return _instance.GetTextById(ProductRegistration.Android.HomeScreen.PRheader);
        }

        public static string GetCTN()
        {
            return _instance.GetTextById(ProductRegistration.Android.HomeScreen.AuroraCTN);
        }

        public static bool IsHeaderVisible()
        {
            bool bVisible = false;
            if (GetHeaderTextPR() == "Register your product")
                bVisible = true;
            return bVisible;

        }

        public static bool IsCTNVisible()
        {
            bool bVisible = false;
            if (GetCTN() == "HX6064/33")
                bVisible = true;
            return bVisible;

        }

        /// <summary>
        /// Waiting for screen
        /// </summary>
        /// <returns></returns>
        public static bool WaitforProdRegHomeScreen()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ProductRegistration.Android.HomeScreen.PRheader);
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

        public static void SelectDate(string date, string month, string year)
        {
            List<IMobilePageControl> cnt7 = _instance.GetElements(SearchBy.ClassName, "android.view.View");
                       
            for (var i = 1; i < cnt7.Count; i++)
            {
                try
                {

                    string a = cnt7[i].GetAttribute("name");


                    // System.Diagnostics.Debug.WriteLine(a);

                    string[] s = a.Split(' ');
                    if ( s[2] == year && s[1] == month)
                    {
                        //string str1 = s[0].TrimStart('0');
                        List<IMobilePageControl> cnt2 = _instance.GetElements(SearchBy.ClassName, "android.view.View");
                        for (var m = 1; m < cnt2.Count; m++)
                        {
                            string a1 = cnt2[m].GetAttribute("name");
                            string str1 = a1.Substring(0, 2);
                            str1 = str1.TrimStart('0');

                            if (str1 == date)
                            {
                                cnt2[m].Click();
                                _instance.GetElement(SearchBy.Id, ProductRegistration.Android.HomeScreen.PROK).Click();
                                break;
                            }

                        }
                        break;
                    }
                    else
                    {
                        SwipeCalender();
                    }
                }
                catch (Exception e)
                {
                    System.Diagnostics.Debug.WriteLine(i);

                }

            }
        }

        public static void SwipeCalender()
        {

            int[] cnt = _instance.GetElement(SearchBy.ClassName, "android.widget.ListView").Coordinates;
            int[] size = _instance.GetElement(SearchBy.ClassName, "android.widget.ListView").Size;
            int x1 = cnt[0] + 200;
            int y1 = cnt[1] + 100;
            int y2 = (cnt[1] + size[0] / 2 + 65);
            MobileDriver.Swipe(x1, y1, x1, y2);
        }

    }
}
