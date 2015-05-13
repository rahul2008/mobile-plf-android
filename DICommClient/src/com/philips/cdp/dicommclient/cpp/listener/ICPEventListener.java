package com.philips.cdp.dicommclient.cpp.listener;

import com.philips.icpinterface.ICPClient;

public interface ICPEventListener {
	void onICPCallbackEventOccurred(int eventType,int status,ICPClient obj) ;
}
