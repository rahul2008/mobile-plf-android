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

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.icpinterface.configuration.KeyProvisioningConfiguration;

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
		MaxNrOfRetry = 0;
	}
	
	public void setNVMConfigParams() {
		this.ICPClientBootStrapID = AppConstants.BOOT_STRAP_ID;
		this.ICPClientBootStrapKey = AppConstants.BOOT_STRAP_KEY;
		this.ICPClientBootStrapProductId = AppConstants.BOOT_STRAP_PRODUCT_ID;
		this.ICPClientproductVersion = 0;
		this.ICPClientproductCountry = "NL"; // TODO: Get from locale
		this.ICPClientproductLanguage = "EN"; // TODO: Get from locale
//		COMPONENT_ID=AC4373-AND //TODO : Add component
		//TODO : Change DPURL to production.
		this.ICPClientdevicePortalURL1="https://www.ecdinterface.philips.com/DevicePortalICPRequestHandler/RequestHandler.ashx";
	}
	
}