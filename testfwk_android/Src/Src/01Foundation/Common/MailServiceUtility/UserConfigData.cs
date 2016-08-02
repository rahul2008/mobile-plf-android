using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Security;
using Microsoft.Exchange.WebServices.Data;
using System.Configuration;

namespace Philips.H2H.Foundation.AutomationCore.MailServiceUtility
{
    public interface IUserData
    {
        ExchangeVersion Version { get; }
        string EmailAddress { get; }
        string Password { get; }
        string enterpriseUser { get; }
        string enterpriseUserPwd { get; }
        Uri AutodiscoverUrl { get; set; }
    }

    public class UserConfigData : IUserData
    {
        private static UserConfigData userData;

        public static IUserData GetUserData()
        {
            if (userData == null)
            {
                GetUserDataFromConsole();
            }

            return userData;
        }

        private static void GetUserDataFromConsole()
        {
            userData = new UserConfigData();
            userData.EmailAddress = ConfigurationManager.AppSettings["functionalAccount"];//Console.ReadLine();
            userData.Password = ConfigurationManager.AppSettings["functionalAccountPwd"];           
        }

        public ExchangeVersion Version { get { return ExchangeVersion.Exchange2010_SP2; } }

        public string EmailAddress
        {
            get;
            private set;
        }

        public String Password
        {
            get;
            private set;
        }

        public Uri AutodiscoverUrl
        {
            get;
            set;
        }

        public string enterpriseUser
        {
            get;
            set;
        }

        public string enterpriseUserPwd
        {
            get;
            set;
        }
    }
}
