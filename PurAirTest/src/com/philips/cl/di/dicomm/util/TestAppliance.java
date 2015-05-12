package com.philips.cl.di.dicomm.util;

import com.philips.cdp.dicomm.appliance.DICommAppliance;
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