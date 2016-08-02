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
using System.Web;
using System.Web.Script.Services;
using System.Web.Services;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Xml;

namespace Philips.H2H.Automation.Dashboard
{
    public partial class buildDetails : System.Web.UI.Page
    {
        static TeamFoundationServerHelper helperObj = new TeamFoundationServerHelper();
        private static string bulidNumber = null;
        private static string reportFileUri = null;
        private static string reportFileName = null;
        private static string BuildRunId = null;

        protected void page_load(object sender, EventArgs e)
        {
            bulidNumber = Request.QueryString["buildNumber"];
            reportFileUri = Request.QueryString["reportFileUri"];
            reportFileName = Request.QueryString["reportFileName"];
            BuildRunId = Request.QueryString["BuildRunId"];
        }

        [WebMethod]
        public static string GeBuildTestRunData()
        {
            IBuildDetail reqdBuild = helperObj.GetBuildDetailsFromTFS(bulidNumber);
            string lastBuildFinishTime = TeamFoundationServerHelper.ConvertTimeToLocalTime(reqdBuild.FinishTime).ToString("dd-MMM-yyyy: hh:mm:tt");
            List<TestRunDetails> master = helperObj.GetTestRunDataByBuild(reqdBuild);
            TestRunDetails runDetails = master.Find(d => d.TestRunId == int.Parse(BuildRunId));

            BuildTestRunDetails buildRunDetailsObj = new BuildTestRunDetails
            {
                buildNumber = bulidNumber,
                buildFinishTime = lastBuildFinishTime,
                TestRunDetails = runDetails
            };
            string jsonString = JsonConvert.SerializeObject(buildRunDetailsObj);
            return jsonString;
        }

        public static MemoryStream GetData(string testReportUri)
        {
            MemoryStream ReturnStream = null;
            try
            {
                WebRequest request = HttpWebRequest.Create(testReportUri);
                NetworkCredential tfsCredentials = new NetworkCredential(WebConfigInitializer.UserName, WebConfigInitializer.Password, WebConfigInitializer.DomainName);
                request.Credentials = tfsCredentials;
                WebResponse response = request.GetResponse();
                using (System.IO.Stream responseStream = response.GetResponseStream())
                {
                    string filename = response.Headers["content-disposition"].Replace("filename=", string.Empty);
                    System.IO.StreamReader reader = new System.IO.StreamReader(responseStream);
                    string content = reader.ReadToEnd();
                    ReturnStream = new MemoryStream();
                    StreamWriter sw = new StreamWriter(ReturnStream);
                    sw.WriteLine(content);
                    sw.Flush();
                    sw.Close();
                }
            }
            catch (Exception e)
            {
                Console.WriteLine("Exception occured while downloading Test Report from Spec Runner in TFS ", e);
            }
            return ReturnStream;
        }

        protected void btnDownload_Click(object sender, EventArgs e)
        {
            //System.IO.MemoryStream mstream = buildDetails.GetData(WebConfigInitializer.ReportPath);

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
            else
            {
                ScriptManager.RegisterClientScriptBlock(this.Page, Page.GetType(), "testing", "<script>alert('Could not find report to download');</script>", false);
            }
        }
    }
}