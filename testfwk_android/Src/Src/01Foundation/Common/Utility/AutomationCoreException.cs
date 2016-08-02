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
using Philips.H2H.Foundation.AutomationCore.Common;

namespace Philips.H2H.Foundation.AutomationCore.Utility
{
    public class MobileException : Exception
    {
        public MobileException(Exception e)
        {
            new CommonDriverFunction().TakeScreenshot(MobileDriver._Mdriver, "");
            Logger.Fail("Error Message: " + e.Message + "\n Stacktrace " + e.StackTrace);
        }
        
        public MobileException(string msg)
        {
            new CommonDriverFunction().TakeScreenshot(MobileDriver._Mdriver, "");
            Logger.Fail(msg);            
        }
    }

    public class BrowserException : Exception
    {
        public BrowserException(Exception e)
        {
            new CommonDriverFunction().TakeScreenshot(BrowserDriver._Bdriver, "");
            Logger.Fail("Error Message: "+ e.Message+"\n Stacktrace " +e.StackTrace);
        }

        public BrowserException(string msg)
        {
            new CommonDriverFunction().TakeScreenshot(BrowserDriver._Bdriver, "");
            Logger.Fail(msg);
        }
    }
}
