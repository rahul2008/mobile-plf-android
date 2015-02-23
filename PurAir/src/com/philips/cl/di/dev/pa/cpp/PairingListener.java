package com.philips.cl.di.dev.pa.cpp;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;


public interface PairingListener {	
	void onPairingSuccess(NetworkNode networkNode);
	void onPairingFailed(NetworkNode networkNode);
}
