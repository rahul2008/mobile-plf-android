package com.philips.cdp.dicommclient.subscription;

public interface UDPEventListener {
	
	void onUDPEventReceived(String data, String fromIp) ;
}
