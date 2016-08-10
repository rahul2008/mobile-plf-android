using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Philips.H2H.Automation.Dashboard.TestRunObjects
{
    public class TestRunDetails
    {
        public int TestRunId { get; set; }
        public List<TestCaseDetails> TestCaseDetails { get; set; }
        //public string Outcome { get; set; }
        //public string ShortDate { get; set; }
        //public string TraceOutput { get; set; }
        //public string StdOutput { get; set; }
    }
}