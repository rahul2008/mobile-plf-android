package com.philips.cl.di.dev.pa.purifier;

public interface UDPEventListener {
	
	void onUDPEventReceived(String data, String fromIp) ;
}
