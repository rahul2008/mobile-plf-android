package com.philips.cl.di.dev.pa.cpp;

import com.philips.cdp.dicommclient.networknode.NetworkNode;


public interface PairingListener {	
	void onPairingSuccess(NetworkNode networkNode);
	void onPairingFailed(NetworkNode networkNode);
}
