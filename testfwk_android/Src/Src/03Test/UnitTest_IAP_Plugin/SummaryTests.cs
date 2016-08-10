using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Philips.H2H.Foundation.AutomationCore.Common;
using System.Threading;
using System.Configuration;
namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin.Tests
{
    [TestClass()]
    public class SummaryTests
    {
        static int _instance = 0;
        public SummaryTests()
        {
            try
            {
               if (_instance == 0)
                {
                    IAPConfiguration.LoadConfigurationForDebug();
                    HomeScreen.Click(HomeScreen.Button.Register);
                    string title1 = LoginScreen.GetScreenTitle();
                    if (title1.Equals("Log In"))
                    {
                        LoginScreen.WaitforLoginScreen();
                        LoginScreen.LoginUser(ConfigurationManager.AppSettings["UsernameIAP"], ConfigurationManager.AppSettings["PasswordIAP"]);

                        HomeScreen.WaitforHomeScreen();
                    }
                    else
                    {
                        LoginScreen.Click(LoginScreen.Button.LoginBackButton);
                        HomeScreen.WaitforHomeScreen();

                    }
                
                    HomeScreen.WaitforHomeScreen();
                    HomeScreen.Click(HomeScreen.Button.CartImage);
                    ShoppingCart.Click(ShoppingCart.Button.Checkout);
                    ShippingAddress.Click(ShippingAddress.Button.Deliver_to_This_Address);
                    bool isExist = false;
                    while (!isExist)
                    {
                        try
                        {
                            Address.IsEnabled(Address.Button.Continue);
                            isExist = true;
                            Address.Click(Address.Button.Continue);
                        }
                        catch(Exception e)
                        {
                            Address.Swipe(Address.Direction.Up, 2);
                        }
                    }
                   
                   
                    Summary.WaitforSummaryScreen();

                }
            }
            catch (Exception)
            {
                Assert.Fail();
            }
            _instance++;
        }
        [TestMethod()]
        public void ClickTest()
        {
            try
            {
                
                Summary.Click(Summary.Button.Pay_Now);
                //Payment.Click(Payment.Button.UpButton);
                Payment.WaitforPaymentScreen();
                Thread.Sleep(35000);
                Payment.EnterPaymentDetails("4444333322221111", "08", "2020", "651");
                Confirmation.WaitforConfirmationScreen();
                Confirmation.Click(Confirmation.Button.OK);
                ShoppingCartItem.Click(ShoppingCartItem.Button.UpButton);
                HomeScreen.WaitforHomeScreen();
               
                    //HomeScreen.GetTextFromCart();
             
                HomeScreen.Click(HomeScreen.Button.Shop_Now);
                Dictionary<int, string> dictionaryvalues = ProductScreen.listofProducts();

                List<string> productid = ProductScreen.GetProductListItems();
                bool isExist = false;
                while (!isExist)
                {
                    try
                    {
                        for (int i = 0; i < dictionaryvalues.Count; i++)
                        {
                            productid = ProductScreen.GetProductListItems();
                            for (int j = 0; j < productid.Count; j++)
                            {
                                if (dictionaryvalues[i].Equals(productid[j]))
                                {

                                    isExist = true;
                                    ProductScreen.Click(dictionaryvalues[i], ProductScreen.Button.productScreen_InfoButton);
                                    ShoppingCartItem.WaitforShoppingCartInfoScreen();
                                   

                                    break;
                                }
                                else
                                    Console.WriteLine("Product is out of stock");
                                //ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                            }
                            if (isExist)
                                break;
                        }
                    }
                    catch (Exception)
                    {

                        ProductScreen.Swipe(ProductScreen.Direction.Up, 2);
                    }
                }
                 isExist = false;
                while (!isExist)
                {
                    try
                    {
                        isExist = ShoppingCartItem.IsExist(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                        if (isExist == false)
                            ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                    }
                    catch (Exception)
                    {

                        ShoppingCartItem.Swipe(ShoppingCartItem.Direction.Up, 2);
                    }
                }


                ShoppingCartItem.Click(ShoppingCartItem.Button.ShoppingCartItem_AddtoCart);
                ShoppingCart.WaitforShoppingCartScreen(); 
                ShoppingCart.Click(ShoppingCart.Button.Checkout);
                ShippingAddress.WaitforShippingAddressScreen();
                ShippingAddress.Click(ShippingAddress.Button.Deliver_to_This_Address);
                isExist = false;
                while (!isExist)
                {
                    try
                    {
                        Address.IsEnabled(Address.Button.Continue);
                        isExist = true;
                        Address.Click(Address.Button.Continue);
                    }
                    catch (Exception e)
                    {
                        Address.Swipe(Address.Direction.Up, 2);
                    }
                }
                   
                Summary.WaitforSummaryScreen();
                Summary.Click(Summary.Button.Cancel);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.Checkout);
                ShippingAddress.WaitforShippingAddressScreen();
                ShippingAddress.Click(ShippingAddress.Button.Deliver_to_This_Address);
                 isExist = false;
                while (!isExist)
                {
                    try
                    {
                        Address.IsEnabled(Address.Button.Continue);
                        isExist = true;
                        Address.Click(Address.Button.Continue);
                    }
                    catch (Exception e)
                    {
                        Address.Swipe(Address.Direction.Up, 2);
                    }
                }
                   
                Summary.WaitforSummaryScreen();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetTextTest()
        {
            try
            {
                Summary.GetText();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetTextTest1()
        {
            try
            {
                    bool isExist = false;
                    while (!isExist)
                    {
                        try{
                            Summary.GetText(Summary.Text.Billing_Address);
                            Summary.GetText(Summary.Text.Billing_FirstName);
                            isExist= true;
                        }
                        catch (Exception)
                        {
                            Summary.Swipe(Summary.Direction.up, 2);
                        }

                    }
                    isExist = false;
                    while (!isExist)
                    {
                        try
                        {
                            Summary.GetText(Summary.Text.Shipping_Address);
                            Summary.GetText(Summary.Text.Shipping_FirstName);
                            isExist = true;
                        }
                        catch (Exception)
                        {
                            Summary.Swipe(Summary.Direction.up, 2);
                        }
                    }

                    isExist = false;
                    while (!isExist)
                    {
                        try
                        {
                            Summary.GetText(Summary.Text.DeliveryPrice);
                            Summary.GetText(Summary.Text.TotalLabel);
                            Summary.GetText(Summary.Text.TotalPrice);
                            isExist = true;
                        }
                        catch (Exception)
                        {
                            Summary.Swipe(Summary.Direction.up, 2);
                        }
                    }
               
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetScreenTitleTest()
        {
            try
            {
                Summary.GetScreenTitle();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void IsExistTest()
        {
            try
            {
                bool isExist = false;
                while (!isExist)
                {
                    try
                    {
                        Summary.IsExist(Summary.Text.Billing_Address);
                        Summary.IsExist(Summary.Text.Billing_FirstName);
                        isExist = true;
                    }
                    catch (Exception)
                    {
                        Summary.Swipe(Summary.Direction.up, 2);
                    }

                }
                isExist = false;
                while (!isExist)
                {
                    try
                    {
                        Summary.IsExist(Summary.Text.Shipping_Address);
                        Summary.IsExist(Summary.Text.Shipping_FirstName);
                        isExist = true;
                    }
                    catch (Exception)
                    {
                        Summary.Swipe(Summary.Direction.up, 2);
                    }
                }

                isExist = false;
                while (!isExist)
                {
                    try
                    {
                        Summary.IsExist(Summary.Text.DeliveryPrice);
                        Summary.IsExist(Summary.Text.TotalLabel);
                        Summary.IsExist(Summary.Text.TotalPrice);
                        isExist = true;
                    }
                    catch (Exception)
                    {
                        Summary.Swipe(Summary.Direction.up, 2);
                    }
                }
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void SwipeTest()
        {
            try
            {
                Summary.Swipe(Summary.Direction.up, 2);
                Summary.Swipe(Summary.Direction.down,2);
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void WaitforSummaryScreenTest()
        {
            try
            {
                Summary.WaitforSummaryScreen();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        
    }
}
