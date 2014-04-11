package com.philips.cl.di.dev.pa.cpp;

import com.philips.icpinterface.ICPClient;

public interface PairingListener {	
	public void onPairingStateReceived(int status, int eventType, ICPClient obj);
	public void onPairingPortTaskFailed();
}
