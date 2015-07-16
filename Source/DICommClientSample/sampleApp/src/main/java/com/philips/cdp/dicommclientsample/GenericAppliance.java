package com.philips.cdp.dicommclientsample;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

class GenericAppliance extends DICommAppliance {

	public GenericAppliance(NetworkNode networkNode,
			CommunicationStrategy communicationStrategy) {
		super(networkNode, communicationStrategy);
	}

	@Override
	public String getDeviceType() {
		return null;
	}
}