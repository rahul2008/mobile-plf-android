/* 
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 */

using Microsoft.TeamFoundation.Build.Client;
using Microsoft.TeamFoundation.Client;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Net;
using System.Web;
using Microsoft.TeamFoundation.VersionControl.Client;
using Microsoft.TeamFoundation.TestManagement.Client;
using System.Globalization;
using System.Web.UI;
using Microsoft.TeamFoundation.Framework.Client;
using Philips.H2H.Automation.Dashboard.TestRunObjects;


namespace Philips.H2H.Automation.Dashboard
{
    public class TeamFoundationServerHelper
    {
        public readonly string tfsCollectionUri = null;
        public readonly string tfsProject = null;
        private static TfsTeamProjectCollection tfs = null;
        public static string LastBuildTime = null;


        public TeamFoundationServerHelper()
        {
            tfsCollectionUri = WebConfigInitializer.TfsCollectionUri;
            tfsProject = WebConfigInitializer.TfsProjectPath;

            if (tfs == null)
            {
                tfs = InitializeTfsProcess();
            }
        }

        public IQueuedBuild StartABuild(string tfsBuildDefinition)
        {
            IBuildServer buildServer = InitializeTfsProcess().GetService(typeof(IBuildServer)) as IBuildServer;
            IBuildDefinition buildDef = buildServer.GetBuildDefinition(tfsProject, tfsBuildDefinition);
            // Queue each build definition
            IBuildRequest req = buildDef.CreateBuildRequest();
            IQueuedBuild runningBuild = buildServer.QueueBuild(req);
            //runningBuild.StatusChanged += new StatusChangedEventHandler(buildQueue_StatusChanged);
            //runningBuild.Connect();
            //Console.WriteLine("Build Running Status: {0}", runningBuild.Status);
           
            //Added by Shaheen for queuing new build
            //return runningBuild;
            IQueuedBuildSpec qbSpec = buildServer.CreateBuildQueueSpec(tfsProject, tfsBuildDefinition);
            IQueuedBuildQueryResult qbResults = buildServer.QueryQueuedBuilds(qbSpec);
            IQueuedBuild build = null;
            if (qbResults.QueuedBuilds.Length > 0)
            {
                build = qbResults.QueuedBuilds.Where(x => x.Id == runningBuild.Id).FirstOrDefault();
            }
            return build;
        }

        //Establishment of TFS connectivity
        public TfsTeamProjectCollection InitializeTfsProcess()
        {
            NetworkCredential tfsCredentials = new NetworkCredential(WebConfigInitializer.UserName, WebConfigInitializer.Password, WebConfigInitializer.DomainName);
            tfs = new TfsTeamProjectCollection(new Uri(tfsCollectionUri), tfsCredentials);
            try
            {
                tfs.Authenticate();
            }
            catch (Exception e)
            {
                Console.WriteLine("TFS authentication failed " + e);
            }
            return tfs;
        }

        public List<IBuildDetail> GetBuildDetailsFromTFS(string buildDefinition, int numberOfBuilds)
        {
            IBuildServer tfsBuildServer = tfs.GetService<IBuildServer>();
            IBuildDefinitionSpec buildSpec =
                     tfsBuildServer.CreateBuildDefinitionSpec(tfsProject);
            IBuildDetailSpec buildDetailSpec = tfsBuildServer.CreateBuildDetailSpec(tfsProject, buildDefinition);
            buildDetailSpec.QueryOrder = BuildQueryOrder.FinishTimeDescending;
            buildDetailSpec.Status = BuildStatus.Succeeded | BuildStatus.PartiallySucceeded | BuildStatus.Failed | BuildStatus.Stopped;
            buildDetailSpec.MaxBuildsPerDefinition = numberOfBuilds;
            IBuildQueryResult results = tfsBuildServer.QueryBuilds(buildDetailSpec);
            return results.Builds.ToList().FindAll(d => d.TestStatus.ToString() != "Unknown"); //Changed by Dibyaranjan
        }

        public IBuildDetail GetBuildDetailsFromTFS(string buildNumber)
        {
            IBuildServer tfsBuildServer = tfs.GetService<IBuildServer>();
            IBuildDefinitionSpec buildSpec =
                     tfsBuildServer.CreateBuildDefinitionSpec(tfsProject);
            IBuildDetailSpec buildDetailSpec = tfsBuildServer.CreateBuildDetailSpec(tfsProject);
            buildDetailSpec.QueryOrder = BuildQueryOrder.FinishTimeDescending;
            buildDetailSpec.Status = BuildStatus.Succeeded | BuildStatus.PartiallySucceeded | BuildStatus.Failed | BuildStatus.Stopped;
            buildDetailSpec.BuildNumber = buildNumber;
            IBuildQueryResult results = tfsBuildServer.QueryBuilds(buildDetailSpec);
            return results.Builds.ToList().FindAll(d => d.TestStatus.ToString() != "Unknown")[0];
        }

        public List<TestRunDetails> GetTestRunDataByBuild(IBuildDetail buildDetail)
        {
            List<TestRunDetails> master = new List<TestRunDetails>();
            try
            {
                ITestManagementService test_service = (ITestManagementService)tfs.GetService(typeof(ITestManagementService));
                ITestManagementTeamProject _testproject = test_service.GetTeamProject(tfsProject);

                var testRuns = _testproject.TestRuns.ByBuild(buildDetail.Uri);
                foreach (ITestRun item in testRuns)
                {
                    ITestRun iTR = _testproject.TestRuns.Find(item.Id);
                    foreach (var data in iTR.Attachments)
                    {
                        if ("TmiTestRunReverseDeploymentFiles".Equals(data.AttachmentType) && "Test Execution Report".Equals(data.Comment))
                        {
                            WebConfigInitializer.ReportPath = data.Uri.AbsoluteUri;
                            // ConfigurationManager.AppSettings["reportFileUri"] = data.Uri.AbsoluteUri;
                            //    ConfigurationManager.AppSettings["reportFileName"] = data.Name;
                            WebConfigInitializer.ReportFileName = data.Name;
                        }
                    }
                    TestRunDetails details = new TestRunDetails
                    {
                        TestRunId = item.Id,
                        TestCaseDetails = GetRunDetails(iTR)
                    };
                    master.Add(details);
                }
            }
            catch (Exception e)
            {
                Console.WriteLine("Exception occured while downloading trx files from TFS ", e.StackTrace);
            }
            return master;
        }

        private List<TestCaseDetails> GetRunDetails(ITestRun iTR)
        {
            List<TestCaseDetails> master = new List<TestCaseDetails>();
            ITestCaseResultCollection resultCollections = iTR.QueryResults();

            foreach (var result in resultCollections)
            {
                TestCaseDetails rd = new TestCaseDetails();
                rd.TestCaseTitle = result.TestCaseTitle;
                rd.Outcome = result.Outcome.ToString();
                rd.ShortDate = result.Duration.TotalMinutes.ToString("#.##");
                //rd.TraceOutput = result.ErrorMessage;
                rd.StdOutput = "<div style=width:900px;	overflow-x: scroll><pre style=background:white;color:black>" + result.Comment + "</pre></div>";
                master.Add(rd);
            }
            return master;
        }

        //Method return data associated with specific run
        public List<TestRunMetadata> GetTestRunDetails(string buildDefinitionName)
        {
            List<TestRunMetadata> list = new List<TestRunMetadata>();
            try
            {
                ITestManagementService test_service = (ITestManagementService)tfs.GetService(typeof(ITestManagementService));
                ITestManagementTeamProject _testproject = test_service.GetTeamProject(tfsProject);
                List<IBuildDetail> allDesiredBuilds = GetBuildDetailsFromTFS(buildDefinitionName, 100);

                foreach (IBuildDetail buildDetail in allDesiredBuilds)
                {
                    var testRuns = _testproject.TestRuns.ByBuild(buildDetail.Uri);
                    foreach (ITestRun item in testRuns)
                    {
                        ITestRun iTR = _testproject.TestRuns.Find(item.Id);
                        TestRunMetadata trmd = new TestRunMetadata();
                        trmd.TotalTestCases = iTR.TotalTests;
                        DateTime dt = GetDateTimeObjectFromString(iTR.DateStarted.ToString());
                        trmd.DateOfTestExecution = (dt.Day + "-" + dt.ToString("MMM") + "-" + dt.Year);
                        trmd.TotalPassedTests = iTR.PassedTests;
                        trmd.TotalFailedTests = (iTR.TotalTests - iTR.PassedTests);
                        trmd.BuildNumber = buildDetail.BuildNumber;
                        trmd.BuildConfigNumber = iTR.BuildConfigurationId;
                        trmd.BuildRunId = iTR.Id;

                        trmd.TotalBuildExecutionTime = Math.Round((buildDetail.FinishTime - buildDetail.StartTime).Duration().TotalMinutes, 2);
                        foreach (var uri in item.Attachments)
                        {
                            if ("TmiTestRunReverseDeploymentFiles".Equals(uri.AttachmentType) && "Test Execution Report".Equals(uri.Comment))
                            {
                                trmd.Uri = uri.Uri.AbsoluteUri;
                                trmd.FileName = uri.Name;
                            }
                        }
                        list.Add(trmd);
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine("Exception occured while downloading trx files from TFS ", e.StackTrace);
            }
            return list;
        }

        public void CheckOutFileFromTFS(string file)
        {
            var versionControl = InitializeTfsProcess().GetService<VersionControlServer>();
            var workspaceInfo = versionControl.GetWorkspace("ingbtcpic5nb0c8", versionControl.AuthorizedUser);
            workspaceInfo.PendEdit(file);
        }

        public void SubscribeToBuildEvent()
        {
            //IEventService es = tfs.GetService(typeof(IEventService)) as IEventService;
            //List<Subscription> ls = es.GetAllEventSubscriptions().ToList();

            //IEventService es = tfs.GetService(typeof(IEventService)) as IEventService;
            //es.SubscribeEvent("BuildCompletionEvent",)
            //DeliveryPreference del = new DeliveryPreference();

            //del.Address = "Webservice address";
            //del.Schedule = DeliverySchedule.Immediate;
            //del.Type = DeliveryType.Soap;

        }

        //Method returns DateTime object from String which contains date and time eg. YYYY-MM-DDThh:mm:ssTZD where T is Times and TZD is Time zone
        private static DateTime GetDateTimeObjectFromString(string datetime)
        {
            return DateTime.Parse(datetime, null, DateTimeStyles.RoundtripKind);
        }

        public List<IBuildDetail> GetRunningBuildDetails()
        {
            List<IBuildDetail> runDetails = new List<IBuildDetail>();
            try
            {
                IBuildServer tfsBuildServer = tfs.GetService<IBuildServer>();
                IBuildDefinitionSpec buildSpec =
                         tfsBuildServer.CreateBuildDefinitionSpec(tfsProject);
                IBuildDetailSpec buildDetailSpec = tfsBuildServer.CreateBuildDetailSpec(tfsProject);
                buildDetailSpec.Status = BuildStatus.InProgress;
                IBuildDetail[] results = tfsBuildServer.QueryBuilds(buildDetailSpec).Builds;
                runDetails = results.ToList();
            }
            catch (Exception e)
            {
                Console.WriteLine("Exception occured while downloading trx files from TFS ", e.StackTrace);
            }
            return runDetails;
        }       

        public static string GetQualifiedBuildName(string buildDefinition)
        {
            string qualifiedName = null;
            //RadiologySolutionAutomation:Regression Suite
            string[] allSuites = WebConfigInitializer.SuiteName.Split(',');
            foreach (var item in allSuites)
            {
                string[] sTemp = item.Split(':');
                if (sTemp[0].Equals(buildDefinition))
                {
                    qualifiedName = sTemp[1];
                    break;
                }
            }
            return qualifiedName;
        }

        public static DateTime ConvertTimeToLocalTime(DateTime dt)
        {
            return dt.ToLocalTime();
        }
    }
}