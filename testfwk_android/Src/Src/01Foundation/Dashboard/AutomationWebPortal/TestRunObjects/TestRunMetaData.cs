using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Philips.H2H.Automation.Dashboard.TestRunObjects
{
    public class TestRunMetadata
    {
        public int TotalTestCases { get; set; }
        public int TotalPassedTests { get; set; }
        public int TotalFailedTests { get; set; }
        public string DateOfTestExecution { get; set; }
        public int BuildConfigNumber { get; set; }
        public string BuildNumber { get; set; }
        public double TotalBuildExecutionTime { get; set; }
        public string Uri { get; set; }
        public string FileName { get; set; }
        public int BuildRunId { get; set; }

    }
}