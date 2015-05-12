package com.philips.cdp.dicomm.cpp.listener;

public interface DcsEventListener {
	void onDCSEventReceived(String data, String fromEui64, String action) ;
}
