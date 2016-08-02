using Philips.H2H.Foundation.AutomationCore;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json.Linq;

namespace Philips.CDP.Automation.TestAuthoring.IAP.Plugin
{
    public class RestClientExtended : RestClient
    {
        public class OAuth
        {
            public string access_token { get; set; }
            public string token_type { get; set; }
            public string refresh_token { get; set; }
            public int expires_in { get; set; }
            public string scope { get; set; }
        }

        public RestClientExtended(string endpoint, HttpVerb method, string contentType, string postData)
        {
            EndPoint = endpoint;
            Method = method;
            ContentType = contentType;
            PostData = postData;
        }

        /// <summary>
        /// Returns a string containing the Authorization token
        /// </summary>
        /// <param name="username"></param>
        /// <param name="password"></param>
        /// <returns></returns>
        public static string FetchAuthorizedToken(string username, string password)
        {
            string access_token_auth = "";
            string endpoint = ConfigurationManager.AppSettings["OCCAuthorizationEndpoint"];
            string parameters = "username=" + username + "&password=" + password + "&grant_type=password&client_id=mobile_android&client_secret=secret";
            string url = endpoint + parameters;
            var request = (HttpWebRequest)WebRequest.Create(url);
            request.Method = HttpVerb.POST.ToString();
            request.ContentLength = 0;
            request.ContentType = "text/json";
            request.Headers.Add(HttpRequestHeader.Authorization, ConfigurationManager.AppSettings["OCCAuthorizationHeader"]);
            WebResponse response = request.GetResponse();
            Stream stream = response.GetResponseStream();
            StreamReader re = new StreamReader(stream);
            String responseStream = re.ReadToEnd();
            JObject root = JObject.Parse(responseStream);
            if (root.HasValues)
            {
                access_token_auth = root.GetValue("access_token").ToString();
            }
            return access_token_auth;
        }

        /// <summary>
        /// Gets the Authorization token, checks the type of Request and makes the desired request
        /// </summary>
        /// <param name="parameters"></param>
        /// <returns></returns>
        public string MakeRequest(string parameters)
        {
            var request = (HttpWebRequest)WebRequest.Create(EndPoint + parameters);

            request.Method = Method.ToString();
            request.ContentLength = 0;
            request.ContentType = ContentType;
            string username = ConfigurationManager.AppSettings["AppLogin"];
            string pswd = ConfigurationManager.AppSettings["AppPassword"];
            request.Headers.Add(HttpRequestHeader.Authorization, "Bearer " + FetchAuthorizedToken(username, pswd));

            if (!string.IsNullOrEmpty(PostData) && Method == HttpVerb.POST)
            {
                var encoding = new UTF8Encoding();
                var bytes = Encoding.GetEncoding("iso-8859-1").GetBytes(PostData);
                request.ContentLength = bytes.Length;

                using (var writeStream = request.GetRequestStream())
                {
                    writeStream.Write(bytes, 0, bytes.Length);
                }
            }

            using (var response = (HttpWebResponse)request.GetResponse())
            {
                var responseValue = string.Empty;

                if (response.StatusCode != HttpStatusCode.OK && response.StatusCode != HttpStatusCode.Created)
                {
                    var message = String.Format("Request failed. Received HTTP {0}", response.StatusCode);
                    throw new ApplicationException(message);
                }

                // grab the response
                using (var responseStream = response.GetResponseStream())
                {
                    if (responseStream != null)
                        using (var reader = new StreamReader(responseStream))
                        {
                            responseValue = reader.ReadToEnd();
                        }
                }

                return responseValue;
            }
        }
    }
}
