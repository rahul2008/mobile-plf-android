using Philips.H2H.Foundation.AutomationCore;
using Philips.SIG.Automation.Mobile.CDP.IAPTestPlugin;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Philips.H2H.Foundation.AutomationCore.Common;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.CDP.Automation.TestAuthoring.IAP.Plugin;
using System.Configuration;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.H2H.Foundation.AutomationCore.Interface;

namespace SandBox
{
    class Program
    {

        static void Main(string[] args)
        {

            MobileDriverConfig mConfig = new MobileDriverConfig();
            IAPConfiguration.LoadConfiguration(mConfig);
            HomeScreen hScreen = new HomeScreen();
            ShoppingCart shop = new ShoppingCart();
            ShippingAddress ship = new ShippingAddress();
            HomeScreen.Click(HomeScreen.Button.BuyNow);
            shop.Click(ShoppingCart.Button.UpButton);
            HomeScreen.Click(HomeScreen.Button.AddToCart);
            HomeScreen.Click(HomeScreen.Button.CartImage);

            ShoppingCart.IsProductExist("Sonicare AirFloss Pro - Interdental cleaner");
            shop.Click(ShoppingCart.Button.Dots);
            shop.Click(ShoppingCart.Button.ClaimVoucherArrow);
            shop.getUPSParcel();
            shop.GetAllProductCost();
            shop.Click(ShoppingCart.Button.Continue_shopping);
            shop.Click(ShoppingCart.Button.Checkout);
            ship.Click(ShippingAddress.Button.arrow);
            hScreen.verifyBuynowCartNumber();

        }
    }
}
