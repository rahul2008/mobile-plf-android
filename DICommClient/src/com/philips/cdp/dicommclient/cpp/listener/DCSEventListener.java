package com.philips.cdp.dicommclient.cpp.listener;

public interface DCSEventListener {
	void onDCSEventReceived(String data, String fromEui64, String action) ;
}
