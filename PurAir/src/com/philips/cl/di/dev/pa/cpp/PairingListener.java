package com.philips.cl.di.dev.pa.cpp;

import com.philips.icpinterface.ICPClient;

public interface PairingListener {	
	public void onPairingSuccess();
	public void onPairingFailed();
}
