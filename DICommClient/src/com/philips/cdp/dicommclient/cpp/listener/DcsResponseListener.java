/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.cpp.listener;

public interface DcsResponseListener {
	void onDCSResponseReceived(String dcsResponse, String conversationId);
}
