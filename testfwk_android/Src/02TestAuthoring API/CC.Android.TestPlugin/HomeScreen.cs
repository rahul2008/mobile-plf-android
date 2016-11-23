using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Collections.Generic;

namespace Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin
{
    public class HomeScreen
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
        /// This class provides all the functionalities related to Home page.
        /// </summary>
        public enum Button
        {
            Launch_DC_as_Activity,
            Launch_Dc_as_Fragment,
            AddCTN
        }

        public enum Fields
        {
            Country_Spinner,
            Langauage
        }

        public enum Language
        {
            English
        }

        public enum Country
        {
            United_Kingdom,
            India,
            United_States
        }

        
        /// <summary>
        /// Checks if a button is Visble or not
        /// </summary>
        public static bool IsVisible(Button btn)
        {
            bool isVisible = false;
            try
            {
                if (btn == Button.Launch_DC_as_Activity)
                    isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.HomeScreen.LaunchasActivity).Displayed;
                else if (btn == Button.Launch_Dc_as_Fragment)
                    isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.HomeScreen.LaunchasFragment).Displayed;
                else if (btn == Button.AddCTN)
                    isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.HomeScreen.AddCTN).Displayed;
            }
            catch (Exception e)
            {
                isVisible = false;
            }
            return isVisible;
        }
        /// <summary>
        /// Checks if a button is Enabled or not
        /// </summary>
        public static bool IsEnabled(Button btn)
        {
            bool isEnabled = false;
            try
            {
                if (btn == Button.Launch_DC_as_Activity)
                    isEnabled = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.HomeScreen.LaunchasActivity).Enabled;
                else if (btn == Button.Launch_Dc_as_Fragment)
                    isEnabled = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.HomeScreen.LaunchasFragment).Enabled;
            }
            catch(Exception e)
            {
                isEnabled = false;
            }
            return isEnabled;
            
        }
        

        /// <summary>
        /// Checks if a button is clickable or not
        /// </summary>
        /// <param name="btn"></param>
        public static void Click(Button btn)
        {
            if (btn == Button.Launch_DC_as_Activity)
                _instance.ClickById(ConsumerCare.Android.HomeScreen.LaunchasActivity);
            else if (btn == Button.Launch_Dc_as_Fragment)
                _instance.ClickById(ConsumerCare.Android.HomeScreen.LaunchasFragment);
            else if (btn == Button.AddCTN)
            {
                bool bVisible = false;
                bVisible = IsVisible(Button.AddCTN);
                if (bVisible)
                {
                    _instance.ClickById(ConsumerCare.Android.HomeScreen.AddCTN);
                }
                else
                {
                    IMobilePageControl control = _instance.GetElement(SearchBy.ClassName, "android.widget.ScrollView");
                    int[] coords = new int[2] ;
                    coords=control.Coordinates;
                    int[] sz = new int[2];
                    sz = control.Size;
                    int Srcx=coords[0] + sz[0] / 2;
                    int Srcy = coords[1] + sz[1] / 2;
                    double count=(double)Srcy;
                    while (!(IsVisible(Button.AddCTN)))
                    {
                        count = count - 100;
                        MobileDriver.Swipe((double)Srcx, (double)Srcy, (double)Srcx, (count) );
                        bVisible = IsVisible(Button.AddCTN);
                         if (bVisible)
                              break;
                    }                  
                        _instance.ClickById(ConsumerCare.Android.HomeScreen.AddCTN);
                }

            }
        }

        public static void Scroll_CountrySpinner()
        {
            //_instance.ClickById(ConsumerCare.Android.HomeScreen.Country_Spinner);
            IMobilePageControl con = _instance.GetElement(SearchBy.ClassName, "android.widget.ListView");
            int[] coords = new int[2];
            coords = con.Coordinates;
            int[] sz = new int[2];
            sz = con.Size;
            int Srcx = coords[0] + sz[0] / 2;
            int Srcy = coords[1] + sz[1] / 2;
            MobileDriver.Swipe((double)Srcx, (double)Srcy, (double)Srcx, (double)Srcy + 400);
        }  

        /// <summary>
        /// Selecting Countries
        /// </summary>
        /// <param name="flds"></param>
        /// <param name="menuItem"></param>
        public static void SelectCountry(Fields flds, Country menuItem)
        {
            if (flds == Fields.Country_Spinner)
            {
                _instance.ClickById(ConsumerCare.Android.HomeScreen.Country_Spinner);
                Scroll_CountrySpinner();
                if (menuItem == Country.United_Kingdom)
                {
                    _instance.ClickByName("United Kingdom (GB)");
                }
                else if (menuItem == Country.India)
                {
                    _instance.ClickByName("India (IN)");
                }
                else if (menuItem == Country.United_States)
                {
                    _instance.ClickByName("United States (US)");
                }
            }

        }
       
        /// <summary>
        /// Selecting Language
        /// </summary>
        /// <param name="fld"></param>
        /// <param name="lang"></param>
        public static void SelectLanguage( Fields fld, Language lang)
        {
            if(fld == Fields.Langauage)
            {
                _instance.ClickById(ConsumerCare.Android.HomeScreen.Language);
                if (lang == Language.English)
                {
                    _instance.ClickByName("English (en)");
                }
            }

        }

        /// <summary>
        /// waiting for homescreen to open
        /// </summary>
        /// <returns></returns>
        public static bool WaitforHomeScreen()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.HomeScreen.LaunchasActivity);
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
        /// checking whether selected language displayed or not
        /// </summary>
        /// <returns>boolean value</returns>
        public static string GetLanguageText()
        {
            string language = _instance.GetTextById(ConsumerCare.Android.HomeScreen.LanguageTitle);
            return language;
        }
        public static bool IsVisible()
        {
            bool bVisible = false;
            if (GetLanguageText() == "English (en)")
                bVisible = true;
            return bVisible;
        }

        /// <summary>
        /// Checking whether selected country displayed or not
        /// </summary>
        /// <returns>boolean value</returns>
        public static string GetCountryText()
        {
            string country = string.Empty;
            List<IMobilePageControl> elements = new List<IMobilePageControl>();
            elements = _instance.GetElements(SearchBy.Id, "android:id/text1");
            foreach (IMobilePageControl elem in elements)
            {
                country = elem.Text;
            }
             return country;       
        }
        public static bool IsVisible1()
        {
            bool bVisible = false;
            if (GetCountryText() == "United Kingdom (GB)")
                bVisible = true;
            else if (GetCountryText() == "India (IN)")
                bVisible = true;
            else if (GetCountryText() == "United States (US)")
                bVisible = true;
            return bVisible;
        }

        /// <summary>
        /// Deleting CTN's//Only for Relaibility
        /// </summary>
        /// <param name="str"></param>
        public static void Delete_CTNforReliability()
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.ClassName, "android.support.v7.widget.RecyclerView");
            int[] coords = new int[2];
            coords = control.Coordinates;
            int[] sz = new int[2];
            sz = control.Size;
            int Srcx = coords[0] + sz[0] / 2;
            int Srcy = coords[1] + sz[1] / 9;
            double count = (double)Srcx;
            count = count - 430;
            MobileDriver.Swipe((double)Srcx, (double)Srcy, count, (double)Srcy);
        }

        /// <summary>
        /// To delete particular CTN
        /// </summary>
        /// <param name="str"></param>
        /// <param name="scrollCount"></param>
        public static void Delete_CTN(string str, int scrollCount)
        {
            int count = 0;
            List<IMobilePageControl> control = _instance.GetElements(SearchBy.Id, "com.philips.cl.di.dev.pa:id/ctn_name");
            foreach (IMobilePageControl cnt in control)
            {
                string strg = cnt.Text;
                if (strg == str)
                {
                    count = 1;
                    int[] coords = new int[2];
                    coords = cnt.Coordinates;
                    int Srcx = coords[0];
                    int Srcy = coords[1];
                    MobileDriver.Swipe((double)Srcx, (double)Srcy, (double)Srcx - 384, (double)Srcy);
                    break;
                }
            }
            if (count != 1)
            {
                HomeScreen.Scroll(scrollCount);
                HomeScreen.Delete_CTN(str, scrollCount);
            }
        }

        public static void Scrollagain()
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.ClassName, "android.widget.ScrollView");
            int[] coords = new int[2];
            coords = control.Coordinates;
            int[] sz = new int[2];
            sz = control.Size;
            int Srcx = coords[0] + sz[0] / 2;
            int Srcy = coords[1] + sz[1] / 2;
            MobileDriver.Swipe((double)Srcx, (double)Srcy, (double)Srcx, (double)Srcy + 400);

        }


        /// <summary>
        /// verifying the CTN is deleted or what
        /// </summary>
        /// <returns></returns>
        public static bool Deleted_product(string str)
        {
            string ctn = string.Empty;
            List<IMobilePageControl> elements = new List<IMobilePageControl>();
            elements = _instance.GetElements(SearchBy.Id, "com.philips.platform.referenceapp:id/ctn_name");
            foreach (IMobilePageControl elem in elements)
            {
                ctn = elem.Text;
                if (ctn == str)
                    break;
            }
            if (ctn == str)
                return false;
            else
                return true;
        }


        
        public static void Scroll(int value)
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.ClassName, "android.support.v7.widget.RecyclerView");
            int[] coords = new int[2];
            coords = control.Coordinates;
            int[] sz = new int[2];
            sz = control.Size;
            int Srcx = coords[0] + sz[0] / 2;
            int Srcy = coords[1] + sz[1] / 2;
            double count = (double)Srcy;
            count = count - value;//(625);
            MobileDriver.Swipe((double)Srcx, (double)Srcy, (double)Srcx, (count));
        }

        /// <summary>
        /// verfying step whether new product added or what
        /// </summary>
        //public static void VerifyAddedProduct(int scrollCount)
        //{
        //    int count = 0;
        //    HomeScreen.Scrollagain();
        //    HomeScreen.Scroll(scrollCount);
        //    string ctn = string.Empty;
        //    List<IMobilePageControl> elements = new List<IMobilePageControl>();
        //    elements = _instance.GetElements(SearchBy.Id, "com.philips.cdp.sampledigitalcare:id/ctn_name");
        //    foreach (IMobilePageControl elem in elements)
        //    {
        //        ctn = elem.Text;
        //        if (ctn == "RQ1250/19")
        //        {
        //            count = 1;
        //            break;
        //        }
        //    }
        //    if(count != 1)
        //    {
        //        VerifyAddedProduct(scrollCount);
        //    }   
        //}
        public static void VerifyAddedProduct(string str, int scrollCount)
        {
            int count = 0;
            //HomeScreen.Scrollagain();
            
            string ctn = string.Empty;
            List<IMobilePageControl> elements = new List<IMobilePageControl>();
            elements = _instance.GetElements(SearchBy.Id, "com.philips.platform.referenceapp:id/ctn_name");
            foreach (IMobilePageControl elem in elements)
            {
                ctn = elem.Text;
                if (ctn == str)
                {
                    count = 1;
                    break;
                }
            }
            if(count != 1)
            {
                HomeScreen.Scroll(scrollCount);
                VerifyAddedProduct(str, scrollCount);
            }   
        }


        public static void SelectCountry(Fields fields, string country)
        {
            throw new NotImplementedException();
        }

        public static List<string> VisibleCTN()
        {
            List<IMobilePageControl> elements = new List<IMobilePageControl>();
            elements = _instance.GetElements(SearchBy.Id, "com.philips.platform.referenceapp:id/ctn_name");
            int count = 0;
            int CTNCount = elements.Count;
            List<string> CTN = new List<string>();
            foreach (IMobilePageControl elem in elements)
            {
                string str = elem.Text;
                count++;
                CTN.Add(str);
                if (count == CTNCount)
                    break;
            }

            return CTN;
        }

    }
}
