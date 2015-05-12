package com.philips.cdp.dicommclient.port.common;

import java.util.HashMap;
import java.util.Map;

import com.philips.cdp.dicomm.util.ALog;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;

public class PairingPort extends DICommPort<Object> {

    private final String PAIRINGPORT_NAME = "pairing";
    private final int PAIRINGPORT_PRODUCTID = 0;

    private static final String KEY_SETUP = "setup";
    private static final String KEY_CONNECTION = "connection";

    public PairingPort(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
		super(networkNode, communicationStrategy);
	}

	@Override
	public boolean isResponseForThisPort(String response) {
	    ALog.e(ALog.PAIRINGPORT,"Pairing does not return responses");
		return false;
	}

	@Override
	public void processResponse(String response) {
        ALog.e(ALog.PAIRINGPORT,"Pairing does not return responses");
	}

	@Override
	public String getDICommPortName() {
		return PAIRINGPORT_NAME;
	}

	@Override
	public int getDICommProductId() {
		return PAIRINGPORT_PRODUCTID;
	}

	@Override
	public boolean supportsSubscription() {
		return false;
	}

    public void disableDemoMode() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put(KEY_SETUP, "inactive");
        dataMap.put(KEY_CONNECTION, "disconnected");
        putProperties(dataMap);
    }

    public void triggerPairing(String appType, String appEui64, String secretKey) {
        String[] dataArray = {appType, appEui64, secretKey};
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("Pair", dataArray);
        putProperties(dataMap);
    }
}
