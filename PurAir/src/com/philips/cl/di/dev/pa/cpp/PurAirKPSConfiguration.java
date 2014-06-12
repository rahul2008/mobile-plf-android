package com.philips.cl.di.dev.pa.cpp;

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

import java.util.Locale;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.security.Util;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.icpinterface.configuration.KeyProvisioningConfiguration;
import com.philips.icpinterface.data.NVMComponentInfo;

/**
*This class provide interface to set ICP Client configuration. 
*configuration parameters set by the application.
*Set the necessary parameters, as per the request. 
*/
public class PurAirKPSConfiguration extends KeyProvisioningConfiguration 
{

	/* Constructor */
	public PurAirKPSConfiguration()
	{
		ICPClientPriority = 20;
		ICPClientStackSize = 131072;
		HTTPTimeout = 30;
		FilterString = "TEST";
		MaxNrOfRetry = 2;
	}
	
	public void setNVMConfigParams() {		
		this.ICPClientBootStrapID = Utils.getBootStrapID();
		this.ICPClientBootStrapKey = Util.getBootstrapKey();
		ALog.i(ALog.KPS, "BootstrapID: "+ICPClientBootStrapID+"\n BootStrapKey: "+ICPClientBootStrapKey) ;
		this.ICPClientBootStrapProductId = AppConstants.BOOT_STRAP_PRODUCT_ID;
		this.ICPClientproductVersion = 0;
		this.ICPClientproductCountry = getCountryCode();
		this.ICPClientproductLanguage = getLanguageCode();
		
		this.ICPClientComponentCount = 1;
		NVMComponentInfo appComponentInfo = new NVMComponentInfo();
		appComponentInfo.componentID = "AC4373-AND";
		appComponentInfo.componentVersion = PurAirApplication.getAppVersion();
		this.ICPClientNVMComponents = new NVMComponentInfo[] {appComponentInfo};

		this.ICPClientdevicePortalURL1="https://www.ecdinterface.philips.com/DevicePortalICPRequestHandler/RequestHandler.ashx";
	}
	
	private String getCountryCode() {
		String  countryCode = Locale.getDefault().getCountry();
		if (countryCode == null || countryCode.isEmpty()) {
			countryCode = "NL";
		}
		return countryCode;
	}
	
	private String getLanguageCode() {
		String languageCode = Locale.getDefault().getLanguage();
		if (languageCode == null || languageCode.isEmpty()) {
			languageCode = "EN";
		} else {
			languageCode = languageCode.toUpperCase();
		}
		return languageCode;
	}
	
}