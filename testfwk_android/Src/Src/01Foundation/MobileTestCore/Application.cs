/* 
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 */
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using Philips.H2H.Foundation.AutomationCore;
using System.Configuration;
using System.IO;
using System.Xml;
using System.Net;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Foundation.AutomationCore.Interface;
using Philips.H2H.Foundation.AutomationCore.Mobile;

namespace Philips.H2H.Automation.Foundation.MobileTestCore
{
    public class Application
    {
        static private MobileDriver _driver=null;
        static private MobilePageInstance elemObj = null;

        public enum Scroll
        {
            ScrollUpIndicator,
            ScrollDownIndicator
        }

        public static MobileDriverConfig DeviceConfig
        {
            get;
            set;
        }

        public static int StartAppiumServer(MobileDriverConfig config)
        {
            bool isUp = UtilityAuto.CheckServerStatus(config.SERVER, config.PORT);
            Logger.Debug("Appium Server Log Path: " + AutomationConstants.BIN_PATH);
            if (File.Exists(AutomationConstants.BIN_PATH + @"\appiumserver.log"))
            {
                File.Delete(AutomationConstants.BIN_PATH + @"\appiumserver.log");
            }

            ProcessStartInfo info = new ProcessStartInfo(AutomationConstants.BIN_PATH + @"\UIAutomation\StartAppiumServer.bat");
            info.WindowStyle = ProcessWindowStyle.Hidden;
            info.Arguments = config.SERVER + " " +
                config.PORT.ToString() + " " +
                config.BOOTSTRAP_PORT + " " +
                config.DEVICE_ID + " " +
                AutomationConstants.BIN_PATH + @"\appiumserver_" + config.PORT + ".log";
            info.UseShellExecute = false;
            Process p = Process.Start(info);

            isUp = UtilityAuto.CheckAppiumServerRunning(config);
            if (isUp)
            {
                Logger.Debug("Appium Server is Started, Continuing to for Test Execution");
            }
            else
            {
                Logger.Fail("Appium Server could not be started/ Test cases will be failed.");
            }
            return p.Id;
        }

        public static MobilePageInstance MobilePageInstanceInstance
        {
            //If  mobile element instance is already created then return and Do not create a new mobile element instance
            get
            {
                if (elemObj != null)
                {
                    return elemObj;
                }
                else
                {
                    elemObj = MobilePageInstance.Instance;
                    return elemObj;
                }
            }
            set
            {
                MobilePageInstance.Instance = value;
                _driver = null;
            }
        }

        public static void Launch(MobileDriverConfig config)
        {
            if (_driver == null)
            {
                //Logger.Info("Launching IAP application again due to some exception in Appium driver");
                DeviceConfig = config;
                
                switch(config.PLATFORM_NAME)
                {
                    case "Android" :
                        _driver = MobileDriver.SetApp(MobileDriver.OsType.Android, config);
                        break;
                    case "iOS":
                        _driver = MobileDriver.SetApp(MobileDriver.OsType.IOs, config);
                        break;
                }
                //Every time create a new Mobile Element Object when Application launched                     
                elemObj = MobilePageInstance.Instance;
                //Logger.Info("IAP application launched in the device. Device ID: " + config.DEVICE_ID);
            }
            else
            {
                MobileDriver.ReLaunchApp(2);
                int ctr = 0;
                while (ctr < 20)
                {
                    if (!ActivityName.Equals(config.APP_ACTIVITY))
                    {
                        //Logger.Debug("Application launched and Current activity is: "+ActivityName);
                        break;
                    }
                    UtilityAuto.ThreadSleep("3");
                    ctr++;
                }

                if (ctr == 20)
                {
                    throw new Exception("Application page could not loaded after 60 seconds");
                }
            }            
        }
              

        public static bool swipe()
        {
            bool isScrollDisplayed = false;
            isScrollDisplayed = isDisplayed(Scroll.ScrollDownIndicator);
            if (isScrollDisplayed)
            {
                Application.SwipeUp();
                UtilityAuto.ThreadSleep(1000);
                isScrollDisplayed = isDisplayed(Scroll.ScrollUpIndicator);
            }
            else
            {
                isScrollDisplayed = isDisplayed(Scroll.ScrollUpIndicator);
                if (isScrollDisplayed)
                {
                    Application.SwipeDown();
                    UtilityAuto.ThreadSleep(1000);
                    isScrollDisplayed = isDisplayed(Scroll.ScrollDownIndicator);
                }
            }
            return isScrollDisplayed;
        }

        public static bool isDisplayed(Scroll option)
        {
            return true;
        }
        
        public static bool isVisible()
        {
            return true;
        }

        public static void Close()
        {
            MobileDriver.CloseApp();
        }

        public static void TakeScreenshot()
        {
            MobileDriver.TakeScreenshot(Application.ActivityName + UtilityAuto.GetRandomString());
        }

        public static string ActivityName
        {
            get
            {                
                string[] temp =  MobileDriver.CurrentActivity.Split('.');
                string activityName = temp[temp.Length - 1].Trim();
                //Logger.Debug("Current Activity Name: " + activityName);
                return activityName;
            }
        }

        public static void SwipeUp()
        {
            //TODO  
        }

        public static void SwipeDown()
        {
            //TODO
        }

        public static void StartAppiumOnMac(MobileDriverConfig config)
        {
            
            ProcessStartInfo info = new ProcessStartInfo(AutomationConstants.BIN_PATH + @"\UIAutomation\ExecuteAppiumCommand.bat");
            info.WindowStyle = ProcessWindowStyle.Hidden;
            info.Arguments = config.SSH_USERNAME + " " +
                config.SERVER + " " +
                config.SSH_PASSWORD + " " +
                AutomationConstants.BIN_PATH + @"\UIAutomation\iOSKillNodeCommand.txt";
            info.UseShellExecute = false;
            Process proc = Process.Start(info);
            Thread.Sleep(15000);
            if(proc.HasExited)
            {
                proc.Close();
                proc.Dispose();
            }

            //info = new ProcessStartInfo(AutomationConstants.BIN_PATH + @"\UIAutomation\ExecuteAppiumCommand.bat");
            //info.WindowStyle = ProcessWindowStyle.Hidden;
            //info.Arguments = config.SSH_USERNAME + " " +
            //    config.SERVER + " " +
            //    config.SSH_PASSWORD + " " +
            //    AutomationConstants.BIN_PATH + @"\UIAutomation\iOSAppium.txt";
            //info.UseShellExecute = false;
            //Process p = Process.Start(info);
            //System.Threading.Thread.Sleep(3000);
        }
    }
}
