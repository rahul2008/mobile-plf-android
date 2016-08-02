using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Diagnostics;
using System.IO;

namespace Philips.MRAutomation.Foundation.LoggingUtility
{
    /// <summary>
    /// Class which monitors and records performance of the system using typeperf tool
    /// </summary>
    public class PerformanceMonitor
    {
        private static bool firstCall = true;
        /// <summary>
        /// 
        /// </summary>
        public static string outputFile = String.Empty;

        /// <summary>
        /// TypePerf process ID intailly it will be 0; 
        /// </summary>
        public static int typePerfProcessID = 0;
        
        /// <summary>
        /// 
        /// </summary>
        public static string outputFolder
        {
            get
            {
                if (mOutputFolder == string.Empty)
                {
                    mOutputFolder =
                        "Reports\\" + "PerformanceReadings_" +
                        DateTime.Now.ToString("MM/dd/yyyyHH:mm:ss").Replace("/", "").Replace(":", "") + "\\";
                }
                return mOutputFolder;
            }

        }
        private static string mOutputFolder = string.Empty;
        
        /// <summary>
        /// 
        /// </summary>
        /// <param name="fileName"></param>
        public static void StartMonitoring(string fileName)
        {
            outputFile = fileName;
            StartMonitoring();
        }

        /// <summary>
        /// 
        /// </summary>
        public static void StartMonitoring ()
        {
            if (firstCall)
            {
                FileLogger.Log(FileLogger.Severity.Information, 
                    "Starting Performance monitoring. New typeperf will be started");
                firstCall = false;
            }
            try
            {
                Process[] ps = Process.GetProcessesByName("typeperf");
                if (outputFile == string.Empty)
                {
                    outputFile = "PerformanceReadings_" +
                        DateTime.Now.ToString("MM/dd/yyyyHH:mm:ss").Replace("/", "").Replace(":", "") + ".csv";
                }                    
                    if (File.Exists(outputFile))
                    {
                        File.Delete(outputFile);
                    }
                    if (!Directory.Exists(outputFolder))
                    {
                        Directory.CreateDirectory(outputFolder);
                    }
                    Process p = new Process();
                    p.StartInfo.FileName = "typeperf";
                    p.StartInfo.Arguments = "-cf Data\\PerformanceMonitor.txt -si 10 -o " + outputFolder +
                        outputFile;
                    p.StartInfo.UseShellExecute = true;
                    p.StartInfo.WindowStyle = ProcessWindowStyle.Hidden;
                    p.Start();
                    typePerfProcessID = p.Id;
                    FileLogger.Log(FileLogger.Severity.Information, "Automation started typeperf. ProcessID: " + p.Id);
                    FileLogger.Log(FileLogger.Severity.Information, "Automation typeperf is running. outputFile: " + outputFile);
            }
            catch (Exception e)
            {
                FileLogger.Log(FileLogger.Severity.Exception,
                    "Exception while starting typeperf: " + e.ToString());
            }
        }
        /// <summary>
        /// 
        /// </summary>
        public static void StopMonitoring()
        {
            try
            {
                Process[] ps = Process.GetProcessesByName("typeperf");                
                for (int i = 0; i < ps.Length; i++)
                {
                    Process p = ps[i];
                    outputFile = string.Empty;
                    if (p.Id.Equals(typePerfProcessID))
                    {
                        FileLogger.Log(FileLogger.Severity.Information, 
                            "Killing typeperf process with pid: " + p.Id);
                        p.Kill();
                        break;
                    }
                }
            }
            catch (Exception e)
            {
                FileLogger.Log(FileLogger.Severity.Exception,
                    "Exception while stopping typeperf: " + e.ToString());
            }
        }
        /// <summary>
        /// 
        /// </summary>
        public static void RestartMonitoring()
        {
            StopMonitoring();
            StartMonitoring();
        }
    }
}
