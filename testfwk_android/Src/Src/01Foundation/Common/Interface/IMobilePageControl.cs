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

namespace Philips.H2H.Foundation.AutomationCore.Interface
{
    public interface IMobilePageControl : IPageControl
    {        
        string GetAttribute(MobilePageInstance.MobileAttribute attribute);
    }
}
