package com.philips.cl.di.dev.pa.cpp;

public interface DCSResponseListener {
	void onDCSResponseReceived(String dcsResponse, String conversationId);
}
