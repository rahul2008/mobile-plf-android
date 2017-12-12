/* 
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 */

using Microsoft.TeamFoundation.Build.Client;
using Newtonsoft.Json;
using Philips.H2H.Automation.Dashboard.TestRunObjects;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Web;
using System.Web.Script.Services;
using System.Web.Services;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Xml;
using System.Xml.Serialization;

namespace Philips.H2H.Automation.Dashboard
{
    public partial class index : System.Web.UI.Page
    {
        static TeamFoundationServerHelper helperObj = new TeamFoundationServerHelper();
        public static string buildStatus = null;
        public static string buildName = null;
        public static string buildFinishedTime = null;
        public static string lastBuildFinishTime = null;
        public static string lastBuildVersion { get; set; }
        public static string reliabilityFileName = null;

        public static string buildDefName { get;set;}

        protected void page_load(object sender, EventArgs e)
        {
            if (!this.IsPostBack)
            {
                int x = 0;
                GetBuildVersionFromServer();
                reliabilityFileName = GetReliabilityLogs();
            }
        }

        [WebMethod]
        public static string GetLastTestRunData(string name)
        {
            List<IBuildDetail> allBuilds = helperObj.GetBuildDetailsFromTFS(name, 50);
            IBuildDetail reqdBuild = allBuilds[0];

            string buildnumber = reqdBuild.BuildNumber;
            lastBuildFinishTime = TeamFoundationServerHelper.ConvertTimeToLocalTime(reqdBuild.FinishTime).ToString("dd-MMM-yyyy: hh:mm:tt");

            List<TestRunDetails> master = helperObj.GetTestRunDataByBuild(reqdBuild);
            BuildTestRunDetails buildRunDetailsObj = null;
            string jsonString = null;
            try { 
                    buildRunDetailsObj = new BuildTestRunDetails
                    {
                        buildName = TeamFoundationServerHelper.GetQualifiedBuildName(reqdBuild.BuildDefinition.Name),
                        buildNumber = buildnumber,
                        buildFinishTime = lastBuildFinishTime,
                        TestRunDetails = master[0]
                    };
                    jsonString = JsonConvert.SerializeObject(buildRunDetailsObj);
            }
            catch (Exception ex)
            {

            }
            return jsonString;
        }

        [WebMethod]
        public static string GetRunningBuildDetailsFromTFS()
        {
            //Get the last run build number from definition name
            List<IBuildDetail> allBuilds = helperObj.GetRunningBuildDetails();
            IBuildDetail reqdBuild = null;
            if (allBuilds.Count == 0)
            {
                return "Currently No build is running on the server.";
            }
            foreach (var item in allBuilds)
            {
                if (WebConfigInitializer.GetAvailableBuildDefinition().Contains(item.BuildDefinition.Name))
                {
                    reqdBuild = item;
                    break;
                }
            }

            string buildnumber = reqdBuild.BuildNumber;
            lastBuildFinishTime = TeamFoundationServerHelper.ConvertTimeToLocalTime(reqdBuild.StartTime).ToString("dd-MMM-yyyy: hh:mm:tt");

            List<TestRunDetails> master = helperObj.GetTestRunDataByBuild(reqdBuild);

            BuildTestRunDetails buildRunDetailsObj = new BuildTestRunDetails
            {
                buildName = TeamFoundationServerHelper.GetQualifiedBuildName(reqdBuild.BuildDefinition.Name),
                buildNumber = buildnumber,
                buildFinishTime = lastBuildFinishTime,
                TestRunDetails = master[0]  
            };
            string jsonString = JsonConvert.SerializeObject(buildRunDetailsObj);
            return jsonString;
        }

        [WebMethod]
        public static string GetReliabilityTestRunDetails()
        {
            //string filePath = "C:\\Philips\\BuildAPK\\Philips.CDPAutomation.Tests.Reliability.dll_4048_TestReport.xml";
            string filePath = reliabilityFileName;
            XmlRootAttribute xRoot = new XmlRootAttribute();
            xRoot.ElementName = "Reporter";
            xRoot.IsNullable = true;
            string jsonResponse = "";
            XmlSerializer xs = new XmlSerializer(typeof(Reporter), xRoot);

            Reporter list = new Reporter();
            if (File.Exists(filePath))
            {
                FileStream stream = File.OpenRead(filePath);
                list = (Reporter)xs.Deserialize(stream);
                jsonResponse = JsonConvert.SerializeObject(list);
            }
            return jsonResponse;
        }

        public void buildQueue_StatusChanged(object sender, StatusChangedEventArgs e)
        {
            IQueuedBuild runningbuild = ((IQueuedBuild)sender);
            runningbuild.Refresh(QueryOptions.All);
            buildStatus = runningbuild.Status.ToString();
            //Modified by Shaheen for queuing new build
            //buildName = runningbuild.Build.BuildDefinition.Name;
            buildName = runningbuild.BuildDefinition.Name;
            buildFinishedTime = runningbuild.Build.FinishTime.ToString();
        }

        public void btn_StartBuild(object sender, EventArgs e)
        {
            //if (buildStatus == "InProgress")
            //{
            //    string msg = "Build Execution in Progress: " + buildName;
            //    ScriptManager.RegisterClientScriptBlock(this.Page, Page.GetType(), "testing", "<script>alert('" + msg + "');</script>", false);
            //}
            //else
            //{
            //    string definitionName = Request.Form["suite"];
            //    IQueuedBuild runningBuild = helperObj.StartABuild("eCPAutomationBuild");
            //    runningBuild.StatusChanged += new StatusChangedEventHandler(buildQueue_StatusChanged);
            //    runningBuild.Connect();
            //}
        }

        public void btn_CheckStatus(object sender, EventArgs e)
        {
            string msg = string.Empty;
            if (buildStatus == "Completed")
            {
                msg = "Build Execution Completed: " + buildName;
                buildStatus = null;
            }
            else if (buildStatus == null)
            {
                msg = "No Running Build";
            }
            else if (buildStatus == "InProgress")
            {
                msg = "Build Execution in Progress: " + buildName;
            }

            else { msg = buildStatus; }
            ScriptManager.RegisterClientScriptBlock(this.Page, Page.GetType(), "testing", "<script>alert('" + msg + "');</script>", false);
        }

        [System.Web.Services.WebMethod()]
        [System.Web.Script.Services.ScriptMethod()]
        public static void UpdateBuildStatus()
        {
            buildStatus = null;
        }

        protected void btnDownload_Click(object sender, EventArgs e)
        {
            //ScriptManager.RegisterClientScriptBlock(this.Page, Page.GetType(), "testing", "<script>alert('Nothing');</script>", false);
            //ConfigurationManager.AppSettings["reportFileUri"]
            //ConfigurationManager.AppSettings["reportFileName"]
            System.IO.MemoryStream mstream = buildDetails.GetData(WebConfigInitializer.ReportPath);
            if (mstream != null)
            {
                byte[] byteArray = mstream.ToArray();
                mstream.Flush();
                mstream.Close();
                Response.Clear();
                Response.AddHeader("Content-Disposition", "attachment; filename=" + WebConfigInitializer.ReportFileName);
                Response.AddHeader("Content-Length", byteArray.Length.ToString());
                Response.ContentType = "application/octet-stream";
                Response.BinaryWrite(byteArray);
            }
            //else
            //{
            //    ScriptManager.RegisterClientScriptBlock(this.Page, Page.GetType(), "testing", "<script>alert('Could not find report to download');</script>", false);
            //}
        }

        public static void GetBuildVersionFromServer()
        {
            WebClient client = new WebClient();
            lastBuildVersion = Encoding.ASCII.GetString(client.DownloadData(new Uri(WebConfigInitializer.pathForBuildVersion).LocalPath));
        }

        protected void execution_Click(object sender, EventArgs e)
        {
            if (buildStatus == "InProgress")
            {
                string msg = "Build Execution in Progress: " + buildName;
                ScriptManager.RegisterClientScriptBlock(this.Page, Page.GetType(), "testing", "<script>alert('" + msg + "');</script>", false);
            }
            else
            {
                string val = sender.ToString();
                string definitionName = Request.Form["suite"];
                IQueuedBuild runningBuild = helperObj.StartABuild(definitionName);
                runningBuild.StatusChanged += new StatusChangedEventHandler(buildQueue_StatusChanged);
                buildStatus = runningBuild.Status.ToString();
                runningBuild.Connect();
            }
        }

        protected string GetReliabilityLogs()
        {
            try
            {
                WebClient client;
                string targetPath = Server.MapPath("~\\chartData\\reliability\\");
                var directory = new DirectoryInfo(new Uri(WebConfigInitializer.pathForReliabilityData).LocalPath);
                if (directory.Exists) {
                    if (directory.GetFiles().Length != 0) { 
                        var latestFile = directory.GetFiles().OrderByDescending(f => f.LastWriteTime).First();
                        client = new WebClient();
                        string destFile = System.IO.Path.Combine(targetPath, System.IO.Path.GetFileName(latestFile.Name));
                        client.DownloadFile(latestFile.FullName, destFile);
                        return destFile;
                    }
                }
                return null;
            }
            catch (Exception e)
            {
                Console.Write(e.StackTrace);
                return null;

            }
        }
    }
}