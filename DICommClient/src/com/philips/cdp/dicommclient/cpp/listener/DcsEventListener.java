/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.cpp.listener;

public interface DcsEventListener {
	void onDCSEventReceived(String data, String fromEui64, String action) ;
}
