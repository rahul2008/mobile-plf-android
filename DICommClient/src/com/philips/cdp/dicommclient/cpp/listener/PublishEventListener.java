package com.philips.cdp.dicommclient.cpp.listener;

public interface PublishEventListener {
	void onPublishEventReceived(int status, int messageId, String conversationId) ;
}
