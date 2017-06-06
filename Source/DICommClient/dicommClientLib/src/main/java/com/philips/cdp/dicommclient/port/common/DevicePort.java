/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

/**
 * The DevicePort is a mandatory port that is available on all DIComm appliances. It contains basic information about the appliance.
 *
 * @publicApi
 * @see com.philips.cdp.dicommclient.port.DICommPort
 * @see DevicePortProperties
 */
public class DevicePort extends DICommPort<DevicePortProperties> {

    private final String DEVICEPORT_NAME = "device";
    private final int DEVICEPORT_PRODUCTID = 1;

    private static final String KEY_DEVICENAME = "name";

    public DevicePort(CommunicationStrategy communicationStrategy) {
        super(communicationStrategy);
    }

    @Override
    public boolean isResponseForThisPort(String jsonResponse) {
        return (parseResponse(jsonResponse) != null);
    }

    @Override
    public void processResponse(String jsonResponse) {
        DevicePortProperties devicePortInfo = parseResponse(jsonResponse);
        if (devicePortInfo != null) {
            setPortProperties(devicePortInfo);
            return;
        }
        DICommLog.e(DICommLog.DEVICEPORT, "DevicePort Info should never be NULL");
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

        DevicePortProperties devicePortInfo = null;
        try {
            devicePortInfo = gson.fromJson(response, DevicePortProperties.class);
        } catch (Exception e) {
            DICommLog.e(DICommLog.DEVICEPORT, e.getMessage());
        }

        if (devicePortInfo != null &&
                (devicePortInfo.getName() == null
                        || devicePortInfo.getType() == null)
                ) {
            return null;
        } else {
            return devicePortInfo;
        }
    }

    public void setDeviceName(String name) {
        putProperties(KEY_DEVICENAME, name);
    }
}
