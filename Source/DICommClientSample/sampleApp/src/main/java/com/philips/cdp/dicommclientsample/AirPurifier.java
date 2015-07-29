package com.philips.cdp.dicommclientsample;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

class AirPurifier extends DICommAppliance {

	public static final String MODELNAME = "AirPurifier";

	public AirPurifier(NetworkNode networkNode,
					   CommunicationStrategy communicationStrategy) {
		super(networkNode, communicationStrategy);
	}

	@Override
	public String getDeviceType() {
		return null;
	}
}