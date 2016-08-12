using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS;
using System.Threading;

namespace Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS.Tests
{
    [TestClass()]
    public class AddressTests
    {
        static int _instance = 0;
        static List<Philips.SIG.Automation.iOS.CDPP.IAPTestPluginForiOS.ShippingAddress.Shipping_Address> addrList;
        public AddressTests()
        {
            if (_instance == 0)
            {
                try
                {

                    IAPConfiguration.LoadConfigurationForiOS();
                    HomeScreen.WaitforHomeScreen();
                    HomeScreen.SelectCountry("US");
                    HomeScreen.WaitforHomeScreen();
                    Thread.Sleep(3000);
                    HomeScreen.Click(HomeScreen.Button.ShopNow);
                    ProductScreen.WaitforProductScreen();
                    ProductScreen.SelectProduct(ProductScreen._getListOfProducts()[2].name);
                    ShoppingCartItem.Click(ShoppingCartItem.Button.AddToCart);
                    //if out of stok, click ok in the popup
                    //UIAApplication[1]/UIAWindow[1]/UIAStaticText[2]
                    //UIAApplication[1]/UIAWindow[1]/UIAButton[4]
                    ShoppingCart.WaitforShoppingCartScreen();
                    Thread.Sleep(3000);
                    ShoppingCart.Click(ShoppingCart.Button.Checkout);
                    ShippingAddress.WaitforShippingAddressScreen();
                    //List<IAP.iOS.TestPlugin.ShippingAddress.Shipping_Address> addrList = ShippingAddress.GetListofAddress();
                    addrList = ShippingAddress.GetListofAddress();
                    
                }
                catch (Exception e)
                {
                    Assert.Fail();
                }
            }
            _instance++;
        }
        
        [TestMethod()]
        public void EnterTextTest()
        {
            try
            {
                ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.AddNewAddr);
                Address.WaitforAddressScreen();
                Address.EnterText(Address.EditText.FirstName, "Test");
                Address.EnterText(Address.EditText.LastName, "Shopper");
                Address.EnterText(Address.EditText.Salutation, "Mr.");
                Address.EnterText(Address.EditText.AddressLine1, "dummy lane");
                Address.EnterText(Address.EditText.AddressLine2, "dummy place");
                Address.EnterText(Address.EditText.City, "Huntsville");
                Address.EnterText(Address.EditText.PostalCode, "123");
                
                if (Address.GetText(Address.EditText.Country) == "")
                {
                    Address.EnterText(Address.EditText.Country, "US");
                    Address.EnterText(Address.EditText.Phone, "7654763465");
                }
                else if (Address.GetText(Address.EditText.Country) == "UK")
                {
                    Address.EnterText(Address.EditText.State, "LONDON");
                    Address.EnterText(Address.EditText.Phone, "01145525889");
                }
                else if (Address.GetText(Address.EditText.Country) == "US")
                {
                    Address.EnterText(Address.EditText.State, "Alabama");
                    Address.EnterText(Address.EditText.Phone, "7654763465");
                }

                if (Address.GetText(Address.EditText.Email) == "")
                {
                    Address.EnterText(Address.EditText.Email, "inapptest@mailinator.com");
                }
                Address.Click(Address.Button.UpButton);
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void GetTextTest()
        {
            try
            {
                ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.DeliverToThisAddr);
                Address.WaitforAddressScreen();
                string b = Address.GetText(Address.EditText.AddressLine1);
                b = Address.GetText(Address.EditText.AddressLine2);
                b = Address.GetText(Address.EditText.City);
                b = Address.GetText(Address.EditText.Country);
                b = Address.GetText(Address.EditText.Email);
                b = Address.GetText(Address.EditText.FirstName);
                b = Address.GetText(Address.EditText.LastName);
                b = Address.GetText(Address.EditText.Phone);
                b = Address.GetText(Address.EditText.PostalCode);
                b = Address.GetText(Address.EditText.Salutation);
                b = Address.GetText(Address.EditText.State);
                Address.Click(Address.Button.UpButton);
            }
            catch (Exception e)
            {
                Assert.Fail();
            } 
        }

        [TestMethod()]
        public void GetHeaderTest()
        {
            try
            {

                ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.DeliverToThisAddr);
                Address.WaitforAddressScreen();
                string str = Address.GetHeader();
                Address.Click(Address.Button.UpButton);
                ShippingAddress.WaitforShippingAddressScreen();
                ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.AddNewAddr);
                Address.WaitforAddressScreen();
                str = Address.GetHeader();
                Address.Click(Address.Button.UpButton);
                ShippingAddress.WaitforShippingAddressScreen();
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void ClickTest()
        {
             //addrList = ShippingAddress.GetListofAddress();
            try
            {
                ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.DeliverToThisAddr);
                Address.WaitforAddressScreen();
                
                if (Address.GetHeader() == "Billing Address")
                    {
                        Address.Click(Address.Button.Switch);
                        Address.Click(Address.Button.Switch);
                        if (Address.IsEnabled(Address.Button.Continue))
                        {
                            Address.Click(Address.Button.Continue);
                            Summary.WaitforSummaryScreen();
                            Summary.Click(Summary.Button.UpButton);
                        }
                            

                        Address.Click(Address.Button.Cancel);
                        ShippingAddress.WaitforShippingAddressScreen();
                    }   
                //Address.Click(Address.Button.UpButton);
                //ShippingAddress.WaitforShippingAddressScreen();
                ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.AddNewAddr);
                Address.WaitforAddressScreen();
                if(Address.GetHeader() == "Shipping Address")
                    {
                    Address.Click(Address.Button.UpButton);
                    ShippingAddress.WaitforShippingAddressScreen();
                    ShippingAddress.Click(addrList[0].completeAddress,ShippingAddress.Button.AddNewAddr);
                    Address.Click(Address.Button.Cancel);
                    ShippingAddress.WaitforShippingAddressScreen();
                    ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.AddNewAddr);
                    Address.WaitforAddressScreen();
                    Address.EnterText(Address.EditText.FirstName, "Test");
                    Address.EnterText(Address.EditText.LastName, "Shopper");
                    Address.EnterText(Address.EditText.Salutation, "Mr.");
                    Address.EnterText(Address.EditText.AddressLine1, "dummy lane");
                    Address.EnterText(Address.EditText.AddressLine2, "dummy place");
                    Address.EnterText(Address.EditText.City, "Huntsville");
                    Address.EnterText(Address.EditText.PostalCode, "123");

                    if (Address.GetText(Address.EditText.Country) == "")
                    {
                        Address.EnterText(Address.EditText.Country, "US");
                        Address.EnterText(Address.EditText.Phone, "7654763465");
                    }
                    else if (Address.GetText(Address.EditText.Country) == "UK")
                    {
                        Address.EnterText(Address.EditText.State, "LONDON");
                        Address.EnterText(Address.EditText.Phone, "01145525889");
                    }
                    else if (Address.GetText(Address.EditText.Country) == "US")
                    {
                        Address.EnterText(Address.EditText.State, "Alabama");
                        Address.EnterText(Address.EditText.Phone, "7654763465");
                    }

                    if (Address.GetText(Address.EditText.Email) == "")
                    {
                        Address.EnterText(Address.EditText.Email, "inapptest@mailinator.com");
                    }

                    Address.Click(Address.Button.Continue);

                    Address.WaitforAddressScreen();
                    Address.Click(Address.Button.UpButton);
                    Address.Click(Address.Button.UpButton);
                    }
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void IsExistTest()
        {
            //List<IAP.iOS.TestPlugin.ShippingAddress.Shipping_Address> addrList = ShippingAddress.GetListofAddress();
            try
            {
                ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.DeliverToThisAddr);
                Address.WaitforAddressScreen();
                bool b = Address.IsExist(Address.Button.Continue);
                b = Address.IsExist(Address.Button.Cancel);
                Address.Click(Address.Button.UpButton);
                ShippingAddress.WaitforShippingAddressScreen();
                ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.AddNewAddr);
                Address.WaitforAddressScreen();
                b = Address.IsExist(Address.Button.Continue);
                b= Address.IsExist(Address.Button.Cancel);
                Address.Click(Address.Button.UpButton);
                ShippingAddress.WaitforShippingAddressScreen();
                ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.Dots);
                b = Address.IsExist(Address.Button.Save);
                b = Address.IsExist(Address.Button.Cancel);
                b = Address.IsExist(Address.Button.UpButton);
                Address.Click(Address.Button.UpButton);
                ShippingAddress.WaitforShippingAddressScreen();
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void IsEnabledTest()
        {
            
            try
            {
                ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.DeliverToThisAddr);
                Address.WaitforAddressScreen();
                bool b = Address.IsEnabled(Address.Button.Continue);
                b = Address.IsEnabled(Address.Button.Cancel);
                Address.Click(Address.Button.UpButton);
                ShippingAddress.WaitforShippingAddressScreen();
                ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.AddNewAddr);
                Address.WaitforAddressScreen();
                b = Address.IsEnabled(Address.Button.Continue);
                b = Address.IsEnabled(Address.Button.Cancel);
                Address.Click(Address.Button.UpButton);
                ShippingAddress.WaitforShippingAddressScreen();
                ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.Dots);
                b = Address.IsEnabled(Address.Button.Save);
                b = Address.IsEnabled(Address.Button.Cancel);
                b = Address.IsEnabled(Address.Button.UpButton);
                Address.Click(Address.Button.UpButton);
                ShippingAddress.WaitforShippingAddressScreen();
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void IsEnabledTest1()
        {
            bool b = true;
            try
            {
                ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.DeliverToThisAddr);
                Address.WaitforAddressScreen();
                b = Address.IsEnabled(Address.EditText.FirstName);
                b = b & Address.IsEnabled(Address.EditText.LastName);
                b = b & Address.IsEnabled(Address.EditText.AddressLine1);
                b = b & Address.IsEnabled(Address.EditText.AddressLine2);
                b = b & Address.IsEnabled(Address.EditText.City);
                b = b & Address.IsEnabled(Address.EditText.Country);
                b = b & Address.IsEnabled(Address.EditText.Email);
                b = b & Address.IsEnabled(Address.EditText.Phone);
                b = b & Address.IsEnabled(Address.EditText.State);
                b = b & Address.IsEnabled(Address.EditText.PostalCode);
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void IsVisibleTest()
        {
            bool b = true;
            try
            {
                ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.Dots);
                Address.WaitforAddressScreen();
                b = b & Address.IsVisible(Address.Button.Save);
                Address.Click(Address.Button.UpButton);
                ShippingAddress.WaitforShippingAddressScreen();
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        //[TestMethod()]
        //public void ValidateTextFieldsTest()
        //{
        //    try
        //    {
        //        Address.ValidateTextFields(Address.EditText.FirstName, Address.EditText.LastName, "ananya");
        //    }
        //    catch (Exception e)
        //    {
        //        Assert.Fail();
        //    }
        //}

        [TestMethod()]
        public void WaitforAddressScreenTest()
        {
            try
            {
                ShippingAddress.Click(addrList[0].completeAddress, ShippingAddress.Button.DeliverToThisAddr);
                bool b = Address.WaitforAddressScreen();
                Address.Click(Address.Button.UpButton);
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        
    }
}
