package com.philips.cl.di.dev.pa.cpp;

import com.philips.cl.di.dev.pa.datamodel.DiscoverInfo;

public interface CppDiscoverEventListener {

	public void onSignedOnViaCpp();

	public void onSignedOffViaCpp();

	public void onDiscoverEventReceived(DiscoverInfo discoverInfo, boolean isResponseToRequest);

}
