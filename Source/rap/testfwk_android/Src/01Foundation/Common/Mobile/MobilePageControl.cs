/* 
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 */
using OpenQA.Selenium;
using OpenQA.Selenium.Appium;
using Philips.H2H.Foundation.AutomationCore.Interface;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Philips.H2H.Foundation.AutomationCore.Mobile
{
    class MobilePageControl : IMobilePageControl
    {
        private AppiumWebElement elem;
        private IWebElement iOSElem;
        public MobilePageControl(AppiumWebElement elem)
        {
            this.elem = elem;
        }

        public MobilePageControl(IWebElement elem1)
        {
            if (ConfigurationManager.AppSettings["MobilePlatformForTest"].Equals("Android"))
                this.elem = (AppiumWebElement)elem1;
            else
                this.iOSElem = elem1;
        }

        public IWebElement ElementInstance
        {
            get
            {
                try
                {
                    if (ConfigurationManager.AppSettings["MobilePlatformForTest"].Equals("Android"))
                        return this.elem;
                    else
                        return this.iOSElem;
                }
                catch (Exception)
                {
                    return null;
                }
            }
        }

        public void Click()
        {
            if (ConfigurationManager.AppSettings["MobilePlatformForTest"].Equals("Android"))
                elem.Click();
            else
                iOSElem.Click();
        }

        public void Clear()
        {
            if (ConfigurationManager.AppSettings["MobilePlatformForTest"].Equals("Android"))
                elem.Clear();
            else
                iOSElem.Clear();
        }

        public bool Displayed
        {
            get
            {
                if (ConfigurationManager.AppSettings["MobilePlatformForTest"].Equals("Android"))
                    return elem.Displayed;
                else
                    return iOSElem.Displayed;
            }
        }

        public bool Enabled
        {
            get
            {
                if (ConfigurationManager.AppSettings["MobilePlatformForTest"].Equals("Android"))
                    return elem.Enabled;
                else
                    return iOSElem.Enabled;
            }
        }

        public bool Selected
        {
            get
            {
                if (ConfigurationManager.AppSettings["MobilePlatformForTest"].Equals("Android"))
                    return elem.Selected;
                else
                    return iOSElem.Selected;
            }
        }

        public string Text
        {
            get
            {
                if (ConfigurationManager.AppSettings["MobilePlatformForTest"].Equals("Android"))
                    return elem.Text;
                else
                    return iOSElem.Text;
            }
        }

        public int[] Coordinates
        {

            get
            {
                Point p = Point.Empty;
                if (ConfigurationManager.AppSettings["MobilePlatformForTest"].Equals("Android"))
                {
                    p = elem.Coordinates.LocationInViewport;
                    return new int[] { p.X, p.Y };
                }
                else
                {
                    p = iOSElem.Location;
                    return new int[] { p.X, p.Y };
                }
            }
        }

        public int[] Size
        {
            get
            {
                if (ConfigurationManager.AppSettings["MobilePlatformForTest"].Equals("Android"))
                    return new int[] { elem.Size.Height, elem.Size.Width };
                else
                    return new int[] { iOSElem.Size.Height, iOSElem.Size.Width };
            }
        }

        public string GetAttribute(string attribute)
        {
            if (ConfigurationManager.AppSettings["MobilePlatformForTest"].Equals("Android"))
                return elem.GetAttribute(attribute);
            else
                return iOSElem.GetAttribute(attribute);
        }

        public string GetAttribute(MobilePageInstance.MobileAttribute attribute)
        {
            try
            {
                switch (attribute)
                {
                    case MobilePageInstance.MobileAttribute.Checkable:
                        return elem.GetAttribute("checkable");
                    case MobilePageInstance.MobileAttribute.Checked:
                        return elem.GetAttribute("checked");
                    case MobilePageInstance.MobileAttribute.Clickable:
                        return elem.GetAttribute("clickable");
                    case MobilePageInstance.MobileAttribute.LongClickable:
                        return elem.GetAttribute("longClickable");
                    case MobilePageInstance.MobileAttribute.Scrollable:
                        return elem.GetAttribute("scrollable");
                    case MobilePageInstance.MobileAttribute.Displayed:
                        return elem.Displayed.ToString();
                    case MobilePageInstance.MobileAttribute.Selected:
                        return elem.Selected.ToString();
                    case MobilePageInstance.MobileAttribute.Text:
                        return elem.GetAttribute("text");
                    case MobilePageInstance.MobileAttribute.Focusable:
                        return elem.GetAttribute("focusable");
                    case MobilePageInstance.MobileAttribute.Focused:
                        return elem.GetAttribute("focused");
                    case MobilePageInstance.MobileAttribute.Enabled:
                        return elem.GetAttribute("enabled");
                    case MobilePageInstance.MobileAttribute.ClassName:
                        return elem.GetAttribute("className");
                    case MobilePageInstance.MobileAttribute.Id:
                        return elem.GetAttribute("resourceId");
                    case MobilePageInstance.MobileAttribute.ContentDesc:
                        return elem.GetAttribute("name");
                    case MobilePageInstance.MobileAttribute.Name:
                        return elem.GetAttribute("name");
                    default: return "";
                }
            }
            catch (Exception)
            {
                throw;
            }
        }

        public void SetText(string text)
        {
            if (ConfigurationManager.AppSettings["MobilePlatformForTest"].Equals("Android"))
                elem.SendKeys(text);
            else
                iOSElem.SendKeys(text);
        }
    }
}
