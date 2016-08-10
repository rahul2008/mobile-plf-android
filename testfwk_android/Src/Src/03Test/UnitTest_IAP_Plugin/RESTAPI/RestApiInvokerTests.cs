using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Net;
using System.Net.Security;
namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin.Tests
{
    [TestClass()]
    public class RestApiInvokerTests
    {
        static int instance = 0;
        public RestApiInvokerTests()
        {if(instance == 0)
        {
            ServicePointManager.ServerCertificateValidationCallback = new RemoteCertificateValidationCallback
            (
                delegate { return true; }
            );
        }
            instance++;
        }
        [TestMethod()]
        public void GetCartTest()
        {
            CartList list = RestApiInvoker.GetCart("inapptest@mailinator.com", "US");
            if (list == null)
                Assert.Fail();
             
        }

        [TestMethod()]
        public void GetCartEntriesTest()
        {
            CartEntriesList list = RestApiInvoker.GetCartEntries("inapptest@mailinator.com", "US");
            if (list == null)
                Assert.Fail();
        }

        [TestMethod()]
        public void GetAddressesTest()
        {
            AddressList list = RestApiInvoker.GetAddresses("inapptest@mailinator.com", "US");
            if (list == null)
                Assert.Fail();
        }

        [TestMethod()]
        public void GetPaymentDetailsTest()
        {
            PaymentList list = RestApiInvoker.GetPaymentDetails("inapptest@mailinator.com", "US");
            if (list == null)
                Assert.Fail();
        }

        [TestMethod()]
        public void CreateCartTest()
        {
            CreateCartResult list = RestApiInvoker.CreateCart("inapptest@mailinator.com", "US");
            if (list == null)
                Assert.Fail();
        }

        [TestMethod()]
        public void AddToCartTest()
        {
            AddToCartResponse list = RestApiInvoker.AddToCart("inapptest@mailinator.com", "14000096440", "HX8331%2F11", "US");
            if (list == null)
                Assert.Fail();
        }

        [TestMethod()]
        public void CreateAddressTest()
        {
            string newAddr = "firstName=rakesh&lastName=r&titleCode=mr&country.isocode=US&line1=test+line1&line2=test+line2&postalCode=12-345&town=testTown&phoneNumber=1234567890";
            CreateAddressResponse list = RestApiInvoker.CreateAddress("inapptest@mailinator.com", newAddr, "US");
            if (list == null)
                Assert.Fail();
        }

        //[TestMethod()]
        //public void GetDeliveryModesTest()
        //{
        //    Assert.Fail();
        //}

        [TestMethod()]
        public void SubmitOrderTest()
        {
            SubmitOrderResponse list = RestApiInvoker.SubmitOrder("inapptest@mailinator.com", "14000096440","US");
            if (list == null)
                Assert.Fail();
        }

        [TestMethod()]
        public void PostPaymentTest()
        {
            PaymentResponse list = RestApiInvoker.PostPayment("inapptest@mailinator.com", "14000096440", "8807749615639", "US");
            if (list == null)
            Assert.Fail();
        }

        //[TestMethod()]
        //public void DeleteCartTest()
        //{
        //    RestApiInvoker.DeleteCart("inapptest@mailinator.com", "14000095903");
        //    Assert.Fail();
        //}

        //[TestMethod()]
        //public void DeleteAddressTest()
        //{
        //    RestApiInvoker.DeleteAddress("inapptest@mailinator.com", "8807749615639");
        //    DeliveryModeList list = RestApiInvoker.GetDeliveryModes("inapptest@mailinator.com", "14000095905");
        //    if (list == null)
        //        Assert.Fail();
        //}

        //[TestMethod()]
        //public void SetDeliveryAddressTest()
        //{
        //    Assert.Fail();
        //}

        //[TestMethod()]
        //public void SetDeliveryModeTest()
        //{
        //    Assert.Fail();
        //}

        //[TestMethod()]
        //public void SetPaymentDetailsTest()
        //{
        //    Assert.Fail();
        //}

        [TestMethod()]
        public void GetSearchCategoriesTest()
        {
            SearchCategories list = RestApiInvoker.GetSearchCategories("", "US");
            if (list == null)
                Assert.Fail(); 
        }
    }
}
