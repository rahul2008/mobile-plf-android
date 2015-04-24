package com.philips.cl.di.dev.pa.cpp;

public interface PublishEventListener {
	void onPublishEventReceived(int status, int messageId, String conversationId) ;
}
