/* 
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 */
using System;
using System.Collections.ObjectModel;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using OpenQA.Selenium.Appium;
using OpenQA.Selenium;
using System.Threading;
using OpenQA.Selenium.Appium.Android;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.H2H.Foundation.AutomationCore.Interface;
using System.Configuration;

namespace Philips.H2H.Foundation.AutomationCore
{
    public sealed class MobilePageInstance
    {
        static private AndroidDriver driver;
        static private IWebDriver iOSDriver;
        static private MobilePageInstance instance = null;
        static private CommonDriverFunction cmonObj = new CommonDriverFunction();

        public enum MobileAttribute
        {
            Id, ClassName, ContentDesc, Name, Text, Enabled, Checkable, Clickable,
            Focusable, LongClickable, Scrollable, Selected, Checked, Displayed, Focused
        }

        private MobilePageInstance()
        {
        }

        //single ton instance of Mobile Element
        public static MobilePageInstance Instance
        {
            set
            {
                if (instance != null)
                {
                    instance = value;
                }
            }
            get
            {
                if (instance == null)
                {
                    if (ConfigurationManager.AppSettings["MobilePlatformForTest"].Equals("Android"))
                    {
                        if (MobileDriver._Mdriver == null)
                        {
                            Logger.Fail("Appium Driver is not yet set through Mobile Driver.");
                        }
                        driver = MobileDriver._Mdriver;
                    }
                    else if (ConfigurationManager.AppSettings["MobilePlatformForTest"].Equals("iOS"))
                    {
                        if (MobileDriver.iOSdriver == null)
                        {
                            Logger.Fail("Appium Driver is not yet set through Mobile Driver.");
                        }
                        iOSDriver = MobileDriver.iOSdriver;
                    }
                    instance = new MobilePageInstance();
                }
                return instance;
            }
        }

        public IMobilePageControl GetElement(SearchBy by, string clause)
        {
            AppiumWebElement elem = null;
            IWebElement iOSelem = null;
            if (ConfigurationManager.AppSettings["MobilePlatformForTest"].Equals("Android"))
            {
                elem = (AppiumWebElement)cmonObj.GetElement(driver, null, by, clause);
                if (elem != null)
                {
                    IMobilePageControl mPageControl = new MobilePageControl(elem);
                    return mPageControl;
                }
                else
                    return null;
            }
            else
            {
                iOSelem = cmonObj.GetElement(iOSDriver, null, by, clause);
                if (iOSelem != null)
                {
                    IMobilePageControl mPageControl = new MobilePageControl(iOSelem);
                    return mPageControl;
                }
                else
                    return null;
            }


        }

        public IMobilePageControl GetElement(IMobilePageControl parent, SearchBy by, string clause)
        {
            AppiumWebElement elem = null;
            if (ConfigurationManager.AppSettings["MobilePlatformForTest"].Equals("Android"))
            {
                elem = (AppiumWebElement)cmonObj.GetElement(driver, parent, by, clause);
            }
            else
            {
                elem = (AppiumWebElement)cmonObj.GetElement(iOSDriver, parent, by, clause);
            }
            if (elem != null)
            {
                IMobilePageControl mPageControl = new MobilePageControl(elem);               
                return mPageControl;
            }
            else return null;
        }

        public IMobilePageControl GetElementByUiAutomator(string selector)
        {
            AppiumWebElement elem = (AppiumWebElement)(driver.FindElementByAndroidUIAutomator(selector));
            if (elem != null)
            {
                return new MobilePageControl(elem);
            }
            else return null;
        }

        public IMobilePageControl GetElementByText(string text)
        {

            AppiumWebElement elem = (AppiumWebElement)cmonObj.GetElement
                (driver, null, SearchBy.Xpath, "//android.widget.TextView[@text='" + text + "']");
            if (elem != null)
            {
                return new MobilePageControl(elem);
            }
            else return null;
        }

        public List<IMobilePageControl> GetElements(IMobilePageControl parent, SearchBy by, string byClause)
        {
            if (ConfigurationManager.AppSettings["MobilePlatformForTest"].Equals("Android"))
            {
                return GetMobileElements(cmonObj.GetElements(driver, parent, by, byClause));
            }
            else
            {
                return GetMobileElements(cmonObj.GetElements(iOSDriver, parent, by, byClause));
            }
        }

        public List<IMobilePageControl> GetElements(SearchBy by, string byClause)
        {
            if (ConfigurationManager.AppSettings["MobilePlatformForTest"].Equals("Android"))
            {
                return GetMobileElements(cmonObj.GetElements(driver, null, by, byClause));
            }
            else
            {
                return GetMobileElements(cmonObj.GetElements(iOSDriver, null, by, byClause));
            }
            
        }

        public List<IMobilePageControl> GetElementsByUiAutomator(string selector)
        {
            return GetMobileElements(driver.FindElementsByAndroidUIAutomator(selector));
        }

        public void ClickByName(string sName)
        {
            Logger.Debug("Clicking on Text : " + sName);
            try
            {
                cmonObj.GetElement(driver, null, SearchBy.Name, sName).Click();
            }
            catch (Exception)
            {                
                throw new Exception("Could not find page control to click with Id: " + sName);
            }
        }

        public void ClickById(string sId)
        {
            Logger.Debug("Clicking on Element By Id : " + sId);
            try
            {
                GetElement(SearchBy.Id, sId).Click();

            }
            catch (Exception)
            {                
               // throw new Exception("Could not find page control to click with Id: " + sId);
            }
        }

        public void ClickByText(string sText)
        {
            Logger.Debug("Clicking on Element By Text : " + sText);
            try
            {
                GetElementByText(sText).Click();

            }
            catch (Exception)
            {                
                throw new Exception("Could not find page control to click with Text: " + sText);
            }
        }

        public void ClickOnButton(string btnText)
        {
            Logger.Debug("Clicking on Mobile Button : " + btnText);
            List<IMobilePageControl> allButtons = GetElements(SearchBy.ClassName, "android.widget.Button");
            try
            {
                allButtons.Find(d => d.Text.Equals(btnText)).Click();

            }
            catch (Exception)
            {
                throw new Exception("Android Button with Text: [" + btnText + "] could not be found in the screen");
            }            
        }

        public IMobilePageControl GetElementNoWait(IMobilePageControl parent, SearchBy by, string clause)
        {
            AppiumWebElement elem = (AppiumWebElement)cmonObj.FindElementNoWait(driver, parent, by, clause);
            if (elem != null)
            {
                IMobilePageControl mPageControl = new MobilePageControl(elem);              
                return mPageControl;
            }
            else return null;
        }       

        public IMobilePageControl GetElementNoScreenshot(IMobilePageControl parent, SearchBy by, string clause)
        {
            AppiumWebElement elem = (AppiumWebElement)cmonObj.FindElementNoScreenshot(driver, parent, by, clause);
            if (elem != null)
            {
                IMobilePageControl mPageControl = new MobilePageControl(elem);               
                return mPageControl;
            }
            else return null;
        }

        public string GetTextById(string sId)
        {
            Logger.Debug("Trying to Fetch text from the Element with Resource Id: " + sId);
            return GetElement(SearchBy.Id, sId).Text;
        }

        public bool isTextPresent(string text)
        {
            IMobilePageControl elem = (IMobilePageControl)GetElement(SearchBy.Name, text);
            if (elem == null)
            {
                return false;
            }
            else
            {
                return elem.Displayed;
            }
        }        

        private List<IMobilePageControl> GetMobileElements(ReadOnlyCollection<IWebElement> elements)
        {
            List<IMobilePageControl> list = new List<IMobilePageControl>();
            foreach (IWebElement elem in elements)
            {
                list.Add(new MobilePageControl(elem));
            }
            return list;
        }

        public string ClickOnSwitch(int index)
        {
            string status = null;
            List<IMobilePageControl> allButtons = GetElements(SearchBy.ClassName, "android.widget.Switch");
            allButtons[index].Click();
            status = allButtons[index].Text;
            return status;
        }

        public IMobilePageControl GetElementByXpath(string xpath)
        {
            Logger.Debug("Trying to Fetch text from the Element with xpath: " + xpath);
            return GetElement(SearchBy.Xpath, xpath);
        }
        public String GetSource()
        {
            return driver.PageSource;
        }

        public void SwitchTo()
        {
            try {
                    Logger.Info("windowhandles");
                    foreach(IWebElement we in driver.FindElements(By.XPath("//*[contains(@text,'Info')]")))
                    {
                        Logger.Info(we.ToString());
                    }
                    driver.FindElementByXPath("//*[contains(@text,'Info')]").Click();
                }
            catch(Exception e)
            {
                Logger.Info("findelements failed");
            }
        }

        public System.Drawing.Size GetScreenSize()
        {
            AppiumWebElement elem = null;
            IWebElement iOSelem = null;
            if (ConfigurationManager.AppSettings["MobilePlatformForTest"].Equals("Android"))
            {
                return driver.Manage().Window.Size;
            }
            else
            {
                return iOSDriver.Manage().Window.Size;
            }
        }
    }
}

