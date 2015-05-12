package com.philips.cl.di.dev.pa.newpurifier;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.security.DISecurity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dicomm.appliance.DICommApplianceFactory;
import com.philips.cl.di.dicomm.communication.CommunicationMarshal;

public class AirPurifierFactory extends DICommApplianceFactory<AirPurifier> {

	@Override
	public boolean canCreateApplianceForNode(NetworkNode networkNode) {
		if (networkNode == null) return false;
		if (networkNode.getModelName().equalsIgnoreCase(AppConstants.MODEL_NAME)) return true;
		return false;
	}

	@Override
	public AirPurifier createApplianceForNode(NetworkNode networkNode) {
		DISecurity diSecurity = new DISecurity();
		CommunicationMarshal communicationStrategy = new CommunicationMarshal(diSecurity);
		return new AirPurifier(networkNode, communicationStrategy);
	}

}
