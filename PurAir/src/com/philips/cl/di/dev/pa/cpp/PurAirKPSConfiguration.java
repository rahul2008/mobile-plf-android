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

import com.philips.cdp.dicommclient.cpp.KpsConfigurationInfo;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.fragment.AlertDialogFragment;
import com.philips.cl.di.dev.pa.util.Utils;

/**
 *This class provides list of
 *configuration parameters set by the application for sign on.
 */

public class PurAirKPSConfiguration extends KpsConfigurationInfo
{

	@Override
	public String getBootStrapId() {
		return AlertDialogFragment.getBootStrapID();
	}

	@Override
	public String getBootStrapKey() {
		return Utils.getBootStrapKey();
	}

	@Override
	public String getProductId() {
		return AppConstants.BOOT_STRAP_PRODUCT_ID;
	}

	@Override
	public int getProductVersion() {
		return 0;
	}

	@Override
	public String getComponentId() {
		return AppConstants.COMPONENT_ID;
	}

	@Override
	public String getAppId() {
		return AppConstants.APP_ID;
	}

	@Override
	public int getAppVersion() {
		return 0;
	}

	@Override
	public String getAppType() {
		return AppConstants.APP_TYPE;
	}

	@Override
	public String getCountryCode() {
		return Utils.getCountryCode();
	}

	@Override
	public String getLanguageCode() {
		return Utils.getCountryLocale();
	}

	@Override
	public String getDevicePortUrl() {
		return AppConstants.DEVICE_PORT_URL;
	}

	@Override
	public int getComponentCount() {
		return 1;
	}

	@Override
	public String getFilterString() {
		return "TEST";
	}

}
