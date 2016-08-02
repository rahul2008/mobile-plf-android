using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS
{
    class AddressDetails
    {
    }

    /// <summary>
    /// 
    /// </summary>
    public class Country
    {
        public string isocode { get; set; }
    }

    public class RestAddress
    {
        public Country country { get; set; }
        public string firstName { get; set; }
        public string id { get; set; }
        public string lastName { get; set; }
        public string line1 { get; set; }
        public string line2 { get; set; }
        public string postalCode { get; set; }
        public string town { get; set; }
    }

    public class RootObjectAddress
    {
        public List<RestAddress> addresses { get; set; }
    }

}
