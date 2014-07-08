package com.philips.cl.di.dev.pa.ews;

public interface EWSListener {
	
	int ERROR_CODE_PHILIPS_SETUP_NOT_FOUND = 1 ;
	int ERROR_CODE_COULDNOT_RECEIVE_DATA_FROM_DEVICE = 2 ;
	int ERROR_CODE_COULDNOT_SEND_DATA_TO_DEVICE = 3 ;
	int ERROR_CODE_COULDNOT_CONNECT_HOME_NETWORK = 4 ;
	int ERROR_CODE_INVALID_PASSWORD = 5 ;
	int ERROR_CODE_COULDNOT_FIND_DEVICE = 6 ;
	void onDeviceAPMode() ;
	
	void onSelectHomeNetwork() ;
	
	void onHandShakeWithDevice() ;
	
	void onDeviceConnectToHomeNetwork() ;
	
	void foundHomeNetwork() ;
	
	void onErrorOccurred(int errorCode) ;
	
	void onWifiDisabled() ;
}
