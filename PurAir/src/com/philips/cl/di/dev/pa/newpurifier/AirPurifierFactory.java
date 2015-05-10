package com.philips.cl.di.dev.pa.newpurifier;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dicomm.appliance.DICommApplianceFactory;

public class AirPurifierFactory extends DICommApplianceFactory<AirPurifier> {

	@Override
	public boolean canCreateApplianceForNode(NetworkNode networkNode) {
		if (networkNode == null) return false;
		if (networkNode.getModelName().equalsIgnoreCase(AppConstants.MODEL_NAME)) return true;
		return false;
	}

	@Override
	public AirPurifier createApplianceForNode(NetworkNode networkNode) {
		// TODO Auto-generated method stub
		return null;
	}

}
