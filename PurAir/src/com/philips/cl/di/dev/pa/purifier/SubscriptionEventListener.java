package com.philips.cl.di.dev.pa.purifier;

public interface SubscriptionEventListener {
	
	public void onLocalEventReceived(String encryptedData, String purifierIp);
	public void onRemoteEventReceived(String data, String purifierEui64);
}
