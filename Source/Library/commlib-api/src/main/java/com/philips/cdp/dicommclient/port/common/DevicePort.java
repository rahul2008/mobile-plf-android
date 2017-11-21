/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

/**
 * The DevicePort is a mandatory port that is available on all DIComm appliances.
 * It contains basic information about the appliance.
 *
 * @publicApi
 * @see com.philips.cdp.dicommclient.port.DICommPort
 * @see DevicePortProperties
 */
public class DevicePort extends DICommPort<DevicePortProperties> {

    private final String DEVICEPORT_NAME = "device";
    private final int DEVICEPORT_PRODUCTID = 1;

    private static final String KEY_DEVICENAME = "name";

    /**
     * @param communicationStrategy CommunicationStrategy The strategy to use during communication
     */
    public DevicePort(final @NonNull CommunicationStrategy communicationStrategy) {
        super(communicationStrategy);
    }

    @Override
    public void processResponse(String jsonResponse) {
        final DevicePortProperties devicePortInfo = parseResponse(jsonResponse);

        if (devicePortInfo == null) {
            DICommLog.e(DICommLog.DEVICEPORT, "DevicePort properties should not be null.");
        } else {
            setPortProperties(devicePortInfo);
        }
    }

    @Override
    @NonNull
    public String getDICommPortName() {
        return DEVICEPORT_NAME;
    }

    @Override
    public int getDICommProductId() {
        return DEVICEPORT_PRODUCTID;
    }

    @Override
    public boolean supportsSubscription() {
        return true;
    }

    @Nullable
    private DevicePortProperties parseResponse(String response) {
        if (response == null || response.isEmpty()) {
            return null;
        }

        try {
            return gson.fromJson(response, DevicePortProperties.class);
        } catch (JsonSyntaxException e) {
            DICommLog.e(DICommLog.DEVICEPORT, "Error parsing response: " + e.getMessage());
        }
        return null;
    }

    /**
     * Convenience method to update the appliance's name.
     * Uses #putProperties internally.
     * @param name String The new name for the appliance.
     */
    public void setDeviceName(String name) {
        putProperties(KEY_DEVICENAME, name);
    }
}
