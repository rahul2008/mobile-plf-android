/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp2.commlib.lan.subscription;

public interface UdpEventListener {
	
	void onUDPEventReceived(String data, String fromIp) ;
}
