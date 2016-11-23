using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Philips.H2H.Automation.Dashboard.TestRunObjects
{
    public class BuildTestRunDetails
    {
        public string buildName { get; set; }
        public string buildNumber { get; set; }
        public string buildFinishTime { get; set; }
        //public int RunId { get; set; }
        public TestRunDetails TestRunDetails { get; set; }
    }
}