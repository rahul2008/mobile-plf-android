package com.philips.cdp.dicomm.cpp;

public interface DCSResponseListener {
	void onDCSResponseReceived(String dcsResponse, String conversationId);
}
