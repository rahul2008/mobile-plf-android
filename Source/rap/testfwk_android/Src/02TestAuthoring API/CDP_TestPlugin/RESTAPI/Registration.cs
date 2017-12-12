///-----------------------------------------------------------------
///   File:      Registration.cs
///   Description:    Classes contains the User registration variables 
///   Author:         Sangameshwara                    Date: 11-11-2016
///---using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin
{
    public class UserJanRain
    {
        public string preferredLanguage { get; set; }
        public string email { get; set; }
        public string uuid { get; set; }
        //public long id { get; set; }
    }

    public class UserData
    {
        public UserJanRain capture_user { get; set; }
    }


    public class Login
    {
        public string access_token { get; set; }
        public UserJanRain capture_user { get; set; }
    }

    public static class CTN
    {
        public static string CTN_Number = "HX6064/33";
    }

    public class RegisteredProduct
    {
        public string productModelNumber { get; set; }
        public string uuid { get; set; }
    }

    public class RegisteredProductsList
    {
        public List<RegisteredProduct> results { get; set; }
    }

    public class DeleteRegistrationResult
    {
        public bool success { get; set; }
    }
}
