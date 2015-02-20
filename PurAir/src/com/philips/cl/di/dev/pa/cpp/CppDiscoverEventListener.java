package com.philips.cl.di.dev.pa.cpp;

public interface CppDiscoverEventListener {

	void onSignedOnViaCpp();

	void onSignedOffViaCpp();

	void onDiscoverEventReceived(String data, boolean isResponseToRequest);

}
