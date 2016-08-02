/* 
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 */
using OpenQA.Selenium;
using Philips.H2H.Foundation.AutomationCore.Interface;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Philips.H2H.Foundation.AutomationCore.Browser
{
    public class BrowserScriptExecutor
    {
        public static void ClickByJavaScriptExecutor(IBrowserPageControl elem)
        {
            IJavaScriptExecutor jsObj = BrowserDriver._Bdriver as IJavaScriptExecutor;
            jsObj.ExecuteScript("arguments[0].click();", elem.ElementInstance);
            UtilityAuto.ThreadSleep(1000);
        }

        public static string ExecuteScript(string script, IBrowserPageControl elem)
        {
            IJavaScriptExecutor jsObj = BrowserDriver._Bdriver as IJavaScriptExecutor;
            if (script.Contains("return"))
            {
                script = script.Replace("return", "");
            }
            return (string)jsObj.ExecuteScript("return " + script, elem.ElementInstance);
        }

        public static string ExecuteScript(string script)
        {
            IJavaScriptExecutor jsObj = BrowserDriver._Bdriver as IJavaScriptExecutor;
            if (script.Contains("return"))
            {
                script = script.Replace("return", "");
            }
            return (string)jsObj.ExecuteScript("return " + script);
        }
    }
}
