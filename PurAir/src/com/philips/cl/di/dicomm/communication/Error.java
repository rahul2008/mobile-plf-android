package com.philips.cl.di.dicomm.communication;

public class Error {
	//TODO: DICOMM Refactor,Remove when new request architecture is ready
	public static enum PurifierEvent { DEVICE_CONTROL, SCHEDULER, FIRMWARE, AQI_THRESHOLD, PAIRING }

	private final int mErrorCode;
	private final String mErrorMessage;
	
	public Error(int errorCode, String errorMessage){
		mErrorCode = errorCode;
		mErrorMessage = errorMessage;		
	}

	public String getErrorMessage() {
		return mErrorMessage;
	}

	public int getErrorCode() {
		return mErrorCode;
	}

}
