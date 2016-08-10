using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Configuration;
using System.Threading;

namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin.Tests
{
    [TestClass()]
    public class PaymentTests
    {
        static int _instance = 0;

        public PaymentTests()
        {
            if (_instance == 0)
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
                    }
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
                        catch (Exception e)
                        {
                            Address.Swipe(Address.Direction.Up, 2);
                        }
                    }


                    Summary.WaitforSummaryScreen();

                    Summary.Click(Summary.Button.Pay_Now);
                    Payment.WaitforPaymentScreen();
                    
                }
                catch (Exception e)
                {
                    Assert.Fail();
                }
            }
            _instance++;
        }

        [TestMethod()]
        public void ClickTest()
        {
            try
            {
                

                // start payment test
                Payment.EnterText(Payment.EditBox.CardNumber, "4444333322221111");
                Payment.Select(Payment.DropDown.ExpiryMonth, "06");
                Payment.Select(Payment.DropDown.ExpiryYear, "2016");
                Payment.EnterText(Payment.EditBox.SecurityCode, "135");
                Thread.Sleep(2000);
               

                //Payment.Click(Payment.Button.CancelPayment);
                //if (CancelPayment.IsExist())
                //    CancelPayment.Click(CancelPayment.Button.No);
                Payment.Click(Payment.Button.MakePayment);
                Confirmation.WaitforConfirmationScreen();
                Confirmation.Click(Confirmation.Button.OK);
                ClickTest();
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        
        }

        [TestMethod()]
        public void EnterTextTest()
        {
            try
            {
                //MobileDriver.
                Payment.EnterText(Payment.EditBox.CardNumber, "4444333322221111");
                Payment.EnterText(Payment.EditBox.SecurityCode, "123");
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
            // Assert.Fail();
        }

        [TestMethod()]
        public void SelectTest()
        {
            try
            {
                
                Payment.Select(Payment.DropDown.ExpiryMonth, "02");
                Payment.Select(Payment.DropDown.ExpiryYear, "2017");
                //put get header in a try catch
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
            //Assert.Fail();
        }

        [TestMethod()]
        public void SwipeTest()
        {
            try
            {
               
                Payment.Swipe(Payment.Direction.up, 2);
                Payment.Swipe(Payment.Direction.down, 2);
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
            
        }

        [TestMethod()]
        public void GetPageTitleTest()
        {
            try
            {
                Payment.GetPageTitle();
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
            // Assert.Fail();
        }

        [TestMethod()]
        public void WaitforPaymentScreenTest()
        {
            try
            {
                Payment.WaitforPaymentScreen();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

      
        [TestMethod()]
        public void GetHeaderTest()
        {
            try
            {
                Payment.GetHeader();
            }
            catch (Exception)
            {
                Assert.Fail();    
            }
        }

        [TestMethod()]
        public void GetListofPaymentMethodsTest()
        {
            try
            {
                Payment.GetListofPaymentMethods();
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void SelectPaymentMethodTest()
        {
            try
            {
               
              //  Payment.SelectPaymentMethod("visa 4444********1111");
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }
                
        [TestMethod()]
        public void SwipeDateTest()
        {
            try
            {
                
                Payment.Select(Payment.DropDown.ExpiryMonth, "9");
            }
            catch (Exception)
            {
                Assert.Fail();
            }
        }
    }
}
