package com.philips.cdp.dicommclient.discovery;

public interface CppDiscoverEventListener {

	public void onSignedOnViaCpp();

	public void onSignedOffViaCpp();

	public void onDiscoverEventReceived(DiscoverInfo discoverInfo, boolean isResponseToRequest);

}
