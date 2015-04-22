package com.philips.cl.di.dicomm.port;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;

public class DevicePort extends DICommPort {

	private final String DEVICEPORT_NAME = "device";
	private final int DEVICEPORT_PRODUCTID = 1;

	public DevicePort(NetworkNode networkNode,
			CommunicationStrategy communicationStrategy) {
		super(networkNode, communicationStrategy);
	}

	@Override
	public boolean isResponseForThisPort(String response) {
		ALog.d("DevicePort", response);
		return false;
	}

	@Override
	public void processResponse(String response) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDICommPortName() {
		return DEVICEPORT_NAME;
	}

	@Override
	public int getDICommProductId() {
		return DEVICEPORT_PRODUCTID;
	}

	@Override
	public boolean supportsSubscription() {
		// TODO DIComm Refactor check if subscription to deviceport is necessary
		return false;
	}

}
