using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Mail;
using System.Text;
using System.Threading.Tasks;

namespace Philips.H2H.Foundation.BuildAgent
{
    public class Common
    {
        public int RunPowerShellScript(string powershellScriptFilePath)
        {
            try
            {
                string command = "/c powershell -executionpolicy unrestricted " + powershellScriptFilePath;
                int output = ExecuteProcess(command);
                return output;
            }
            catch (Exception e)
            {
                WriteToLog("Exception while running Powershell script: " + e.StackTrace);
                return 1;
            }            
        }

        public int RunAntBuildScript(string antscriptFilePath)
        {
            try
            {
                string command = "/c ant -buildfile " + antscriptFilePath;
                return ExecuteProcess(command);
            }
            catch (Exception e)
            {
                WriteToLog("Exception while running Ant script: " + e.StackTrace);
                return 1;
            }
        }

        public void CopyDirectory(string source, string dest)
        {
            WriteToLog("Build Location: " + source);

            try
            {
                if (!Directory.Exists(dest))
                {
                    Directory.CreateDirectory(dest);
                }
                DirectoryInfo dirInfo = new DirectoryInfo(source);
                FileInfo[] files = dirInfo.GetFiles();
                foreach (FileInfo tempfile in files)
                {
                    WriteToLog("Coping File from Build Location: " + tempfile.Name);
                    tempfile.CopyTo(Path.Combine(dest, tempfile.Name), true);
                }
                DirectoryInfo[] dirctororys = dirInfo.GetDirectories();
                foreach (DirectoryInfo tempdir in dirctororys)
                {
                    if (tempdir.Name.Equals("apache-ant-1.9.3"))
                    {
                        //skip copy for this folder if already exist
                        if (!Directory.Exists(dest + "/apache-ant-1.9.3"))
                        {
                            CopyDirectory(Path.Combine(source, tempdir.Name), Path.Combine(dest, tempdir.Name));
                        }
                    }
                    else
                    {
                        CopyDirectory(Path.Combine(source, tempdir.Name), Path.Combine(dest, tempdir.Name));
                    }
                }
            }
            catch (Exception e)
            {
                WriteToLog("While Coping build files from Build Server: "+e.StackTrace);
                throw e;
            }
        }

        public int ToggleLANConnection(bool isEnable)
        {
            string cmd = string.Empty;
            if (isEnable)
            {
                cmd = "/C netsh interface set interface name=\"Local Area Connection\" admin=ENABLED";               
            }
            else
            {
                cmd = "/C netsh interface set interface name=\"Local Area Connection\" admin=DISABLED";                
            }
            
            int code = ExecuteProcess(cmd);
            System.Threading.Thread.Sleep(6000);
            return code;
        }

        public int ExecuteProcess(string cmd)
        {
            ProcessStartInfo info = new ProcessStartInfo("cmd.exe", cmd);
            info.CreateNoWindow = true;
            info.WindowStyle = ProcessWindowStyle.Hidden;
            info.RedirectStandardOutput = true;
            info.RedirectStandardError = true;
            info.UseShellExecute = false;

            Process proc = Process.Start(info);
            WriteToLog(proc.StandardOutput.ReadToEnd());
            WriteToLog(proc.StandardError.ReadToEnd());
            
            proc.WaitForExit();
            System.Threading.Thread.Sleep(6000);
            return proc.ExitCode;
        }

        public string ExecuteProcessAndReturnOutput(string cmd)
        {
            ProcessStartInfo info = new ProcessStartInfo("cmd.exe", cmd);
            info.CreateNoWindow = true;
            info.WindowStyle = ProcessWindowStyle.Hidden;
            info.RedirectStandardOutput = true;
            info.RedirectStandardError = true;
            info.UseShellExecute = false;

            Process proc = Process.Start(info);
            string output = proc.StandardOutput.ReadToEnd();
            string outError = proc.StandardError.ReadToEnd();

            proc.WaitForExit();
            System.Threading.Thread.Sleep(6000);
            return output+outError;
        }

        public static void WriteToLog(string msg)
        {
            using (StreamWriter writer = new StreamWriter(Runner.logfileLoc,true))
            {
                Console.SetOut(writer);
                Console.WriteLine(msg);
            }
        }

        public string ReadStringFromFile(string filePath)
        {
            using (System.IO.StreamReader file = new System.IO.StreamReader(filePath))
            {
                return file.ReadToEnd();
            }
        }

        public bool UninstallApkFromDevice(string packageName)
        {
            Common.WriteToLog("Un-installing application from the device");
            string command = "/c adb uninstall " + packageName;
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

        public bool InstallApkToDevice(string apkPath)
        {
            Common.WriteToLog("Installing IAP application to the device");
            string command = "/c adb install " + apkPath;
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

        public bool RunUIAutomationTest(string uiAutomatorJarPath, string uiAutiomationTestCmd)
        {
            Common.WriteToLog("Pushing UIAutomator JAR to the Android Test device");
            string command = "/c adb push " + uiAutomatorJarPath +" /data/local/tmp/";
            int testCode = ExecuteProcess(command);
            if (testCode==0)
            {
                string tstCmd = "/c adb shell " + uiAutiomationTestCmd;
                string outPutCode = ExecuteProcessAndReturnOutput(tstCmd);
                WriteToLog(outPutCode);
                if (!outPutCode.Contains("INSTRUMENTATION_STATUS_CODE: -1"))
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

        public static void SendMail(string buildNumber)
        {
            try
            {
                string subject = "ECC1.1.1 Build# " + buildNumber + " deployed in eccqa1 org";
                MailMessage mail = new MailMessage();
                SmtpClient SmtpServer = new SmtpClient("161.85.26.5", 25);

                mail.From = new MailAddress("cdptestserver@philips.com");
                mail.To.Add("dibyaranjan.kar@philips.com");
                mail.Subject = subject;
                SmtpServer.Credentials = new System.Net.NetworkCredential("cdptestserver@philips.com", "");
                SmtpServer.Send(mail);
            }
            catch (Exception)
            {
                throw;
            }
        }

        public static bool DownloadApplicationUnderTest(string serverUrl, string destination)
        {
            try
            {
                new WebClient().DownloadFile(serverUrl, destination);
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }

    }
}
