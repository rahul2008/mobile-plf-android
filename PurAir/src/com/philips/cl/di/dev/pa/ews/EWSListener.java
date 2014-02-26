package com.philips.cl.di.dev.pa.ews;

public interface EWSListener {
	
	public static final int ERROR_CODE_PHILIPS_SETUP_NOT_FOUND = 1 ;
	public static final int ERROR_CODE_COULDNOT_RECEIVE_DATA_FROM_DEVICE = 2 ;
	public static final int ERROR_CODE_COULDNOT_SEND_DATA_TO_DEVICE = 3 ;
	public static final int ERROR_CODE_COULDNOT_FIND_DEVICE = 4 ;
	public void onDeviceAPMode() ;
	
	public void onSelectHomeNetwork() ;
	
	public void onHandShakeWithDevice() ;
	
	public void onDeviceConnectToHomeNetwork() ;
	
	public void foundHomeNetwork() ;
	
	public void onErrorOccurred(int errorCode) ;
	
	public void onWifiDisabled() ;
}
