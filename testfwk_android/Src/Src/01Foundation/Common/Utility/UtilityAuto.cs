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
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Threading;
using System.Diagnostics;
using System.Net.Sockets;
using System.IO;
using System.Net.Mail;
using System.Configuration;
using System.Net;
using System.Globalization;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using System.Management.Automation.Runspaces;
using System.Management.Automation;
//using System.Data.OleDb;
//using System.Data;

namespace Philips.H2H.Foundation.AutomationCore
{
    public class UtilityAuto
    {

        public enum NetworkConnection { LocalAreaConnection, WirelessNetworkConnection }

        /// <summary>
        /// Suspends the current thread for specified time.
        /// </summary>
        /// <param name="miliseconds"></param>
        public static void ThreadSleep(int miliseconds)
        {
            try
            {
                Logger.Debug("Waiting for " + miliseconds + " milliseconds");
                Thread.Sleep(miliseconds);
                Logger.Debug("Waiting completed after " + miliseconds + " milliseconds");
            }
            catch (Exception)
            {
                throw;
            }
        }

        public static void ThreadSleep(string seconds)
        {
            try
            {
                int sec = Int32.Parse(seconds);
                int miliseconds = sec * 1000;
                Logger.Debug("Sleeping started for " + sec + " seconds");
                Thread.Sleep(miliseconds);
                Logger.Debug("Sleeping completed after " + sec + " seconds");
            }
            catch (Exception)
            {
                throw;
            }
        }

        /// <summary>
        /// Gets an Unique Number based on the time stamp.
        /// </summary>
        /// <returns></returns>
        public static int GetUniqueNumber()
        {
            return (int)DateTime.Now.Ticks;
        }

        /// <summary>
        /// Creates a Directory if does not exist in the given path
        /// </summary>
        /// <param name="path">returns DirectoryInfo</param>
        public static DirectoryInfo CreateDirectoryIfNotExist(string path)
        {
            if (!Directory.Exists(path))
            {
                return Directory.CreateDirectory(path);
            }
            else { return new DirectoryInfo(path); }
        }

        public static string GetRandomString()
        {
            return Guid.NewGuid().ToString("n");
        }

        /// <summary>
        /// Checks that Server is up and connected
        /// </summary>
        /// <param name="server">address of the server</param>
        /// <param name="port">port number</param>
        /// <returns></returns>
        public static bool CheckServerStatus(string server, string port)
        {
            try
            {
                TcpClient client = new TcpClient(server, Int32.Parse(port));
                return client.Connected;
            }
            catch (Exception)
            {
                return false;
            }
        }

        /// <summary>
        /// Starts appium server
        /// </summary>
        public static int StartAppiumServer(MobileDriverConfig config)
        {
            bool isUp = UtilityAuto.CheckServerStatus(config.SERVER, config.PORT);
            if (isUp)
            {
                Logger.Debug("One Instance of Appium Server is already running / Closing all existing running instances.");
                CloseAppiumServer();
            }
            else
            {
                Logger.Debug("Starting Appium server instance. please wait............");
            }
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

            isUp = CheckAppiumServerRunning(config);
            if (isUp)
            {
                Logger.Debug("Appium Server is Started, Continuing to for Test Execution");
            }
            else
            {
                Logger.Fail("Appium Server could not be started/ Test cases will be failed.");
            }
            return GetProcessId("node")[0];
        }

        /// <summary>
        /// Stops appium server
        /// </summary>
        public static void CloseAppiumServer(int processId)
        {
            Logger.Debug("Closing running Appium server instance....");
            KillProcess(processId);
        }

        public static void CloseAppiumServer()
        {
            Logger.Debug("Closing running Appium server instance....");
            KillProcess("node");
        }

        public static void CloseIEDriverServer()
        {
            Logger.Debug("Closing running IE Driver server instance....");
            KillProcess("IEdriverServer.exe");
        }

        /// <summary>
        /// Checks if the appium server is running
        /// </summary>
        /// <returns>true if appium server is running else false</returns>
        public static bool CheckAppiumServerRunning(MobileDriverConfig config)
        {
            bool isUp = false;
            int time = 40000;
            int ctr = time / AutomationConstants.PING_TIME;
            int i = 0;

            isUp = UtilityAuto.CheckServerStatus(config.SERVER, config.PORT);
            while (!isUp & i < ctr)
            {
                Logger.Debug("Appium Server Running Status: " + isUp);
                UtilityAuto.ThreadSleep(AutomationConstants.PING_TIME);
                isUp = UtilityAuto.CheckServerStatus(config.SERVER, config.PORT);
                if (isUp)
                {
                    break;
                }
                else
                {
                    i++;
                }
            }
            return isUp;
        }

        public static void KillProcess(string processName)
        {
            try
            {
                //string sProcessName = Path.GetFileNameWithoutExtension(processName);
                var process = Process.GetProcessesByName(processName);
                foreach (Process nextProcess in process)
                {
                    nextProcess.Kill();
                }
            }
            catch (Exception ex)
            {
                Logger.Debug("Exception while Killing Process: " + processName + " Exception Stacktrace: " + ex.StackTrace);
            }
        }

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
        /// Uninstall APK from the device
        /// </summary>
        /// <param name="packageName">Name of the package</param>
        /// <returns>true if the operation is success else false</returns>
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
        /// Install android application in the device
        /// </summary>
        /// <param name="apkPath">path of the apk file</param>
        /// <returns></returns>
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

        public static int ExecuteProcess(string cmd)
        {
            ProcessStartInfo info = new ProcessStartInfo("cmd.exe", cmd);
            info.CreateNoWindow = true;
            info.WindowStyle = ProcessWindowStyle.Hidden;
            info.RedirectStandardOutput = true;
            info.RedirectStandardError = true;
            info.UseShellExecute = false;

            Process proc = Process.Start(info);
            Logger.Debug(proc.StandardOutput.ReadToEnd());
            Logger.Debug(proc.StandardError.ReadToEnd());

            proc.WaitForExit();
            KillProcess(proc.Id);
            System.Threading.Thread.Sleep(6000);
            return proc.ExitCode;
        }

        public static void ExecuteProcessNoWait(string cmd)
        {
            ProcessStartInfo info = new ProcessStartInfo("cmd.exe", cmd);
            info.CreateNoWindow = true;

            info.WindowStyle = ProcessWindowStyle.Hidden;
            info.RedirectStandardOutput = true;
            info.RedirectStandardError = true;
            info.UseShellExecute = false;
            Process proc = Process.Start(info);
            System.Threading.Thread.Sleep(6000);
        }

        public static bool RunUIAutomationTest(string uiAutomatorJarPath, string uiAutiomationTestCmd, string deviceId)
        {
            Logger.Debug("Pushing UIAutomator JAR to the Android Test device");
            //string command = "/c adb push " + uiAutomatorJarPath + " /data/local/tmp/";
            int code = Device.CopyFileToDevice(uiAutomatorJarPath, "/data/local/tmp/", deviceId);

            if (code == 0)
            {
                string tstCmd = "/c adb -s " + deviceId + " shell uiautomator runtest " + uiAutiomationTestCmd;
                int testCode = ExecuteProcess(tstCmd);
                if (testCode == 0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }

        /// <summary>
        /// Checks two dictionary and compares the contents of it.        
        /// </summary>
        /// <param name="dict1"></param>
        /// <param name="dict2"></param>
        /// <returns></returns>
        public static bool CompareDictionary(Dictionary<string, string> dict1, Dictionary<string, string> dict2)
        {
            int flag = 0;
            if (dict1.Count != dict2.Count)
                return false;
            foreach (KeyValuePair<string, string> pr1 in dict1)
            {
                string key1 = pr1.Key;
                string value1 = pr1.Value;
                foreach (KeyValuePair<string, string> pr2 in dict2)
                {
                    string key2 = pr2.Key;
                    string value2 = pr2.Value;
                    if (key1.Equals(key2))
                    {
                        if (value1.Equals(value2))
                        {
                            Logger.Debug("Source Data Set :1 Key: [" + key1 + "] Value : [" + value1 + "]");
                            Logger.Debug("Source Data Set :2 Key: [" + key2 + "] Value : [" + value2 + "]");
                            flag++;
                            break;
                        }
                    }
                }
            }
            if (flag == dict1.Count)
                return true;
            else
                return false;
        }

        /// <summary>
        /// Starts adb logcat to record adb logs
        /// </summary>
        public static void StartADBLogCat()
        {
            Logger.Debug("Starting to collect logcat of the application");
            if (File.Exists(AutomationConstants.LOG_PATH + "//adblogcat.log"))
            {
                File.Delete(AutomationConstants.LOG_PATH + "//adblogcat.log");
            }
            ProcessStartInfo info = new ProcessStartInfo(AutomationConstants.UI_AUTOMATION + "//logcat.bat");
            info.Arguments = AutomationConstants.LOG_PATH + "//adblogcat.log";
            info.WindowStyle = ProcessWindowStyle.Hidden;
            info.UseShellExecute = false;
            Process.Start(info);
        }

        public static DateTime ConvertDateTimeToSpecificTimeZone(DateTime sourceDateTime, string timeZone)
        {
            var est = TimeZoneInfo.FindSystemTimeZoneById(timeZone);
            if (est.Id != timeZone)
            {
                return TimeZoneInfo.ConvertTime(sourceDateTime, est);
            }
            else
            {
                return sourceDateTime;
            }
        }

        public static void ReadyDeviceWithLatestApk(string ApkName, string packageName, string deviceId)
        {
            UninstallApkFromDevice(packageName, deviceId);
            InstallApkToDevice(AutomationConstants.APK_PATH + ApkName, deviceId);
        }
        
        /// <summary>
        /// 
        /// </summary>
        /// <param name="adbCommands"></param>
        /// <param name="text"></param>
        /// <returns></returns>
        public static int ExecuteAdbCommand(AutomationConstants.AdbShellCommand adbCommands, string text, string deviceId)
        {
            int retCode = 1;
            switch (adbCommands)
            {
                case AutomationConstants.AdbShellCommand.InputText:
                    retCode = ExecuteProcess("/c adb -s " + deviceId + " shell input text " + text);
                    break;
                case AutomationConstants.AdbShellCommand.InputEnterKey:
                    retCode = ExecuteProcess("/c adb -s " + deviceId + " shell input keyevent ENTER ");
                    break;
                case AutomationConstants.AdbShellCommand.InputTabKey:
                    retCode = ExecuteProcess("/c adb -s " + deviceId + " shell input keyevent TAB ");
                    break;
                case AutomationConstants.AdbShellCommand.InputKeyCode:
                    retCode = ExecuteProcess("/c adb -s " + deviceId + " shell input keyevent " + text);
                    break;
            }
            return retCode;
        }
        /// <summary>
        /// Stops Selenium server
        /// </summary>
        public static void CloseSeleniumServer(BrowserDriver.BrowserType type)
        {
            Logger.Debug("Closing running Selenium server instance....");
            switch (type)
            {
                case BrowserDriver.BrowserType.InternetExplorer:
                    KillProcess("IEDriverServer.exe");
                    KillProcess("iexplore.exe");
                    break;
                case BrowserDriver.BrowserType.Firefox:
                    break;
                case BrowserDriver.BrowserType.Chrome:
                    KillProcess("chromedriver.exe");
                    break;
                default:
                    break;
            }
        }
        //Method returns DateTime object from String which contains date and time eg. YYYY-MM-DDThh:mm:ssTZD where T is Times and TZD is Time zone
        public static DateTime GetDateTimeObjectFromString(string datetime)
        {
            return DateTime.Parse(datetime, null, DateTimeStyles.RoundtripKind);
        }

        public static string GetRealDateFromString(string dateString)
        {
            string actualDate = dateString;
            try
            {
                switch (dateString)
                {
                    case "Today":
                        actualDate = DateTime.Today.ToShortDateString();
                        break;
                    case "Yesterday":
                        actualDate = DateTime.Today.AddDays(-1).ToShortDateString();
                        break;
                    case "Tomorrow":
                        actualDate = DateTime.Today.AddDays(1).ToShortDateString();
                        break;
                }
            }
            catch (Exception e)
            {
                Logger.Debug("Exception while getting real date : " + e);
            }
            return actualDate;
        }

        public static string ConvertStringArrayToString(string[] array)
        {
            //
            // Concatenate all the elements into a StringBuilder.
            //
            StringBuilder builder = new StringBuilder();
            foreach (string value in array)
            {
                if (builder.Length == 0)
                {
                    builder.Append(value);
                }
                else
                {
                    builder.Append("|" + value);
                }
            }
            return builder.ToString();
        }

        public static int ToggleNetworkConnection(NetworkConnection connectionType, bool isEnable)
        {
            string connection = null;
            string cmd = null;

            switch (connectionType)
            {
                case NetworkConnection.LocalAreaConnection:
                    connection = "Local Area Connection";
                    break;
                case NetworkConnection.WirelessNetworkConnection:
                    connection = "Wireless Network Connection";
                    break;
            }

            if (isEnable)
            {
                Logger.Debug("Enabling Network connection: " + connection);
                cmd = "/C netsh interface set interface name=\"" + connection + "\" admin=ENABLED";
            }
            else
            {
                Logger.Debug("Disabling Network connection: " + connection);
                cmd = "/C netsh interface set interface name=\"" + connection + "\" admin=DISABLED";
            }
            Process netshProcess = StartAndReturnProcessInfo(cmd);
            netshProcess.WaitForExit(2000);
            int ctr = 0;
            while (ctr < 5)
            {
                bool actualStatus = CheckNetworkConnectionStatus(connectionType);
                if (isEnable == actualStatus)
                {
                    Logger.Debug("Status of Network connection: " + connection + " is: " + actualStatus);
                    break;
                }
                else
                {
                    Logger.Debug("Network status is not updated, trying agian");
                    netshProcess = StartAndReturnProcessInfo(cmd);
                    netshProcess.WaitForExit(2000);
                }
            }
            ThreadSleep(6000);
            return netshProcess.ExitCode;
        }

        public static bool CheckNetworkConnectionStatus(NetworkConnection connectionType)
        {
            string checkText = null;
            switch (connectionType)
            {
                case NetworkConnection.LocalAreaConnection:
                    checkText = "Configuration for interface \"Local Area Connection\"";
                    break;
                case NetworkConnection.WirelessNetworkConnection:
                    checkText = "Configuration for interface \"Wireless Network Connection\"";
                    break;
            }
            Process netshProcess = StartAndReturnProcessInfo("/c netsh interface ip show config");
            netshProcess.WaitForExit();
            string outputText = netshProcess.StandardOutput.ReadToEnd();
            if (outputText.Contains(checkText))
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        public static void DownloadFile(string file, string destPath)
        {
            using (WebClient myWebClient = new WebClient())
            {
                // Download the Web resource and save it into the current filesystem folder.                
                try
                {
                    myWebClient.DownloadFile(file, destPath);
                }
                catch (Exception)
                {
                    throw;
                }
                finally
                {
                    // sourceApkResponse.Close();
                }
            }
        }

        public static string[] RemoveDuplicates(string[] s)
        {
            HashSet<string> set = new HashSet<string>(s);
            string[] result = new string[set.Count];
            set.CopyTo(result);
            return result;
        }

        public static string CompleteStringWithPlaceHolder(string inText, string[] placeHolderValue)
        {
            int plHolderCnt = GetStringPlaceHolderCount(inText);
            if (placeHolderValue.Length < plHolderCnt)
            {
                //Return empty string to avoid exception (c# throws an exception when the placeholdervalues are less than placeholders count) here
                return "";
            }
            return string.Format(inText, placeHolderValue);
        }

        public static int GetStringPlaceHolderCount(string inText)
        {
            const string pattern = @"(?<!\{)(?>\{\{)*\{\d(.*?)";
            var matches = Regex.Matches(inText, pattern);
            int cnt = matches.Count;
            return cnt;
        }

        public static Process StartAndReturnProcessInfo(string cmd)
        {
            ProcessStartInfo info = new ProcessStartInfo("cmd.exe", cmd);
            info.CreateNoWindow = true;
            info.WindowStyle = ProcessWindowStyle.Hidden;
            info.RedirectStandardOutput = true;
            info.RedirectStandardError = true;
            info.UseShellExecute = false;
            Process proc = Process.Start(info);


            //Process p = new Process();
            //p.StartInfo.UseShellExecute = false;
            //p.StartInfo.CreateNoWindow = true;
            //p.StartInfo.FileName = "cmd.exe";
            //p.StartInfo.Arguments = cmd;
            //p.StartInfo.RedirectStandardError = true;
            //p.StartInfo.RedirectStandardOutput = true;
            //proc.Start();
            return proc;
        }

        public static List<int> GetProcessId(string processName)
        {
            try
            {
                List<Process> processlist = Process.GetProcesses().ToList();
                List<Process> refinedList = processlist.FindAll(d => d.ProcessName.Contains(processName)).OrderByDescending(j => j.StartTime).ToList();
                return refinedList.Select(k => k.Id).ToList();
            }
            catch (Exception)
            {
                throw;
            }
        }

        public static List<string> ExecutePowerShellScript(string scriptFilePath)
        {
            try
            {
                /*Remove Read-Host line from the file as 
                 *it is throwing exception while executing from script*/
                List<string> scriptFileLines = File.ReadAllLines(scriptFilePath).ToList();
                scriptFileLines.Remove("Read-Host");
                File.WriteAllLines(scriptFilePath, scriptFileLines);

                RunspaceConfiguration runspaceConfiguration = RunspaceConfiguration.Create();
                Runspace runspace = RunspaceFactory.CreateRunspace(runspaceConfiguration);
                runspace.Open();
                RunspaceInvoke scriptInvoker = new RunspaceInvoke(runspace);
                scriptInvoker.Invoke("Set-ExecutionPolicy Unrestricted");
                Pipeline pipeline = runspace.CreatePipeline();

                //Here's how you add a new script with arguments
                Command myCommand = new Command(scriptFilePath);
                pipeline.Commands.Add(myCommand);
                // Execute PowerShell script
                return pipeline.Invoke().ToList().Select(d => d.ToString()).ToList();
            }
            catch (Exception)
            {
                throw;
            }
        }
    }
}
