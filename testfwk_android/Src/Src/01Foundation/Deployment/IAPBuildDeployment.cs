using System;
using System.Collections.Generic;
using System.Configuration;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace Philips.H2H.Foundation.BuildAgent
{
    class IAPBuildDeployment
    {
        private static string localdir = ConfigurationManager.AppSettings["localSource"];
        private static string automationCmd = ConfigurationManager.AppSettings["AntScriptFile"];
        private static string jenkinsdir = ConfigurationManager.AppSettings["ReleaseBuild"];
        private static string sourcedir = ConfigurationManager.AppSettings["PendingBuild"];
        private static string CopyIAPBuild = ConfigurationManager.AppSettings["CopyIAPBuild"];
       
        private static Common cmn = new Common();
        
        public static void RunIAPAutomation()
        {
            Common.WriteToLog("IAP Build deployment and Automation Execution started");

            if (CopyIAPBuild.Equals("true"))
            {
                //Download the build from Jenkins and Copy to source directory
                bool isModifiedBuild=IAPBuildDeployment.CopyReleaseApkFromJenkins(jenkinsdir, sourcedir);

                if (true)
                {
                    cmn.UninstallApkFromDevice("com.philips.ecp.app");
                    if (cmn.InstallApkToDevice(sourcedir))
                    {                       
                        Common.WriteToLog("Uninstall and Install application completed");
                    }
                }

                Common.WriteToLog("Starting to execute Automation test cases through Ant Build script");
                //Trigger automation script
                int retCode = cmn.RunAntBuildScript(automationCmd);
                if (retCode == 0)
                {
                    Common.WriteToLog("Automation Script execution Completed without any error.");
                }
                else
                {
                    Common.WriteToLog("Automation Script execution Completed but with Error Code: " + retCode);
                }
            }            
        }
        
        private void WriteTestResultsToDB()
        {
            //TO DO
        }

        private static bool CopyReleaseApkFromJenkins(string source, string dest)
        {
            bool isNewBuild = false;
            string crntModifedTime = string.Empty;
            try
            {
                Common.WriteToLog("Coping the IAP release apk from  from Jenkins Server to local directory.");
                using (WebClient myWebClient = new WebClient())
                {
                    HttpWebRequest sourceApkRequest = (HttpWebRequest)WebRequest.Create(source);
                    HttpWebResponse sourceApkResponse = (HttpWebResponse)sourceApkRequest.GetResponse();
                    DateTime lastModifiedDatTime = sourceApkResponse.LastModified;
                    crntModifedTime = lastModifiedDatTime.ToString();
                    Common.WriteToLog("Last Modified Date And Time of Apk in the server: " + lastModifiedDatTime);

                    bool isExists1 = File.Exists(localdir);
                    if (!isExists1)
                    {
                        Common.WriteToLog("Local File to store Apk modfied date/time stamp is not present : Creating one");
                        FileStream fStream = File.Create(localdir);
                        fStream.Close();

                        Common.WriteToLog("Starting to Download IAP Apk from Jenkins Server.");
                        // Download the Web resource and save it into the current filesystem folder.                
                        myWebClient.DownloadFile(source, dest);
                        isNewBuild = true;
                    }
                    else
                    {
                        string prevDatestr = string.Empty;
                        using (System.IO.StreamReader file = new System.IO.StreamReader(localdir, false))
                        {
                            prevDatestr = file.ReadLine();
                        }

                        DateTime prevDt = Convert.ToDateTime(prevDatestr);
                        Common.WriteToLog("Read from Local File to check if the Apk modified: " + prevDt);

                        if (!prevDatestr.Equals(lastModifiedDatTime.ToString()))
                        {
                            Common.WriteToLog("Starting to Download IAP Apk from Jenkins Server.");
                            // Download the Web resource and save it into the current filesystem folder.                
                            myWebClient.DownloadFile(source, dest);
                            isNewBuild = true;
                        }
                        else
                        {
                            Common.WriteToLog("APK file is not modified in the Jenkins Server, so not proceeding for the downnload.");
                        }                        
                    }                   
                }
            }
            catch (Exception e)
            {
                Common.WriteToLog("Exception while downloading IAP Apk from Jenkins: " + e.StackTrace);   
            }
            if (isNewBuild)
            {
                Common.WriteToLog("Writing last modified datetime to the local file");
                using (System.IO.StreamWriter fileObj = new System.IO.StreamWriter(localdir, false))
                {
                    fileObj.WriteLine(crntModifedTime);
                }
            }
            return isNewBuild;
        }
    }
}
