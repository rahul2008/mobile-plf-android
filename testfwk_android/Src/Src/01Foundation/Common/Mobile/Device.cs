using OpenQA.Selenium.Appium.Android;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Diagnostics;

namespace Philips.H2H.Foundation.AutomationCore.Mobile
{

    public class Device
    {
        public enum ConnectionType
        {
            WifiOn,
            WifiOff,
            DataOn,
            DataOff,
            AirplaneMode
        }
       

        public static void Unlock(string deviceId)
        {
            Logger.Debug("BIN Path of the Assembly: " + AutomationConstants.BIN_PATH);
            Logger.Debug("Log Path of the Test Execution: " + AutomationConstants.LOG_PATH);

            Logger.Debug("Unlocking Android device for test execution.....");
            Logger.Debug("UiAutomation Script Path: " + AutomationConstants.UI_AUTOMATION);
            bool code = UtilityAuto.RunUIAutomationTest(AutomationConstants.BIN_PATH + @"\UIAutomation\ECPUIAutomatorTest.jar", "ECPUIAutomatorTest.jar -c com.philips.uiautomator.eCP_DeviceAdminUiTest#testDeviceIsLocked", deviceId);
            if (code)
            {
                Logger.Debug("Android device is unlocked/Proceeding for test execution...");
            }
            else
            {
                Logger.Fail("Could not unlock the device, stopping Test Execution.");
            }
        }

        private int GetBatteryLevel()
        {
            return 0;
        }

        public static void LockDevice(int seconds)
        {
            MobileDriver._Mdriver.LockDevice(seconds);
        }

        public static bool IsDeviceLocked()
        {
            return MobileDriver._Mdriver.IsLocked();
        }

        public static ConnectionType GetConnectionType()
        {
            ConnectionType retType = ConnectionType.WifiOn;
            OpenQA.Selenium.Appium.Android.ConnectionType type = MobileDriver._Mdriver.ConnectionType;
            switch (type)
            {
                case OpenQA.Selenium.Appium.Android.ConnectionType.WifiOnly:
                    retType = ConnectionType.WifiOn;
                    break;
                case OpenQA.Selenium.Appium.Android.ConnectionType.DataOnly:
                    retType = ConnectionType.DataOn;
                    break;
                case OpenQA.Selenium.Appium.Android.ConnectionType.None:
                    retType = ConnectionType.WifiOff;
                    break;
            }
            return retType;
        }

        public static void SetConnectionType(Device.ConnectionType type)
        {
            switch (type)
            {
                case ConnectionType.WifiOn:
                    MobileDriver._Mdriver.ConnectionType = OpenQA.Selenium.Appium.Android.ConnectionType.WifiOnly;
                    break;
                case ConnectionType.WifiOff:
                    MobileDriver._Mdriver.ConnectionType = OpenQA.Selenium.Appium.Android.ConnectionType.DataOnly;
                    break;
                case ConnectionType.DataOn:
                    MobileDriver._Mdriver.ConnectionType = OpenQA.Selenium.Appium.Android.ConnectionType.DataOnly;
                    break;
                case ConnectionType.DataOff:
                    MobileDriver._Mdriver.ConnectionType = OpenQA.Selenium.Appium.Android.ConnectionType.None;
                    break;
                case ConnectionType.AirplaneMode:
                    MobileDriver._Mdriver.ConnectionType = OpenQA.Selenium.Appium.Android.ConnectionType.AirplaneMode;
                    break;
            }
        }

        public static int MoveFile(string source, string destination, string deviceId)
        {   string cmd = "/c adb -s " + deviceId + " shell mv " + source + " " + destination;
            Logger.Debug("Command executing by MoveFile: " + cmd);
            return UtilityAuto.ExecuteProcess(cmd);
        }

        public static void RecordScreen(string fileNamePath)
        {
            //string source="/sdcard"
            //Logger.Debug("Recording of the screen started now");
            //UtilityAuto.ExecuteProcess("/c adb shell screenrecord")
        }

        public static int CopyFileToDevice(string source, string destination, string deviceId)
        {
            Logger.Debug("Pushing file from source : " + source + " to destination : " + destination);
            string command = "/c adb -s " + deviceId +" push " + source + " " + destination;
            return UtilityAuto.ExecuteProcess(command);
        }

        public static int CopyFileFromDevice(string source, string destination, string deviceId)
        {
            Logger.Debug("Coping file from source : " + source + " to destination : " + destination);
            string command = "/c adb -s " + deviceId + " pull " + source + " " + destination;
            return UtilityAuto.ExecuteProcess(command);
        }


        public static int RemoveFileFromDevice(string filepath, string deviceId)
        {
            Logger.Debug("Remove file path : " + filepath);
            string command = "/c adb -s " + deviceId + " shell rm " + filepath;
            return UtilityAuto.ExecuteProcess(command);
        }

        public static string GetDirectoryContents(string yourDirectory, string deviceId)
        {
            string command = "/C adb -s " + deviceId + " shell ls " + yourDirectory;
            Process process = UtilityAuto.StartAndReturnProcessInfo(command);
            process.WaitForExit();
            //List<string> output = process.StandardOutput.ReadToEnd().Replace("\r", "").Replace("\r", "").Trim().Split('\n').ToList();
            string contents = process.StandardOutput.ReadToEnd().Replace("\r", "").Replace("\r", "").Trim().Split('\n').ToString();
            return contents;
            //======================

            ////adb shell ls -Ral yourDirectory | grep -i yourString
            //Logger.Debug("list file in the directory : " + yourDirectory);
            ////string command = "/c adb -s " + deviceId + " shell ls -Ral " + yourDirectory + " | grep -i " + yourString;
            //string command = "/c adb -s " + deviceId + " shell ls " + yourDirectory;
            //ProcessStartInfo info = new ProcessStartInfo("cmd.exe", command);
            //info.CreateNoWindow = true;
            //info.WindowStyle = ProcessWindowStyle.Hidden;
            //info.RedirectStandardOutput = true;
            //info.RedirectStandardError = true;
            //info.UseShellExecute = false;

            //Process proc = Process.Start(info);
            //Logger.Debug(proc.StandardOutput.ReadToEnd());
            //Logger.Debug(proc.StandardError.ReadToEnd());
            //string errContents = proc.StandardError.ReadToEnd();
            //string contents = proc.StandardOutput.ReadToEnd();
            
            //proc.WaitForExit();

            //System.Threading.Thread.Sleep(6000);

            //return contents;
        }

        private List<string> GetConnectedDevicesId()
        {
            List<string> devicesId = new List<string>();
            Process pInfo=UtilityAuto.StartAndReturnProcessInfo("/c adb devices");
            pInfo.WaitForExit();
            string msg= pInfo.StandardOutput.ReadToEnd();
            return devicesId;
        }
    }

}
