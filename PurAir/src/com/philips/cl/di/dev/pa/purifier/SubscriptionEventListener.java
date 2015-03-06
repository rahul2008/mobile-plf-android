package com.philips.cl.di.dev.pa.purifier;

import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager.PurifierEvent;

public interface SubscriptionEventListener {
	
	void onLocalEventReceived(String encryptedData);
	void onRemoteEventReceived(String data);
	void onLocalEventLost(PurifierEvent purifierEvent) ;
}
