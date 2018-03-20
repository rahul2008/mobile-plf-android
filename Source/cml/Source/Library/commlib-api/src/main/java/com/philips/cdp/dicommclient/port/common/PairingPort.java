/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
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
 *
 * @param <P> Type of PortProperties
 * @publicApi
 */
public class PairingPort<P extends PortProperties> extends DICommPort<P> {

    private static final String PAIRINGPORT_NAME = "pairing";
    private static final int PAIRINGPORT_PRODUCTID = 0;

    private static final String KEY_SETUP = "setup";
    private static final String KEY_CONNECTION = "connection";

    static final String METHOD_PAIR = "Pair";
    static final String METHOD_UNPAIR = "Unpair";

    /**
     * Instantiates a PairingPort object
     *
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
     *
     * @deprecated
     */
    @Deprecated
    public void disableDemoMode() {
        final Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(KEY_SETUP, "inactive");
        dataMap.put(KEY_CONNECTION, "disconnected");

        putProperties(dataMap);
    }

    /**
     * Trigger pairing using the default client provider as configured by the backend.
     *
     * @param clientType String The type of client to trigger pairing for
     * @param clientId   String The ID of the client triggering pairing
     * @param secretKey  String A secret key
     * @deprecated Use {@link #pair(String, String, String)} instead.
     */
    @Deprecated
    public void triggerPairing(String clientType, String clientId, String secretKey) {
        final String[] dataArray = {clientType, clientId, secretKey};
        final Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(METHOD_PAIR, dataArray);

        putProperties(dataMap);
    }

    /**
     * Trigger pairing using a custom client provider, custom relation type and permissions.
     *
     * @param clientProvider String The entity to provide the client to pair with
     * @param clientType     String The type of client who wants to start pairing
     * @param clientId       String An ID to indicate who the client is
     * @param secretKey      String A secret key
     * @param type           String The type of pairing the client wants to perform
     * @param permissions    String[] The kind of permissions to set up a pairing relation for
     * @deprecated Use {@link #pair(String, String, String, String, String, String[])} instead.
     */
    @Deprecated
    public void triggerPairing(String clientProvider, String clientType, String clientId, String secretKey, String type, String[] permissions) {
        final Object[] dataArray = {clientProvider, clientType, clientId, secretKey, type, permissions};
        final Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(METHOD_PAIR, dataArray);

        putProperties(dataMap);
    }

    /**
     * Create a pairing relation using the default client provider as configured by the backend.
     *
     * @param clientType String The type of client who wants to start pairing
     * @param clientId   String An ID to indicate who the client is
     * @param secretKey  String A secret key
     */
    public void pair(String clientType, String clientId, String secretKey) {
        triggerPairing(clientType, clientId, secretKey);
    }

    /**
     * Create a pairing relation using a custom client provider, custom relation type and permissions.
     *
     * @param clientProvider String The entity to provide the client to pair with
     * @param clientType     String The type of client who wants to start pairing
     * @param clientId       String An ID to indicate who the client is
     * @param secretKey      String A secret key
     * @param type           String The type of pairing the client wants to perform
     * @param permissions    String[] The kind of permissions to set up a pairing relation for
     * @see #unpair(String, String, String, String)
     */
    public void pair(String clientProvider, String clientType, String clientId, String secretKey, String type, String[] permissions) {
        triggerPairing(clientProvider, clientType, clientId, secretKey, type, permissions);
    }

    /**
     * Remove pairing relation for a custom client provider and custom relation type.
     *
     * @param clientProvider String The entity to provide the client to pair with
     * @param clientType     String The type of client who wants to start pairing
     * @param clientId       String An ID to indicate who the client is
     * @param type           String The type of pairing the client wants to perform
     * @see #pair(String, String, String, String, String, String[])
     */
    public void unpair(String clientProvider, String clientType, String clientId, String type) {
        final Object[] dataArray = {clientProvider, clientType, clientId, type};
        final Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(METHOD_UNPAIR, dataArray);

        putProperties(dataMap);
    }
}
