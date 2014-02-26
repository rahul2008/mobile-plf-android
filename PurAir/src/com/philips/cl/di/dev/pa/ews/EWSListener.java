package com.philips.cl.di.dev.pa.ews;

public interface EWSListener {
	
	public static final int ERROR_CODE1 = 1 ;
	public static final int ERROR_CODE2 = 2 ;
	
	public void onDeviceAPMode() ;
	
	public void onSelectHomeNetwork() ;
	
	public void onHandShakeWithDevice() ;
	
	public void onDeviceConnectToHomeNetwork() ;
	
	public void foundHomeNetwork() ;
	
	public void onErrorOccurred(int errorCode) ;
	
	public void onWifiDisabled() ;
}
