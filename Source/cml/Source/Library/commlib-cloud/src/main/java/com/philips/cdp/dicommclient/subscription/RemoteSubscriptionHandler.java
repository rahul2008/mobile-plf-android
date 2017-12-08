/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.cloudcontroller.api.CloudController;
import com.philips.cdp.cloudcontroller.api.listener.DcsEventListener;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class RemoteSubscriptionHandler extends SubscriptionHandler implements DcsEventListener {

    private CloudController cloudController;
    private Set<SubscriptionEventListener> subscriptionEventListeners;
    private NetworkNode networkNode;

    public RemoteSubscriptionHandler(final @NonNull CloudController cloudController) {
        this.cloudController = cloudController;
    }

    @Override
    public void enableSubscription(final @NonNull NetworkNode networkNode, final @NonNull Set<SubscriptionEventListener> subscriptionEventListeners) {
        DICommLog.i(DICommLog.REMOTE_SUBSCRIPTION, "Enabling remote subscription (start dcs)");

        this.networkNode = networkNode;
        this.subscriptionEventListeners = subscriptionEventListeners;

        cloudController.addDCSEventListener(networkNode.getCppId(), this);
    }

    @Override
    public void disableSubscription() {
        DICommLog.i(DICommLog.REMOTE_SUBSCRIPTION, "Disabling remote subscription (stop dcs)");
        this.subscriptionEventListeners = null;

        if (networkNode != null) {
            cloudController.removeDCSEventListener(networkNode.getCppId());
        }
    }

    @Override
    public void onDCSEventReceived(@NonNull final String data, @NonNull final String fromEui64, @NonNull final String action) {
        DICommLog.i(DICommLog.REMOTE_SUBSCRIPTION, "onDCSEventReceived: " + data);

        if (subscriptionEventListeners == null) {
            DICommLog.d(DICommLog.REMOTE_SUBSCRIPTION, "Ignoring event, no subscriptionsEventListeners.");
            return;
        }

        if (data.isEmpty() || fromEui64.isEmpty()) {
            return;
        }

        if (!fromEui64.equals(networkNode.getCppId())) {
            DICommLog.d(DICommLog.REMOTE_SUBSCRIPTION, "Ignoring event, not from associated network node (" + fromEui64 + ")");
            return;
        }

        DICommLog.i(DICommLog.REMOTE_SUBSCRIPTION, "DCS event received from " + fromEui64);
        DICommLog.i(DICommLog.REMOTE_SUBSCRIPTION, data);

        try {
            // TODO: Convert from org.json to Gson
            final String portName = extractPortName(data);
            final String payload = extractData(data);

            postSubscriptionEventOnUiThread(portName, payload, subscriptionEventListeners);
        } catch (JSONException e) {
            DICommLog.e(DICommLog.REMOTE_SUBSCRIPTION, "Error parsing DCS event data: " + e.getMessage());
        }
    }

    private String extractPortName(final String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        String value = jsonObject.optString("port");

        if (value == null || value.isEmpty()) {
            throw new JSONException("Error, no port name received: " + json);
        }
        return value;
    }

    @Nullable
    private String extractData(final String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject dataObject = jsonObject.optJSONObject("data");

        if(dataObject == null) {
            throw new JSONException("Error, no data received: " + json);
        }
        return dataObject.toString();
    }
}
