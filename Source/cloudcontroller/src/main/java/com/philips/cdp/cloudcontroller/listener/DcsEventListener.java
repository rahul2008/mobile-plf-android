/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.cloudcontroller.listener;

public interface DcsEventListener {
	void onDCSEventReceived(String data, String fromEui64, String action) ;
}
