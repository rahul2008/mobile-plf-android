using OpenQA.Selenium;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Philips.H2H.Foundation.AutomationCore.Browser
{
    public class BrowserAlert
    {

        public IAlert alert { get; set; }
        
        private IWebDriver BrowserDriverInstance
        {
            get
            {
                return BrowserDriver._Bdriver;
            }
        }
       
        public BrowserAlert()
        {
            alert= BrowserDriverInstance.SwitchTo().Alert();
        }

        public bool IsAlertPresent()
        {
            if (alert != null)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        public void Accept()
        {
            if (alert != null)
            {
                alert.Accept();
            }
            else
            {
                throw new Exception("Alert is not present");
            }
        }

        public void Dismiss()
        {
            if (alert != null)
            {
                alert.Dismiss();
            }
            else
            {
                throw new Exception("Alert is not present");
            }
        }

        public string Text
        {
            get
            {
                if (alert != null)
                {
                    return alert.Text;
                }
                else
                {
                    throw new Exception("Alert is not present");
                }
            }
           
        }
    }
}
