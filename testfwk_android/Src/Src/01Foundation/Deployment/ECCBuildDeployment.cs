using System;
using System.Collections.Generic;
using System.Configuration;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace Philips.H2H.Foundation.BuildAgent
{
    class ECCBuildDeployment
    {
        private static string eccBuildLoc = ConfigurationManager.AppSettings["ECCBuildLoc"];
        private static string localBuildLoc = ConfigurationManager.AppSettings["LocalECCBuildLoc"];
        private static string scriptName = ConfigurationManager.AppSettings["BuildDeploymentScript"];
        private static string buildNumberFile = localBuildLoc + "/ECCBuildNumber.txt";
        private static string CopyECCBuild = ConfigurationManager.AppSettings["CopyECCBuild"];
        private static string DeployBuild = ConfigurationManager.AppSettings["DeployBuild"];
        public static string deployedBuildNumer = string.Empty;
                
        public void DeployECCBuild()
        {
            string currentBuildName = string.Empty;
            DirectoryInfo buildDirInfo = null;
            bool buildChanged = false;
            Common cmnObj = new Common();

            if (CopyECCBuild.Equals("true"))
            {
                int prevBuildNumber = 0;
                //Get the last downloaded build number
                bool isExists = File.Exists(buildNumberFile);
                if (!isExists)
                {

                    if (!Directory.Exists(localBuildLoc))
                    {
                        Directory.CreateDirectory(localBuildLoc);
                    }
                    FileStream fStream = File.Create(buildNumberFile);
                    fStream.Close();
                }
                else
                {
                    using (System.IO.StreamReader file = new System.IO.StreamReader(buildNumberFile, false))
                    {
                        prevBuildNumber = int.Parse(file.ReadLine());
                    }
                }

                Common.WriteToLog("Build Number stored in the local file: " + prevBuildNumber);
                int buildNumber = 0;

                try
                {
                    DirectoryInfo dir = new DirectoryInfo(eccBuildLoc);
                    DirectoryInfo[] dirs = dir.GetDirectories("eCC*");
                    foreach (DirectoryInfo dirObj in dirs)
                    {
                        string[] temp = dirObj.Name.Split('.');
                        int len = temp.Length;
                        buildNumber = int.Parse(temp[len - 1]);
                        if (prevBuildNumber < buildNumber)
                        {
                            buildChanged = true;
                            prevBuildNumber = buildNumber;
                            buildDirInfo = dirObj;
                        }
                    }
                }
                catch (Exception e)
                {
                    Common.WriteToLog("Exception while accessing Network Build Folder: " + e.Message);
                    throw e;
                }
                Common.WriteToLog("Latest Build Number from Build Drop Server: " + prevBuildNumber);

                //Now COpy the build folder to destination folder. 
                if (buildChanged && buildDirInfo !=null)
                {
                    if (!Directory.Exists(localBuildLoc))
                    {
                        Directory.CreateDirectory(localBuildLoc);
                    }
                    //Copy the latest Build from Build Server to local Machine
                    cmnObj.CopyDirectory(buildDirInfo.FullName, localBuildLoc);

                    //Have the build number stored in a file and compare with 
                    using (System.IO.StreamWriter fileObj = new System.IO.StreamWriter(buildNumberFile, false))
                    {
                        fileObj.WriteLine(prevBuildNumber);
                        deployedBuildNumer = prevBuildNumber+"";
                    }
                }
            }
            if ((DeployBuild.Equals("true")) || (buildChanged == true && buildDirInfo != null))
            {
                Common.WriteToLog("ECC Build deployment started.");
                //Update QA Json File with user name,  password & security token
                UpdateQAJson();

                //Disable LAN Connection through netsh
                Common.WriteToLog("Return Code while disabling LAN Connection: " + cmnObj.ToggleLANConnection(false));
                System.Threading.Thread.Sleep(4000);

                //Salesforce build Deployment started
                int exitcode=cmnObj.RunPowerShellScript(localBuildLoc + "\\" + scriptName);
                Common.WriteToLog("Return Code while Enabling LAN Connection: " + cmnObj.ToggleLANConnection(true));
                if (exitcode != 0)
                {
                    Common.WriteToLog("ECC Build deployment Completed with Error: Error Code: "+exitcode);
                }
                else
                {
                    Common.WriteToLog("ECC Build deployment Completed: Build Number / See Top for the build number.");
                }
               // Common.SendMail(deployedBuildNumer);
            }
            else
            {
                Common.WriteToLog("No New Build in ECC build drop folder, skipping Deployment.");
            }            
        }

        private static void UpdateQAJson()
        {
            try
            {
                Common.WriteToLog("Updating QA Json file with Deployment Org details");
                string value = new Common().ReadStringFromFile(localBuildLoc + "\\qa-config.json");
                Common.WriteToLog("Reading Value from QA Json: " + value);
                JObject obj = JObject.Parse(value);

                obj["username"] = ConfigurationManager.AppSettings["UserName"];
                obj["password"] = ConfigurationManager.AppSettings["Password"];
                obj["securityToken"] = ConfigurationManager.AppSettings["SecurityToken"];

                Common.WriteToLog("Writing new Value to QA Json: " + obj.ToString());
                using (StreamWriter writer = new StreamWriter(localBuildLoc + "\\qa-config.json",false))
                {
                    writer.Write(obj.ToString());
                }
            }
            catch (Exception e)
            {
                Common.WriteToLog("Exception while Updating QA Json file: " + e.StackTrace);
                throw e;
            }
        }
    }
}
