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

namespace Philips.SIG.Automation.Android.CDPP.AppFramework_TestPlugin
{
   
     public class WelcomeScreen
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
            RightArrow,
            Skip,
            LeftArrow,
            Done,
            Alogout

        }

        public static void Click(Button btn)
        {
            if (btn == Button.Skip)
                _instance.ClickById(AppFrameWork.Android.HomeScreen.Skip);
            else if (btn == Button.LeftArrow)
                _instance.ClickById(AppFrameWork.Android.HomeScreen.LeftArrow);
            else if (btn == Button.RightArrow)
                _instance.ClickById(AppFrameWork.Android.HomeScreen.RightArrow);
            else if (btn == Button.Done)
                _instance.ClickById(AppFrameWork.Android.HomeScreen.Done);
        }

     

        public static bool WelcomeScreenImage()
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.Id, AppFrameWork.Android.HomeScreen.WelcomeScreen);
            if (control != null)
                return true;
            else
                return false;
           
        }

        public static bool Status()
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.Id, AppFrameWork.Android.HomeScreen.Alogout);
            if (control != null)
                return true;
            else
                return false;

        }

         public static bool CheckUserRegistration()
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.Id, AppFrameWork.Android.HomeScreen.PhilipsAccountReg);
            if (control != null)
                return true;
            else
                return false;
        }

         public static bool HomeScreenExist()
        {
            bool IsVisible1 = false;
            if (AppHomeScreen.GetScreenTitleAppFameworkHomeScreen() == "Mobile App Home")
                IsVisible1 = true;
            return IsVisible1;

        }


    

        public static bool IsVisible(Button btn)
        {
            bool IsVisible = false;
            try
            {
                if (btn == Button.Skip)
                    IsVisible = _instance.GetElement(SearchBy.Id, AppFrameWork.Android.HomeScreen.Skip).Displayed;
                else if (btn == Button.Done)
                    IsVisible = _instance.GetElement(SearchBy.Id, AppFrameWork.Android.HomeScreen.Done).Displayed;
                else if (btn == Button.LeftArrow)
                    IsVisible = _instance.GetElement(SearchBy.Id, AppFrameWork.Android.HomeScreen.LeftArrow).Displayed;
                else if (btn == Button.RightArrow)
                    IsVisible = _instance.GetElement(SearchBy.Id, AppFrameWork.Android.HomeScreen.RightArrow).Displayed;
                else if (btn == Button.Alogout)
                    IsVisible = _instance.GetElement(SearchBy.Id, AppFrameWork.Android.HomeScreen.Alogout).Displayed;

                IsVisible = true;
            }
            catch (Exception e)
            {
                IsVisible = false;
            }
            

            return IsVisible;

        }

        public static void SwipeLeft(int scrollcount)
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.Id, AppFrameWork.Android.HomeScreen.Swipe_Welcome);
            //int[] coords = new int[2];
            //coords = control.Coordinates;
            int[] sz = new int[2];
            sz = control.Size;
            int Srcx = sz[0] / 2;
            int Srcy = sz[1] / 2;
            double count = (double)Srcx;
            count = count - scrollcount;
            MobileDriver.Swipe((double)Srcx, (double)Srcy, count, (double)Srcy);
        }


        public static void SwipeRight(int scrollcount)
        {
            IMobilePageControl control = _instance.GetElement(SearchBy.Id, AppFrameWork.Android.HomeScreen.Swipe_Welcome);
            int[] sz = new int[2];
            sz = control.Size;
            int Srcx = sz[0] / 4;
            int Srcy = sz[1] / 4;
            double count = (double)Srcx;
            count = count + scrollcount;
            MobileDriver.Swipe((double)Srcx, (double)Srcy, count, (double)Srcy);
        }

        public static int CarouselNumber()
        {
            IMobilePageControl indicator = _instance.GetElement(null, SearchBy.Id, AppFrameWork.Android.HomeScreen.CarouselHighlighted);
            List<IMobilePageControl> dots = _instance.GetElements(indicator, SearchBy.ClassName, "android.view.View");
            int i = dots.Count();
            return i;
        }

        public static int CarouselHighlighted()
        {
            IMobilePageControl indicator = _instance.GetElement(null, SearchBy.Id, AppFrameWork.Android.HomeScreen.CarouselHighlighted);
            List<IMobilePageControl> dots = _instance.GetElements(indicator, SearchBy.ClassName, "android.view.View");
            int i = dots.Count();



            for (int x = 0; x < i; x++)
            {
                try
                {
                    string a = dots[x].GetAttribute("clickable");
                    if (a.Equals("false"))
                    {
                        Logger.Info("Verify that the dot which is highlighted indicates me on which screen number I am at");
                        return x;
                    }
                }
                catch (Exception e)
                {
                    Logger.Info("dots: Exception");
                }


            }
            return -1;
        }

    }

}

