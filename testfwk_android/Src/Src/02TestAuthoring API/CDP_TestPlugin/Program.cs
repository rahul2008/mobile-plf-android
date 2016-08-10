using Philips.SIG.Automation.Android.CDP.IAPTestPlugin;
using Philips.H2H.Foundation.AutomationCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Security;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.Configuration;


namespace RESTAPICallsTestApp
{
    class Program
    {
        public static String endpoint = "https://tst.pl.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/"+ConfigurationManager.AppSettings["AppLogin"];
        public static String contentType = "application/json";
        static void Main(string[] args)
        {
            ServicePointManager.ServerCertificateValidationCallback = new RemoteCertificateValidationCallback
            (
                delegate { return true; }
            );
            int id=getStockLevel();
            Console.WriteLine(id);
            Console.Read();
            String address = getAddress();
            if(address!=null)
            {
                Console.WriteLine("User is not first time user");
            }
        }
             public  static int getStockLevel()
        {
            
            HttpVerb method = HttpVerb.GET;
            String postdata="";
            RestClientExtended rest = new RestClientExtended(endpoint,method,contentType,postdata);
            string response = rest.MakeRequest("carts/current/entries");

            RootObject root = JsonConvert.DeserializeObject<RootObject>(response);
            int level = root.orderEntries[1].product.stock.stockLevel;
            
        return level;
        }
        public static string getAddress()
             {
                 HttpVerb method = HttpVerb.GET;
                 String postdata = "";
                 RestClientExtended rest = new RestClientExtended(endpoint, method, contentType, postdata);
                 string response = rest.MakeRequest("addresses");
                // RootObjectAddress root = JsonConvert.DeserializeObject<RootObjectAddress>(response);
                 JObject obj = JObject.Parse(response);
                 JObject obj1 = (JObject)obj["addresses"];
                 JArray array = (JArray)obj1["addresses"];
                 return response;
             }
        }
    }

