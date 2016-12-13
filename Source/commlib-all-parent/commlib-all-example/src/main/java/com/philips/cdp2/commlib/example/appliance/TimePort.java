/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.example.appliance;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;

public class TimePort extends DICommPort<TimePortProperties> {

    private static final String TAG = "TimePort";

    private static final String TIME_PORT_NAME = "time";
    private static final int TIME_PORT_PRODUCTID = 0;

    public TimePort(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
    }

    @Override
    public boolean isResponseForThisPort(String jsonResponse) {
        return (parseResponse(jsonResponse) != null);
    }

    @Override
    protected void processResponse(String jsonResponse) {
        TimePortProperties timePortProperties = parseResponse(jsonResponse);
        if (timePortProperties != null) {
            setPortProperties(timePortProperties);
            return;
        }
        Log.e(TAG, "TimePort is null.");
    }

    @Override
    protected String getDICommPortName() {
        return TIME_PORT_NAME;
    }

    @Override
    protected int getDICommProductId() {
        return TIME_PORT_PRODUCTID;
    }

    @Override
    public boolean supportsSubscription() {
        return false;
    }

    private TimePortProperties parseResponse(String response) {
        if (response == null || response.isEmpty()) {
            return null;
        }
        Gson gson = new GsonBuilder().create();
        TimePortProperties timePortInfo = null;
        try {
            timePortInfo = gson.fromJson(response, TimePortProperties.class);
        } catch (JsonSyntaxException | JsonIOException e) {
            Log.e(TAG, e.getMessage());
        }

        return timePortInfo;
    }
}
