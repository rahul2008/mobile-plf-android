package com.philips.cdp.dicommclient.port.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.DLog;

public class DevicePort extends DICommPort<DevicePortProperties> {

    private final String DEVICEPORT_NAME = "device";
    private final int DEVICEPORT_PRODUCTID = 1;

    private static final String KEY_DEVICENAME = "name";

    public DevicePort(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
    }

    @Override
    public boolean isResponseForThisPort(String response) {
        return (parseResponse(response) == null);
    }

    @Override
    public void processResponse(String response) {
        DevicePortProperties devicePortInfo = parseResponse(response);
        if (devicePortInfo != null) {
            setPortProperties(devicePortInfo);
            return;
        }
        DLog.e(DLog.DEVICEPORT, "DevicePort Info should never be NULL");
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

    private DevicePortProperties parseResponse(String response) {
        if (response == null || response.isEmpty()) {
            return null;
        }
        Gson gson = new GsonBuilder().create();
        DevicePortProperties devicePortInfo = null;
        try {
            devicePortInfo = gson.fromJson(response, DevicePortProperties.class);
        } catch (JsonSyntaxException e) {
            DLog.e(DLog.DEVICEPORT, "JsonSyntaxException");
        } catch (JsonIOException e) {
            DLog.e(DLog.DEVICEPORT, "JsonIOException");
        } catch (Exception e2) {
            DLog.e(DLog.DEVICEPORT, "Exception");
        }
        return devicePortInfo;
    }

    public void setDeviceName(String name){
        putProperties(KEY_DEVICENAME, name);
    }
 }
