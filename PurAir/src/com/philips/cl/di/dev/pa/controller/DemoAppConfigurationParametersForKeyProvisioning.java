package com.philips.cl.di.dev.pa.controller;

/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without 
the written consent of the copyright holder.

Project           : Android Demo ICP App  

File Name         : DemoAppConfigurationParametersForKeyProvisioning.java        

Description       : 

Revision History:
Version 1: 
    Date: 26-Jun-2013
    Original author: Haranadh Kaki
    Description: Initial version    
----------------------------------------------------------------------------*/

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

import com.philips.cl.di.dev.pa.utils.ALog;
import com.philips.icpinterface.configuration.KeyProvisioningConfiguration;

/**
*This class provide interface to set ICP Client configuration. 
*configuration parameters set by the application.
*Set the necessary parameters, as per the request. 
*/
public class DemoAppConfigurationParametersForKeyProvisioning extends KeyProvisioningConfiguration 
{

	/* Constructor */
	public DemoAppConfigurationParametersForKeyProvisioning()
	{
		ICPClientPriority = 20;
		ICPClientStackSize = 131072;
		HTTPTimeout = 30;
		FilterString = "TEST";
		MaxNrOfRetry = 0;
	}
	

	/* NVM configuration */
	public void setNVMConfigParams(BufferedReader br)
	{
		
		String st,token;
		StringTokenizer parser;
		if( br == null)
			return;
		try
		{
			while( (st = br.readLine())!= null)
			{
				
				parser = new StringTokenizer(st, "=");
				while (parser.hasMoreTokens()) 
				{ 
					token = parser.nextToken();
					if("BOOTSTRAP_ID".equals(token))
					{
						this.ICPClientBootStrapID = parser.nextToken();
					}
					else if("BOOTSTRAP_KEY".equals(token))
					{
						this.ICPClientBootStrapKey = parser.nextToken();
					}
					else if("BOOTSTRAP_PRODUCT_ID".equals(token))
					{
						this.ICPClientBootStrapProductId = parser.nextToken();
					}
					else if("PRODUCT_VERSION".equals(token))
					{
						this.ICPClientproductVersion = Integer.parseInt(parser.nextToken());
					}
					else if("PRODUCT_COUNTRY".equals(token))
					{
						this.ICPClientproductCountry = parser.nextToken();
					}
					else if("PRODUCT_LANGUAGE".equals(token))
					{
						this.ICPClientproductLanguage = parser.nextToken();
					}
					else if("PROXY_ENABLED".equals(token))
					{
						if( (parser.nextToken()).equalsIgnoreCase("true"))
							this.ICPClientproductProxyEnabledStatus = true;
						else	
							this.ICPClientproductProxyEnabledStatus = false;
					}
					else if("PROXY_ADDRESS".equals(token))
					{
						this.ICPClientproductProxyAddress = parser.nextToken();
					}
					else if("PROXY_PORT".equals(token))
					{
						this.ICPClientproductProxyPort = Integer.parseInt(parser.nextToken());
					}
					else if("DPURL1".equals(token))
					{
						this.ICPClientdevicePortalURL1 = parser.nextToken();
					}
					else if("DPURL2".equals(token))
					{
						this.ICPClientdevicePortalURL2 = parser.nextToken();
					}
					else
					{
						break;
					}
				}
			}
		}
		catch(RuntimeException e)
		{
			ALog.e(ALog.KPS, e.getMessage());
		}
		catch(Exception e)
		{
			ALog.e(ALog.KPS, e.getMessage());
		}
		
	}
}