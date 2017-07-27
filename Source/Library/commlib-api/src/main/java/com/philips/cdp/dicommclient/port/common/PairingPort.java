/*
 * © Koninklijke Philips N.V., 2015, 2016, 2017.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.PortProperties;

import java.util.HashMap;
import java.util.Map;

public class PairingPort<P extends PortProperties> extends DICommPort<P> {

    private final String PAIRINGPORT_NAME = "pairing";
    private final int PAIRINGPORT_PRODUCTID = 0;

    private static final String KEY_SETUP = "setup";
    private static final String KEY_CONNECTION = "connection";

    public PairingPort(CommunicationStrategy communicationStrategy) {
        super(communicationStrategy);
    }

    @Override
    public boolean isResponseForThisPort(String jsonResponse) {
        return false;
    }

    @Override
    public void processResponse(String jsonResponse) {
        DICommLog.d(DICommLog.PAIRINGPORT, "Pairing does not return responses.");
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

    public void triggerPairing(String clientType, String clientId, String secretKey) {
        String[] dataArray = {clientType, clientId, secretKey};
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("Pair", dataArray);
        putProperties(dataMap);
    }

    public void triggerPairing(String clientProvider, String clientType, String clientId, String secretKey, String type, String[] permissions) {
        Object[] dataArray = {clientProvider, clientType, clientId, secretKey, type, permissions};
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("Pair", dataArray);
        putProperties(dataMap);
    }
}
