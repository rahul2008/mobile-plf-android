/* 
 * (C) Koninklijke Philips Electronics N.V. 2015 
 * All rights are reserved. Reproduction or transmission in whole or in part, 
 * in any form or by any means, electronic, mechanical or otherwise, 
 * is prohibited without the prior written consent of the copyright owner. 
 */
using OpenQA.Selenium;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Philips.H2H.Foundation.AutomationCore.Common
{
    public class SelectorBy
    {
        public By GetByClause(SearchBy by, string clause)
        {
            switch (by)
            {
                case SearchBy.Id:
                    return By.Id(clause);
                case SearchBy.Name:
                    return By.Name(clause);
                case SearchBy.Xpath:
                    return By.XPath(clause);
                case SearchBy.TagName:
                    return By.TagName(clause);
                case SearchBy.Css:
                    return By.CssSelector(clause);
                case SearchBy.LinkText:
                    return By.LinkText(clause);
                case SearchBy.PartialLinkText:
                    return By.PartialLinkText(clause);
                case SearchBy.ClassName:
                    return By.ClassName(clause);
                default: return null;
            }
        }
    }
}
