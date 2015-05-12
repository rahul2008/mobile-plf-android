package com.philips.cdp.dicomm.cpp.listener;

public interface PublishEventListener {
	void onPublishEventReceived(int status, int messageId, String conversationId) ;
}
