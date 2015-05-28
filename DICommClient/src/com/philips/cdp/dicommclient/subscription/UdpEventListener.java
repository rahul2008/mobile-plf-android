package com.philips.cdp.dicommclient.subscription;

public interface UdpEventListener {
	
	void onUDPEventReceived(String data, String fromIp) ;
}
