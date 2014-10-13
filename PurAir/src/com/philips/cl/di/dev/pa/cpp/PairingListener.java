package com.philips.cl.di.dev.pa.cpp;

import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;


public interface PairingListener {	
	void onPairingSuccess(PurAirDevice purifier);
	void onPairingFailed(PurAirDevice purifier);
}
