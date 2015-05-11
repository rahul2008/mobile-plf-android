package com.philips.cl.di.dicomm.cpp;

public abstract class KPSConfigurationInfo {
		
	public abstract String getBootStrapId(); 
	
	public abstract String getBootStrapKey();
	
	public abstract String getProductId();
	
	public abstract int getProductVersion();
	
	public abstract String getComponentId();
	
	public abstract int getComponentCount();
	
	public abstract String getAppId();
	
	public abstract int getAppVersion();
	
	public abstract String getAppType();
	
	public abstract String getCountryCode();
	
	public abstract String getLanguageCode();
	
	public abstract String getDevicePortUrl();

}
