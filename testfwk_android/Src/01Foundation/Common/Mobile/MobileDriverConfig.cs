/* 
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 */
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Philips.H2H.Foundation.AutomationCore.Mobile
{
    public class MobileDriverConfig
    {
        public MobileDriverConfig()
        {
            string mobilePlatform = ConfigurationManager.AppSettings["MobilePlatformForTest"];

            if (mobilePlatform.Equals("Android"))
            {
                SERVER = ConfigurationManager.AppSettings["ServerAddress"];
                PORT = ConfigurationManager.AppSettings["ServerPort"];
                BOOTSTRAP_PORT = ConfigurationManager.AppSettings["BootStrapPort"];
                BROWSER_NAME = ConfigurationManager.AppSettings["AndroidBrowser"];
                PLATFORM_NAME = ConfigurationManager.AppSettings["MobilePlatform"];
                PLATFORM_VERSION = ConfigurationManager.AppSettings["Version"];
                SYSTEM_OS = ConfigurationManager.AppSettings["SystemOS"]; ;
                TIME_OUT = ConfigurationManager.AppSettings["CommandTimeOut"];
                DEVICE_ID = ConfigurationManager.AppSettings["udid"];
                TEST_APK_PATH = ConfigurationManager.AppSettings["ApkPath"];
                APP_PACKAGE = ConfigurationManager.AppSettings["AppPackage"];
                APP_ACTIVITY = ConfigurationManager.AppSettings["AppActivity"];
            }
            else if (mobilePlatform.Equals("iOS"))
            {
                SSH_USERNAME = ConfigurationManager.AppSettings["iOSSSHUsername"];
                SSH_PASSWORD = ConfigurationManager.AppSettings["iOSSSHPassword"];
                SERVER = ConfigurationManager.AppSettings["iOSServerAddress"];
                PORT = ConfigurationManager.AppSettings["iOSServerPort"];
                BOOTSTRAP_PORT = ConfigurationManager.AppSettings["BootStrapPort"];
                DEVICE_NAME = ConfigurationManager.AppSettings["iOSDeviceName"];
                PLATFORM_NAME = ConfigurationManager.AppSettings["iOSMobilePlatform"];
                PLATFORM_VERSION = ConfigurationManager.AppSettings["iOSVersion"];
                SYSTEM_OS = ConfigurationManager.AppSettings["iOSSystemOS"]; ;
                TIME_OUT = ConfigurationManager.AppSettings["CommandTimeOut"];
                DEVICE_ID = ConfigurationManager.AppSettings["iOSudid"];
                BUNDLE_ID = ConfigurationManager.AppSettings["iOSBundleId"];
                APP_PATH = ConfigurationManager.AppSettings["iOSAppPath"];
            }

        }

        public string SERVER
        {
            get;
            set;
        }

        public string PORT
        {
            get;
            set;
        }

        public string BOOTSTRAP_PORT
        {
            get;
            set;
        }

        public string BROWSER_NAME
        {
            get;
            set;
        }

        public string PLATFORM_NAME
        {
            get;
            set;
        }

        public string PLATFORM_VERSION
        {
            get;
            set;
        }

        public string SYSTEM_OS
        {
            get;
            set;
        }

        public string TIME_OUT
        {
            get;
            set;
        }

        public string DEVICE_ID
        {
            get;
            set;
        }

        public string TEST_APK_PATH
        {
            get;
            set;
        }

        public string APP_PACKAGE
        {
            get;
            set;
        }

        public string APP_ACTIVITY
        {
            get;
            set;
        }

        public int NODE_PROCESS_ID
        {
            get;
            set;
        }

        //iOS related properties
        public string SSH_USERNAME
        {
            get;
            set;
        }

        public string SSH_PASSWORD
        {
            get;
            set;
        }

        public string DEVICE_NAME
        {
            get;
            set;
        }

        public string BUNDLE_ID
        {
            get;
            set;
        }

        public string APP_PATH
        {
            get;
            set;
        }
    }
}
