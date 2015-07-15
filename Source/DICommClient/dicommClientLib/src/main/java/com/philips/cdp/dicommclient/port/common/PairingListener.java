/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import com.philips.cdp.dicommclient.networknode.NetworkNode;


public interface PairingListener {	
	void onPairingSuccess(NetworkNode networkNode);
	void onPairingFailed(NetworkNode networkNode);
}
