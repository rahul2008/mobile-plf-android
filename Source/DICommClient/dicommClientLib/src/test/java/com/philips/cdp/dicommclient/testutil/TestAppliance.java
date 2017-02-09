/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.testutil;

import com.philips.commlib.core.appliance.Appliance;
import com.philips.commlib.core.communication.NullCommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

public class TestAppliance extends Appliance {

	public TestAppliance(NetworkNode networkNode) {
		super(networkNode, new NullCommunicationStrategy());
	}

	@Override
	public String getDeviceType() {
		return null;
	}
}
