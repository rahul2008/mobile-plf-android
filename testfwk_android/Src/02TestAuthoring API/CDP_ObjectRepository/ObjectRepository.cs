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

        public static string NucleousValue
        {
            get
            {
                return GetData("NucleousValue");
            }
        }
        public static string MomentValue
        {
            get
            {
                return GetData("MomentValue");
            }
        }
        public static string DatacoreErrorMsg
        {
            get
            {
                return GetData("DatacoreErrorMsg");
            }
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
                public static string Terms
                {
                    get
                    {
                        return GetData("Terms");
                    }

                }
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
                public static string Connectivity
                {
                    get
                    {
                        return GetData("Connectivity");
                    }
                }
                public static string NucleousDevice
                {
                    get
                    {
                        return GetData("NucleousDevice");
                    }

                }
                public static string MomentValueFromDatacore
                {
                    get
                    {
                        return GetData("MomentValueFromDatacore");
                    }

                }
                public static string Alogout
                {
                    get
                    {
                        return GetData("Alogout");
                    }
                }
                public static string AlogoutConfirm
                {
                    get
                    {
                        return GetData("AlogoutConfirm");
                    }
                }
                public static string ALoginButton
                {
                    get
                    {
                        return GetData("ALoginButton");
                    }
                }
                public static string ALoginContinue
                {
                    get
                    {
                        return GetData("ALoginContinue");
                    }
                }
                public static string ALoginContinueConfirm
                {
                    get
                    {
                        return GetData("ALoginContinueConfirm");
                    }
                }
                public static string Support
                {
                    get
                    {
                        return GetData("Support");
                    }

                }
            }
        }

    }

    public class ConsumerCare
    {
        static XmlDocument doc = new XmlDocument();
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
        public class Android
        {
            public class HomeScreen
            {
                public static string LaunchasActivity
                {
                    get
                    {
                        return GetData("LaunchasActivity");
                    }

                }
                public static string LaunchasFragment
                {
                    get
                    {
                        return GetData("LaunchasFragment");
                    }
                }
                public static string AddCTN
                {
                    get
                    {
                        return GetData("AddCTN");
                    }

                }
                public static string Country_Spinner
                {
                    get
                    {
                        return GetData("CountrySpinner");
                    }
                }
                public static string Language
                {
                    get
                    {
                        return GetData("LanguageSpinner");
                    }
                }
                static string langaugeTitle = null;
                public static string LanguageTitle
                {
                    get
                    {
                        return GetData("LangaugeTitle");
                    }
                    set
                    {
                        langaugeTitle = value;
                    }
                }
                static string selectedCountry = null;
                public static string CountryTitle
                {
                    get
                    {
                        return GetData("CountryTitle");
                    }
                    set
                    {
                        selectedCountry = value;
                    }
                }

            }

            public class AddProduct
            {
                public static string Submit
                {
                    get
                    {
                        return GetData("Submit");
                    }
                }
                public static string AddCTN
                {
                    get
                    {
                        return GetData("CTN_Name");
                    }
                }
                public static string AddCategory
                {
                    get
                    {
                        return GetData("AddCategory");
                    }
                }
                public static string AddCatalog
                {
                    get
                    {
                        return GetData("AddCatalog");
                    }
                }
            }

            public class SupportScreen
            {
                public static string ViewProductInfo
                {
                    get
                    {
                        return GetData("ViewProductInfo");
                    }

                }
                public static string ReadFAQ
                {
                    get
                    {
                        return GetData("ReadFAQ");
                    }

                }
                public static string ContactUs
                {
                    get
                    {
                        return GetData("ContactUs");
                    }

                }
                public static string FindPhilips
                {
                    get
                    {
                        return GetData("FindPhilips");
                    }

                }
                public static string TellUs
                {
                    get
                    {
                        return GetData("TellUs");
                    }

                }
                public static string MyPhilipsAccount
                {
                    get
                    {
                        return GetData("MyPhilipsAccount");
                    }

                }
                public static string ChangeSelectedProduct
                {
                    get
                    {
                        return GetData("ChangeSelectedProduct");
                    }

                }
                public static string ReturnToHomeScreen
                {
                    get
                    {
                        return GetData("ReturnToHomeScreen");
                    }
                }
                static string demotitle = null;
                public static string DemoTitle
                {
                    get
                    {
                        return GetData("DemoTitle");
                    }
                    set
                    {
                        demotitle = value;
                    }
                }
            }

            public class ProductInformationScreen
            {
                public static string ProductManual
                {
                    get
                    {
                        return GetData("Product_Manual");
                    }
                }
                public static string ProductInformation
                {
                    get
                    {
                        return GetData("Product_Information");
                    }
                }
                public static string BacktoSupport
                {
                    get
                    {
                        return GetData("Back_to_Support");
                    }
                }
                static string produtdetail = null;
                public static string ProductDetail
                {
                    get
                    {
                        return GetData("ProductDetail");
                    }
                    set
                    {
                        produtdetail = value;
                    }

                }
                static string productInfoTitle = null;
                public static string ProductInfoTitle
                {
                    get
                    {
                        return GetData("ProductInfoTitle");
                    }
                    set
                    {
                        productInfoTitle = value;
                    }
                }
                public static string ProductImage
                {
                    get
                    {
                        return GetData("Image");
                    }
                }
                static string videoText = null;
                public static string VideoTitle
                {
                    get
                    {
                        return GetData("VideoHeading");
                    }
                    set
                    {
                        videoText = value;
                    }
                }
                public static string VideoPlayButton
                {
                    get
                    {
                        return GetData("VideoPlayButton");
                    }
                }
                public static string WebViewofProductInfo
                {
                    get
                    {
                        return GetData("WebViewofProductInfo");
                    }
                }
                public static string PopUpview
                {
                    get
                    {
                        return GetData("OK");
                    }

                }
                public static string ProductVariant
                {
                    get
                    {
                        return GetData("ProductVariant");
                    }
                }

            }

            public class FAQ
            {
                public static string BackToHomePage
                {
                    get
                    {
                        return GetData("Back_to_FAQHomePage");
                    }
                }
                static string FAQheadertxt = null;
                public static string FAQHeaderText
                {
                    get
                    {
                        return GetData("FAQHeaderText");
                    }
                    set
                    {
                        FAQheadertxt = value;
                    }
                }
                public static string BeforeyouBye
                {
                    get
                    {
                        return GetData("BeforeYouBye");
                    }
                }
                public static string BuyYourProduct
                {
                    get
                    {
                        return GetData("BuyYourProdut");
                    }
                }
                public static string GettingStarted
                {
                    get
                    {
                        return GetData("GettingStarted");
                    }
                }
                public static string UseandLearn
                {
                    get
                    {
                        return GetData("UseandLearn");
                    }
                }
                public static string Apps
                {
                    get
                    {
                        return GetData("Apps");
                    }
                }
                public static string TroubleShooting
                {
                    get
                    {
                        return GetData("Troubleshooting");
                    }
                }
                public static string CleaningandMaintenance
                {
                    get
                    {
                        return GetData("CleaningandMaintenance");
                    }
                }
                public static string ArethereanySideEffects
                {
                    get
                    {
                        return GetData("AreThereanySideeffects");
                    }
                }
                public static string HaveOrder
                {
                    get
                    {
                        return GetData("HaveOrder");
                    }
                }
                public static string Working
                {
                    get
                    {
                        return GetData("Working");
                    }
                }
                public static string LifeExpectancy
                {
                    get
                    {
                        return GetData("LifeExpectancy");
                    }
                }
                public static string DownloadInstall
                {
                    get
                    {
                        return GetData("Dowload_Install");
                    }
                }
                public static string FindApp
                {
                    get
                    {
                        return GetData("Find_App");
                    }
                }
                public static string Clean
                {
                    get
                    {
                        return GetData("Clean");
                    }
                }
            }

            public class QuestionandAnswer
            {
                public static string BackToFAQHomePage
                {
                    get
                    {
                        return GetData("BacktoFAQpage");
                    }
                }
                static string QAHeadertxt = null;
                public static string QAHeaderText
                {
                    get
                    {
                        return GetData("QAHeaderText");
                    }
                    set
                    {
                        QAHeaderText = value;
                    }
                }
            }

            public class ContactUs
            {
                public static string OnTwitter
                {
                    get
                    {
                        return GetData("OnTwitter");
                    }
                }
                public static string OnFacebook
                {
                    get
                    {
                        return GetData("OnFacebook");
                    }
                }
                public static string LiveChat
                {
                    get
                    {
                        return GetData("LiveChat");
                    }
                }
                public static string SendEmail
                {
                    get
                    {
                        return GetData("SendEmail");
                    }
                }
                public static string Call
                {
                    get
                    {
                        return GetData("Call");
                    }
                }
                //static string Leave_Msg = null;
                //public static string LeaveMessage
                //{
                //    get
                //    {
                //        return GetData("LeaveMessage");
                //    }
                //    set
                //    {
                //        Leave_Msg = value;
                //    }

                //}
                //static string contact = null;
                //public static string Contact
                //{
                //    get
                //    {
                //        return GetData("Contact");
                //    }
                //    set
                //    {
                //        contact = value;
                //    }
                //}
                static string headerTxt = null;
                public static string HeaderTxt
                {
                    get
                    {
                        return GetData("HeaderText");
                    }
                    set
                    {
                        headerTxt = value;
                    }
                }
                public static string BacktoSupport
                {
                    get
                    {
                        return GetData("BackToHomePage");
                    }
                }
                public static string OK
                {
                    get
                    {
                        return GetData("Ok");
                    }
                }
            }

            public class FacebookViewPage
            {
                public static string UserName
                {
                    get
                    {
                        return GetData("UserName");
                    }
                }
                public static string Password
                {
                    get
                    {
                        return GetData("Password");
                    }
                }
                public static string LoginButton
                {
                    get
                    {
                        return GetData("LogInButton");
                    }
                }
                public static string Join
                {
                    get
                    {
                        return GetData("Join");
                    }
                }

            }

            public class TwitterViewPage
            {
                public static string SignUp
                {
                    get
                    {
                        return GetData("SignUpButton");
                    }
                }
                public static string LogIn
                {
                    get
                    {
                        return GetData("LogInButton");
                    }
                }
            }

            public class ChatWithPhilips
            {
                static string headertxt = null;
                public static string HeaderText
                {
                    get
                    {
                        return GetData("HeaderText");
                    }
                    set
                    {
                        headertxt = value;
                    }
                }
                public static string BacktoContactUs
                {
                    get
                    {
                        return GetData("BacktoContactUs");
                    }
                }
                public static string ChatNow
                {
                    get
                    {
                        return GetData("ChatNow");
                    }
                }
                public static string Cancel
                {
                    get
                    {
                        return GetData("Cancel");
                    }
                }
            }

            public class ChatNow
            {
                public static string BacktoHome
                {
                    get
                    {
                        return GetData("BacktoHomeScreen");
                    }
                }
                static string headerTxt = null;
                public static string HeaderText
                {
                    get
                    {
                        return GetData("HeaderText");
                    }
                    set
                    {
                        headerTxt = value;
                    }
                }
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
                public static string Email
                {
                    get
                    {
                        return GetData("Email");
                    }
                }
                public static string MobileNo
                {
                    get
                    {
                        return GetData("MobileNo");
                    }
                }
                public static string CheckBox
                {
                    get
                    {
                        return GetData("CheckBox");
                    }
                }
                public static string StartChat
                {
                    get
                    {
                        return GetData("StartChat");
                    }
                }
                public static string Close
                {
                    get
                    {
                        return GetData("Close");
                    }
                }
                public static string ChatBusy_View
                {
                    get
                    {
                        return GetData("ChatBusy");
                    }
                }
                public static string Querytext
                {
                    get
                    {
                        return GetData("QueryTextBox");
                    }
                }
                public static string SendButton
                {
                    get
                    {
                        return GetData("Send");
                    }
                }
                public static string ChatFrame
                {
                    get
                    {
                        return GetData("ChatFrame");
                    }
                }
            }

            public class SendEmail
            {
                public static string BacktoHomeScrn
                {
                    get
                    {
                        return GetData("BacktohomeScrn");
                    }
                }
                static string header = null;
                public static string HeaderTxt
                {
                    get
                    {
                        return GetData("Header");
                    }
                    //set
                    //{
                    //    header = value;
                    //}
                }
            }

            public class FindPhilips
            {
                public static string BackToHome
                {
                    get
                    {
                        return GetData("BackToHome");
                    }
                }
                public static string Allow
                {
                    get
                    {
                        return GetData("Allow");
                    }
                }
                static string findHeaderTxt = null;
                public static string FindHeaderText
                {
                    get
                    {
                        return GetData("HeaderFindPhilips");
                    }
                    set
                    {
                        findHeaderTxt = null;
                    }
                }
                public static string Search
                {
                    get
                    {
                        return GetData("Search");
                    }
                }
                public static string searchButton
                {
                    get
                    {
                        return GetData("SearchButton");
                    }
                }
                public static string ErrorOk
                {
                    get
                    {
                        return GetData("ErrorOk");
                    }
                }

            }

            public class DialogsFindPhilips
            {
                static string txtDesc = null;
                public static string TextDesc
                {
                    get
                    {
                        return GetData("TextDesc");
                    }
                    set
                    {
                        txtDesc = value;
                    }
                }
                public static string GoToContactPge
                {
                    get
                    {
                        return GetData("GotoContactPage");
                    }
                }
                public static string TextOK
                {
                    get
                    {
                        return GetData("OKButton");
                    }
                }
            }
            public class DialogsFindPhilips1
            {
                static string textData = null;
                public static string ErrorData
                {
                    get
                    {
                        return GetData("NoDataAvailable");
                    }
                    set
                    {
                        textData = value;
                    }
                }
                //public static string ParentDialog
                //{
                //    get
                //    {
                //        return GetData("ParentLayout");
                //    }
                //}
                public static string DataOK
                {
                    get
                    {
                        return GetData("NoDataOK");
                    }
                }
            }

            public class DialogsFindPhilips2
            {
                static string textData = null;
                public static string GPSErrorData
                {
                    get
                    {
                        return GetData("GPSErrorMsg");
                    }
                    set
                    {
                        textData = value;
                    }
                }
                public static string GPS_OK
                {
                    get
                    {
                        return GetData("GPSOKButton");
                    }
                }
                public static string GPS_Cancel
                {
                    get
                    {
                        return GetData("GPSCancleButton");
                    }
                }
            }

            public class DialogsFindPhilips4
            {
                public static string ShareLocationToPhilipsOKButton
                {
                    get
                    {
                        return GetData("ShareLocationToPhilipsOKButton");
                    }
                }

                public static string NoServiceAvailableOKButton
                {
                    get
                    {
                        return GetData("NoServiceAvailableOKButton");
                    }
                }
            }

            public class GPSLocation
            {
                public static string SwitchButton
                {
                    get
                    {
                        return GetData("SwitchButton");
                    }
                }
                static string txtHeader = null;
                public static string HeaderTxt
                {
                    get
                    {
                        return GetData("Header");
                    }
                    set
                    {
                        txtHeader = value;
                    }
                }
            }

            public class TellUs
            {
                static string headertxt = null;
                public static string TellUsHeaderText
                {
                    get
                    {
                        return GetData("TellUsHeaderText");
                    }
                    set
                    {
                        headertxt = value;
                    }
                }
                public static string BackToHomescreen
                {
                    get
                    {
                        return GetData("BackTo_Homescreen");
                    }
                }
                public static string AppReview
                {
                    get
                    {
                        return GetData("AppReview");
                    }
                }
                public static string ProductReview
                {
                    get
                    {
                        return GetData("ProductReview");
                    }
                }
                public static string RetryButton
                {
                    get
                    {
                        return GetData("RetryButton");
                    }
                }
                public static string PlaySire_view
                {
                    get
                    {
                        return GetData("PlayStore_View");
                    }
                }

                public static string PlayStoreRetryButton
                {
                    get
                    {
                        return GetData("PlayStoreRetryButton");
                    }
                }
            }

            public class ProductScreen
            {
                public static string Back_To_home
                {
                    get
                    {
                        return GetData("Backto_Home");
                    }
                }
                static string productHeaderTxt = null;
                public static string ProductHeaderText
                {
                    get
                    {
                        return GetData("ProductHeadertxt");
                    }
                    set
                    {
                        productHeaderTxt = value;
                    }
                }
                public static string Product_Screen
                {
                    get
                    {
                        return GetData("Product_List_View");
                    }
                }
                public static string Product_Id
                {
                    get
                    {
                        return GetData("Product_Id");
                    }
                }
                public static string Selected_Produt
                {
                    get
                    {
                        return GetData("SelectedProduct");
                    }
                }
                public static string BlueTouch
                {
                    get
                    {
                        return GetData("BluetouchProduct");
                    }
                }
            }

            public class SelectedProductScreen
            {
                public static string Backto_home
                {
                    get
                    {
                        return GetData("BacktoHome");
                    }
                }
                public static string Select_Button
                {
                    get
                    {
                        return GetData("SelectProductButton");
                    }
                }
                static string txtHeader = null;
                public static string TextHeader
                {
                    get
                    {
                        return GetData("TextHeader");
                    }
                    set
                    {
                        txtHeader = value;
                    }
                }
                static string name = null;
                public static string Selected_Productname
                {
                    get
                    {
                        return GetData("ProductName");
                    }
                    set
                    {
                        name = value;
                    }

                }
                static string CTN = null;
                public static string Selected_ProductCTN
                {
                    get
                    {
                        return GetData("ProductCTN");
                    }
                    set
                    {
                        CTN = value;
                    }
                }
            }

            public class ConfirmationScreen
            {
                public static string Bact_To_Home
                {
                    get
                    {
                        return GetData("Backto_Home");
                    }
                }
                public static string Change
                {
                    get
                    {
                        return GetData("Changeutton");
                    }
                }
                public static string Continue
                {
                    get
                    {
                        return GetData("ContinueButton");
                    }
                }
                static string textheader = null;
                public static string TextHeader
                {
                    get
                    {
                        return GetData("TextHeader");
                    }
                    set
                    {
                        textheader = value;
                    }
                }
                static string productVariant = null;
                public static string ProductVrnt
                {
                    get
                    {
                        return GetData("ProductVrnt");
                    }
                }
            }

            public class SelectProduct
            {
                public static string BackTo_Home
                {
                    get
                    {
                        return GetData("Backto_Home");
                    }
                }
                public static string SeachProduct
                {
                    get
                    {
                        return GetData("Search");
                    }
                }
                static string txtHeader = null;
                public static string HeaderText
                {
                    get
                    {
                        return GetData("HeaderText");
                    }
                    set
                    {
                        txtHeader = value;
                    }
                }
            }
        }
    }

    public class ProductRegistration
    {
        static XmlDocument doc = new XmlDocument();
        static string GetData(string element)
        {
            string retVal = string.Empty;
            if (doc.BaseURI == "")
            {
                string filepath = Path.Combine(Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location), "Repository_ProductRegistration.xml");
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
                public static string ProductReg
                {
                    get
                    {
                        return GetData("ProductReg");
                    }

                }


                public static string PR_shopicon
                {
                    get
                    {
                        return GetData("PR_shopicon");
                    }

                }

                public static string PR_Hamburgericon
                {
                    get
                    {
                        return GetData("PR_Hamburgericon");
                    }

                }

                public static string PROK
                {
                    get
                    {
                        return GetData("PROK");
                    }

                }

                public static string PRCancel
                {
                    get
                    {
                        return GetData("PRCancel");
                    }

                }


                public static string PRSuccess
                {
                    get
                    {
                        return GetData("PRSuccess");
                    }

                }

                public static string AuroraCTN
                {
                    get
                    {
                        return GetData("AuroraCTN");
                    }

                }

                public static string PRContinue
                {
                    get
                    {
                        return GetData("PRContinue");
                    }

                }
                public static string PRBack
                {
                    get
                    {
                        return GetData("PRBack");
                    }

                }
                public static string RepeatPRText
                {
                    get
                    {
                        return GetData("RepeatPRText");
                    }

                }
                public static string PRheader
                {
                    get
                    {
                        return GetData("PRheader");
                    }

                }

                public static string DatePurchase
                {
                    get
                    {
                        return GetData("DatePurchase");
                    }

                }

                public static string PRRegister
                {
                    get
                    {
                        return GetData("PRRegister");
                    }

                }  
                
            }
        }

    }


}


