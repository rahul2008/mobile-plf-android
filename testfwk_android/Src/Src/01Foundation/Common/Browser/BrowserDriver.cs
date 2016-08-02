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
using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using OpenQA.Selenium.Firefox;
using OpenQA.Selenium.IE;
using OpenQA.Selenium.Remote;
using OpenQA.Selenium.Support.UI;
using Philips.H2H.Foundation.AutomationCore.Common;
using OpenQA.Selenium.Appium.Android;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using OpenQA.Selenium.Safari;
using Philips.H2H.Foundation.AutomationCore.Interface;
using System.Drawing;
using Philips.H2H.Foundation.AutomationCore.Browser;


namespace Philips.H2H.Foundation.AutomationCore
{
    public class BrowserDriver
    {
        public static IWebDriver _Bdriver = null;
        // private static WebDriverWait wait = null;
        private static List<string> allWindowHndles = new List<string>();
        private static string mainWindowHandle = string.Empty;
        private static CommonDriverFunction cmonDriverObj = new CommonDriverFunction();

        public enum BrowserType
        {
            InternetExplorer,
            Firefox,
            Chrome,
            Safari,
            Android
        }

        private BrowserDriver(BrowserType type, CrossBrowserConfig StackContainer)
        {
            DesiredCapabilities capability;
            switch (type)
            {
                case BrowserType.InternetExplorer:
                    capability = DesiredCapabilities.InternetExplorer();
                    _Bdriver = StartRemoteDriver(capability, StackContainer);
                    break;
                case BrowserType.Chrome:
                    capability = DesiredCapabilities.Chrome();
                    _Bdriver = StartRemoteDriver(capability, StackContainer);

                    break;
                case BrowserType.Firefox:
                    capability = DesiredCapabilities.Firefox();
                    _Bdriver = StartRemoteDriver(capability, StackContainer);
                    break;
                case BrowserType.Safari:
                    capability = DesiredCapabilities.Safari();
                    _Bdriver = StartRemoteDriver(capability, StackContainer);
                    break;

                case BrowserType.Android:
                    MobileDriverConfig config = new MobileDriverConfig();
                    Device.Unlock(config.DEVICE_ID);
                    UtilityAuto.StartAppiumServer(config);
                    MobileDriver driver = MobileDriver.SetApp(MobileDriver.OsType.Android, config);
                    _Bdriver = MobileDriver._Mdriver;
                    break;
            }
            _Bdriver.Manage().Timeouts().ImplicitlyWait(new TimeSpan(0, 0, 3));
            NavigateToUrl(StackContainer.URL);
            //Get the Current window handle and add it to all Window handle list
            mainWindowHandle = CurrentWindowHandle;
            allWindowHndles.Add(mainWindowHandle);
            cmonDriverObj = new CommonDriverFunction();
        }

        private IWebDriver StartRemoteDriver(DesiredCapabilities capability, CrossBrowserConfig StackContainer)
        {
            if (StackContainer.Capabilities != null)
            {
                Dictionary<string, object> capabilityList = StackContainer.Capabilities;
                foreach (var item in capabilityList)
                {
                    capability.SetCapability(item.Key, item.Value);
                }
            }
            capability.SetCapability(StackContainer.UserNameKey, StackContainer.UserName);
            capability.SetCapability(StackContainer.AccessKey, StackContainer.AccessToken);
            _Bdriver = new RemoteWebDriver(new Uri(StackContainer.CrossBrowserHubUrl), capability);
            _Bdriver.Manage().Window.Maximize();
            return _Bdriver;
        }

        private BrowserDriver(BrowserType type, string url)
        {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            switch (type)
            {
                case BrowserType.InternetExplorer:
                    if (AutomationConstants.GRID_SETUP == "true")
                    {
                        InternetExplorerOptions option = new InternetExplorerOptions();
                        option.AddAdditionalCapability("webdriver.ie.driver", AutomationConstants.BIN_PATH + @"\BrowserDriversExe\IEDriverServer.exe");
                        capabilities.SetCapability(CapabilityType.BrowserName, "internet explorer");
                        capabilities.SetCapability(CapabilityType.Platform, new Platform(PlatformType.Windows));
                        _Bdriver = new RemoteWebDriver(new Uri("http://localhost:4444/wd/hub"), capabilities);
                    }
                    else
                    {
                        InternetExplorerOptions option = new InternetExplorerOptions();
                        option.AddAdditionalCapability("logFile", AutomationConstants.LOG_PATH + "\\IEDriverEventLog.log");
                        option.AddAdditionalCapability("logLevel", InternetExplorerDriverLogLevel.Info);
                        option.UnexpectedAlertBehavior = InternetExplorerUnexpectedAlertBehavior.Dismiss;
                        option.EnsureCleanSession = false;
                        _Bdriver = new InternetExplorerDriver(AutomationConstants.BIN_PATH + @"\BrowserDriversExe", option);
                    }
                    _Bdriver.Manage().Window.Maximize();
                    break;
                case BrowserType.Chrome:
                    if (AutomationConstants.GRID_SETUP == "true")
                    {
                        capabilities.SetCapability(CapabilityType.BrowserName, "chrome");
                        capabilities.SetCapability(CapabilityType.Platform, new Platform(PlatformType.Windows));
                        _Bdriver = new RemoteWebDriver(new Uri("http://localhost:4444/wd/hub"), capabilities);
                    }
                    else
                    {
                        ChromeDriverService service = ChromeDriverService.CreateDefaultService(AutomationConstants.BIN_PATH + @"\BrowserDriversExe");
                        service.LogPath = AutomationConstants.BIN_PATH + @"\ChromeServer.log";
                        service.EnableVerboseLogging = true;
                        ChromeOptions option = new ChromeOptions();
                        _Bdriver = new ChromeDriver(service, option);
                    }
                    _Bdriver.Manage().Window.Maximize();
                    break;

                case BrowserType.Safari:
                    _Bdriver = new SafariDriver();
                    break;
                case BrowserType.Firefox:
                    capabilities = DesiredCapabilities.Firefox();
                    capabilities.SetCapability(CapabilityType.BrowserName, "firefox");
                    capabilities.SetCapability(CapabilityType.Platform, new Platform(PlatformType.Windows));
                    //_Bdriver = new FirefoxDriver();
                    _Bdriver = new RemoteWebDriver(new Uri("http://localhost:4444/wd/hub"), capabilities);
                    _Bdriver.Manage().Window.Maximize();
                    break;

                case BrowserType.Android:
                    MobileDriverConfig config = new MobileDriverConfig();
                    Device.Unlock(config.DEVICE_ID);
                    UtilityAuto.StartAppiumServer(config);
                    MobileDriver driver = MobileDriver.SetApp(MobileDriver.OsType.Android, config);
                    _Bdriver = MobileDriver._Mdriver;
                    break;
            }
            _Bdriver.Manage().Timeouts().ImplicitlyWait(new TimeSpan(0, 0, 3));
            NavigateToUrl(url);
            //Get the Current window handle and add it to all Window handle list
            mainWindowHandle = CurrentWindowHandle;
            allWindowHndles.Add(mainWindowHandle);
            cmonDriverObj = new CommonDriverFunction();
        }

        public static void NavigateToUrl(string url)
        {
            _Bdriver.Navigate().GoToUrl(url);
        }

        public static BrowserDriver Launch(BrowserType type, string url)
        {
            return new BrowserDriver(type, url);
        }

        public static BrowserDriver Launch(BrowserType type, CrossBrowserConfig Container)
        {
            return new BrowserDriver(type, Container);
        }

        public static string CurrentWindowHandle
        {
            get
            {
                return _Bdriver.CurrentWindowHandle;
            }
        }

        public static string PageSource
        {
            get
            {
                return cmonDriverObj.GetPageSource(BrowserDriver._Bdriver);
            }
        }

        public static string PageTitle
        {
            get
            {
                return cmonDriverObj.GetPageTitle(BrowserDriver._Bdriver);
            }
        }

        public static string CurrentURL
        {
            get
            {
                return BrowserDriver._Bdriver.Url;
            }
        }

        public static void SwitchToNextWindow()
        {
            List<string> currentWindowHandles = _Bdriver.WindowHandles.ToList();
            foreach (string windowHandle in currentWindowHandles)
            {
                if (!allWindowHndles.Contains(windowHandle))
                {
                    allWindowHndles.Add(windowHandle);
                    _Bdriver.SwitchTo().Window(windowHandle);
                    _Bdriver.Manage().Window.Maximize();
                    break;
                }
            }
        }

        public static void SwitchToMainWindow()
        {
            allWindowHndles.Clear();
            allWindowHndles.Add(mainWindowHandle);
            _Bdriver.SwitchTo().DefaultContent();
            _Bdriver.Manage().Window.Maximize();
        }

        public static void SwitchToFrame(object frame)
        {
            cmonDriverObj.SwitchToFrame(_Bdriver, frame);
        }

        /// <summary>
        /// Takes a Screenshot of the current application page.
        /// </summary>
        /// <param name="fileName">Name of the File Name</param>
        public static void TakeScreenshot(string fileName)
        {
            cmonDriverObj.TakeScreenshot(_Bdriver, fileName);
        }

        public static void Close()
        {
            cmonDriverObj.Quit(_Bdriver);
        }
        public static void Refresh()
        {
            _Bdriver.Navigate().Refresh();
        }

        public static WindowSize GetWindowSize()
        {
            return cmonDriverObj.GetWindowSize(_Bdriver);
        }

        public static void SetWindowSize(int height, int width)
        {
            _Bdriver.Manage().Window.Size = new Size(width, height);
        }
    }

    public class CrossBrowserConfig
    {
        public string UserNameKey { get; set; }
        public string UserName { get; set; }
        public string AccessKey { get; set; }
        public string AccessToken { get; set; }
        public string CrossBrowserHubUrl { get; set; }
        public string URL { get; set; }
        public Dictionary<string, object> Capabilities { get; set; }
    }
}
