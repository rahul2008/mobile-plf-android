package com.philips.cl.di.dev.pa.cpp;

import com.philips.icpinterface.ICPClient;

public interface ICPEventListener {
	public void onICPCallbackEventOccurred(int eventType,int status,ICPClient obj) ;
}
