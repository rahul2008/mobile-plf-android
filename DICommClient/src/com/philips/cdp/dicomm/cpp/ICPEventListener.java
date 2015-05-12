package com.philips.cdp.dicomm.cpp;

import com.philips.icpinterface.ICPClient;

public interface IcpEventListener {
	void onICPCallbackEventOccurred(int eventType,int status,ICPClient obj) ;
}
