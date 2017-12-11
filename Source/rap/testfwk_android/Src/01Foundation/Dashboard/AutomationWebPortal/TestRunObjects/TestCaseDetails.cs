using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Philips.H2H.Automation.Dashboard.TestRunObjects
{
    public class TestCaseDetails
    {
        public string TestCaseTitle { get; set; }
        public string Outcome { get; set; }
        public string ShortDate { get; set; }
        //public string TraceOutput { get; set; }
        public string StdOutput { get; set; }
    }
}