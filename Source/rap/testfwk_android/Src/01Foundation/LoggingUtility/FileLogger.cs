/*  ----------------------------------------------------------------------------
 *  MR Automation : Philips Healthcare Bangalore Center of Competence
 *  ----------------------------------------------------------------------------
 *  Logging Utility
 *  ----------------------------------------------------------------------------
 *  File:       FileLogger.cs
 *  Author:     Girish Prasad
 *  Creation Date: 1-30-2011
 *  ----------------------------------------------------------------------------
 */

using System;
using System.IO;
using System.Threading;
using System.Reflection;

namespace Philips.MRAutomation.Foundation.LoggingUtility
{
    /// <summary>
    /// 
    /// </summary>
    public class FileLogger
    {
        /// <summary>
        /// Indicates the Logging Severity
        /// </summary>
        public enum Severity
        {
            /// <summary>
            /// Only Information
            /// </summary>
            Information,
            /// <summary>
            /// Error, Usually one cannot continue after an error.
            /// </summary>
            Error,
            /// <summary>
            /// Only Exception, One can catch and continue on exceptions.
            /// </summary>
            Exception,
            /// <summary>
            /// Trace
            /// </summary>
            Trace
        }

        /// <summary>
        /// Name of the logfile.
        /// </summary>
        private string logFile = Path.GetDirectoryName(Assembly.GetExecutingAssembly().GetModules()[0].FullyQualifiedName) + 
            @"\Reports\MRAutomationLogs\Logs\Log_" +
            DateTime.Now.Year + "_" +
            DateTime.Now.Month + "_" +
            DateTime.Now.Day ;

        // The Pointer to the file to which Console class will write to.
        private static StreamWriter fileWriter = null;

        /// <summary>
        /// Write to the given TextWriter
        /// </summary>
        /// <param name="severeValue">Severity</param>
        /// <param name="message">message</param>
        public static void Log(Severity severeValue, string message)
        {
            FileLogger fl = new FileLogger();
            fl.LogMe (severeValue, message);
        }

        private void LogMe(Severity severeValue, string message)
        {
            try
            {
                lock (this)
                {
                    SetOut();
                    Console.WriteLine(
                    DateTime.Now.ToString() + " - " + severeValue + " : " + message);
                    Reset();
                }
            }
            catch
            {
            }
        }

        /// <summary>
        /// Sets the Console.Out
        /// </summary>
        private void SetOut()
        {
            try
            {
                string fileName = logFile + ".txt";
                if (fileWriter == null)
                {
                    if (!File.Exists(fileName))
                    {
                        if (!Directory.Exists(Path.GetDirectoryName(fileName)))
                        {
                            Directory.CreateDirectory(Path.GetDirectoryName(fileName));
                        }
                        FileStream fs = File.Create(fileName);
                        fs.Close();
                    }
                }
                fileWriter = new StreamWriter(fileName, true);
                Console.SetOut(fileWriter);
            }
            catch
            {
            }
        }

        /// <summary>
        /// Resets the Console.Out
        /// </summary>
        private void Reset()
        {
            try
            {
                if (fileWriter != null)
                {
                    fileWriter.Close();
                }
                StreamWriter standardOutput = new StreamWriter(Console.OpenStandardOutput());
                standardOutput.AutoFlush = true;
                Console.SetOut(standardOutput);
            }
            catch
            {
            }

        }
    }
}

#region Revision History
/*
 * 01-30-2011  : Created by Girish, review pending
 * 02-13-2011  : Modified by Girish, handled race conditions by locking the code
 */

#endregion
