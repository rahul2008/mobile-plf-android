using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace Philips.H2H.Automation.Dashboard
{
    public partial class PerformanceMonitor : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            GetPerformanceLogs();
        }

        public void GetPerformanceLogs()
        {
            try
            {
                WebClient client;
                string fileName = "";
                string targetPath = Server.MapPath("~\\chartData");               
                string destFile = "";
                var directory = new DirectoryInfo(new Uri(WebConfigInitializer.pathForChartData).LocalPath);
                var latestDir = (from dir in directory.GetDirectories() orderby dir.LastWriteTime descending select dir).First();
                if (System.IO.Directory.Exists(latestDir.FullName))
                {
                    string[] files = System.IO.Directory.GetFiles(latestDir.FullName);

                    foreach (string s in files)
                    {
                        fileName = System.IO.Path.GetFileName(s);
                        destFile = System.IO.Path.Combine(targetPath, fileName);
                       // System.IO.File.Copy(s, destFile, true);

                        client = new WebClient();
                        client.DownloadFile(s, targetPath+"\\"+fileName);
                    }
                }
            }
            catch (Exception e)
            {
                Console.Write(e.StackTrace);

            }
        }
    }
}