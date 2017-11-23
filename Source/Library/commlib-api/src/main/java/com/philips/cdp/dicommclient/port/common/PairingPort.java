/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.PortProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * The PairingPort is an extension to {@link DICommPort} with the added functionality of pairing.
 * @param <P> Type of PortProperties
 * @publicApi
 */
public class PairingPort<P extends PortProperties> extends DICommPort<P> {

    private final String PAIRINGPORT_NAME = "pairing";
    private final int PAIRINGPORT_PRODUCTID = 0;

    private static final String KEY_SETUP = "setup";
    private static final String KEY_CONNECTION = "connection";

    /**
     * Instantiates a PairingPort object
     * @param communicationStrategy CommunicationStrategy The strategy to use for communication during pairing.
     */
    public PairingPort(final @NonNull CommunicationStrategy communicationStrategy) {
        super(communicationStrategy);
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

    /**
     * Disables demo mode.
     * Calls #putProperties internally.
     */
    public void disableDemoMode() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put(KEY_SETUP, "inactive");
        dataMap.put(KEY_CONNECTION, "disconnected");
        putProperties(dataMap);
    }

    /**
     *
     * @param clientType String The type of client to trigger pairing for
     * @param clientId String The ID of the client triggering pairing
     * @param secretKey String A secret key
     */
    public void triggerPairing(String clientType, String clientId, String secretKey) {
        String[] dataArray = {clientType, clientId, secretKey};
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("Pair", dataArray);
        putProperties(dataMap);
    }

    /**
     *
     * @param clientProvider String The entity to provide the client to pair with
     * @param clientType String The type of client who wants to start pairing
     * @param clientId String An ID to indicate who the client is
     * @param secretKey String A secret key
     * @param type String The type of pairing the client wants to perform
     * @param permissions String[] The kind of permissions to set up a pairing relation for
     */
    public void triggerPairing(String clientProvider, String clientType, String clientId, String secretKey, String type, String[] permissions) {
        Object[] dataArray = {clientProvider, clientType, clientId, secretKey, type, permissions};
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("Pair", dataArray);
        putProperties(dataMap);
    }
}
