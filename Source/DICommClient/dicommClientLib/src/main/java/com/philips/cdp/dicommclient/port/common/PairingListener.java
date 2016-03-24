/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;


public interface PairingListener<T extends DICommAppliance> {
	void onPairingSuccess(T appliance);
	void onPairingFailed(T appliance);
}
