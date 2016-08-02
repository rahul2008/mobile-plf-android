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
{[Binding]
    class WF6
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
        [Then(@"Verify product quantity is editable")]
        public void ThenVerifyProductQuantityIsEditable()
        {
            ShoppingCart.WaitforShoppingCartScreen();
            List<String> ProdNames = ShoppingCart.GetProductNames();
            int Quantity = Int32.Parse(ShoppingCart.GetText(ProdNames[0], ShoppingCart.ProductDetailsText.Quantity));
            Quantity += 1;
            ProdNames = ShoppingCart.GetProductNames();
            ShoppingCart.Select(ProdNames[0], Quantity);
            int newQuantity = Int32.Parse(ShoppingCart.GetText(ProdNames[0], ShoppingCart.ProductDetailsText.Quantity));
            if (newQuantity == Quantity)
                IapReport.Message("Step Passed: quantity is editable");
            else
                IapReport.Fail("Step Failed: quantity is not editable");
                
        }

    }
}