/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.port.ble;

import android.support.annotation.NonNull;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

public class BleParamsPort extends DICommPort<BleParamsPortProperties> {

    private static final String TAG = "BleParamsPort";

    private static final String BLE_PARAMS_PORT_NAME = "bleparams";
    private static final int BLE_PARAMS_PORT_PRODUCTID = 1;

    /**
     * Constructs a DICommPort instance
     *
     * @param communicationStrategy CommunicationStrategy The communication strategy for the port to use.
     */
    public BleParamsPort(@NonNull CommunicationStrategy communicationStrategy) {
        super(communicationStrategy);
    }

    @Override
    protected void processResponse(String jsonResponse) {
        final BleParamsPortProperties portProperties = parseResponse(jsonResponse);

        if (portProperties == null) {
            DICommLog.e(TAG, "BleParamsPort is null.");
        } else {
            setPortProperties(portProperties);
        }
    }

    @Override
    public String getDICommPortName() {
        return BLE_PARAMS_PORT_NAME;
    }

    @Override
    protected int getDICommProductId() {
        return BLE_PARAMS_PORT_PRODUCTID;
    }

    @Override
    public boolean supportsSubscription() {
        return false;
    }

    private BleParamsPortProperties parseResponse(String response) {
        if (response == null || response.isEmpty()) {
            return null;
        }
        BleParamsPortProperties bleParams = null;
        try {
            bleParams = gson.fromJson(response, BleParamsPortProperties.class);
        } catch (JsonSyntaxException | JsonIOException e) {
            DICommLog.e(TAG, e.getMessage());
        }
        return bleParams;
    }
}
