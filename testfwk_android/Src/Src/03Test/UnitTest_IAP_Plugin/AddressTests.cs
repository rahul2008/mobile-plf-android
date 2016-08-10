using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Philips.CDP.Automation.TestAuthoring.IAP.Plugin;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
namespace Philips.CDP.Automation.TestAuthoring.IAP.Plugin.Tests
{
    [TestClass()]
    public class AddressTests
    {
        static int _instance = 0;
         public AddressTests()
        {
            if (_instance == 0)
            {
                try
                {
                    IAPConfiguration.LoadConfigurationForDebug();
                    //IAPConfiguration.LoadConfiguration();
                    //LoginScreen.Login("lokesh", "philips@123");
                    HomeScreen.WaitforHomeScreen();
                    HomeScreen.Click(HomeScreen.Button.CartImage);
                    ShoppingCart.WaitforShoppingCartScreen();
                    ShoppingCart.Click(ShoppingCart.Button.Checkout);
                    ShippingAddress.WaitforShippingAddressScreen();
                    ShippingAddress.Click(ShippingAddress.Button.Add_a_new_Address);
                    Address.WaitforAddressScreen();
                }catch(Exception e)
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
                ShippingAddress.Click(ShippingAddress.Button.Add_a_new_Address);
                Address.EnterText(Address.EditText.FirstName, "abhi");
                Address.HideKeyboard("Android");
                Address.EnterText(Address.EditText.LastName, "ram");
                Address.HideKeyboard("Android");
                Address.ContextMenu.Select(Address.ContextMenu.Fields.Salutation, Address.ContextMenu.MenuItem.Mr);
                Address.EnterText(Address.EditText.AddressLine1, "manyata tech park");
                Address.HideKeyboard("Android");
                Address.EnterText(Address.EditText.AddressLine2, "abc");
                Address.HideKeyboard("Android");
                Address.EnterText(Address.EditText.City, "xyz");
                Address.HideKeyboard("Android");
                Address.EnterText(Address.EditText.PostalCode, "534654");
                Address.HideKeyboard("Android");
                //Address.EnterText(Address.EditText.Country, "US");
                //Address.HideKeyboard("Android");
                //Address.EnterText(Address.EditText.State, "Alabama");
                if (Address.GetText(Address.EditText.Email) == "")
                {
                    Address.EnterText(Address.EditText.Email, "inapptest@mailinator.com");
                    Address.HideKeyboard("Android");
                }
                Address.EnterText(Address.EditText.Phone, "442071234567");
                Address.HideKeyboard("Android");
                

                bool isExist = false;
                while (!isExist)
                {
                    try
                    {
                        Address.GetHeader();
                        isExist = true;
                    }
                    catch (Exception)
                    {
                        Address.Swipe(Address.Direction.Down, 2);
                    }
                }
                Address.Click(Address.Button.UpButton);
            }
            catch(Exception e)
            {
                Assert.Fail();
            }
            //Assert.Fail();
        }

        [TestMethod()]
        public void GetTextTest()
        {
            try
            {
                string b = Address.GetText(Address.EditText.Country);
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
            //Assert.Fail();
        }

        [TestMethod()]
        public void GetHeaderTest()
        {
            try
            {
               bool isExist = false;
                while (!isExist)
                {
                    try
                    {
                        Address.GetHeader();
                        isExist = true;
                    }
                    catch (Exception)
                    {
                        Address.Swipe(Address.Direction.Down, 2);
                    }
                }
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
            //Assert.Fail();
        }

        [TestMethod()]
        public void ClickTest()
        {
            try
            {
                  
                    ShippingAddress.Click(ShippingAddress.Button.Deliver_to_This_Address);
                    if (Address.GetHeader() == "Billing address")
                        Address.Click(Address.Button.Switch);
                
                bool isExist = false;
                while (!isExist)
                {
                    try{
                    isExist = Address.IsExist(Address.Button.Cancel);
                    if (isExist == false)
                        Address.Swipe(Address.Direction.Up, 2);
                    else
                        isExist = true;
                    }
                    catch(Exception)
                {
                    Address.Swipe(Address.Direction.Up);
                }
                }
                if (Address.IsEnabled(Address.Button.Continue))
                    Address.Click(Address.Button.Continue);
               // Summary.Click(Summary.Button.UpButton);
                //Address.WaitforAddressScreen();

                
              
                Address.Click(Address.Button.Cancel);
                ShippingAddress.WaitforShippingAddressScreen();
                ShippingAddress.Click(ShippingAddress.Button.Cancel);
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.UpButton);
                HomeScreen.WaitforHomeScreen();
                HomeScreen.Click(HomeScreen.Button.CartImage);
            
                ShoppingCart.WaitforShoppingCartScreen();
                ShoppingCart.Click(ShoppingCart.Button.Checkout);
                ShippingAddress.WaitforShippingAddressScreen();
               
              
              
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void IsExistTest()
        {
            try
            {
                ShippingAddress.Click(ShippingAddress.Button.Deliver_to_This_Address);
                Address.IsExist(Address.Button.UpButton);
                bool isExist = false;
                while (!isExist)
                {
                    try
                    {
                        Address.IsExist(Address.Button.Continue);
                        isExist = true;
                    }
                    catch(Exception e)
                    {
                        Address.Swipe(Address.Direction.Up, 2);
                    }
                }
                Address.Click(Address.Button.UpButton);
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void IsEnabledTest()
        {
            bool b = true;
            try
            {
                Address.Click(Address.Button.UpButton);
                ShippingAddress.WaitforShippingAddressScreen();
                ShippingAddress.Click(ShippingAddress.Button.Deliver_to_This_Address);
                Address.WaitforAddressScreen();
                if (Address.GetHeader() == "Billing address")
                    b = Address.IsEnabled(Address.Button.Switch);
                bool isExist = false;
                while (!isExist)
                {
                    try
                    {
                        isExist = Address.IsExist(Address.Button.Cancel);
                        isExist = true;
                    }catch(Exception e)
                    {
                         Address.Swipe(Address.Direction.Up, 2);
                    }
                        
                            
                }
                b = b & Address.IsEnabled(Address.Button.Cancel);
                b = b & Address.IsEnabled(Address.Button.Continue);
                b = b & Address.IsEnabled(Address.Button.UpButton);
                isExist = false;
                while (!isExist)
                {
                    try
                    {
                        if (Address.GetHeader() == "Billing address")
                        {
                            isExist = true;
                            Address.Click(Address.Button.UpButton);
                           
                        }
                    }
                    catch (Exception)
                    {
                        Address.Swipe(Address.Direction.Down, 2);
                    }
                }
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
                ShippingAddress.Click(ShippingAddress.Button.Deliver_to_This_Address);
                b = Address.IsEnabled(Address.EditText.FirstName);
                b = b & Address.IsEnabled(Address.EditText.LastName);
                b = b & Address.IsEnabled(Address.EditText.AddressLine1);
                b = b & Address.IsEnabled(Address.EditText.AddressLine2);
                bool isExist = false;
                while (!isExist)
                {
                    try
                    {
                        isExist = Address.IsExist(Address.Button.Cancel);
                        isExist = true;
                    }
                    catch(Exception e)
                    {
                          Address.Swipe(Address.Direction.Up, 2);
                    }
                    
                  
                        
                }
                b = b & Address.IsEnabled(Address.EditText.City);
                b = b & Address.IsEnabled(Address.EditText.Country);
                b = b & Address.IsEnabled(Address.EditText.Email);
                b = b & Address.IsEnabled(Address.EditText.Phone);
                b = b & Address.IsEnabled(Address.EditText.PostalCode);
                isExist = false;
                while (!isExist)
                {
                    try
                    {
                        Address.GetHeader();
                        isExist = true;
                    }
                    catch (Exception)
                    {
                        Address.Swipe(Address.Direction.Down, 2);
                    }
                }
                Address.Click(Address.Button.UpButton);
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
                ShippingAddress.Click(ShippingAddress.Button.Add_a_new_Address);
                Address.WaitforAddressScreen();
               
                bool isExist = false;
                while (!isExist)
                {
                    try {
                        isExist = Address.IsExist(Address.Button.Cancel);
                        isExist = true;
                    }
                    catch(Exception e)
                    { 
                        Address.Swipe(Address.Direction.Up, 2);
                    }
                      
                        
                            
                }
                
                
                b = b & Address.IsVisible(Address.Button.Continue);
                isExist = false;
                while (!isExist)
                {
                    try
                    {
                        Address.GetHeader();
                        isExist = true;
                    }
                    catch (Exception)
                    {
                        Address.Swipe(Address.Direction.Down, 2);
                    }
                }
                Address.Click(Address.Button.UpButton);
            }catch(Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void SwipeTest()
        {
            try
            {
                ShippingAddress.Click(ShippingAddress.Button.Add_a_new_Address);
                Address.Swipe(Address.Direction.Up);
                Address.Click(Address.Button.UpButton);
               
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void ValidateTextFieldsTest()
        {
            try
            {
                ShippingAddress.Click(ShippingAddress.Button.Add_a_new_Address);
                Address.WaitforAddressScreen();
                Address.ValidateTextFields(Address.EditText.FirstName, Address.EditText.LastName, "ananya");
                Address.HideKeyboard("Android");
                Address.Click(Address.Button.UpButton);
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void WaitforAddressScreenTest()
        {
            try
            {
                ShippingAddress.Click(ShippingAddress.Button.Add_a_new_Address);
                 Address.WaitforAddressScreen();
                Address.Click(Address.Button.UpButton);
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }

        [TestMethod()]
        public void HideKeyboardTest()
        {
            try
            {
                ShippingAddress.Click(ShippingAddress.Button.Add_a_new_Address);
                Address.EnterText(Address.EditText.FirstName, "");
                Address.HideKeyboard("Android");
                Address.Click(Address.Button.UpButton);
            }
            catch (Exception e)
            {
                Assert.Fail();
            }
        }
    }
}
