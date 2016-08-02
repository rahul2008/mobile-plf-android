using System;
using System.Collections.Generic;
using System.Configuration;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net;
using System.Security.Permissions;
using System.Text;
using System.Threading.Tasks;

namespace Philips.H2H.Foundation.BuildAgent
{
    public class Runner
    {
        public static string logfileLoc = ConfigurationManager.AppSettings["logfileLoc"];
        private static string RunAutomation = ConfigurationManager.AppSettings["RunAutomation"];
        public static string ArchivePath = ConfigurationManager.AppSettings["ArchivePath"];

        static void Main(string[] args)
        {           

            //Delete the existing log file if exists
            if (File.Exists(logfileLoc))
            {
                File.Delete(logfileLoc);
            }

            Common.WriteToLog("IAP Build deployment and automation script execution started: " + DateTime.Now);

            try
            {
                //Copy the latest ECC build from latest Build server
               // new ECCBuildDeployment().DeployECCBuild();
            }
            catch(Exception e)
            {
                Common.WriteToLog("Exception while ECC Build Deployment:  " + e.StackTrace);            
            }

            try
            {
                if (RunAutomation.Equals("true"))
                {
                    //Run automation script after new build copied from Jenkins to local directory
                    IAPBuildDeployment.RunIAPAutomation();
                }
            }
            catch (Exception e)
            {
                Common.WriteToLog("Exception while Executing IAP Automation Test Cases:  " + e.StackTrace);            
            }

            Common.WriteToLog("IAP Build deployment and automation script execution completed: " + DateTime.Now);
        }
    }
}
