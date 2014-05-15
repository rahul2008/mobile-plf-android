package com.philips.cl.di.dev.pa.purifier;

public interface SubscriptionEventListener {
	
	public void onLocalEventReceived(String encryptedData);
	public void onRemoteEventReceived(String data, String purifierEui64);
}
