package com.philips.cl.di.dev.pa.purifier;

import com.philips.cl.di.dev.pa.newpurifier.PurifierManager.PURIFIER_EVENT;

public interface SubscriptionEventListener {
	
	void onLocalEventReceived(String encryptedData, String purifierIp);
	void onRemoteEventReceived(String data, String purifierEui64);
	void onLocalEventLost(PURIFIER_EVENT purifierEvent) ;
}
