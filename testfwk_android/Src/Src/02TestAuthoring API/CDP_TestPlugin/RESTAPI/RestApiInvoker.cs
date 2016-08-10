using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using System.Net;
using System.Net.Security;

namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin
{
    /// <summary>
    /// This class is used to invoke REST APIs from the tests.
    /// </summary>
    public class RestApiInvoker
    {
        static string endPoint = null;
        static string parameters = null;
        static string response = null;
        static string storeName = null;
        static string environmentUsed = ConfigurationManager.AppSettings["envForRESTCalls"];
        static string baseURL = null;
        /// <summary>
        /// This method calls the Get Cart API to fetch all the carts defined for logged in user.
        /// </summary>
        /// <param name="username">username represents the logged in user.</param>
        /// <returns>It returns a list of carts as an object of CartList class.</returns>
        public static CartList GetCart(string username,string store)
         {
             ValidateSSLCertificate();
 
            if (environmentUsed.Equals("Test"))
                 baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsTst"];
             else
                 baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsProd"]; 
            
            if (store.Equals("US"))
                storeName = ConfigurationManager.AppSettings["SiteNameForUS"];
            else
                storeName = ConfigurationManager.AppSettings["SiteNameForUK"];

             endPoint = baseURL + "v2/" + storeName + "/users/" + username + "/carts/?fields=FULL";
            string contentType = "text/json";
            RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.GET, contentType, "");
            parameters = "";
            response = restObj.MakeRequest(parameters);
            CartList root = JsonConvert.DeserializeObject<CartList>(response);
            return root;
        }

        /// <summary>
        /// This method calls the Get Cart Entries API to fetch all the entries in the cart defined for logged in user.
        /// </summary>
        /// <param name="username">username represents the logged in user.</param>
        /// <returns>It returns a list of carts as an object of CartEntriesList class.</returns>
        public static CartEntriesList GetCartEntries(string username,string store)
        {
            ValidateSSLCertificate();

            if (environmentUsed.Equals("Test"))
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsTst"];
            else
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsProd"];

            if (store.Equals("US"))
                storeName = ConfigurationManager.AppSettings["SiteNameForUS"];
            else
                storeName = ConfigurationManager.AppSettings["SiteNameForUK"];

            endPoint = baseURL + "v2/" + storeName + "/users/" + username + "/carts/current/entries";
            string contentType = "text/json";
            RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.GET, contentType, "");
            parameters = "";
            response = restObj.MakeRequest(parameters);
            CartEntriesList root = JsonConvert.DeserializeObject<CartEntriesList>(response);
            return root;
        }

        /// <summary>
        /// This method calls the Get Addresses API to fetch all the addresses defined for logged in user.
        /// </summary>
        /// <param name="username">username represents the logged in user.</param>
        /// <returns>It returns a list of carts as an object of AddressList class.</returns>
        public static AddressList GetAddresses(string username, string store)
        {
            ValidateSSLCertificate();

            if (environmentUsed.Equals("Test"))
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsTst"];
            else
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsProd"];

            if (store.Equals("US"))
                storeName = ConfigurationManager.AppSettings["SiteNameForUS"];
            else
                storeName = ConfigurationManager.AppSettings["SiteNameForUK"];

            endPoint = baseURL + "v2/" + storeName + "/users/" + username + "/addresses?fields=FULL";
            string contentType = "text/json";
            RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.GET, contentType, "");
            parameters = "";
            response = restObj.MakeRequest(parameters);
            AddressList root = JsonConvert.DeserializeObject<AddressList>(response);
            return root;
        }

        /// <summary>
        /// This method calls the Get Payment Details API to fetch all the payment details defined for logged in user.
        /// </summary>
        /// <param name="username">username represents the logged in user.</param>
        /// <returns>It returns a list of carts as an object of PaymentList class.</returns>
        public static PaymentList GetPaymentDetails(string username, string store)
        {
            ValidateSSLCertificate();

            if (environmentUsed.Equals("Test"))
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsTst"];
            else
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsProd"];

            if (store.Equals("US"))
                storeName = ConfigurationManager.AppSettings["SiteNameForUS"];
            else
                storeName = ConfigurationManager.AppSettings["SiteNameForUK"];


            endPoint = baseURL + "v2/"+storeName+"/users/" + username + "/paymentdetails";
            string contentType = "text/json";
            RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.GET, contentType, "");
            parameters = "";
            response = restObj.MakeRequest(parameters);
            PaymentList root = JsonConvert.DeserializeObject<PaymentList>(response);
            return root;
        }

        /// <summary>
        /// This method calls the Create Cart API(POST) to create a new cart for logged in user.
        /// </summary>
        /// <param name="username">username represents the logged in user.</param>
        /// <returns>It returns a list of carts as an object of CreateCartResult class.</returns>
        public static CreateCartResult CreateCart(string username, string store)
        {
            ValidateSSLCertificate();

            if (environmentUsed.Equals("Test"))
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsTst"];
            else
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsProd"];

            if (store.Equals("US"))
                storeName = ConfigurationManager.AppSettings["SiteNameForUS"];
            else
                storeName = ConfigurationManager.AppSettings["SiteNameForUK"];

            endPoint = baseURL + "v2/" + storeName + "/users/" + username + "/carts";
            string contentType = "text/json";
            RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.POST, contentType, "");
            parameters = "";
            response = restObj.MakeRequest(parameters);
            CreateCartResult root = JsonConvert.DeserializeObject<CreateCartResult>(response);
            return root;
        }

        //Post Add to cart
        /// <summary>
        /// This method calls the Add to Cart API(POST) to add items to the cart for logged in user.
        /// </summary>
        /// <param name="username">username represents the logged in user.</param>
        /// <param name="cartNumber">cartNumber represents the cart number to which the items are to be added.</param>
        /// <param name="itemCode">itemCode represents the item to be added.</param>
        /// <returns>It returns a list of carts as an object of AddToCartResponse class</returns>
        public static AddToCartResponse AddToCart(string username, string cartNumber, string itemCode, string store)
        {
            ValidateSSLCertificate();

            if (environmentUsed.Equals("Test"))
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsTst"];
            else
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsProd"];

            if (store.Equals("US"))
                storeName = ConfigurationManager.AppSettings["SiteNameForUS"];
            else
                storeName = ConfigurationManager.AppSettings["SiteNameForUK"];

            endPoint = baseURL + "v2/" + storeName + "/users/" + username + "/carts/" + cartNumber + "/entries";
            string contentType = "application/x-www-form-urlencoded";
            RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.POST, contentType, "code="+itemCode);
            parameters = "";
            response = restObj.MakeRequest(parameters);
            AddToCartResponse root = JsonConvert.DeserializeObject<AddToCartResponse>(response);
            return root;
        }

        /// <summary>
        /// This method calls the Create Address API(POST) to create new addresses for logged in user.
        /// </summary>
        /// <param name="username">username represents the logged in user.</param>
        /// <param name="newAddress">newAddress represents the address to be added.</param>
        /// <returns>It returns the address as an object of CreateAddressResponse class.</returns>
        public static CreateAddressResponse CreateAddress(string username,string newAddress,string store)
        {
            ValidateSSLCertificate();

            if (environmentUsed.Equals("Test"))
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsTst"];
            else
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsProd"];

            if (store.Equals("US"))
                storeName = ConfigurationManager.AppSettings["SiteNameForUS"];
            else
                storeName = ConfigurationManager.AppSettings["SiteNameForUK"];

            endPoint = baseURL + "v2/"+storeName+"/users/" + username + "/addresses";

            string contentType = "application/x-www-form-urlencoded";
            RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.POST, contentType, newAddress);
            parameters = "";
            response = restObj.MakeRequest(parameters);
            CreateAddressResponse root = JsonConvert.DeserializeObject<CreateAddressResponse>(response);
            return root;
        }

        /// <summary>
        /// This method calls the Get Delivery Modes API to get the delivery modes for given user.
        /// </summary>
        /// <param name="username">username represents the logged in user.</param>
        /// <param name="cartNumber">cartNumber represents the cart number for which the delivery mode is to </param>
        /// <returns>It returns the list of delivery modes for given cart.</returns>
        public static DeliveryModeList GetDeliveryModes(string username, string cartNumber, string store)
        {
            ValidateSSLCertificate();

            if (environmentUsed.Equals("Test"))
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsTst"];
            else
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsProd"];

            if (store.Equals("US"))
                storeName = ConfigurationManager.AppSettings["SiteNameForUS"];
            else
                storeName = ConfigurationManager.AppSettings["SiteNameForUK"];

            endPoint = baseURL + "v2/"+storeName+"/users/" + username + "/carts/" + cartNumber + "/deliverymodes";
            string contentType = "text/json";
            RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.GET, contentType, "");
            parameters = "";
            response = restObj.MakeRequest(parameters);
            DeliveryModeList root = JsonConvert.DeserializeObject<DeliveryModeList>(response);
            return root;
        }

        /// <summary>
        /// This method calls the Get Search Categories API to get the list of products available.
        /// </summary>
        /// <param name="searchCriteria">searchCriteria represents the criteria for search</param>
        /// <returns>It returns the list of products.</returns>
        public static SearchCategories GetSearchCategories(string searchCriteria, string store)
        {
            ValidateSSLCertificate();

            if (environmentUsed.Equals("Test"))
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsTst"];
            else
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsProd"];

            if (store.Equals("US"))
                storeName = ConfigurationManager.AppSettings["SiteNameForUS"];
            else
                storeName = ConfigurationManager.AppSettings["SiteNameForUK"];

            if (searchCriteria.Equals(""))
                endPoint = baseURL + "v2/" + storeName + "/products/search?query=::category:Tuscany_Campaign&lang=en";
            else
                endPoint = baseURL + "v2/" + storeName + "/products/search?query=" + searchCriteria;

            string contentType = "text/json";
            RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.GET, contentType, "");
            parameters = "";
            response = restObj.MakeRequest(parameters);
            SearchCategories root = JsonConvert.DeserializeObject<SearchCategories>(response); 
            return root;
        }

        public static Product GetProduct(String productId, String store)
        {
            ValidateSSLCertificate();
            productId = productId.Replace('/', '_');

            if (environmentUsed.Equals("Test"))
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsTst"];
            else
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsProd"];

            if (store.Equals("US"))
                storeName = ConfigurationManager.AppSettings["SiteNameForUS"];
            else
                storeName = ConfigurationManager.AppSettings["SiteNameForUK"];


            endPoint = ConfigurationManager.AppSettings["BaseURLforRESTCalls"] + "v2/"+storeName+"/products/" + productId;
            string contentType = "text/json";
            RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.GET, contentType, "");
            parameters = "";
            response = restObj.MakeRequest(parameters);
            Product root = JsonConvert.DeserializeObject<Product>(response);
            return root;
        }

        public static void GetDeliveryAddress(string username, string cartNumber){
            throw new Exception("Method not implemented");
        //    endPoint = ConfigurationManager.AppSettings["BaseURLforRESTCalls"] + "v2/US_Tuscany/users/" + username + "/carts/"+cartNumber+"addresses";
        //    string contentType = "text/json";
        //    RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.GET, contentType, "");
        //    parameters = "";
        //    response = restObj.MakeRequest(parameters);
        //    CartList root = JsonConvert.DeserializeObject<CartList>(response);
        //    return root;
        }

        public static void GetAddressesVerify(string username)
        {
            throw new Exception("Method not implemented");
        //    endPoint = ConfigurationManager.AppSettings["BaseURLforRESTCalls"] + "v2/US_Tuscany/users/" + username + "/addresses/verification";
        //    string contentType = "text/json";
        //    RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.GET, contentType, "");
        //    parameters = "";
        //    response = restObj.MakeRequest(parameters);
        //    CartList root = JsonConvert.DeserializeObject<CartList>(response);
        //    return root;
        }

        /// <summary>
        /// This method calls the Submit Order API to post the order details for given user.
        /// </summary>
        /// <param name="username">username represents the logged in user.</param>
        /// <param name="cartNumber">cartNumber represents the cart number for which the order is to be placed.</param>
        /// <returns>It returns the submitted order for given cart.</returns>
        public static SubmitOrderResponse SubmitOrder(string username, string cartNumber,string store)
        {
            ValidateSSLCertificate();

            if (environmentUsed.Equals("Test"))
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsTst"];
            else
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsProd"];

            if (store.Equals("US"))
                storeName = ConfigurationManager.AppSettings["SiteNameForUS"];
            else
                storeName = ConfigurationManager.AppSettings["SiteNameForUK"];

            endPoint = baseURL + "v2/"+storeName+"/users/" + username + "/orders";

            string contentType = "application/x-www-form-urlencoded";
            RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.POST, contentType, "cartId=" + cartNumber);
                       response = restObj.MakeRequest(parameters);
            SubmitOrderResponse root = JsonConvert.DeserializeObject<SubmitOrderResponse>(response);
            return root;
        }

        /// <summary>
        /// This method calls the Post Payment API to post the payment details for given user.
        /// </summary>
        /// <param name="username">username represents the logged in user.</param>
        /// <param name="orderId">orderId represents the orderId for which payment is to be done.</param>
        /// <param name="addressId">addressId represents the addressId for payment.</param>
        /// <returns>It returns the payment in PaymentResponse class.</returns>
        public static PaymentResponse PostPayment(string username, string orderId, string addressId, string store)
        {
            ValidateSSLCertificate();

            if (environmentUsed.Equals("Test"))
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsTst"];
            else
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsProd"];

            if (store.Equals("US"))
                storeName = ConfigurationManager.AppSettings["SiteNameForUS"];
            else
                storeName = ConfigurationManager.AppSettings["SiteNameForUK"];

            endPoint = baseURL + "v2/"+storeName+"/users/" + username + "/orders/" + orderId + "/pay";
            string contentType = "application/x-www-form-urlencoded";
            RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.POST, contentType, "addressId="+addressId);
            parameters = "";
            response = restObj.MakeRequest(parameters);
            PaymentResponse root = JsonConvert.DeserializeObject<PaymentResponse>(response);
            return root;
        }

        /// <summary>
        /// This method calls the Delete Cart API to delete the cart for given user.
        /// </summary>
        /// <param name="username">username represents the logged in user.</param>
        /// <param name="cartNumber">cartNumber represents the cart number for which the order is to be placed.</param>
        public static void DeleteCart(string username, string cartNumber, string store)
        {
            ValidateSSLCertificate();

            if (environmentUsed.Equals("Test"))
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsTst"];
            else
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsProd"];

            if (store.Equals("US"))
                storeName = ConfigurationManager.AppSettings["SiteNameForUS"];
            else
                storeName = ConfigurationManager.AppSettings["SiteNameForUK"];

            endPoint = baseURL + "v2/"+storeName+"/users/" + username + "/carts/"+cartNumber;
            string contentType = "text/json";
            RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.GET, contentType, "");
            parameters = "";
            response = restObj.MakeRequest(parameters);
        }

        /// <summary>
        /// This method calls the Delete Address API to delete the address for given user.
        /// </summary>
        /// <param name="username">username represents the logged in user.</param>
        /// <param name="addressId">addressId represents the addressId.</param>
        public static void DeleteAddress(string username, string addressId, string store)
        {
            ValidateSSLCertificate();

            if (environmentUsed.Equals("Test"))
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsTst"];
            else
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsProd"];

            if (store.Equals("US"))
                storeName = ConfigurationManager.AppSettings["SiteNameForUS"];
            else
                storeName = ConfigurationManager.AppSettings["SiteNameForUK"];

            endPoint = baseURL + "v2/" + storeName + "/users/" + username + "/addresses/" + addressId;
            string contentType = "text/json";
            RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.GET, contentType, "");
            parameters = "";
            response = restObj.MakeRequest(parameters);
        }

        /// <summary>
        /// This method calls the Set Delivery Address API to set the address for given user.
        /// </summary>
        /// <param name="username">username represents the logged in user.</param>
        /// <param name="cartNumber">cartNumber represents the cart number for which the order is to be placed.</param>
        /// <param name="addressId">addressId represents the addressId.</param>
        public static void SetDeliveryAddress(string username, string cartNumber,string addressId, string store)
        {
            ValidateSSLCertificate();

            if (environmentUsed.Equals("Test"))
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsTst"];
            else
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsProd"];

            if (store.Equals("US"))
                storeName = ConfigurationManager.AppSettings["SiteNameForUS"];
            else
                storeName = ConfigurationManager.AppSettings["SiteNameForUK"];

            endPoint = baseURL + "v2/"+storeName+"/users/" + username + "/carts/" + cartNumber + "/addresses/delivery";
            string contentType = "application/x-www-form-urlencoded";
            RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.PUT, contentType, "addressId=" + addressId);
            parameters = "";
            response = restObj.MakeRequest(parameters);
        }

        /// <summary>
        /// This method calls the Set Delivery Address API to set the delivery mode for given user.
        /// </summary>
        /// <param name="username">username represents the logged in user.</param>
        /// <param name="cartNumber">cartNumber represents the cart number for which the order is to be placed.</param>
        /// <param name="deliveryModeId">deliveryModeId represents the delivery mode.</param>
        public static void SetDeliveryMode(string username, string cartNumber, string deliveryModeId, string store)
        {
            ValidateSSLCertificate();

            if (environmentUsed.Equals("Test"))
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsTst"];
            else
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsProd"];

            if (store.Equals("US"))
                storeName = ConfigurationManager.AppSettings["SiteNameForUS"];
            else
                storeName = ConfigurationManager.AppSettings["SiteNameForUK"];

            endPoint = baseURL + "v2/"+storeName+"/users/" + username + "/carts/" + cartNumber + "/deliverymode";
            string contentType = "application/x-www-form-urlencoded";
            RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.PUT, contentType, "deliveryModeId=" + deliveryModeId);
            parameters = "";
            response = restObj.MakeRequest(parameters);
        }

        /// <summary>
        /// This method calls the Set Payment Details API to set the payment details for given user.
        /// </summary>
        /// <param name="username">username represents the logged in user.</param>
        /// <param name="cartNumber">cartNumber represents the cart number for which the order is to be placed.</param>
        /// <param name="paymentId">paymentId represents the payment Id.</param>
        public static void SetPaymentDetails(string username, string cartNumber, string paymentId, string store)
        {
            ValidateSSLCertificate();

            if (environmentUsed.Equals("Test"))
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsTst"];
            else
                baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsProd"];

            if (store.Equals("US"))
                storeName = ConfigurationManager.AppSettings["SiteNameForUS"];
            else
                storeName = ConfigurationManager.AppSettings["SiteNameForUK"];

            endPoint = baseURL+ "v2/"+storeName+"/users/" + username + "/carts/" + cartNumber + "/paymentdetails";
            string contentType = "application/x-www-form-urlencoded";
            RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.PUT, contentType, "paymentDetailsId=" + paymentId);
            parameters = "";
            response = restObj.MakeRequest(parameters);
        }

        /// <summary>
        /// Get a janrain token
        /// </summary>
        /// <param name="username">username represents the logged in user.</param>
        /// <param name="cartNumber">cartNumber represents the cart number for which the order is to be placed.</param>
        /// <param name="paymentId">paymentId represents the payment Id.</param>
        public static void GetJanRainToken(string uuid, string cartNumber, string paymentId, string store)
        {

            //https://philips.eval.janraincapture.com
            //access/getAuthorizationCode?redirect_uri=dddd&uuid={{uuid}}&client_id=ptz7d2rvc3a3drzba9am2859acb8euq7&client_secret=7av7v25kym4f7jg9r6wumshrmu7pvk7q&type_name=user
            ValidateSSLCertificate();

            if (environmentUsed.Equals("Test"))
                baseURL = ConfigurationManager.AppSettings["BaseURLforJanrain"];
            else
                baseURL = ConfigurationManager.AppSettings["BaseURLforJanrain"];

            endPoint = baseURL + "access/getAuthorizationCode?redirect_uri=dddd&uuid={{"+ uuid +"}}&client_id=ptz7d2rvc3a3drzba9am2859acb8euq7&client_secret=7av7v25kym4f7jg9r6wumshrmu7pvk7q&type_name=user";
            string contentType = "application/x-www-form-urlencoded";
            RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.PUT, contentType, "paymentDetailsId=" + paymentId);
            parameters = "";
            response = restObj.MakeRequest(parameters);
        }

        public static void ValidateSSLCertificate()
        {
            ServicePointManager.ServerCertificateValidationCallback = new RemoteCertificateValidationCallback
           (
               delegate { return true; }
           );
        }


        public static ProductPRX GetProductDescriptionFromPRX(string ProductID)
        {
            //http://www.philips.com/prx/product/B2C/en_US/CONSUMER/products/HX8331/11.summary

            endPoint = "http://www.philips.com/prx/product/B2C/en_US/CONSUMER/products/"+ProductID+".summary";
            string contentType = "text/json";
            RestClientExtended restObj = new RestClientExtended(endPoint, HttpVerb.GET, contentType, "");
            parameters = "";
            response = restObj.MakeRequest(parameters);
            ProductPRX root = JsonConvert.DeserializeObject<ProductPRX>(response);
            return root;
        }
    }
}
