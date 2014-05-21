package com.philips.cl.di.dev.pa.cpp;

public interface DCSEventListener {
	void onDCSEventReceived(String data, String fromEui64) ;
}
