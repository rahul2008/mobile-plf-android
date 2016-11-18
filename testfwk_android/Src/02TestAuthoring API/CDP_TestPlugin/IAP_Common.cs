using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using OpenQA.Selenium.Appium;
using OpenQA.Selenium.Appium.Android;
using OpenQA.Selenium.Appium.MultiTouch;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Drawing;
using OpenQA.Selenium;
using Philips.MRAutomation.Foundation.Common.ImageCompareLibrary;
using System.Configuration;
using System.Net;
using System.Net.Security;
using System.Threading;

namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin
{
    public class IAP_Common
    {
        public static MobilePageInstance Instance;
        public static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();

        /// <summary>
        /// An object of the type ObjectRepository
        /// </summary>
        public ObjectRepository objRep = new ObjectRepository();

        public static String GetPageTitle()
        {
            IMobilePageControl ScreenTitle = FindElement(new List<String> { ObjectRepository.DemoTitle, ObjectRepository.productScreen_title });
            return (null == ScreenTitle) ? "" : ScreenTitle.Text;
        }
        

        public static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }
        public static String country;

        public static String getCountry()
        {
            return country;
        }

        public static IMobilePageControl FindElement(List<String> elements)
        {
            int ctr = (AutomationConstants.WAIT_TIME * 1000) / AutomationConstants.PING_TIME;
            int i = 0;
            //IWebElement elem = checkElement(driver, parent, by);
            while (i < ctr)
            {
                foreach (String element in elements)
                {
                    try
                    {
                        IMobilePageControl wElement = _instance.GetElementNoWait(null, SearchBy.Id, element);
                        return wElement;
                    }
                    catch (Exception e)
                    {
                        Thread.Sleep(AutomationConstants.PING_TIME);
                    }

                }
                ctr++;
            }
            Logger.Debug("FindElement: did not find after waiting");
            return null;
        }

        public static void Back()
        {
            MobileDriver._Mdriver.Navigate().Back();
        }


        public static String GetScreenTitle()
        {
            List<String> ScreenTitleIDs = new List<String>();
            ScreenTitleIDs.Add(ObjectRepository.ScreenTitle);
            IMobilePageControl titleElem = null;
            foreach(String id in ScreenTitleIDs)
            {
                titleElem = _instance.GetElementNoWait(null, SearchBy.Id, id);
                if (null != titleElem) break;
            }

            return (titleElem == null) ? "" : titleElem.Text;
        }

        public static bool Busy()
        {
            //MobileDriver.TakeScreenshot("BusyStart.bmp");
            Logger.Info("Busy: Start");
            bool bVisible = true;
            while (bVisible == true)
            {
                Thread.Sleep(500);
                //if (_instance.GetElementByText("Please Wait...") == null)
                if (_instance.GetElementNoWait(null, SearchBy.Xpath, ObjectRepository.Busy) == null)
                    bVisible = false;
                else
                {
                    bVisible = true;
                }
            }
            Logger.Info("Busy: End");
            //MobileDriver.TakeScreenshot("BusyEnd.bmp");
            return bVisible;
        }

        //Reloading products screen by tapping back icon -> reach home screen -> Tap shop now again 
        public static bool ReLoadProductScreen()
        {
            Logger.Info("Products are not loaded : Reloading the page again");
            bool bVisible;
            ProductScreen.Click(ProductScreen.Button.productScreen_InfoButton);
            HomeScreen.WaitforHomeScreen();
            HomeScreen.Click(HomeScreen.Button.Shop_Now);
            bVisible = ProductScreen.WaitforProductScreen();
            if (bVisible == true)
            {
                Logger.Info("Products are reloaded in product screen");
                return true;
            }
            else
            {
                Logger.Info("On reloading also products are not loaded");
                return false;
            }
                
        }
    }
}
