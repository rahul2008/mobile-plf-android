package com.philips.cl.di.dicomm.util;

import com.philips.cl.di.dev.pa.newpurifier.DICommAppliance;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dicomm.communication.NullStrategy;

public class TestAppliance extends DICommAppliance {

	public TestAppliance(NetworkNode networkNode) {
		super(networkNode, new NullStrategy());
	}
}