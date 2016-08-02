using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace Philips.MRAutomation.Foundation.LoggingUtility
{
    /// <summary>
    /// 
    /// </summary>
    public class MTBCLog
    {
        /// <summary>
        /// 
        /// </summary>
        public static string mtbcLogFile = string.Empty;

        /// <summary>
        /// Creates a LogFile to report crash information with the execution start time
        /// </summary>
        public static void Create()
        {
            try
            {
                if (mtbcLogFile == string.Empty)
                {
                    mtbcLogFile = "Reports\\MeanTimeBetweenCrash_" +
                        DateTime.Now.ToString("MM/dd/yyyyHH:mm").Replace("/", "").Replace(":", "") + ".csv";

                    TextWriter tw = new StreamWriter(mtbcLogFile);
                    string s = "Execution Start Time," + DateTime.Now.ToString();
                    tw.WriteLine(s);
                    tw.Close();
                }

            }
            catch
            {
            }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="dt"></param>
        /// <param name="runNumber"></param>
        /// <param name="totalRuns"></param>
        public static void Log(DateTime dt, int runNumber, int totalRuns)
        {
            try
            {
                string[] allLines = File.ReadAllLines(mtbcLogFile);
                TextWriter tw = new StreamWriter(mtbcLogFile, true);
                if (allLines.Length == 1)
                {
                    string s1 = "Crash No,Crash Time,Time B/W Crash (min),Run No,Total Runs";
                    tw.WriteLine(s1);
                }
                tw.Close();
                allLines = File.ReadAllLines(mtbcLogFile);

                string s2 = (allLines.Length - 1).ToString() +
                    "," + dt.ToString() +
                    "," + (dt - GetLastCrashTime()).TotalMinutes.ToString() +
                    "," + runNumber +
                    "," + totalRuns;
                tw = new StreamWriter(mtbcLogFile, true);
                tw.WriteLine(s2);
                tw.Close();
            }
            catch
            {
            }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        private static DateTime GetLastCrashTime()
        {            
            DateTime dt = DateTime.MinValue;
            try
            {
                if (mtbcLogFile != string.Empty)
                {
                    string[] allLines = File.ReadAllLines(mtbcLogFile);
                    bool continueLoop = true;
                    int i = allLines.Length;
                    while (continueLoop == true && i >= 0)
                    {
                        try
                        {
                            dt = Convert.ToDateTime(allLines[--i].Split(',')[1]);
                            continueLoop = false;
                        }
                        catch (Exception e)
                        {
                            FileLogger.Log(FileLogger.Severity.Exception, e.ToString());
                        }
                    }
                }

            }
            catch
            {
            }
            return dt;
        }
        
        /// <summary>
        /// Close the MTBC log file with adding execution end time and MTBC
        /// </summary>
        public static void WrapUp()
        {
            try
            {
                TextWriter tw = new StreamWriter(mtbcLogFile, true);
                string s = "Execution End Time," + DateTime.Now.ToString();
                tw.WriteLine(s);
                tw.Close();

                string[] allLines = File.ReadAllLines(mtbcLogFile);
                double tsTotal = 0;
                for (int i = 2; i < allLines.Length - 1; i++)
                {
                    try
                    {
                        tsTotal += Convert.ToDouble(allLines[i].Split(',')[2]);
                    }
                    catch
                    {
                    }
                }
                double tsAvg = tsTotal / (allLines.Length - 3);
                tw = new StreamWriter(mtbcLogFile, true);
                s = "MTBC (min),," + tsAvg.ToString();
                tw.WriteLine(s);
                tw.Close();
                mtbcLogFile = string.Empty;
            }
            catch
            {
            }
        }

        /// <summary>
        /// Recreate a MTBC log file
        /// </summary>
        public static void ReCreate()
        {
            WrapUp();
            Create();
        }
    }
}
