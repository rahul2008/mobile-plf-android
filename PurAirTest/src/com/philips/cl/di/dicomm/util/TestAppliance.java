package com.philips.cl.di.dicomm.util;

import com.philips.cdp.dicommclient.communication.NullStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cl.di.dev.pa.newpurifier.DICommAppliance;

public class TestAppliance extends DICommAppliance {

	public TestAppliance(NetworkNode networkNode) {
		super(networkNode, new NullStrategy());
	}

	@Override
	public String getDeviceType() {
		return null;
	}
}