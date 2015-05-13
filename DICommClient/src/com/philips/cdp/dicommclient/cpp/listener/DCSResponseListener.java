package com.philips.cdp.dicommclient.cpp.listener;

public interface DCSResponseListener {
	void onDCSResponseReceived(String dcsResponse, String conversationId);
}
