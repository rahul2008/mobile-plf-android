package com.philips.cl.di.dicomm.communication;

public enum Error {
	
	//TODO: DICOMM Refactor,Remove Air specific error codes when new request architecture is ready
	
	NODATA("Request cannot be performed with null or empty data"),
	REQUESTFAILED("Failed to perform request - check log for more info"),
	IOEXCEPTION("IOException occurred - check log for more info"),
	BADREQUEST("HTTP BADREQUEST occurred - check log for more info"),
	BADGATEWAY("HTTP BADGATEWAY occurred "),
	NOTCONNECTED("Request cannot be performed - Not connected to an appliance"),
	DEVICE_CONTROL(""),
	SCHEDULER(""),
	FIRMWARE(""),
	AQI_THRESHOLD(""),
	PAIRING("");	

	private final String mErrorMessage;
	
	Error(String errorMessage){		
		mErrorMessage = errorMessage;		
	}

	public String getErrorMessage() {
		return mErrorMessage;
	}
}
