/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

public interface UdpEventListener {
	
	void onUDPEventReceived(String data, String fromIp) ;
}
