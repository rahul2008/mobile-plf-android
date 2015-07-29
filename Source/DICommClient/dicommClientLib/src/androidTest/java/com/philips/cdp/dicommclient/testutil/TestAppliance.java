/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.testutil;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.communication.NullStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

public class TestAppliance extends DICommAppliance {

	public TestAppliance(NetworkNode networkNode) {
		super(networkNode, new NullStrategy());
	}

	@Override
	public String getDeviceType() {
		return null;
	}
}