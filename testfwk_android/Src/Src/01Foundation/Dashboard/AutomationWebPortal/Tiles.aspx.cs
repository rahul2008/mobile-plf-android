/* 
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 */
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;

namespace Philips.H2H.Automation.Dashboard
{
    public partial class Tiles : System.Web.UI.Page
    {
        public static string tfsProjectModule;
        public static string suiteName;
        static TeamFoundationServerHelper helperObj = new TeamFoundationServerHelper();

        protected void Page_Load(object sender, EventArgs e)
        {
            tfsProjectModule = Request.QueryString["buildDefinition"];
            suiteName = TeamFoundationServerHelper.GetQualifiedBuildName(tfsProjectModule);
            HtmlMeta meta = new HtmlMeta();
            meta.HttpEquiv = "Refresh";
            meta.Content = "300";
            this.Page.Header.Controls.Add(meta); 
        }

        [WebMethod]
        public static string GetTestRunDetails()
        {
            return JsonConvert.SerializeObject(helperObj.GetTestRunDetails(tfsProjectModule));
        }
    }
}