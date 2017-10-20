/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.port.brighteyes;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import java.util.Collections;

import static com.philips.cdp2.demouapp.port.brighteyes.WakeUpAlarmPortProperties.KEY_ENABLED;

public class WakeUpAlarmPort extends DICommPort<WakeUpAlarmPortProperties> {

    private static final String TAG = "WakeUpAlarmPort";

    static final String NAME = "wualm/prfwu";
    static final int PRODUCTID = 1;

    public WakeUpAlarmPort(@NonNull CommunicationStrategy communicationStrategy) {
        super(communicationStrategy);
    }

    @Override
    protected void processResponse(String jsonResponse) {
        final WakeUpAlarmPortProperties portProperties = parseResponse(jsonResponse);

        if (portProperties == null) {
            DICommLog.e(TAG, "WakeUpAlarmPort properties should not be null.");
        } else {
            setPortProperties(portProperties);
        }
    }

    @Override
    public String getDICommPortName() {
        return NAME;
    }

    @Override
    protected int getDICommProductId() {
        return PRODUCTID;
    }

    @Override
    public boolean supportsSubscription() {
        return true;
    }

    public void setEnabled(boolean isEnabled) {
        putProperties(Collections.<String, Object>singletonMap(KEY_ENABLED, isEnabled));
    }

    @Nullable
    private WakeUpAlarmPortProperties parseResponse(String response) {
        if (response == null || response.isEmpty()) {
            return null;
        }

        try {
            return gson.fromJson(response, WakeUpAlarmPortProperties.class);
        } catch (JsonSyntaxException e) {
            DICommLog.e(TAG, "Error parsing response: " + e.getMessage());
        }
        return null;
    }
}
