using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TechTalk.SpecFlow;
using Philips.H2H.Automation.Foundation.MobileTestCore;
using Philips.H2H.Foundation.AutomationCore;
using Philips.H2H.Foundation.AutomationCore.Mobile;
using Philips.SIG.Automation.Mobile.CDP.Repository;
using System;
using System.Threading;
using TechTalk.SpecFlow;
using System.Collections.Generic;
using Philips.H2H.Foundation.AutomationCore.Interface;
using System.Configuration;
using Philips.CDP.Automation.IAP.Tests.Workflows;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;

namespace Philips.CDP.Automation.IAP.Tests.Workflows
{
    [Binding]
    class UJ03
    {
        HomeScreen hScreen = new HomeScreen();

        ShoppingCart shop = new ShoppingCart();

        ShippingAddress ship = new ShippingAddress();

        Address address = new Address();

        public static MobileDriverConfig mobileDriverConfig = new MobileDriverConfig();

        string Cartnumber = string.Empty;

        private int errCount;
        [Then(@"Verify shopping cart has ""(.*)""")]
        public void ThenVerifyShoppingCartHas(string ProdDesc)
        {
            ShoppingCart.WaitforShoppingCartScreen();
            List<ShoppingCart.ProductDetails> CartItems = ShoppingCart._getproductList();
            //ShoppingCart.ProductDetails ci = CartItems.Find(item => item.Prod_Description.Equals(ProdDesc));
            int quantity = CartItems.Find(item => item.Prod_Description.Equals(ProdDesc)).iQty;
            if (quantity>0)
            {
                IapReport.Message("Pass: Item present in shopping cart: "+ProdDesc);
            }
        }

        [When(@"invalid shipping address is entered")]
        public void WhenInvalidShippingAddressIsEntered()
        {
            errCount = 0;
            //Address.CreateAddress("abhi", "ram", Address.ContextMenu.MenuItem.Mr, "Manyata Tech Park", "Banagalore", "35007", "US", false,"9876543210");
            Thread.Sleep(1000);
            String msg = Address.ValidateTextFields(Address.EditText.FirstName, Address.EditText.LastName, "");
            if (msg.Equals("Please enter a valid first name"))
            { IapReport.Message("Pass: FirstName"); errCount++; }
            else
                Logger.Info("Failed: FirstName: " + msg);
            Thread.Sleep(1000);
            Address.EnterText(Address.EditText.FirstName, "abhi");

            msg = Address.ValidateTextFields(Address.EditText.LastName, Address.EditText.AddressLine1, "");
            if (msg.Equals("Please enter a valid last name"))
            { IapReport.Message("Pass: LastName"); errCount++; }
            else
                Logger.Info("Failed: LastName: " + msg);
            Thread.Sleep(1000);
            Address.EnterText(Address.EditText.LastName, "ram");

            Address.EnterText(Address.EditText.Salutation, "Mr");

            msg = Address.ValidateTextFields(Address.EditText.AddressLine1, Address.EditText.AddressLine2, "");
            if (msg.Equals("Please enter a valid address"))
            { IapReport.Message("Pass: AddressLine1"); errCount++; }
            else
                Logger.Info("Failed: AddressLine1: " + msg);
            Thread.Sleep(1000);
            Address.EnterText(Address.EditText.AddressLine1, "Manyata Tech Park");

            msg = Address.ValidateTextFields(Address.EditText.City, Address.EditText.PostalCode, "");
            if (msg.Equals("Please enter a valid town"))
            { IapReport.Message("Pass: Town"); errCount++; }
            else
                Logger.Info("Failed: Town: " + msg);
            Thread.Sleep(1000);
            Address.EnterText(Address.EditText.City, "Banagalore");

            msg = Address.ValidateTextFields(Address.EditText.PostalCode, Address.EditText.Phone, "");
            if (msg.Equals("Please enter a valid Zip Code"))
            { IapReport.Message("Pass: PostalCode"); errCount++; }
            else
                Logger.Info("Failed: PostalCode: " + msg);
            Thread.Sleep(1000);
            Address.EnterText(Address.EditText.PostalCode, "35007");

            Thread.Sleep(1000);
            if (Address.GetText(Address.EditText.Country) == "US")
            {
                Address.ContextMenu.Select(Address.ContextMenu.Fields.State, Address.ContextMenu.MenuItem.Alabama);
                Address.EnterText(Address.EditText.Phone, "15417543010");
                //Address.HideKeyboard("Android");
            }
            else
            {
                Address.EnterText(Address.EditText.Phone, "442071234567");
                //Address.HideKeyboard("Android");
            }

            Address.Click(Address.Button.Continue);
            Thread.Sleep(5000);
        }

        [Then(@"verify error message is seen")]
        public void ThenVerifyErrorMessageIsSeen()
        {
            if(errCount == 5)
                IapReport.Message("Pass: Shipping address page invalid details messages validated");
            else
                IapReport.Message("Failed: Shipping address page invalid details messages expected: 6"+" found: "+errCount);
            //Address.Click(Address.Button.UpButton);
            //Address.Click(Address.Button.Add_a_new_Address);
        }

        [When(@"invalid billing address is entered")]
        public void WhenInvalidBillingAddressIsEntered()
        {
            errCount = 0;
            //Address.CreateAddress("abhi", "ram", Address.ContextMenu.MenuItem.Mr, "Manyata Tech Park", "Banagalore", "35007", "US", false,"9876543210");
            Thread.Sleep(1000);
            String msg = Address.ValidateTextFields(Address.EditText.FirstName, Address.EditText.LastName, "");
            if (msg.Equals("Please enter a valid first name"))
            { IapReport.Message("Pass: FirstName"); errCount++; }
            else
                Logger.Info("Failed: FirstName: " + msg);
            Thread.Sleep(1000);
            Address.EnterText(Address.EditText.FirstName, "abhi");

            msg = Address.ValidateTextFields(Address.EditText.LastName, Address.EditText.AddressLine1, "");
            if (msg.Equals("Please enter a valid last name"))
            { IapReport.Message("Pass: LastName"); errCount++; }
            else
                Logger.Info("Failed: LastName: " + msg);
            Thread.Sleep(1000);
            Address.EnterText(Address.EditText.LastName, "ram");

            Address.EnterText(Address.EditText.Salutation, "Mr");

            msg = Address.ValidateTextFields(Address.EditText.AddressLine1, Address.EditText.AddressLine2, "");
            if (msg.Equals("Please enter a valid address"))
            { IapReport.Message("Pass: AddressLine1"); errCount++; }
            else
                Logger.Info("Failed: AddressLine1: " + msg);
            Thread.Sleep(1000);
            Address.EnterText(Address.EditText.AddressLine1, "Manyata Tech Park");

            msg = Address.ValidateTextFields(Address.EditText.City, Address.EditText.PostalCode, "");
            if (msg.Equals("Please enter a valid town"))
            { IapReport.Message("Pass: Town"); errCount++; }
            else
                Logger.Info("Failed: Town: " + msg);
            Thread.Sleep(1000);
            Address.EnterText(Address.EditText.City, "Banagalore");

            msg = Address.ValidateTextFields(Address.EditText.PostalCode, Address.EditText.Phone, "");
            if (msg.Equals("Please enter a valid Zip Code"))
            { IapReport.Message("Pass: PostalCode"); errCount++; }
            else
                Logger.Info("Failed: PostalCode: " + msg);
            Thread.Sleep(1000);
            Address.EnterText(Address.EditText.PostalCode, "35007");

            Thread.Sleep(1000);
            if (Address.GetText(Address.EditText.Country) == "US")
            {
                Address.ContextMenu.Select(Address.ContextMenu.Fields.State, Address.ContextMenu.MenuItem.Alabama);
                Address.EnterText(Address.EditText.Phone, "15417543010");
                //Address.HideKeyboard("Android");
            }
            else
            {
                Address.EnterText(Address.EditText.Phone, "442071234567");
                //Address.HideKeyboard("Android");
            }

            Address.Click(Address.Button.Continue);
            Thread.Sleep(5000);
        }




    }
}
