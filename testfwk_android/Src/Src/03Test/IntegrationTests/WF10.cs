using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TechTalk.SpecFlow;

namespace Philips.CDP.Automation.IAP.Tests.Workflows
{
    [Binding]
    class WF10
    {
        HomeScreen hScreen = new HomeScreen();
        ShoppingCart shop = new ShoppingCart();

        private static MobilePageInstance _instance
        {
            get
            {
                return Application.MobilePageInstanceInstance;
            }
        }
    }
}
