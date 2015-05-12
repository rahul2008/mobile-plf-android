package com.philips.cl.di.dicomm.appliance;

import com.philips.cdp.dicommclient.networknode.NetworkNode;

public abstract class DICommApplianceFactory<T> {

	public abstract boolean canCreateApplianceForNode(NetworkNode networkNode);
	public abstract T createApplianceForNode(NetworkNode networkNode);

}
