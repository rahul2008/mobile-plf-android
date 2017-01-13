package com.philips.platform.appframework.connectivity.appliance;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

/**
 * Created by philips on 13/01/17.
 */

public class DeviceMeasurementPort extends DICommPort<DeviceMeasurementPortProperties> {
    private static final String TAG = "TimePort";

    private static final String DEVICE_MEASUREMENT_PORT_NAME = "devicemeasurement";
    private static final int DEVICE_MEAUREMENT_PRODUCT_ID = 1;

    public DeviceMeasurementPort(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
    }

    @Override
    public boolean isResponseForThisPort(String jsonResponse) {
        return (parseResponse(jsonResponse) != null);
    }

    @Override
    protected void processResponse(String jsonResponse) {
        DeviceMeasurementPortProperties deviceMeasurementPortProperties = parseResponse(jsonResponse);
        if (deviceMeasurementPortProperties != null) {
            setPortProperties(deviceMeasurementPortProperties);
            return;
        }
        Log.e(TAG, "TimePort is null.");
    }

    @Override
    protected String getDICommPortName() {
        return DEVICE_MEASUREMENT_PORT_NAME;
    }

    @Override
    protected int getDICommProductId() {
        return DEVICE_MEAUREMENT_PRODUCT_ID;
    }

    @Override
    public boolean supportsSubscription() {
        return false;
    }

    private DeviceMeasurementPortProperties parseResponse(String response) {
        if (response == null || response.isEmpty()) {
            return null;
        }
        Gson gson = new GsonBuilder().create();
        DeviceMeasurementPortProperties deviceMeasurementPortProperties = null;
        try {
            deviceMeasurementPortProperties = gson.fromJson(response, DeviceMeasurementPortProperties.class);
        } catch (JsonSyntaxException | JsonIOException e) {
            Log.e(TAG, e.getMessage());
        }

        return deviceMeasurementPortProperties;
    }
}
