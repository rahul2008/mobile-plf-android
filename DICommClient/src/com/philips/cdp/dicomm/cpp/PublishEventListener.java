package com.philips.cdp.dicomm.cpp;

public interface PublishEventListener {
	void onPublishEventReceived(int status, int messageId, String conversationId) ;
}
