package com.philips.cl.di.dev.pa.ews;

public interface EWSListener {
	
	static final int ERROR_CODE_PHILIPS_SETUP_NOT_FOUND = 1 ;
	static final int ERROR_CODE_COULDNOT_RECEIVE_DATA_FROM_DEVICE = 2 ;
	static final int ERROR_CODE_COULDNOT_SEND_DATA_TO_DEVICE = 3 ;
	static final int ERROR_CODE_COULDNOT_FIND_DEVICE = 4 ;
	static final int ERROR_CODE_INVALID_PASSWORD = 5 ;
	void onDeviceAPMode() ;
	
	void onSelectHomeNetwork() ;
	
	void onHandShakeWithDevice() ;
	
	void onDeviceConnectToHomeNetwork() ;
	
	void foundHomeNetwork() ;
	
	void onErrorOccurred(int errorCode) ;
	
	void onWifiDisabled() ;
}
