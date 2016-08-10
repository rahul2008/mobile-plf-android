using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using System.Xml;

namespace Philips.SIG.Automation.Mobile.CDP.Repository
{
    public class ObjectRepository
    {
        static XmlDocument doc = new XmlDocument();

        public ObjectRepository()
        {

        }
        static string _appiumSrv = null;
        public static string AppiumServer
        {
            get
            {
                return GetData("AppiumSrv");
            }
            set
            {
                _appiumSrv = value;
            }
        }


        static string _platformName = null;
        public static string platformName
        {
            get
            {
                return GetData("AppiumSrv");
            }
            set
            {
                _platformName = value;
            }
        }

        public static string Busy
        {
            get
            {
                return GetData("PleaseWait");
            }
        }

        public static string ProductDetailsArrow
        {
            get
            {
                return GetData("productDetailsarrow");
            }
        }

        public static string CartItem
        {
            get
            {
                return GetData("CartItem");
            }
        }

        public static string LoginTitle
        {
            get
            {
                return GetData("LoginTitle");
            }
        }
        public static string CreatePhilipsAccount
        {
            get
            {
                return GetData("CreatePhilipsAccount");
            }
        }
        public static string LoginContinue_1
        {
            get
            {
                return GetData("LoginContinue_1");
            }
        }
        public static string PhilipsAccount
        {
            get
            {
                return GetData("PhilipsAccount");
            }
        }
        public static string GooglePlus
        {
            get
            {
                return GetData("GooglePlus");
            }
        }
        public static string Facebook
        {
            get
            {
                return GetData("Facebook");
            }
        }
        public static string PhilipsAccountTitle
        {
            get
            {
                return GetData("PhilipsAccountTitle");
            }
        }
        public static string UserName
        {
            get
            {
                return GetData("UserName");
            }
        }
        public static string PassWord
        {
            get
            {
                return GetData("PassWord");
            }
        }
        public static string LoginButton
        {
            get
            {
                return GetData("LoginButton");
            }
        }
        public static string ForgotPassWord
        {
            get
            {
                return GetData("ForgotPassWord");
            }
        }
        public static string LoginBackButton
        {
            get
            {
                return GetData("LoginBackButton");
            }
        }
        public static string TermsCheckBox
        {
            get
            {
                return GetData("TermsCheckBox");
            }
        }
        public static string LoginContinue
        {
            get
            {
                return GetData("LoginContinue");
            }
        }
        public static string LogOut
        {
            get
            {
                return GetData("LogOut");
            }
        }
        public static string ALWAYS_SEND
        {
            get
            {
                return GetData("ALWAYS_SEND");
            }
        }
        public static string DISMISS
        {
            get
            {
                return GetData("DISMISS");
            }
        }
        public static string SEND
        {
            get
            {
                return GetData("SEND");
            }
        }
        public static string Crash_Title
        {
            get
            {
                return GetData("Crash_Title");
            }
        }
        static string _productList = null;
        public static string ProductList
        {
            get
            {
                return GetData("ProductList");
            }
            set
            {
                _productList = value;
            }
        }

        static string _TextView = null;
        public static string TextView
        {
            get
            {
                return GetData("TextView");
            }
            set
            {
                _TextView = value;
            }
        }
        static string _cartnumber = null;
        public static string CartNumber
        {
            get
            {
                return GetData("CartNumber");
            }
            set
            {
                _cartnumber = value;
            }
        }
        static string _imageCart = null;
        public static string ImageCart
        {
            get
            {
                return GetData("ImageCart");
            }
            set
            {
                _imageCart = value;
            }
        }
        static string _Shop_Now = null;
        public static string Shop_Now
        {
            get
            {
                return GetData("Shop_Now");
            }
        }
        static string _Country_Spinner = null;
        public static string Country_Spinner
        {
            get
            {
                return GetData("Country_Spinner");
            }
        }

        static string _Country = null;
        public static string Country
        {
            get
            {
                return GetData("Country");
            }
        }
        static string _productScreen_title = null;
        public static string productScreen_title
        {
            get
            {
                return GetData("productScreen_title");
            }
        }
        static string _productScreen_BackButton = null;
        public static string productScreen_BackButton
        {
            get
            {
                return GetData("productScreen_BackButton");
            }
        }
        static string _productScreen_ImageCart = null;
        public static string productScreen_ImageCart
        {
            get
            {
                return GetData("productScreen_ImageCart");
            }
        }
        static string _productScreen_InfoButton = null;
        public static string productScreen_InfoButton
        {
            get
            {
                return GetData("productScreen_InfoButton");
            }
        }
        static string _productScreen_ProductId = null;
        public static string productScreen_ProductId
        {
            get
            {
                return GetData("productScreen_ProductId");
            }
        }
        static string _productScreen_ProductList = null;
        public static string productScreen_ProductList
        {
            get
            {
                return GetData("productScreen_ProductList");
            }
        }
        static string _buyNow = null;
        public static string BuyNow
        {
            get
            {
                return GetData("BuyNow");
            }
            set
            {
                _buyNow = value;
            }
        }
        static string _BuyFromRetailer = null;
        public static string BuyFromRetailer
        {
            get
            {
                return GetData("BuyFromRetailer");
            }
        }
        static string _RetailerProductName = null;
        public static string RetailerProductName
        {
            get
            {
                return GetData("RetailerProductName");
            }
        }
        static string _RetailerList = null;
        public static string RetailerList
        {
            get
            {
                return GetData("RetailerList");
            }
        }
        static string _demotitle = null;
        public static string DemoTitle
        {
            get
            {
                return GetData("DemoTitle");
            }
            set
            {
                _demotitle = value;
            }
        }

        static string _addToCart = null;
        public static string AddToCart
        {
            get
            {
                return GetData("AddToCart");
            }
            set
            {
                _addToCart = value;
            }
        }

        static string _DiscountPrice = null;
        public static string DiscountPrice
        {
            get
            {
                return GetData("DiscountPrice");
            }
        }
        static string _upButton = null;
        public static string UpButton
        {
            get
            {
                return GetData("UpButton");
            }
            set
            {
                _upButton = value;
            }
        }

        public static string ScreenTitle
        {
            get
            {
                return GetData("ScreenTitle");
            }

        }

        static string _tbd = null;
        public static string UPSParcel
        {
            get
            {
                return GetData("UPSParcel");
            }
            set
            {
                _tbd = value;
            }
        }
        static string _shortproductdesc = null;
        public static string ShortProductDesc
        {
            get
            {
                return GetData("ShortProductDesc");
            }
            set
            {
                _shortproductdesc = value;
            }
        }
        static string _quantity = null;
        public static string Quantity
        {
            get
            {
                return GetData("Quantity");
            }
            set
            {
                _quantity = value;
            }
        }
        static string _dots = null;
        public static string Dots
        {
            get
            {
                return GetData("Dots");
            }
            set
            {
                _dots = value;
            }
        }
        static string _voucher = null;
        public static string Voucher
        {
            get
            {
                return GetData("Voucher");
            }
            set
            {
                _voucher = value;
            }
        }

        public static string Price
        {
            get
            {
                return GetData("Price");
            }

        }
        public static string TotalItems
        {
            get
            {
                return GetData("TotalItems");
            }

        }
        static string _checkout = null;
        public static string Checkout
        {
            get
            {
                return GetData("Checkout");
            }
            set
            {
                _checkout = value;
            }
        }
        static string _continue = null;
        public static string ContinueShopping
        {
            get
            {
                return GetData("ContinueShopping");
            }
            set
            {
                _continue = value;
            }
        }
        static string _imagetonguecare = null;
        public static string ImageTongueCare
        {
            get
            {
                return GetData("ImageTongueCare");
            }
            set
            {
                _imagetonguecare = value;
            }
        }
        static string _arrow = null;
        public static string Arrow
        {
            get
            {
                return GetData("Arrow");
            }
            set
            {
                _arrow = value;
            }
        }
        static string _ShoppingCartItem_AddtoCart = null;
        public static string ShoppingCartItem_AddtoCart
        {
            get
            {
                return GetData("ShoppingCartItem_AddtoCart");
            }
        }


        static string qtyListView = null;
        public static string QuantityDropDown
        {
            get
            {
                return GetData("QuantityDropDown");
            }
            set
            {
                qtyListView = value;
            }
        }
        static string qtydropdownMenuItems = null;
        public static string QuantitydropdownMenuItem
        {
            get
            {
                return GetData("QuantitydropdownMenuItem");
            }
            set
            {
                qtydropdownMenuItems = value;
            }
        }

        static string username = null;
        public static string HomeScreen_UserName
        {
            get
            {
                return GetData("username");
            }
            set
            {
                username = value;
            }

        }

        static string password = null;
        public static string HomeScreen_Password
        {
            get
            {
                return GetData("password");
            }
            set
            {
                password = value;
            }
        }



        static string submitbutton = null;
        public static string HomeScreen_Submit
        {
            get
            {
                return GetData("submitbutton");
            }
            set
            {
                submitbutton = value;
            }
        }

        public static string Register
        {
            get
            {
                return GetData("Register");
            }
        }
        static string NetworkError_okButton = null;
        public static string NetworkError_OK
        {
            get
            {
                return GetData("NetworkError_OK");
            }
            set
            {
                NetworkError_okButton = value;
            }
        }

        public static string ShoppingCartItemScroll
        {
            get
            {
                return GetData("ShoppingCartItemScroll");
            }
        }
        public static string VAT
        {
            get
            {
                return GetData("VAT");
            }

        }

        public static string TotalCost
        {
            get
            {
                return GetData("TotalCost");
            }

        }
        public static string Continues
        {
            get
            {
                return GetData("Continues");
            }
        }
        public static string RecyclerView
        {
            get
            {
                return GetData("RecyclerView");
            }
        }


        public static string ShoppingCartProductList
        {
            get
            {
                return GetData("ShoppingCartProductList");
            }

        }


        public static string ArrowButton
        {
            get
            {
                return GetData("ArrowButton");
            }

        }

        public static string CTN
        {
            get
            {
                return GetData("CTN");
            }

        }
        public static string ProductOverview
        {
            get
            {
                return GetData("ProductOverview");
            }

        }
        public static string IndividualPrice
        {
            get
            {
                return GetData("IndividualPrice");
            }

        }

        public static string View
        {
            get
            {
                return GetData("View");
            }

        }

        public static string ViewPager
        {
            get
            {
                return GetData("ViewPager");
            }

        }

        public static string ProductDescription
        {
            get
            {
                return GetData("ProductDescription");
            }

        }
        public static string CrossButton
        {
            get
            {
                return GetData("CrossButton");
            }

        }
        public static string RetailerInfo
        {
            get
            {
                return GetData("RetailerInfo");
            }

        }
        public static string RetailerScreenTitle
        {
            get
            {
                return GetData("RetailerScreenTitle");
            }

        }

        /// <summary>
        /// Shipping Address Screen
        /// </summary>
        /// <param name="element"></param>
        /// <returns></returns>

        public static string DeliverButton
        {
            get
            {
                return GetData("DeliverButton");
            }

        }
        public static string NewAddressButton
        {
            get
            {
                return GetData("NewAddressButton");
            }

        }
        public static string CancelButton
        {
            get
            {
                return GetData("CancelButton");
            }

        }
        public static string RadioButton
        {
            get
            {
                return GetData("RadioButton");
            }

        }
        public static string Address_Name
        {
            get
            {
                return GetData("tv_Name");
            }

        }
        public static string Address_Address
        {
            get
            {
                return GetData("tv_address");
            }

        }
        public static string Address_ContextMenu_Dots
        {
            get
            {
                return GetData("ContextMenu_Dots");
            }
        }
        public static string ShippingAddressesScroll
        {
            get
            {
                return GetData("ShippingAddressesScroll");
            }
        }
        /// <summary>
        /// Address Screen
        /// </summary>
        /// <param name="element"></param>
        /// <returns></returns>

        public static string FirstName
        {
            get
            {
                return GetData("FirstName");
            }
        }
        public static string LastName
        {
            get
            {
                return GetData("LastName");
            }
        }
        public static string AddressLine1
        {
            get
            {
                return GetData("AddressLine1");
            }
        }
        public static string AddressLine2
        {
            get
            {
                return GetData("AddressLine2");
            }
        }
        public static string Salutation
        {
            get
            {
                return GetData("Salutation");
            }
        }
        public static string City
        {
            get
            {
                return GetData("City");
            }
        }
        public static string State
        {
            get
            {
                return GetData("State");
            }
        }
        public static string PostalCode
        {
            get
            {
                return GetData("PostalCode");
            }
        }
        public static string Select_Country
        {
            get
            {
                return GetData("Select_Country");
            }
        }
        public static string Email
        {
            get
            {
                return GetData("Email");
            }
        }
        public static string Phone
        {
            get
            {
                return GetData("Phone");
            }
        }
        public static string Switch
        {
            get
            {
                return GetData("Switch");
            }
        }
        public static string Title
        {
            get
            {
                return GetData("Title");
            }
        }
        public static string ContinueButton
        {
            get
            {
                return GetData("ContinueButton");
            }
        }
        public static string ErrorText
        {
            get
            {
                return GetData("ErrorText");
            }
        }


        /// <summary>
        /// Summary Screen
        /// </summary>
        /// <param name="element"></param>
        /// <returns></returns>


        public static string PayNow
        {
            get
            {
                return GetData("PayNow");
            }
        }
        public static string OrderSummary
        {
            get
            {
                return GetData("OrderSummary");
            }
        }
        public static string Billing_Address
        {
            get
            {
                return GetData("Billing_Address");
            }
        }
        public static string Shipping_Address
        {
            get
            {
                return GetData("Shipping_Address");
            }
        }
        public static string Billing_Name
        {
            get
            {
                return GetData("Billing_Name");
            }
        }
        public static string Shipping_Name
        {
            get
            {
                return GetData("Shipping_Name");
            }
        }
        public static string TotalPrice
        {
            get
            {
                return GetData("TotalPrice");
            }
        }
        public static string DeliveryPrice
        {
            get
            {
                return GetData("DeliveryPrice");
            }
        }
        public static string TotalLabel
        {
            get
            {
                return GetData("TotalLabel");
            }
        }

        public static string ProductName
        {
            get
            {
                return GetData("ProductName");
            }
        }
        public static string SummaryQuantity
        {
            get
            {
                return GetData("SummaryQuantity");
            }
        }
        public static string SummaryScroll
        {
            get
            {
                return GetData("SummaryScroll");
            }
        }
        /// <summary>
        /// Payment Screen
        /// </summary>
        /// <param name="element"></param>
        /// <returns></returns>
        public static string CardNumber
        {
            get
            {
                return GetData("cardNumber");
            }
        }
        public static string CardHolderName
        {
            get
            {
                return GetData("cardholderName");
            }
        }
        public static string expiryMonth
        {
            get
            {
                return GetData("expiryMonth");
            }
        }
        public static string expiryYear
        {
            get
            {
                return GetData("expiryYear");
            }
        }
        public static string securityCode
        {
            get
            {
                return GetData("securityCode");
            }
        }
        public static string MakePayment
        {
            get
            {
                return GetData("MakePayment");
            }
        }
        public static string CancelPayment
        {
            get
            {
                return GetData("CancelPayment");
            }
        }
        public static string WebViewByClassName
        {
            get
            {
                return GetData("WebViewByClassName");
            }
        }

        public static string MonthYearListView
        {
            get
            {
                return GetData("MonthYearListView");
            }
        }

        public static string ScrollView
        {
            get
            {
                return GetData("ScrollView");
            }
        }
        public static string RecyclerPaymentMethod
        {
            get
            {
                return GetData("RecyclerPaymentMethod");
            }
        }
        public static string PaymentHeader
        {
            get
            {
                return GetData("PaymentHeader");
            }
        }
        public static string RadioButtonPayment
        {
            get
            {
                return GetData("RadioButtonPayment");
            }
        }
        public static string CardName
        {
            get
            {
                return GetData("CardName");
            }
        }
        public static string cardholdername
        {
            get
            {
                return GetData("cardholdername");
            }
        }
        public static string CardValidity
        {
            get
            {
                return GetData("CardValidity");
            }
        }
        public static string UseThisPaymentMethod
        {
            get
            {
                return GetData("UseThisPaymentMethod");
            }
        }
        public static string AddNewPayment
        {
            get
            {
                return GetData("AddNewPayment");
            }
        }
        public static string PaymentScroll
        {
            get
            {
                return GetData("PaymentScroll");
            }
        }

        /// <summary>
        /// Confirmation Screen
        /// </summary>
        /// <param name="element"></param>
        /// <returns></returns>
        public static string Thankyou_TV
        {
            get
            {
                return GetData("Thankyou_TV");
            }
        }
        public static string YourOrderNumber
        {
            get
            {
                return GetData("YourOrderNumber");
            }
        }
        public static string OrderNumber
        {
            get
            {
                return GetData("OrderNumber");
            }
        }
        public static string ConfirmEmail
        {
            get
            {
                return GetData("ConfirmEmail");
            }
        }
        public static string OKButton
        {
            get
            {
                return GetData("OKButton");
            }
        }



        /// <summary>
        /// Account Settings
        /// </summary>
        /// <param name="element"></param>
        /// <returns></returns>
        public static string rlxCheckBox
        {
            get
            {
                return GetData("rlxCheckBox");
            }
        }

        public static string PhilipsAnnouncementLink
        {
            get
            {
                return GetData("PhilipsAnnouncementLink");
            }
        }

        public static string BackButton
        {
            get
            {
                return GetData("BackButton");
            }
        }


        public static string GetData(string element)
        {
            string retVal = string.Empty;
            if (doc.BaseURI == "")
            {
                string filepath = Path.Combine(Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location), "Repository.xml");
                doc.Load(filepath);
            }
            XmlNode n1 = doc.GetElementsByTagName(element)[0];
            if (n1 != null)
                retVal = n1.InnerText;
            return retVal;

        }

        public static void LoadData(string element, string txtVal)
        {
            string retVal = string.Empty;
            if (doc == null)
                doc.Load("Repository.xml");
            XmlNode n1 = doc.GetElementById(element);
            if (n1 != null)
                n1.InnerText = txtVal;
            doc.Save("Repository.xml");
        }



    }

    public class Repository
    {
        static XmlDocument doc = new XmlDocument();
        static string GetData(string element)
        {
            string retVal = string.Empty;
            if (doc.BaseURI == "")
            {
                string filepath = Path.Combine(Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location), "Repository_iOS.xml");
                doc.Load(filepath);
            }
            XmlNode n1 = doc.GetElementsByTagName(element)[0];
            if (n1 != null)
                retVal = n1.InnerText;
            return retVal;

        }
        public static class iOS
        {
            public static class HomeScreen
            {
                public static string LogOut
                {
                    get
                    {
                        return GetData("Logout");
                    }

                }

                public static string ImageCart
                {
                    get
                    {
                        return GetData("ImageCart");
                    }

                }

                public static string ProductList
                {
                    get
                    {
                        return GetData("ProductList");
                    }

                }

                public static string DemoTitle
                {
                    get
                    {
                        return GetData("DemoTitle");
                    }

                }

                public static string ShopNow
                {
                    get
                    {
                        return GetData("ShopNow");
                    }

                }
                public static string SelectStore
                {
                    get
                    {
                        return GetData("SelectStore");
                    }

                }
                public static string Register
                {
                    get
                    {
                        return GetData("Register");
                    }

                }
            }
            public static class LoginScreen
            {
                public static string LoginPhilips
                {
                    get
                    {
                        return GetData("LoginPhilips");
                    }
                }

                public static string LoginFacebook
                {
                    get
                    {
                        return GetData("LoginFacebook");
                    }
                }

                public static string LoginGoogleplus
                {
                    get
                    {
                        return GetData("LoginGoogleplus");
                    }
                }

                public static string CreatePhilipsAcc
                {
                    get
                    {
                        return GetData("CreatePhilipsAcc");
                    }
                }

                public static string LoginSubTitle
                {
                    get
                    {
                        return GetData("LoginSubTitle");
                    }
                }

                public static string Username
                {
                    get
                    {
                        return GetData("Username");
                    }
                }

                public static string Password
                {
                    get
                    {
                        return GetData("Password");
                    }
                }

                public static string ShowPassword
                {
                    get
                    {
                        return GetData("ShowPassword");
                    }
                }

                public static string Signin
                {
                    get
                    {
                        return GetData("Signin");
                    }
                }

                public static string ForgotPassword
                {
                    get
                    {
                        return GetData("ForgotPassword");
                    }
                }

                public static string AcceptTermsToggle
                {
                    get
                    {
                        return GetData("AcceptTermsToggle");
                    }
                }

                public static string Continue
                {
                    get
                    {
                        return GetData("Continue");
                    }
                }

                public static string SignOut
                {
                    get
                    {
                        return GetData("SignOut");
                    }
                }

                public static string LoginTitle
                {
                    get
                    {
                        return GetData("LoginTitle");
                    }
                }

                public static string LoginBack
                {
                    get
                    {
                        return GetData("LoginBack");
                    }
                }

                public static string CreateAccBack
                {
                    get
                    {
                        return GetData("CreateAccBack");
                    }
                }

                public static string LoginMainBack
                {
                    get
                    {
                        return GetData("LoginMainBack");
                    }
                }

                public static string SignInBack
                {
                    get
                    {
                        return GetData("SignInBack");
                    }
                }

                public static string ForgotPswdEmailField
                {
                    get
                    {
                        return GetData("ForgotPswdEmailField");
                    }
                }

                public static string ForgotPswdContinueButton
                {
                    get
                    {
                        return GetData("ForgotPswdContinueButton");
                    }
                }

                public static string ForgotPswdDoneButton
                {
                    get
                    {
                        return GetData("ForgotPswdDoneButton");
                    }
                }

                public static string ForgotPswdResetAlert
                {
                    get
                    {
                        return GetData("ForgotPswdResetAlert");
                    }
                }
            }
            public static class ShoppingCart
            {
                public static string Back
                {
                    get
                    {
                        return GetData("UpButton");
                    }

                }
                public static string ScreenTitle
                {
                    get
                    {
                        return GetData("ScreenTitle");
                    }

                }

                public static string BuyFromRetailer
                {
                    get
                    {
                        return GetData("BuyFromRetailer");
                    }

                }
                public static string AddToCart
                {
                    get
                    {
                        return GetData("AddToCart");
                    }

                }
                public static string CTN
                {
                    get
                    {
                        return GetData("CTN");
                    }

                }
                public static string ProductOverview
                {
                    get
                    {
                        return GetData("ProductOverview");
                    }

                }
                public static string NewPrice
                {
                    get
                    {
                        return GetData("NewPrice");
                    }

                }
                public static string OriginalPrice
                {
                    get
                    {
                        return GetData("OriginalPrice");
                    }

                }
                public static string ProductDescription
                {
                    get
                    {
                        return GetData("ProductDescription");
                    }

                }
                public static string image
                {
                    get
                    {
                        return GetData("image");
                    }

                }
                public static string View
                {
                    get
                    {
                        return GetData("View");
                    }

                }

                public static string Checkout
                {
                    get
                    {
                        return GetData("Checkout");
                    }

                }

                public static string ContinueShopping
                {
                    get
                    {
                        return GetData("ContinueShopping");
                    }

                }

                public static string DeleteOkButton
                {
                    get
                    {
                        return GetData("DeleteOkButton");
                    }

                }
                public static string DeleteCancelButton
                {
                    get
                    {
                        return GetData("DeleteCancelButton");
                    }

                }
                public static string Delete
                {
                    get
                    {
                        return GetData("Delete");
                    }

                }
                public static string DeleteCartItem
                {
                    get
                    {
                        return GetData("DeleteCartItem");
                    }

                }

            }
            public static class ShippingAddressScreen
            {
                //Shipping Address main screen
                public static string ShippingAddress
                {
                    get
                    {
                        return GetData("ShippingAddressesList");
                    }

                }
                public static string CancelButton
                {
                    get
                    {
                        return GetData("CancelButton");
                    }

                }
                public static string ShippingAddrTitle
                {
                    get
                    {
                        return GetData("ShippingAddrTitle");
                    }

                }
            }
            public static class AddressScreen
            {
                //Edit Shipping Address screen
                public static string FirstName
                {
                    get
                    {
                        return GetData("FirstName");
                    }

                }
                public static string LastName
                {
                    get
                    {
                        return GetData("LastName");
                    }

                }
                public static string AddressLine1
                {
                    get
                    {
                        return GetData("AddressLine1");
                    }

                }
                public static string AddressLine2
                {
                    get
                    {
                        return GetData("AddressLine2");
                    }

                }
                public static string Salutation
                {
                    get
                    {
                        return GetData("Salutation");
                    }

                }
                public static string City
                {
                    get
                    {
                        return GetData("City");
                    }

                }
                public static string PostalCode
                {
                    get
                    {
                        return GetData("PostalCode");
                    }

                }
                public static string Country
                {
                    get
                    {
                        return GetData("Country");
                    }

                }
                public static string State
                {
                    get
                    {
                        return GetData("State");
                    }

                }
                public static string AddrEmail
                {
                    get
                    {
                        return GetData("AddrEmail");
                    }

                }
                public static string Phone
                {
                    get
                    {
                        return GetData("Phone");
                    }

                }
                public static string Save
                {
                    get
                    {
                        return GetData("Save");
                    }

                }
                public static string Cancel
                {
                    get
                    {
                        return GetData("Cancel");
                    }

                }
                public static string ScrollView
                {
                    get
                    {
                        return GetData("ScrollView");
                    }

                }
                public static string ErrorText
                {
                    get
                    {
                        return GetData("ErrorText");
                    }

                }
                public static string Title
                {
                    get
                    {
                        return GetData("Title");
                    }

                }
                public static string SalutationPopOver
                {
                    get
                    {
                        return GetData("SalutationPopOver");
                    }
                }
                public static string StatePopOver
                {
                    get
                    {
                        return GetData("StatePopOver");
                    }
                }
                public static string ContinueButton
                {
                    get
                    {
                        return GetData("ContinueButton");
                    }
                }
                public static string Switch
                {
                    get
                    {
                        return GetData("Switch");
                    }

                }
            }
            public static class ConfirmationScreen
            {
                public static string Thankyou_TV
                {
                    get
                    {
                        return GetData("Thankyou_TV");
                    }

                }
                public static string OrderNumber
                {
                    get
                    {
                        return GetData("OrderNumber");
                    }

                }
                public static string ConfirmEmail
                {
                    get
                    {
                        return GetData("ConfirmEmail");
                    }

                }
                public static string OKButton
                {
                    get
                    {
                        return GetData("OKButton");
                    }

                }
            }
            public static class PaymentScreen
            {
                public static string cardNumber
                {
                    get
                    {
                        return GetData("cardNumber");
                    }

                }
                public static string cardholderName
                {
                    get
                    {
                        return GetData("cardholderName");
                    }
                }
                public static string expiryMonth
                {
                    get
                    {
                        return GetData("expiryMonth");
                    }

                }
                public static string expiryYear
                {
                    get
                    {
                        return GetData("expiryYear");
                    }
                }
                public static string securityCode
                {
                    get
                    {
                        return GetData("securityCode");
                    }
                }
                public static string makePaymemt
                {
                    get
                    {
                        return GetData("MakePaymemt");
                    }
                }
                public static string cancelPayment
                {
                    get
                    {
                        return GetData("CancelPayment");
                    }
                }

                public static string okWarning
                {
                    get
                    {
                        return GetData("okWarning");
                    }
                }
                public static string cancelWarning
                {
                    get
                    {
                        return GetData("cancelWarning");
                    }
                }
            }
            public static class SummaryScreen
            {

                public static string SummaryScreenTitle
                {
                    get
                    {
                        return GetData("SummaryScreenTitle");
                    }

                }
                public static string SummaryCancel
                {
                    get
                    {
                        return GetData("SummaryCancel");
                    }

                }
                public static string PayNow
                {
                    get
                    {
                        return GetData("PayNow");
                    }

                }
            }

        }
    }

    public class AppFrameWork
    {
        static XmlDocument doc = new XmlDocument();
        static string GetData(string element)
        {
            string retVal = string.Empty;
            if (doc.BaseURI == "")
            {
                string filepath = Path.Combine(Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location), "Repository.xml");
                doc.Load(filepath);
            }
            XmlNode n1 = doc.GetElementsByTagName(element)[0];
            if (n1 != null)
                retVal = n1.InnerText;
            return retVal;

        }
        public static class Android
        {
            public static class HomeScreen
            {
                public static string HamburgerIcon
                {
                    get
                    {
                        return GetData("HamburgerIcon");
                    }

                }

                public static string AppFrameworkHomeScreen
                {
                    get
                    {
                        return GetData("AppFrameworkHomeScreen");
                    }

                }

                public static string HamburgerList
                {
                    get
                    {
                        return GetData("HamburgerList");
                    }
                }

                
                public static string LoginSettings
                {
                    get
                    {
                        return GetData("LoginSettings");
                    }
                }

                public static string VerticalSettings
                {
                    get
                    {
                        return GetData("VerticalSettings");
                    }
                }
                public static string WelcomeScreen
                {
                    get
                    {
                        return GetData("WelcomeScreen");
                    }
                }

                public static string RightArrow
                {
                    get
                    {
                        return GetData("RightArrow");
                    }
                }

                public static string LeftArrow
                {
                    get
                    {
                        return GetData("LeftArrow");
                    }
                }
               
                public static string Skip
                {
                    get
                    {
                        return GetData("Skip");
                    }
                }
                public static string Done
                {
                    get
                    {
                        return GetData("Done");
                    }
                }
                public static string UserName
                {
                    get
                    {
                        return GetData("AUsername");
                    }
                }

                public static string PassWord
                {
                    get
                    {
                        return GetData("APassWord");
                    }
                }

                public static string PhilipsAccountReg
                {
                    get
                    {
                        return GetData("AppPhilipsAccountReg");
                    }
                }

                public static string Settings
                {
                    get
                    {
                        return GetData("Settings");
                    }
                }
                public static string Alogout
                {
                    get
                    {
                        return GetData("Alogout");
                    }
                }

            }
        }

    }



}


