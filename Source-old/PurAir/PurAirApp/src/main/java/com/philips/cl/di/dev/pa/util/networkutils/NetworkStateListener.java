package com.philips.cl.di.dev.pa.util.networkutils;

public interface NetworkStateListener {
	
	void onConnected(String ssid); //TODO : Add network SSID and connection type (wifi or mobile).
	
	void onDisconnected();
}
