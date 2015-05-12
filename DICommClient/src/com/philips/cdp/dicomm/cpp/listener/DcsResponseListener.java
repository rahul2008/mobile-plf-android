package com.philips.cdp.dicomm.cpp.listener;

public interface DcsResponseListener {
	void onDCSResponseReceived(String dcsResponse, String conversationId);
}
