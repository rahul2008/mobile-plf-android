/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.DICommLog;

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

    private DevicePortProperties parseResponse(String response) {
        if (response == null || response.isEmpty()) {
            return null;
        }
        Gson gson = new GsonBuilder().create();
        DevicePortProperties devicePortInfo = null;
        try {
            devicePortInfo = gson.fromJson(response, DevicePortProperties.class);
        } catch (JsonSyntaxException e) {
            DICommLog.e(DICommLog.DEVICEPORT, "JsonSyntaxException");
        } catch (JsonIOException e) {
            DICommLog.e(DICommLog.DEVICEPORT, "JsonIOException");
        } catch (Exception e2) {
            DICommLog.e(DICommLog.DEVICEPORT, "Exception");
        }

        if (devicePortInfo != null && (
                devicePortInfo.getName() == null || devicePortInfo.getType() == null
                        || devicePortInfo.getModelid() == null || devicePortInfo.getSwversion() == null)) {
            return null;
        } else {
            return devicePortInfo;
        }
    }

    public void setDeviceName(String name) {
        putProperties(KEY_DEVICENAME, name);
    }
}
