using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System.Collections.Generic;

using System.Linq;

namespace Philips.SIG.Automation.Android.CDPP.ConsumerCareTestPlugin
{
    public class FAQ
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
            Back_to_HomeFAQPage,
            
        }

        /// <summary>
        /// getting header text
        /// </summary>
        /// <returns></returns>
        public static string GetHeaderTextFAQ()
        {
            return _instance.GetTextById(ConsumerCare.Android.FAQ.FAQHeaderText);
        }
        public static bool IsVisible1()
        {
            bool bVisible = false;
            if (GetHeaderTextFAQ() == "FAQs")
                bVisible = true;
            return bVisible;

        }

        /// <summary>
        /// waiting for FAQ home screen
        /// </summary>
        /// <returns></returns>
        public static bool WaitforFAQScreen()
        {
            int loopCount = 0;
            IMobilePageControl homeScreenElement = null;
            while (homeScreenElement == null)
            {
                homeScreenElement = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.FAQ.FAQHeaderText);
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
        /// checking whether btn visible or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsVisible(Button btn)
        {
            bool isVisible = false;
            if (btn == Button.Back_to_HomeFAQPage)
                isVisible = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.FAQ.BackToHomePage).Displayed;
            return isVisible;
        }

        

        /// <summary>
        /// checking btn enable or not
        /// </summary>
        /// <param name="btn"></param>
        /// <returns></returns>
        public static bool IsEnable(Button btn)
        {
            bool isEnable = false;
            if (btn == Button.Back_to_HomeFAQPage)
                isEnable = _instance.GetElement(SearchBy.Id, ConsumerCare.Android.FAQ.BackToHomePage).Enabled;
                return isEnable;
        }

       

        /// <summary>
        /// for Reliabilty
        /// </summary>
        public static void Click(string mainQuestion)
        {
            List<IMobilePageControl> elements = new List<IMobilePageControl>();
            elements = _instance.GetElements(SearchBy.ClassName, "android.widget.TextView");
            foreach (IMobilePageControl cnt in elements.Skip(1))
            {
                string str = cnt.Text;
                if (str == mainQuestion)
                {
                    _instance.ClickByName(str);
                    break;
                    //_instance.ClickByName(str);
                }
            }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="heading"></param>
        /// <param name="subQuestion"></param>
        public static void Click(string heading, string subQuestion)
        {
            List<IMobilePageControl> elements = new List<IMobilePageControl>();
            elements = _instance.GetElements(SearchBy.ClassName, "android.widget.TextView");
            foreach (IMobilePageControl cnt in elements.Skip(1))
            {
                string str = cnt.Text;
                if (str == heading)
                {
                    _instance.ClickByName(str);
                    break;
                }
            }
            FAQ.ClickSubQustn(subQuestion);
        }

        public static void ClickSubQustn(string subQuestion)
        {
            int count = 0;
            List<IMobilePageControl> elements1 = new List<IMobilePageControl>();
            elements1 = _instance.GetElements(SearchBy.ClassName, "android.widget.TextView");
            foreach (IMobilePageControl cnt1 in elements1.Skip(1))
            {
                string str = cnt1.Text;
                if (str.Contains(subQuestion))
                {
                    count = 1;
                    _instance.ClickByName(str);
                    Question_and_Answer.WaitforQAScreen();
                    break;
                }
            }
            if(count != 1)
            {
                Scroll();
                ClickSubQustn(subQuestion);
            }
        }
          

        /// <summary>
        /// Scrool function
        /// </summary>
        public static void Scroll()
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.ClassName, "android.widget.ScrollView");
            int[] coords = new int[2];
            coords = control.Coordinates;
            int[] sz = new int[2];
            sz = control.Size;
            int Srcx = coords[0] + sz[0] / 2;
            int Srcy = coords[1] + sz[1] / 2;
            MobileDriver.Swipe((double)Srcx, (double)Srcy, (double)Srcx, (double)Srcy - 300);
        }


        /// <summary>
        /// Clicking back_home button
        /// </summary>
        /// <param name="btn"></param>
        public static void ClickBack(Button btn)
        {
            if (btn == Button.Back_to_HomeFAQPage)
                _instance.ClickById(ConsumerCare.Android.FAQ.BackToHomePage);
        }
      
    }
}
