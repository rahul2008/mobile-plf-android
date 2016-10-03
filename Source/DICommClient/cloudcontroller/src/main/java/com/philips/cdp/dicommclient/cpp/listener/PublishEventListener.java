/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.cpp.listener;

public interface PublishEventListener {
	void onPublishEventReceived(int status, int messageId, String conversationId) ;
}
