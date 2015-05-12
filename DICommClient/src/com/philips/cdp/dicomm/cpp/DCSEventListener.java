package com.philips.cdp.dicomm.cpp;

public interface DCSEventListener {
	void onDCSEventReceived(String data, String fromEui64, String action) ;
}
