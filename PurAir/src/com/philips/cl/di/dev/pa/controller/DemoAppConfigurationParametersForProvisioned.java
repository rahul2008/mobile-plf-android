package com.philips.cl.di.dev.pa.controller;

/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without 
the written consent of the copyright holder.

Project           : Android Demo ICP App  

File Name         : DemoAppConfigurationParametersForProvisioned.java        

Description       : 

Revision History:
Version 1: 
    Date: 26-Jun-2013
    Original author: Haranadh Kaki
    Description: Initial version    
----------------------------------------------------------------------------*/

import java.io.BufferedReader;
import java.util.StringTokenizer;

import android.content.Context;

import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.utils.Utils;
import com.philips.icpinterface.configuration.ProvisionedConfiguration;

/**
*This class provide interface to set ICP Client configuration. 
*configuration parameters set by the application.
*Set the necessary parameters, as per the request. 
*/
public class DemoAppConfigurationParametersForProvisioned extends ProvisionedConfiguration 
{
	private Context context ;
	public DemoAppConfigurationParametersForProvisioned(Context context)
	{
		this.context = context ;
		ICPClientPriority = 20;
		ICPClientStackSize = 16384;
		HTTPTimeout = 30;
		FilterString = "TEST";
		MaxNrOfRetry = 0;
	}
	
 	
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
					if("EUI64".equals(token)) 
					{
						if ( Utils.getEuid(context) != null ) {
							this.ICPClientEui64 = Utils.getEuid(context);
						}
						else {
							this.ICPClientEui64 = parser.nextToken();
						}
					}
					else if("PRIVATEKEY".equals(token))
					{
						if ( Utils.getPrivateKey(context) != null ) {
							this.ICPClientPrivateKey = Utils.getPrivateKey(context) ;
						}
						else {
							this.ICPClientPrivateKey = parser.nextToken();
						}
					}
					else if("PRODUCT_ID".equals(token))
					{
						this.ICPClientproductId = parser.nextToken();
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
		catch(Exception e)
		{
			System.out.println("Exception Raised While reading NVM");
		}
		
	}

}