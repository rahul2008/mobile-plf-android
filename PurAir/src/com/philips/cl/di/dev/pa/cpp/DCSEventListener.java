package com.philips.cl.di.dev.pa.cpp;

public interface DCSEventListener {
	
	public void onDCSEventReceived(String data, String fromEui64) ;
}
