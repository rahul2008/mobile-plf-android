/* 
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 */
using OpenQA.Selenium;
using OpenQA.Selenium.Support.UI;
using Philips.H2H.Foundation.AutomationCore.Interface;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Philips.H2H.Foundation.AutomationCore.Browser
{
    class BrowserPageControl : IBrowserPageControl
    {
        private IWebElement _elem;

        public BrowserPageControl(IWebElement elem)
        {
            this._elem = elem;
        }

        public IWebElement ElementInstance
        {
            get
            {
                return this._elem;
            }
        }

        public void Click()
        {
            _elem.Click();
        }

        public void Clear()
        {
            _elem.Clear();
        }

        public bool Displayed
        {
            get { return _elem.Displayed; }
        }

        public bool Enabled
        {
            get { return _elem.Enabled; }
        }

        public bool Selected
        {
            get { return _elem.Selected; }
        }

        public string Text
        {
            get { return _elem.Text; }
        }
        public string Title
        {
            get { return _elem.GetAttribute("title"); }
        }

        public int[] Coordinates
        {
            get
            {
                Point p = _elem.Location;
                return new int[] { p.X, p.Y };
            }
        }

        public int[] Size
        {
            get
            {
                return new int[] { _elem.Size.Height, _elem.Size.Width };
            }
        }

        public string TagName
        {
            get { return _elem.TagName; }
        }

        public string GetAttribute(string attribute)
        {
            return _elem.GetAttribute(attribute);
        }

        public string GetAttribute(BrowserPageInstance.BrowserAttribute attribute)
        {
            switch (attribute)
            {
                case BrowserPageInstance.BrowserAttribute.Displayed:
                    return _elem.Displayed.ToString();
                case BrowserPageInstance.BrowserAttribute.Selected:
                    return _elem.Selected.ToString();
                case BrowserPageInstance.BrowserAttribute.Text:
                    return _elem.Text;
                case BrowserPageInstance.BrowserAttribute.Enabled:
                    return _elem.GetAttribute("enabled");
                case BrowserPageInstance.BrowserAttribute.ClassName:
                    return _elem.GetAttribute("className");
                case BrowserPageInstance.BrowserAttribute.Id:
                    return _elem.GetAttribute("id");
                case BrowserPageInstance.BrowserAttribute.Name:
                    return _elem.GetAttribute("name");
                default: return "";
            }
        }

        public void SetText(string text)
        {
            _elem.Clear();
            _elem.SendKeys(text);
        }

        public void SelectFromDropDownList(string text)
        {
            var selElement = new SelectElement(_elem);
            selElement.SelectByText(text);
        }

        public string GetCSSAttributeValue(string attribute)
        {
            return _elem.GetCssValue(attribute);
        }
    }
}
