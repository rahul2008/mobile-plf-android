/* 
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 */
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using log4net;
using log4net.Config;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace Philips.H2H.Foundation.AutomationCore
{
    public static class Logger
    {
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(
            (System.Reflection.MethodBase.GetCurrentMethod().DeclaringType));
        
        static Logger()
        {
            UtilityAuto.CreateDirectoryIfNotExist(AutomationConstants.LOG_PATH);            
            log4net.GlobalContext.Properties["LogPath"] = AutomationConstants.LOG_PATH;
            log4net.GlobalContext.Properties["DateTime"] = DateTime.Now.ToString("yyyy_MM_dd_HH_mm");
            XmlConfigurator.Configure();
        }
        
        private static string _testName;

        public static string TestName
        {
            get { return _testName; }

            set
            {
                _testName = value;
                GlobalContext.Properties["TestName"] = value;
            }
        }
        
        public static void Info(string text)
        {
            log.Info(text);
        }       

        public static void Fail(string text)
        {
            log.Error(text);
            Assert.Fail(text);
        }

        public static void Warn(string text)
        {
            log.Warn(text);
        }

        public static void Debug(string text)
        {
            log.Debug(text);
        }

        public static void Check(string Xpctd, string Actual)
        {
            if (!Xpctd.Equals(Actual))
            {
                Logger.Fail("Check Failed: Expected Value: " +  Xpctd + " Actual Value: " + Actual);
            }
            else
            {
                Logger.Debug("Check Passed: Expected Value: " + Xpctd + " Actual Value: " + Actual);
            }
        }

        public static void CheckNotNull(object Xpctd)
        {
            if (Xpctd==null)
            {
                Logger.Fail("Check Failed: Expected Value: Not Null" + Xpctd + " Actual Value: " + Xpctd);
            }
            else
            {
                Logger.Debug("Check Passed: Expected Value: Null Actual Value: " + Xpctd);
            }
        }

        public static void CheckNull(object Xpctd)
        {
            if (Xpctd !=null)
            {
                Logger.Fail("Check Failed: Expected Value: Null  Actual Value: " + Xpctd);
            }
            else
            {
                Logger.Debug("Check Passed: Expected Value: Null  Actual Value: " + Xpctd);
            }
        }


        public static void Check(string[] Xpctd, string[] Actual)
        {
            if (!Xpctd.SequenceEqual<string>(Actual))
            {
                Logger.Fail("Check Failed: Expected Value: " + string.Join(",", Xpctd) + " Actual Value: " + string.Join(",", Actual));
            }
            else
            {
                Logger.Debug("Check Passed: Expected Value: " + string.Join(",", Xpctd) + " Actual Value: " + string.Join(",", Actual));
            }
        }        

        public static void Check(int Xpctd, int Actual)
        {
            if (Xpctd !=Actual)
            {
                Logger.Fail("Check Failed: Expected Value: " + Xpctd + " Actual Value: " + Actual);
            }
            else
            {
                Logger.Debug("Check Passed: Expected Value: " + Xpctd + " Actual Value: " + Actual);
            }
        }

    }
}
