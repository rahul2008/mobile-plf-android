/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.cpp;

public abstract class KpsConfigurationInfo {

	private static final int ICPCLIENT_PRIORITY = 20;
	private static final int ICPCLIENT_STACKSIZE = 131072;
	private static final int HTTP_TIMEOUT = 30;
	private static final String FILTERSTRING_NOFILTER = "";
	private static final int MAX_NUMBER_RETRIES = 2;

	public int getICPClientPriority() {
		return ICPCLIENT_PRIORITY;
	}

	public int getICPClientStackSize() {
		return ICPCLIENT_STACKSIZE;
	}

	public int getHttpTimeout() {
		return HTTP_TIMEOUT;
	}

	public String getFilterString() {
		return FILTERSTRING_NOFILTER;
	}

	public int getMaxNumberOfRetries() {
		return MAX_NUMBER_RETRIES;
	}

	public abstract String getBootStrapId();

	public abstract String getBootStrapKey();
	
	public abstract String getProductId();
	
	public abstract int getProductVersion();
	
	public abstract String getComponentId();
	
	public abstract int getComponentCount();
	
	public abstract String getRelationshipId();
	
	public abstract int getAppVersion();
	
	public abstract String getAppType();
	
	public abstract String getCountryCode();
	
	public abstract String getLanguageCode();
	
	public abstract String getDevicePortUrl();

}
