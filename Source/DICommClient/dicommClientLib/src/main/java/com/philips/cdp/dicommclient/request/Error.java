/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.request;

public enum Error {
	
	NODATA("Request cannot be performed with null or empty data"),
	REQUESTFAILED("Failed to perform request - check log for more info"),
	IOEXCEPTION("IOException occurred - check log for more info"),
	BADREQUEST("HTTP BADREQUEST occurred - check log for more info"),
	BADGATEWAY("HTTP BADGATEWAY occurred "),
	NOTCONNECTED("Request cannot be performed - Not connected to an appliance"), 
	NOWIFIAVAILABLE("Request cannot be performed - No WifiConnection available for local request");

    private final String mErrorMessage;
	
	Error(String errorMessage){		
		mErrorMessage = errorMessage;		
	}

	public String getErrorMessage() {
		return mErrorMessage;
	}
}
