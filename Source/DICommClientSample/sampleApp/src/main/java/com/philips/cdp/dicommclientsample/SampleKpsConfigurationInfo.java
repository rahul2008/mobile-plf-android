package com.philips.cdp.dicommclientsample;

import com.philips.cdp.dicommclient.cpp.KpsConfigurationInfo;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
class SampleKpsConfigurationInfo extends KpsConfigurationInfo {

	// TOOD can we provide a default CPP configuration here?

    @Override
	public String getBootStrapId() {
		return null;
	}

    @Override
	public String getBootStrapKey() {
		return null;
	}

    @Override
	public String getProductId() {
		return null;
	}

    @Override
	public int getProductVersion() {
		return 0;
	}

    @Override
	public String getComponentId() {
		return null;
	}

    @Override
	public int getComponentCount() {
		return 0;
	}

    @Override
	public String getAppId() {
		return null;
	}

    @Override
	public int getAppVersion() {
		return 0;
	}

    @Override
	public String getAppType() {
		return null;
	}

    @Override
	public String getCountryCode() {
		return null;
	}

    @Override
	public String getLanguageCode() {
		return null;
	}

    @Override
	public String getDevicePortUrl() {
		return null;
	}
}
