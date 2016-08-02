/* 
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Configuration;

namespace Philips.H2H.Automation.Dashboard
{
    public static class WebConfigInitializer
    {
        public static string ProjectTitle = ConfigurationManager.AppSettings["HomePage_ProjectTitle"] ;
        public static string ProjectName =  ConfigurationManager.AppSettings["ProjectName"];
        public static string SuiteName = ConfigurationManager.AppSettings["AvailableSuites"];
        public static string UserName = ConfigurationManager.AppSettings["TfsUsername"];
        public static string Password = ConfigurationManager.AppSettings["TfsPassword"];
        public static string DomainName = ConfigurationManager.AppSettings["TfsDomain"];
        public static string TfsCollectionUri = ConfigurationManager.AppSettings["TfsCollectionUri"];
        public static string TfsProjectPath = ConfigurationManager.AppSettings["TfsProject"];
        public static string ReportPath = null;
        public static string ReportFileName = null;
        public static int TileCount = int.Parse(ConfigurationManager.AppSettings["TileCount"]);
        public static string reliabilityConst = ConfigurationManager.AppSettings["reliabilitySuiteType"];
        public static string pathForChartData = ConfigurationManager.AppSettings["PathForChartData"];
        public static string hostForSTFApp = ConfigurationManager.AppSettings["HostForSTFApp"];
        public static string pathForReliabilityData = ConfigurationManager.AppSettings["PathForReliabilityData"];
        public static string pathForBuildVersion = ConfigurationManager.AppSettings["PathForBuildVersion"];
        public static string pageTitle = ConfigurationManager.AppSettings["PageTitle"];
        public static List<string> GetAvailableBuildDefinition()
        {
            List<string> allBuildDefinitions = new List<string>();
            string[] allSuites = SuiteName.Split(',');
            foreach (var item in allSuites)
            {
                string[] sTemp = item.Split(':');
                allBuildDefinitions.Add(sTemp[0]);
            }
            return allBuildDefinitions;
        }
    }
}