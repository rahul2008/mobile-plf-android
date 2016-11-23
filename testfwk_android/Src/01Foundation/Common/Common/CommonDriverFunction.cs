/* 
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 */
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Configuration;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using OpenQA.Selenium;
using OpenQA.Selenium.Appium;
using OpenQA.Selenium.Appium.Android;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Browser;

namespace Philips.H2H.Foundation.AutomationCore.Common
{
    class CommonDriverFunction
    {
        SelectorBy byObj = new SelectorBy();
        #region DriverFunctions
        public string GetPageSource(IWebDriver driver)
        {
            return driver.PageSource;
        }

        public string GetPageTitle(IWebDriver driver)
        {
            return driver.Title;
        }

        public void SwitchToFrame(IWebDriver driver, object frame)
        {
            Type type = frame.GetType();
            if (frame is string)
                driver.SwitchTo().Frame((string)frame);
            else if (frame is BrowserPageControl)
                driver.SwitchTo().Frame(((BrowserPageControl)frame).ElementInstance);
            else if (frame is int)
                driver.SwitchTo().Frame((Int32)frame);
            else
                throw new Exception("Browser frame object can be integer/IWebelement/string");
        }

        public void Quit(IWebDriver driver)
        {
            if (driver is AndroidDriver)
            {
                driver.Quit();
            }
            else if (driver is IWebDriver)
            {
                driver.Quit();
            }
            else
            {
                throw new Exception("Driver is not valid driver object.");
            }
        }

        public void TakeScreenshot(IWebDriver driver, string fileName)
        {
            string tkScrnshot = ConfigurationManager.AppSettings["TK_SCRNSHOT"];
            if (tkScrnshot.Equals("true"))
            {
                string dir = AutomationConstants.LOG_PATH + "Screenshots\\";
                if (!Directory.Exists(dir))
                {
                    Directory.CreateDirectory(dir);
                }
                string filePath = string.Empty;
                if (driver is AndroidDriver)
                {
                    if (fileName.Equals(""))
                    {
                        filePath = dir + MobileDriver.CurrentActivity.Replace('.', '_') + "_" + UtilityAuto.GetUniqueNumber() + ".jpeg";
                    }
                    else
                    {
                        filePath = dir + fileName + ".jpeg";
                    }
                    try
                    {
                        ((AndroidDriver)driver).GetScreenshot().SaveAsFile(filePath, System.Drawing.Imaging.ImageFormat.Jpeg);
                    }
                    catch (Exception)
                    {
                        Logger.Debug("Catching Exception while taking screenshot as in some initial page of eCC we can not take screenshots and"
                            + " we do not want to fail the test case because of that");
                    }
                }
                else if (driver is IWebDriver)
                {
                    if (fileName.Equals(""))
                    {
                        filePath = dir + BrowserDriver.PageTitle + "_" + UtilityAuto.GetUniqueNumber() + ".jpeg";
                    }
                    else
                    {
                        filePath = dir + fileName + ".jpeg";
                    }
                    Screenshot err = ((ITakesScreenshot)driver).GetScreenshot();
                    err.SaveAsFile(filePath, System.Drawing.Imaging.ImageFormat.Jpeg);
                }
                Logger.Debug("Screenshot captured and File Name: " + filePath);
            }
        }

        public WindowSize GetWindowSize(IWebDriver driver)
        {
            WindowSize size = new WindowSize();
            size.Height = driver.Manage().Window.Size.Height;
            size.Width = driver.Manage().Window.Size.Width;
            return size;
        }
        #endregion DriverFunctions

        #region ElementFunctions
        private IWebElement checkElement(IWebDriver driver, IPageControl parent, By by)
        {
            //TODO - Wait for element to appear
            IWebElement elem = null;
            try
            {
                if (parent != null)
                {
                    elem = parent.ElementInstance.FindElement(by);
                }
                else
                {
                    elem = driver.FindElement(by);
                }
                Logger.Debug("Found Requested Element: " + by.ToString() + " in the page.");
            }
            catch (Exception)
            {                
                Logger.Debug("Element not found, retrying again...: "+by.ToString());
            }
            return elem;
        }
       
        public IWebElement FindElementNoScreenshot(IWebDriver driver, IPageControl parent, SearchBy by, string clause)
        {
            int ctr = (AutomationConstants.WAIT_TIME * 1000) / AutomationConstants.PING_TIME;
            int i = 0;
            IWebElement elem = checkElement(driver, parent, byObj.GetByClause(by, clause));
            while (i < ctr)
            {
                UtilityAuto.ThreadSleep(AutomationConstants.PING_TIME);
                elem = checkElement(driver, parent, byObj.GetByClause(by, clause));
                if (elem != null)
                {
                    break;
                }
                i++;
            }
            return elem;
        }

        public IWebElement FindElement(IWebDriver driver, IPageControl parent, By by)
        {            
            int ctr = (AutomationConstants.WAIT_TIME * 1000) / AutomationConstants.PING_TIME;
            int i = 0;
            IWebElement elem = checkElement(driver,parent, by);
            while (i < ctr && elem == null)
            {
                UtilityAuto.ThreadSleep(AutomationConstants.PING_TIME);
                elem = checkElement(driver,parent, by);
                i++;
            }
            if (i == ctr && elem != null)
            {
                string fileName = string.Empty;
                if (driver is IWebDriver)
                {
                    TakeScreenshot(BrowserDriver._Bdriver, "");
                }
                else if (driver is AndroidDriver)
                {
                    TakeScreenshot(MobileDriver._Mdriver, "");
                }
                Logger.Debug("Could not find the element:(" + by.ToString() + ") after multiple retry in : 30 seconds");
            }
            return elem;
        }

        public IWebElement FindElementNoWait(IWebDriver driver, IPageControl parent, SearchBy by, string clause)
        {
            return checkElement(driver, parent, byObj.GetByClause(by, clause));
        }  

        public IWebElement GetElement(IWebDriver driver, IPageControl parent, SearchBy by, string clause)
        {
            By byClause = byObj.GetByClause(by, clause);
            return FindElement(driver, parent, byClause);            
        }

        public ReadOnlyCollection<IWebElement> GetElements(IWebDriver driver, IPageControl parent, SearchBy by, string clause)
        {
            By byClause = byObj.GetByClause(by, clause);

            if (parent != null)
            {
                return parent.ElementInstance.FindElements(byClause);
            }
            else
            {
                return driver.FindElements(byClause);
            }
        }
    }
        #endregion ElementFunctions
}


