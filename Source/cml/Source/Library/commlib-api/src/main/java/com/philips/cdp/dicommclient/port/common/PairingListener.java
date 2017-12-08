/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import com.philips.cdp2.commlib.core.appliance.Appliance;


public interface PairingListener<T extends Appliance> {
	void onPairingSuccess(T appliance);
	void onPairingFailed(T appliance);
}
