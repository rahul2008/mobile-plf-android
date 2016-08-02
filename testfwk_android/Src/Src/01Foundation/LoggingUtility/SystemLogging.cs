using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Diagnostics;

namespace Philips.MRAutomation.Foundation.LoggingUtility
{
    /// <summary>
    /// 
    /// </summary>
    public static class SystemLogging
    {
        /// <summary>
        /// 
        /// </summary>
        /// <param name="textToLog"></param>
        public static void CreateSystemLogEntry(string textToLog)
        {
            ProcessStartInfo startInfo = new ProcessStartInfo("cmd.exe");
            startInfo.RedirectStandardInput = true;
            startInfo.RedirectStandardOutput = true;
            startInfo.UseShellExecute = false;

            Process p = Process.Start(startInfo);

            if (p != null)
            {
                p.StandardInput.WriteLine("p_logfile_nu createnewlogfile");
                p.StandardInput.WriteLine("p_logfile_nu logcentral \"" + textToLog + " \"");
                p.StandardInput.Close();
            }

            p.WaitForExit();
            p.Close();
        }
    }
}
