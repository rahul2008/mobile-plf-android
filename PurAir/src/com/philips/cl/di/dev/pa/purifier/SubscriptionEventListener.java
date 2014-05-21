package com.philips.cl.di.dev.pa.purifier;

public interface SubscriptionEventListener {
	
	void onLocalEventReceived(String encryptedData, String purifierIp);
	void onRemoteEventReceived(String data, String purifierEui64);
}
