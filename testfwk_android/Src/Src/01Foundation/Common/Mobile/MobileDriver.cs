/* 
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 */
using System;
using OpenQA.Selenium.Remote;
using OpenQA.Selenium.Appium;
using System.Configuration;
using System.Diagnostics;
using System.IO;
using OpenQA.Selenium.Appium.Android;
using OpenQA.Selenium;
using Philips.H2H.Foundation.AutomationCore.Common;
using OpenQA.Selenium.Appium.MultiTouch;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.H2H.Foundation.AutomationCore.Interface;
using OpenQA.Selenium.Appium.Interfaces;
using System.Threading;
using OpenQA.Selenium.Appium.iOS;



namespace Philips.H2H.Foundation.AutomationCore
{
    public class MobileDriver : IDriverWindow
    {
        private static CommonDriverFunction cmdObj = new CommonDriverFunction();
        public static IOSDriver iOSdriver = null;

        public enum OsType
        {
            Android,
            Windows,
            IOs
        }

        public enum ViewType
        {
            Native, Webview
        }

        public static AndroidDriver _Mdriver = null;
       

        private MobileDriver()
        {
            //Do Nothing
        }

        private MobileDriver(MobileDriver.OsType osType, MobileDriverConfig config)
        {
            try
            {
                switch (osType)
                {
                    case OsType.Android:
                        if (!UtilityAuto.CheckAppiumServerRunning(config))
                        {
                            Logger.Fail("Appium Server is not running / Please start Appium Server");
                        }
                        else
                        {
                            Logger.Debug("Appium server is running / Starting Execution");
                        }
                        _Mdriver = new AndroidDriver(new Uri("http://" + config.SERVER + ":" + config.PORT + "/wd/hub/"),
                            MobileDriver.SetAppiumCapability("Android", config));
                        break;
                    case OsType.Windows:
                        Logger.Fail("Windows Phone Operating System is not supported yet.");
                        break;
                    case OsType.IOs:
                        iOSdriver = new IOSDriver(new Uri("http://" + config.SERVER + ":" + config.PORT + "/wd/hub"), 
                            MobileDriver.SetAppiumCapabilityForIOS("iOS", config));    
                        //Logger.Fail("IOs Operating System is not supported yet.");
                        break;
                }
                UtilityAuto.ThreadSleep(3000);
            }
            catch (Exception)
            {
                throw;
            }
        }

        public static MobileDriver SetApp(MobileDriver.OsType osType, MobileDriverConfig config)
        {
            return new MobileDriver(osType, config);
        }

        /// <summary>
        /// Returns the Current Activity Name
        /// </summary>
        public static string CurrentActivity
        {
            get { return _Mdriver.CurrentActivity; }
        }
              
        private static DesiredCapabilities SetAppiumCapability(String sAutomationName, MobileDriverConfig config)
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.SetCapability("automationName", "Appium");
            capabilities.SetCapability("deviceName", "Android");
            capabilities.SetCapability("platformName", config.PLATFORM_NAME);
            capabilities.SetCapability("platformVersion", config.PLATFORM_VERSION);
            capabilities.SetCapability(CapabilityType.Platform, config.SYSTEM_OS);
            capabilities.SetCapability("autoWebView", true);
            capabilities.SetCapability("app", config.TEST_APK_PATH);
            capabilities.SetCapability("appPackage", config.APP_PACKAGE);
            if (!string.IsNullOrEmpty(config.DEVICE_ID))
            {
                capabilities.SetCapability("udid", config.DEVICE_ID);
            }
            //this is to check if the server needs to start with Selendroid to automate webview elements
            if (sAutomationName.Equals("Selendroid"))
            {
                capabilities.SetCapability("appActivity", "");
            }
            else
            {
                capabilities.SetCapability("appActivity", config.APP_ACTIVITY);
            }
            capabilities.SetCapability(CapabilityType.BrowserName, config.BROWSER_NAME);

            if (ConfigurationManager.AppSettings["CleanAppData"].Equals("false"))
            {
                // capabilities.SetCapability("autoLaunch", false);
                capabilities.SetCapability("fullReset", false);
                capabilities.SetCapability("noReset", true);
            }
            else
            {
                capabilities.SetCapability("fullReset", true);
            }

            int timeOut = Int32.Parse(config.TIME_OUT);
            capabilities.SetCapability("newCommandTimeout", timeOut);
            return capabilities;
        }
        /// <summary>
        /// added by Ananya on 13th April at 11:30 am to set the capabilities for Ios
        /// </summary>
        /// <param name="sAutomationName"></param>
        /// <param name="config"></param>
        /// <returns></returns>
        private static DesiredCapabilities SetAppiumCapabilityForIOS(String sAutomationName, MobileDriverConfig config)
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.SetCapability("platformName", config.PLATFORM_NAME);
            capabilities.SetCapability("platformVersion", config.PLATFORM_VERSION);
            capabilities.SetCapability("platform", config.SYSTEM_OS);
            capabilities.SetCapability("deviceName", config.DEVICE_NAME);
            capabilities.SetCapability("udid", config.DEVICE_ID);
            capabilities.SetCapability("bundleId", config.BUNDLE_ID);
            capabilities.SetCapability("ipa", config.APP_PATH);
            

            if (!string.IsNullOrEmpty(config.DEVICE_ID))
            {
                capabilities.SetCapability("udid", config.DEVICE_ID);
            }
            //this is to check if the server needs to start with Selendroid to automate webview elements
            if (sAutomationName.Equals("Selendroid"))
            {
                capabilities.SetCapability("appActivity", "");
            }
            else
            {
                capabilities.SetCapability("appActivity", config.APP_ACTIVITY);
            }
            capabilities.SetCapability(CapabilityType.BrowserName, config.BROWSER_NAME);

            if (ConfigurationManager.AppSettings["CleanAppData"].Equals("false"))
            {
                // capabilities.SetCapability("autoLaunch", false);
                capabilities.SetCapability("fullReset", false);
                capabilities.SetCapability("noReset", true);
            }
            else
            {
                capabilities.SetCapability("fullReset", true);
            }

            int timeOut = Int32.Parse(config.TIME_OUT);
            capabilities.SetCapability("newCommandTimeout", timeOut);
            return capabilities;
        }


        /// <summary>
        /// Quit/Kill driver instance and Close Application
        /// </summary>
        public static void CloseApp()
        {
            cmdObj.Quit(_Mdriver);
        }

        /// <summary>
        /// Wait for Application's Activity for maximum 8 seconds. 
        /// </summary>
        /// <param name="sActivity">Name of Activity</param>
        /// <returns>Returns true or false</returns>
        public static bool WaitForActivity(string sActivity)
        {
            bool isFound = false;
            int ctr = AutomationConstants.WAIT_ACTIVITY / AutomationConstants.PING_TIME;
            int i = 0;

            while (i < ctr)
            {
                String sCurrActivity = MobileDriver.CurrentActivity;
                Logger.Debug("Current Activity for Application: " + sCurrActivity);
                if (sCurrActivity.Contains(sActivity))
                {
                    isFound = true;
                    break;
                }
                Logger.Debug("Checking for Activity to appear / Waiting 1 sec :"
                                + sActivity);
                UtilityAuto.ThreadSleep(AutomationConstants.PING_TIME);
                i++;
            }
            return isFound;
        }

        /// <summary>
        /// Performs swipe operation
        /// </summary>
        /// <param name="x1"></param>
        /// <param name="y1"></param>
        /// <param name="x2"></param>
        /// <param name="y2"></param>
        public static void Swipe(double x1, double y1, double x2, double y2)
        {
            Logger.Debug("Swiping Screens Coordinates: X1: " + x1 + " Y1: " + y1 + " X2: " + x2 + " Y2: " + y2);
            try
            {
                TouchAction tAction = new TouchAction(_Mdriver);
                tAction.Press(x1, y1).Wait(200).MoveTo(x2, y2).Release().Perform();
            }
            catch (Exception e)
            {
                Logger.Debug("Error while swiping screen: " + e.StackTrace);
            }
        }

        /// <summary>
        /// Takes a Screenshot of the current application page.
        /// </summary>
        /// <param name="fileName">Name of the File Name</param>
        public static void TakeScreenshot(string fileName)
        {
            cmdObj.TakeScreenshot(_Mdriver, fileName);
        }

        /// <summary>
        /// Suspends the Applications and Run it in background
        /// </summary>
        /// <param name="seconds"></param>
        public static void SuspendApp(int seconds)
        {
            _Mdriver.BackgroundApp(seconds);
            //_driver.o
        }

        public static void StopApp()
        {
            Logger.Debug("Stopping Application");
            _Mdriver.CloseApp();
        }

        public static void ReLaunchApp(int resumeTimeInSeconds)
        {
            _Mdriver.CloseApp();
            UtilityAuto.ThreadSleep(resumeTimeInSeconds * 1000);
            _Mdriver.LaunchApp();
        }

        public static void LaunchApp()
        {
            _Mdriver.LaunchApp();
        }

        public static void KillRunningAppProcess(string packageName)
        {
            Logger.Debug("Killing All the Application Process running in the device");
            string command = "/c adb shell am force-stop " + packageName;
            int code = UtilityAuto.ExecuteProcess(command);
        }

        /// <summary>
        /// Resets the Applications to its initia state. Removes all the application data
        /// </summary>
        public static void ResetApp()
        {
            _Mdriver.ResetApp();
        }

        public static void StartActivity(string package, string activity)
        {
            ((AndroidDriver)_Mdriver).StartActivity(package, activity);
        }

        public static void ClearAppData(string packageName)
        {
            //Clearing application local data through ADB commands
            Logger.Debug("Clearing application data from the device");
            string command = "/c adb shell pm clear " + packageName;
            int code = UtilityAuto.ExecuteProcess(command);
        }

        public static bool IsAppInstalled(string sAppPath)
        {
            return _Mdriver.IsAppInstalled(sAppPath);
        }

        public static void OpenNotifications()
        {
            _Mdriver.OpenNotifications();
        }

        public static void SwitchToContext(ViewType type)
        {
            System.Collections.ObjectModel.ReadOnlyCollection<string> allContexts = _Mdriver.Contexts;
            foreach (string context in allContexts)
            {
                Logger.Debug("Context Name: " + context);
                switch (type)
                {
                    case ViewType.Native:
                        if (context.Contains("NATIVE"))
                        {
                            _Mdriver.Context = context;
                            break;
                        }
                        break;

                    case ViewType.Webview:
                        if (context.Contains("WEBVIEW"))
                        {
                            _Mdriver.Context = context;
                            break;
                        }
                        break;
                }
            }
        }

        public static void HideKeyBoard()
        {
            //_driver.HideKeyboard();
            //Hiding Keyboard useing Back button as library HideKeyBoard is not working
            FireKeyEvent(4);
        }

        public static void TypeUsingKeyBoardEvent(string value)
        {
            char[] chars = value.ToCharArray();
            foreach (char c in chars)
            {
                switch (c)
                {
                    case 'a':
                        FireKeyEvent(AutomationConstants.KEYCODE_A);
                        break;
                    case 'b':
                        FireKeyEvent(AutomationConstants.KEYCODE_B);
                        break;
                    case 'c':
                        FireKeyEvent(AutomationConstants.KEYCODE_C);
                        break;
                    case 'd':
                        FireKeyEvent(AutomationConstants.KEYCODE_D);
                        break;
                    case 'e':
                        FireKeyEvent(AutomationConstants.KEYCODE_E);
                        break;
                    case 'f':
                        FireKeyEvent(AutomationConstants.KEYCODE_F);
                        break;
                    case 'g':
                        FireKeyEvent(AutomationConstants.KEYCODE_G);
                        break;
                    case 'h':
                        FireKeyEvent(AutomationConstants.KEYCODE_H);
                        break;
                    case 'i':
                        FireKeyEvent(AutomationConstants.KEYCODE_I);
                        break;
                    case 'j':
                        FireKeyEvent(AutomationConstants.KEYCODE_J);
                        break;
                    case 'k':
                        FireKeyEvent(AutomationConstants.KEYCODE_K);
                        break;
                    case 'l':
                        FireKeyEvent(AutomationConstants.KEYCODE_L);
                        break;
                    case 'm':
                        FireKeyEvent(AutomationConstants.KEYCODE_M);
                        break;
                    case 'n':
                        FireKeyEvent(AutomationConstants.KEYCODE_N);
                        break;
                    case 'o':
                        FireKeyEvent(AutomationConstants.KEYCODE_O);
                        break;
                    case 'p':
                        FireKeyEvent(AutomationConstants.KEYCODE_P);
                        break;
                    case 'q':
                        FireKeyEvent(AutomationConstants.KEYCODE_Q);
                        break;
                    case 'r':
                        FireKeyEvent(AutomationConstants.KEYCODE_R);
                        break;
                    case 's':
                        FireKeyEvent(AutomationConstants.KEYCODE_S);
                        break;
                    case 't':
                        FireKeyEvent(AutomationConstants.KEYCODE_T);
                        break;
                    case 'u':
                        FireKeyEvent(AutomationConstants.KEYCODE_U);
                        break;
                    case 'v':
                        FireKeyEvent(AutomationConstants.KEYCODE_V);
                        break;
                    case 'w':
                        FireKeyEvent(AutomationConstants.KEYCODE_W);
                        break;
                    case 'x':
                        FireKeyEvent(AutomationConstants.KEYCODE_X);
                        break;
                    case 'y':
                        FireKeyEvent(AutomationConstants.KEYCODE_Y);
                        break;
                    case 'z':
                        FireKeyEvent(AutomationConstants.KEYCODE_Z);
                        break;
                    case '0':
                        FireKeyEvent(AutomationConstants.KEYCODE_0);
                        break;
                    case '1':
                        FireKeyEvent(AutomationConstants.KEYCODE_1);
                        break;
                    case '2':
                        FireKeyEvent(AutomationConstants.KEYCODE_2);
                        break;
                    case '3':
                        FireKeyEvent(AutomationConstants.KEYCODE_3);
                        break;
                    case '4':
                        FireKeyEvent(AutomationConstants.KEYCODE_4);
                        break;
                    case '5':
                        FireKeyEvent(AutomationConstants.KEYCODE_5);
                        break;
                    case '6':
                        FireKeyEvent(AutomationConstants.KEYCODE_6);
                        break;
                    case '7':
                        FireKeyEvent(AutomationConstants.KEYCODE_7);
                        break;
                    case '8':
                        FireKeyEvent(AutomationConstants.KEYCODE_8);
                        break;
                    case '9':
                        FireKeyEvent(AutomationConstants.KEYCODE_9);
                        break;
                    case '-':
                        FireKeyEvent(AutomationConstants.KEYCODE_SUBTRACT);
                        break;
                    case '.':
                        FireKeyEvent(AutomationConstants.KEYCODE_DOT);
                        break;
                }
            }
        }

        public static void FireKeyEvent(int keyCode)
        {
            Logger.Debug("Firing Device Key Event: " + keyCode);
            _Mdriver.KeyEvent(keyCode);
        }

        private static void GetScreenOrientation()
        {
            OpenQA.Selenium.ScreenOrientation scrnOr = _Mdriver.Orientation;
            //scrnOr.GetTypeCode().
            IRotatable rotatable = ((IRotatable)_Mdriver);
            rotatable.Orientation = ScreenOrientation.Portrait;
            //Assert.AreEqual(ScreenOrientation.Portrait, rotatable.Orientation);
        }

        public static void RefreshPage()
        {
            cmdObj.GetPageSource(_Mdriver);
        }

        public WindowSize WindowSize
        {
            get { return cmdObj.GetWindowSize(_Mdriver); }
        }

        public static void swipeForiOS(double x1, double y1, double x2, double y2)
        {
            try
            {
                ITouchAction action = new TouchAction(iOSdriver)
                        .Press(x1, y1)
                        .Wait(2000)
                        .MoveTo(x2, y2)
                        .Release();

                action.Perform();
                Thread.Sleep(2000);
            }
            catch (Exception e)
            {
                Logger.Debug("Error while swiping screen: " + e.StackTrace);
            }
        }
    }  
}
