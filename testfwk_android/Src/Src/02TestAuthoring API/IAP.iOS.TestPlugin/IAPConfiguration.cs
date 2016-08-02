using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Configuration;

using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using System.Net;
using System.Diagnostics;
using System.Threading;
using System.IO;

namespace Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS
{
    /// <summary>
    /// This class represents the configurations required for IAP application.
    /// </summary>
    public class IAPConfiguration
    {
        private static int processId;
        static MobilePageInstance Instance;
        static string output;

        /// <summary>
        ///Downloads the file from the server
        ///Configures and start the Appium Server
        ///Unlocks the Device
        ///Uninstall and Reinstalls the server version of the apk
        ///Launches the two applications
        /// </summary>
        public static void LoadConfiguration()
        {
            //creating directory on the system if it is not already
            if (!Directory.Exists(Path.GetDirectoryName(ConfigurationManager.AppSettings["Destination"])))
            {
                Directory.CreateDirectory(Path.GetDirectoryName(ConfigurationManager.AppSettings["Destination"]));
            }

            //downloading the apk files from server
            bool downloadFile = false;
            downloadFile = DownloadApplicationUnderTest(ConfigurationManager.AppSettings["Source"], ConfigurationManager.AppSettings["IapApkLoc"] + "_" + ConfigurationManager.AppSettings["udid"] + ".apk");
            MobileDriverConfig mConfig = new MobileDriverConfig();
            Device.Unlock(mConfig.DEVICE_ID);
           //Pattabhi: Added Closing the Appium Server ( Will it affect Parallel Execution?? PLease check.
            UtilityAuto.CloseAppiumServer();
            //start appium server
            processId = Application.StartAppiumServer(mConfig);
            UtilityAuto.ThreadSleep(2000);
            if (downloadFile)
            {
                UninstallApkFromDevice(mConfig.APP_PACKAGE, mConfig.DEVICE_ID);
                InstallApkToDevice(ConfigurationManager.AppSettings["IapApkLoc"] + "_" + ConfigurationManager.AppSettings["udid"] + ".apk", mConfig.DEVICE_ID);
            }
            //installing and launching start activity of performance apk on the device for iap app monitoring.
            if (ConfigurationManager.AppSettings["IsPerformanceMonitoringEnable"].Equals("true"))
            {
                new WebClient().DownloadFile(ConfigurationManager.AppSettings["PerformanceApkServer"], ConfigurationManager.AppSettings["PerformanceApkLoc"] + "_" + ConfigurationManager.AppSettings["udid"] + ".apk");

                mConfig.APP_PACKAGE = ConfigurationManager.AppSettings["PerformanceAppPackage"];
                mConfig.APP_ACTIVITY = ConfigurationManager.AppSettings["PerformanceAppActivity"];
                UninstallApkFromDevice(mConfig.APP_PACKAGE, mConfig.DEVICE_ID);
                InstallApkToDevice(ConfigurationManager.AppSettings["PerformanceApkLoc"] + "_" + ConfigurationManager.AppSettings["udid"] + ".apk", mConfig.DEVICE_ID);

                ExecuteProcess("/c adb -s " + ConfigurationManager.AppSettings["udid"] + " shell am start -n " + ConfigurationManager.AppSettings["AppPackage"] + "/" + ConfigurationManager.AppSettings["AppActivity"]);
                ExecuteProcess("/c adb -s " + ConfigurationManager.AppSettings["udid"] + " shell ps " + ConfigurationManager.AppSettings["AppPackage"].Substring(ConfigurationManager.AppSettings["AppPackage"].Length - 15, 15));
                string[] words = output.Split(' ');
                string pid = words[32];
                string uid = "10" + words[29].Substring(words[29].Length - 3, 3);
                System.IO.File.WriteAllText(ConfigurationManager.AppSettings["IapPidLoc"] + ConfigurationManager.AppSettings["udid"] + ".txt", pid + "," + uid);
                ExecuteProcess("/c adb -s " + ConfigurationManager.AppSettings["udid"] + " push " + ConfigurationManager.AppSettings["IapPidLoc"] + ConfigurationManager.AppSettings["udid"] + ".txt" + " /sdcard/iappid.txt");
                ExecuteProcess("/c adb -s " + ConfigurationManager.AppSettings["udid"] + " shell am start -n " + ConfigurationManager.AppSettings["PerformanceAppPackage"] + "/" + ConfigurationManager.AppSettings["PerformanceAppActivity"]); 
            }

            //installing application on the device
            string filepath = ConfigurationManager.AppSettings["Destination"];
            mConfig.APP_PACKAGE = ConfigurationManager.AppSettings["AppPackage"];
            mConfig.APP_ACTIVITY = ConfigurationManager.AppSettings["AppActivity"];
            Application.Launch(mConfig);
        }

        /// <summary>
        /// Configures and start the Appium Server
        /// </summary>
        public static void LoadiOSConfiguration()
        {
            MobileDriverConfig mDriverConfig = new MobileDriverConfig();
            Application.Launch(mDriverConfig);

        }

        
        /// <summary>
        /// Downloads the file from the server
        /// Starts the Appium Server
        /// Unlocks the Device
        /// Uninstall and Reinstalls the server version of the apk
        /// Launches the two applications
        /// </summary>
        /// <param name="mConfig">mConfig represents mobile driver configuration object</param>
        public static void LoadConfiguration(MobileDriverConfig mConfig)
        {
            //creating directory on the system if it is not already
            if (!Directory.Exists(Path.GetDirectoryName(ConfigurationManager.AppSettings["Destination"])))
            {
                Directory.CreateDirectory(Path.GetDirectoryName(ConfigurationManager.AppSettings["Destination"]));
            }

            //downloading the apk files from server
            bool downloadFile = false;
            downloadFile = DownloadApplicationUnderTest(ConfigurationManager.AppSettings["Source"], ConfigurationManager.AppSettings["IapApkLoc"] + "_" + ConfigurationManager.AppSettings["udid"] + ".apk");
            
            Device.Unlock(mConfig.DEVICE_ID);
            //start appium server
            processId = Application.StartAppiumServer(mConfig);
            UtilityAuto.ThreadSleep(2000);
            if (downloadFile)
            {
                UninstallApkFromDevice(mConfig.APP_PACKAGE, mConfig.DEVICE_ID);
                InstallApkToDevice(ConfigurationManager.AppSettings["IapApkLoc"] + "_" + ConfigurationManager.AppSettings["udid"] + ".apk", mConfig.DEVICE_ID);
            }
            //installing and launching start activity of performance apk on the device for iap app monitoring.
            if (ConfigurationManager.AppSettings["IsPerformanceMonitoringEnable"].Equals("true"))
            {
                new WebClient().DownloadFile(ConfigurationManager.AppSettings["PerformanceApkServer"], ConfigurationManager.AppSettings["PerformanceApkLoc"] + "_" + ConfigurationManager.AppSettings["udid"] + ".apk");

                mConfig.APP_PACKAGE = ConfigurationManager.AppSettings["PerformanceAppPackage"];
                mConfig.APP_ACTIVITY = ConfigurationManager.AppSettings["PerformanceAppActivity"];
                UninstallApkFromDevice(mConfig.APP_PACKAGE, mConfig.DEVICE_ID);
                InstallApkToDevice(ConfigurationManager.AppSettings["PerformanceApkLoc"] + "_" + ConfigurationManager.AppSettings["udid"] + ".apk", mConfig.DEVICE_ID);

                ExecuteProcess("/c adb -s " + ConfigurationManager.AppSettings["udid"] + " shell am start -n " + ConfigurationManager.AppSettings["AppPackage"] + "/" + ConfigurationManager.AppSettings["AppActivity"]);
                ExecuteProcess("/c adb -s " + ConfigurationManager.AppSettings["udid"] + " shell ps " + ConfigurationManager.AppSettings["AppPackage"].Substring(ConfigurationManager.AppSettings["AppPackage"].Length - 15, 15));
                string[] words = output.Split(' ');
                string pid = words[32];
                string uid = "10" + words[29].Substring(words[29].Length - 3, 3);
                System.IO.File.WriteAllText(ConfigurationManager.AppSettings["IapPidLoc"] + ConfigurationManager.AppSettings["udid"] + ".txt", pid + "," + uid);
                ExecuteProcess("/c adb -s " + ConfigurationManager.AppSettings["udid"] + " push " + ConfigurationManager.AppSettings["IapPidLoc"] + ConfigurationManager.AppSettings["udid"] + ".txt" + " /sdcard/iappid.txt");
                ExecuteProcess("/c adb -s " + ConfigurationManager.AppSettings["udid"] + " shell am start -n " + ConfigurationManager.AppSettings["PerformanceAppPackage"] + "/" + ConfigurationManager.AppSettings["PerformanceAppActivity"]);
            }


            //installing application on the device
            string filepath = ConfigurationManager.AppSettings["Destination"];
            mConfig.APP_PACKAGE = ConfigurationManager.AppSettings["AppPackage"];
            mConfig.APP_ACTIVITY = ConfigurationManager.AppSettings["AppActivity"];
            Application.Launch(mConfig);
        }

        /// <summary>
        /// This should be called when the app is already installed in the device.
        /// This starts the Appium server and launches the two applications
        /// </summary>
        public static void LoadConfigurationForDebug()
        {
            MobileDriverConfig mConfig = new MobileDriverConfig();
            Device.Unlock(mConfig.DEVICE_ID);
            //Pattabhi: Added Closing the Appium Server ( Will it affect Parallel Execution?? PLease check.
            UtilityAuto.CloseAppiumServer();
            //start appium server
            processId = Application.StartAppiumServer(mConfig);
            UtilityAuto.ThreadSleep(2000);
           
            Application.Launch(mConfig);
            
        }
     
        /// <summary>
        /// Stops the Performance monitor
        /// Pulls the Performance log from the device to the local server
        /// Deletes the Performance log from the device
        /// Closes the Appium server
        /// Uninstalls Apk from the device
        /// </summary>
        public static void CloseAll()
        {
            MobileDriverConfig mConfig = new MobileDriverConfig();
            if (ConfigurationManager.AppSettings["IsPerformanceMonitoringEnable"].Equals("true"))
            {
                mConfig.APP_PACKAGE = ConfigurationManager.AppSettings["PerformanceAppPackage"];
                mConfig.APP_ACTIVITY = ConfigurationManager.AppSettings["PerformanceAppActivity"];
                //Application.Launch(mConfig);
                ExecuteProcess("/c adb -s " + ConfigurationManager.AppSettings["udid"] + " shell am start -n " + ConfigurationManager.AppSettings["PerformanceAppPackage"] + "/" + ConfigurationManager.AppSettings["PerformanceAppActivity"]);
                ExecuteProcess("/c adb -s " + ConfigurationManager.AppSettings["udid"] + " shell input tap 600 460");
                Thread.Sleep(5000);
                //Pull the performance logs from device to local system
                if (!Directory.Exists(Path.GetDirectoryName(ConfigurationManager.AppSettings["PerfMonLogFolder"])))
                {
                    Directory.CreateDirectory(Path.GetDirectoryName(ConfigurationManager.AppSettings["PerfMonLogFolder"]));
                }
                string movePerformanceLogToLocal = "/c adb -s " + ConfigurationManager.AppSettings["udid"] + " pull /sdcard/PERFORMANCE_LOGS_CSV " + ConfigurationManager.AppSettings["PerfMonLogFolder"] + ConfigurationManager.AppSettings["udid"] + "_" + DateTime.Now.ToString("yyyyMMddHHmmssfff");
                ExecuteProcess(movePerformanceLogToLocal);
                ExecuteProcess("/c adb -s " + ConfigurationManager.AppSettings["udid"] + " shell rm -r /sdcard/PERFORMANCE_LOGS_CSV");
                //ExecuteProcess("del " + ConfigurationManager.AppSettings["IapApkLoc"] + "*.apk");
                //ExecuteProcess("del " + ConfigurationManager.AppSettings["PerformanceApkLoc"] + "*.apk");
            }
            mConfig.APP_PACKAGE = ConfigurationManager.AppSettings["AppPackage"];
            mConfig.APP_ACTIVITY = ConfigurationManager.AppSettings["AppActivity"];
            Application.Close();
            UninstallApkFromDevice(ConfigurationManager.AppSettings["AppPackage"], ConfigurationManager.AppSettings["udid"]);
            //UtilityAuto.CloseAppiumServer(processId);
            //close the appium server (to kill the process node on system) :: for parallel execution need to change killing process by process id.
            UtilityAuto.CloseAppiumServer();
        }


        /// <summary>
        /// Checks if the latest build is present in the local folder. 
        /// If the latest build is not present, the apk is downloaded from the server
        /// </summary>
        /// <param name="serverUrl">serverUrl is the source from which the apk needs to be downloaded</param>
        /// <param name="destination">destination represents the location where the latest apk is downloaded</param>
        /// <returns>a boolean value</returns>
        public static bool DownloadApplicationUnderTest(string serverUrl, string destination)
        {
            try
            {
                bool latest = false;
                try
                {
                    new WebClient().DownloadFile(ConfigurationManager.AppSettings["ServerBuildFileLoc"], ConfigurationManager.AppSettings["ServerBuild"]);
                }
                catch (Exception e)
                {
                    e = e;
                }
                if (System.IO.File.Exists(destination))
                {
                    if (System.IO.File.Exists(ConfigurationManager.AppSettings["ServerBuild"]) && System.IO.File.Exists(ConfigurationManager.AppSettings["LocalBuild"]))
                    {
                        string serverBuild = System.IO.File.ReadAllText(ConfigurationManager.AppSettings["ServerBuild"]);
                        string localBuild = System.IO.File.ReadAllText(ConfigurationManager.AppSettings["LocalBuild"]);
                        if (serverBuild.Equals(localBuild))
                            latest = true;
                        else
                        {
                            latest = false;
                            System.IO.File.Copy(ConfigurationManager.AppSettings["ServerBuild"], ConfigurationManager.AppSettings["LocalBuild"], true);
                        }
                    }
                    else
                        latest = false;
                    if (!latest)
                    {
                        new WebClient().DownloadFile(serverUrl, destination);
                    }
                    return true;
                }
                else
                {
                    System.IO.File.Copy(ConfigurationManager.AppSettings["ServerBuild"], ConfigurationManager.AppSettings["LocalBuild"], true);
                    new WebClient().DownloadFile(serverUrl, destination);
                    return true;
                }
            }
            catch (Exception e)
            {
                return false;
            }
            
        }

        /// <summary>
        /// Uninstalls the apk from the device
        /// </summary>
        /// <param name="packageName">packageName represents the package that has to be deleted from the device</param>
        /// <param name="deviceId">deviceId represents the device ID</param>
        /// <returns>a boolean value</returns>
        public static bool UninstallApkFromDevice(string packageName, string deviceId)
        {
            Logger.Debug("Un-installing application from the device");
            string command = "/c adb -s " + deviceId + " uninstall " + packageName;
            int code = ExecuteProcess(command);
            if (code == 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        /// <summary>
        /// Installs apk to the device
        /// </summary>
        /// <param name="apkPath">path where the apk should be installed</param>
        /// <param name="deviceId">Device ID </param>
        /// <returns>a boolean value</returns>
        public static bool InstallApkToDevice(string apkPath, string deviceId)
        {
            Logger.Debug("Installing IAP application to the device");
            string command = "/c adb -s " + deviceId + " install " + apkPath;
            int code = ExecuteProcess(command);
            if (code == 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        /// <summary>
        /// Executes commands in the command line 
        /// </summary>
        /// <param name="cmd">cmd represents the command that has to be executed</param>
        /// <returns>an integer value</returns>
        public static int ExecuteProcess(string cmd)
        {
            ProcessStartInfo info = new ProcessStartInfo("cmd.exe", cmd);
            info.CreateNoWindow = true;
            info.WindowStyle = ProcessWindowStyle.Hidden;
            info.RedirectStandardOutput = true;
            info.RedirectStandardError = true;
            info.UseShellExecute = false;
            
            Process proc = Process.Start(info);
            output = proc.StandardOutput.ReadToEnd();
            Logger.Debug(proc.StandardOutput.ReadToEnd());
            Logger.Debug(proc.StandardError.ReadToEnd());

            proc.WaitForExit();
            KillProcess(proc.Id);
            System.Threading.Thread.Sleep(6000);
            return proc.ExitCode;
        }
        /// <summary>
        /// Kills the specified process
        /// </summary>
        /// <param name="processId">processId represents the process ID to be killed</param>
        public static void KillProcess(int processId)
        {
            try
            {
                var process = Process.GetProcessById(processId);
                if (process != null)
                {
                    process.Kill();
                }
            }
            catch (Exception ex)
            {
                Logger.Debug("Exception while Killing Process: " + processId + " Exception: " + ex.Message);
            }
        }

        /// <summary>
        ///Configures and start the Appium Server
        ///Launches the application
        /// </summary>
        public static void LoadConfigurationForiOS()
        {
            MobileDriverConfig mConfig = new MobileDriverConfig();
            //Application.StartAppiumOnMac(mConfig);
            Application.Launch(mConfig);
        }

        /// <summary>
        /// Starts the Appium Server
        ///Launches the application
        /// </summary>
        /// <param name="mConfig">mConfig represents mobile driver configuration object</param>
        public static void LoadConfigurationForiOS(MobileDriverConfig mConfig)
        {
            //Application.StartAppiumOnMac(mConfig);
            Application.Launch(mConfig);
        }
    }
}
