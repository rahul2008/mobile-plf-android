package com.philips.cl.di.dev.pa.cpp;

public interface CppDiscoverEventListener {
	
	public void onSignedOnViaCpp();
	public void onSignedOffViaCpp();
	public void onDiscoverEventReceived(String data, boolean isResponseToRequest);

}
