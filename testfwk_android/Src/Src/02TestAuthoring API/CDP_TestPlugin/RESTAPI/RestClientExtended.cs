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

namespace Philips.SIG.Automation.Android.CDP.IAPTestPlugin
{
    /// <summary>
    /// This class provides all the functionalities to make REST calls.
    /// </summary>
    public class RestClientExtended : RestClient
    {
        static string environmentUsed = ConfigurationManager.AppSettings["envForRESTCalls"];
        static string baseURL = null;

        public RestClientExtended(string endpoint, HttpVerb method, string contentType, string postData)
        {
            EndPoint = endpoint;
            Method = method;
            ContentType = contentType;
            PostData = postData;
        }

        /// <summary>
        /// Gets the Authorization token, checks the type of Request and makes the desired request.
        /// </summary>
        /// <param name="parameters">Takes url parameters.</param>
        /// <returns>Returns a JSON response returned from API call.</returns>
        public string MakeRequest(string parameters)
        {
            var request = (HttpWebRequest)WebRequest.Create(EndPoint + parameters);

            request.Method = Method.ToString();
            request.ContentLength = 0;
            //request.Accept = ContentType;
            request.ContentType = ContentType;
            string uuid = ConfigurationManager.AppSettings["UserUUID"];
            //request.UseDefaultCredentials = true;
            //srequest.Proxy.Credentials = System.Net.CredentialCache.DefaultCredentials;
            request.Headers.Add(HttpRequestHeader.Authorization, "Bearer " + FetchAuthorizationTokenfromJanrain(uuid));

            if ((!string.IsNullOrEmpty(PostData) && Method == HttpVerb.POST) || (!string.IsNullOrEmpty(PostData) && Method == HttpVerb.PUT))
            {
                var encoding = new UTF8Encoding();
                var bytes = Encoding.GetEncoding("iso-8859-1").GetBytes(PostData);
                request.ContentLength = bytes.Length;

                using (var writeStream = request.GetRequestStream())
                {
                    writeStream.Write(bytes, 0, bytes.Length);
                }
            }

            using ( var response = (HttpWebResponse)request.GetResponse())
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

        /// <summary>
        /// Returns a string containing the Authorization token from Janrain.
        /// </summary>
        /// <param name="useruuid">useruuid represents the uuid of logged in user.</param>
        /// <returns>Authorization token valid for the user.</returns>
        public static string FetchAuthorizationTokenfromJanrain(string useruuid)
        {
            try
            {
                string authCode = null;
                string janrainToken = null;
                string authorizationHeaderToken = null;
                JObject root = null;


                string getAuthToken = ConfigurationManager.AppSettings["BaseURLforJanrain"] + "access/getAuthorizationCode?redirect_uri=dddd&uuid=" + useruuid + "&client_id=ptz7d2rvc3a3drzba9am2859acb8euq7&client_secret=7av7v25kym4f7jg9r6wumshrmu7pvk7q&type_name=user";
                 string response = MakeRequestForAuth(getAuthToken);
                root = JObject.Parse(response);
                if (root.HasValues)
                {
                    authCode = root.GetValue("authorizationCode").ToString();
                }
                string getEntityByAccessToken = ConfigurationManager.AppSettings["BaseURLforJanrain"] + "oauth/token?redirect_uri=dddd&grant_type=authorization_code&client_id=ptz7d2rvc3a3drzba9am2859acb8euq7&client_secret=7av7v25kym4f7jg9r6wumshrmu7pvk7q&code=" + authCode;
                response = MakeRequestForAuth(getEntityByAccessToken);
                root = JObject.Parse(response);
                if (root.HasValues)
                {
                    janrainToken = root.GetValue("access_token").ToString();
                }

                 if (environmentUsed.Equals("Test"))
                    baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsTst"];
                else
                    baseURL = ConfigurationManager.AppSettings["BaseURLforRESTCallsProd"];

                string getAccessToken = baseURL + "oauth/token?janrain=" + janrainToken + "&grant_type=janrain&client_id=mobile_android&client_secret=secret";
                response = MakeRequestForAuth(getAccessToken);
                root = JObject.Parse(response);
                if (root.HasValues)
                {
                    authorizationHeaderToken = root.GetValue("access_token").ToString();
                }
                return authorizationHeaderToken;
            }
            catch (Exception e)
            {
                Console.Write(e.StackTrace);
                Logger.Info(e.StackTrace);
                return null;
            }
        }

        /// <summary>
        /// Returns a response string for every request made
        /// </summary>
        /// <param name="useruuid">useruuid represents the uuid of logged in user.</param>
        /// <returns>Authorization token valid for the user.</returns>
        public static string MakeRequestForAuth(string url)
        {
            var request = (HttpWebRequest)WebRequest.Create(url);
            request.Method = HttpVerb.POST.ToString();
            request.ContentLength = 0;
            request.ContentType = "text/json";
            WebResponse response = request.GetResponse();
            Stream stream = response.GetResponseStream();
            StreamReader re = new StreamReader(stream);
            String responseStream = re.ReadToEnd();

            return responseStream;
        }
    }
}
