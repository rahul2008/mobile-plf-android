/* 
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using log4net;
using OpenQA.Selenium;
using System.Threading;
using OpenQA.Selenium.Firefox;
using OpenQA.Selenium.Support.UI;
using System.Collections.ObjectModel;
using OpenQA.Selenium.IE;
using OpenQA.Selenium.Remote;
using OpenQA.Selenium.Chrome;
using Philips.H2H.Foundation.AutomationCore.Common;
using System.Drawing;
using OpenQA.Selenium.Interactions;
using Philips.H2H.Foundation.AutomationCore.Browser;
using Philips.H2H.Foundation.AutomationCore.Interface;

namespace Philips.H2H.Foundation.AutomationCore
{
    public sealed class BrowserPageInstance
    {
        public enum BrowserAttribute
        {
            Id, ClassName, Name, Text, Enabled, Selected, Checked, Displayed
        }
        private static IWebDriver driver;
        private CommonDriverFunction cmdFuncObj = new CommonDriverFunction();

        public BrowserPageInstance()
        {
            driver = BrowserDriver._Bdriver;
        }

        public IBrowserPageControl GetElementNoScreenshot(IBrowserPageControl parent, SearchBy by, string clause)
        {
            IWebElement elem = cmdFuncObj.FindElementNoScreenshot(driver, parent, by, clause);
            if (elem != null)
            {
                return new BrowserPageControl(elem);
            }
            else
                return null;
        }

        public IBrowserPageControl GetElementByText(string tag, string text)
        {
            IWebElement elem = cmdFuncObj.GetElement(driver, null, SearchBy.Xpath, "//" + tag + "[@text='" + text + "']");
            if (elem != null)
            {
                return new BrowserPageControl(elem);
            }
            else
                return null;
        }

        public IBrowserPageControl GetElement(SearchBy by, string clauseValue)
        {
            IWebElement elem = cmdFuncObj.GetElement(driver, null, by, clauseValue);
            if (elem != null)
            {
                return new BrowserPageControl(elem);
            }
            else
                return null;
        }

        public IBrowserPageControl GetElement(IBrowserPageControl parent, SearchBy by, string clauseValue)
        {
            IWebElement elem = cmdFuncObj.GetElement(driver, parent, by, clauseValue);
            if (elem != null)
            {
                return new BrowserPageControl(elem);
            }
            else
                return null;
        }

        public List<IBrowserPageControl> GetElements(SearchBy by, string clauseValue)
        {
            return getBrowserElements(cmdFuncObj.GetElements(driver, null, by, clauseValue));
        }

        public List<IBrowserPageControl> GetElements(IBrowserPageControl parent, SearchBy by, string clauseValue)
        {
            return getBrowserElements(cmdFuncObj.GetElements(driver, parent, by, clauseValue));
        }

        public IBrowserPageControl GetParentElement(IBrowserPageControl elem)
        {
            IWebElement parentElem = null;
            if (elem != null)
            {
                parentElem = cmdFuncObj.FindElement(BrowserDriver._Bdriver, elem, By.XPath(".."));
            }
            return new BrowserPageControl(parentElem);
        }

        public int[] GetElementCoordinates(IBrowserPageControl elem)
        {
            Point point = elem.ElementInstance.Location;
            int x = point.X;
            int y = point.Y;
            return new int[] { x, y };
        }

        public void ClickAt(IBrowserPageControl elem, int x, int y)
        {
            new Actions(BrowserDriver._Bdriver)
                .MoveToElement(elem.ElementInstance, 0, 0)
                .MoveByOffset(x, y)
                .Click()
                .Build()
                .Perform();
        }

        private List<IBrowserPageControl> getBrowserElements(ReadOnlyCollection<IWebElement> elements)
        {
            List<IBrowserPageControl> list = new List<IBrowserPageControl>();
            foreach (IWebElement elem in elements)
            {
                list.Add(new BrowserPageControl((IWebElement)elem));
            }
            return list;
        }

        public void ClickByName(string sName)
        {
            Logger.Debug("Clicking on Text : " + sName);
            try
            {
                cmdFuncObj.GetElement(driver, null, SearchBy.Name, sName).Click();

            }
            catch (Exception)
            {
                throw new Exception("Could not find page control to click with Name: " + sName);
            }
        }

        public void ClickById(string sId)
        {
            Logger.Debug("Clicking on Element By Id : " + sId);
            try
            {
                cmdFuncObj.GetElement(driver, null, SearchBy.Id, sId).Click();

            }
            catch (Exception)
            {
                throw new Exception("Could not find page control to click with Id: " + sId);
            }
        }

        public void ClickByText(IBrowserPageControl parent, string tag, string sText)
        {
            Logger.Debug("Clicking on Element By Text : " + sText);
            List<IBrowserPageControl> elems = getBrowserElements(cmdFuncObj.GetElements(driver, parent, SearchBy.TagName, tag));
            elems.Find(d => d.Text.Equals(sText)).Click();
        }

        public void ClickOnButton(string btnText)
        {
            Logger.Debug("Clicking on Button : " + btnText);
            IList<IBrowserPageControl> allButtons = getBrowserElements(
                cmdFuncObj.GetElements(driver, null, SearchBy.TagName, "Button"));
            foreach (IBrowserPageControl button in allButtons)
            {
                if (button.Text.Equals(btnText))
                {
                    button.Click();
                    break;
                }
            }
        }

        public string GetTextById(string sId)
        {
            Logger.Debug("Trying to Fetch text from the Element with Resource Id: " + sId);
            return cmdFuncObj.GetElement(driver, null, SearchBy.Id, sId).Text;
        }

        public bool SearchAndClick(IBrowserPageControl parent, SearchBy by, string clauseValue, string contains)
        {
            Logger.Debug("Contains Text: " + contains);
            List<IBrowserPageControl> allElements = GetElements(parent, by, clauseValue);
            IBrowserPageControl clickElement = allElements.Find(d => d.Text.Trim().Contains(contains.Trim()) || d.Text.Trim().Equals(contains.Trim()));
            if (clickElement != null)
            {
                Logger.Debug("Found the requested item: " + contains);
                clickElement.Click();
                return true;
            }
            //foreach (var item in allElements)
            //{
            //    Logger.Debug("Search Item: " + item.Text);
            //    if (item.Text.Trim().Contains(contains) || item.Text.Trim().Equals(contains,StringComparison.CurrentCultureIgnoreCase))
            //    {
            //        Logger.Debug("Found the requested item: " + contains);
            //        item.Click();
            //        return true;
            //    }
            //}
            return false;
        }

        public bool SearchAndClick(SearchBy by, string clauseValue, string contains)
        {
            return SearchAndClick(null, by, clauseValue, contains);
        }

        public IBrowserPageControl GetActiveElement()
        {
            return new BrowserPageControl(BrowserDriver._Bdriver.SwitchTo().ActiveElement());
        }
    }
}
