/*
 * (C) Koninklijke Philips N.V., 2018.
 * All rights reserved.
 */
package com.philips.cdp.dicommclient.port.common;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import java.util.Collection;

public class WiFiNetworksPort extends DICommPort<WiFiNetworksPortProperties> {

    private static final String TAG = "WiFiNetworksPort";
    private static final int PRODUCT_ID = 0;
    private static final String PORT_NAME = "wifi/networks";

    private final Gson jsonParser;

    public WiFiNetworksPort(@NonNull final CommunicationStrategy communicationStrategy) {
        super(communicationStrategy);
        jsonParser = new GsonBuilder().registerTypeHierarchyAdapter(Collection.class,
                new WiFiNetworkPortResponseDeserializer()).
                create();
    }

    @Override
    public void processResponse(String jsonResponse) {
        WiFiNetworksPortProperties properties = parseProperties(jsonResponse);
        setPortProperties(properties);
    }

    private WiFiNetworksPortProperties parseProperties(String jsonResponse) {
        try {
            return jsonParser.fromJson(jsonResponse, WiFiNetworksPortProperties.class);
        } catch (JsonSyntaxException exception) {
            DICommLog.e(TAG, "Unable to parse properties:" + exception);
            return null;
        }
    }

    @Override
    public String getDICommPortName() {
        return PORT_NAME;
    }

    @Override
    public int getDICommProductId() {
        return PRODUCT_ID;
    }

    @Override
    public boolean supportsSubscription() {
        return false;
    }
}
