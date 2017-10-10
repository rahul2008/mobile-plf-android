/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

import android.support.annotation.NonNull;

import com.philips.cdp.cloudcontroller.api.CloudController;
import com.philips.cdp.cloudcontroller.api.listener.DcsEventListener;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class RemoteSubscriptionHandler extends SubscriptionHandler implements DcsEventListener {

    private Set<SubscriptionEventListener> mSubscriptionEventListeners;
    private NetworkNode mNetworkNode;
    private CloudController cloudController;

    public RemoteSubscriptionHandler(CloudController cppController) {
        cloudController = cppController;
    }

    @Override
    public void enableSubscription(NetworkNode networkNode, Set<SubscriptionEventListener> subscriptionEventListener) {
        DICommLog.i(DICommLog.REMOTE_SUBSCRIPTION, "Enabling remote subscription (start dcs)");
        mNetworkNode = networkNode;
        mSubscriptionEventListeners = subscriptionEventListener;
        //DI-Comm change. Moved from Constructor
        cloudController.addDCSEventListener(networkNode.getCppId(), this);
    }

    @Override
    public void disableSubscription() {
        DICommLog.i(DICommLog.REMOTE_SUBSCRIPTION, "Disabling remote subscription (stop dcs)");
        mSubscriptionEventListeners = null;
        //DI-Comm change. Removing the listener on Disabling remote subscription
        if (mNetworkNode != null) {
            cloudController.removeDCSEventListener(mNetworkNode.getCppId());
        }
    }

    @Override
    public void onDCSEventReceived(@NonNull final String data, @NonNull final String fromEui64, @NonNull final String action) {
        DICommLog.i(DICommLog.REMOTE_SUBSCRIPTION, "onDCSEventReceived: " + data);
        if (data.isEmpty())
            return;
        if (fromEui64.isEmpty())
            return;

        if (!mNetworkNode.getCppId().equals(fromEui64)) {
            DICommLog.d(DICommLog.REMOTE_SUBSCRIPTION, "Ignoring event, not from associated network node (" + fromEui64 + ")");
            return;
        }

        DICommLog.i(DICommLog.REMOTE_SUBSCRIPTION, "DCS event received from " + fromEui64);
        DICommLog.i(DICommLog.REMOTE_SUBSCRIPTION, data);

        if (mSubscriptionEventListeners != null) {
            try {
                final String portName = getJsonValue(data, "port");
                final String payload = getJsonValue(data, "data");

                postSubscriptionEventOnUIThread(portName, payload, mSubscriptionEventListeners);
            } catch (JSONException e) {
                DICommLog.e(DICommLog.REMOTE_SUBSCRIPTION, "Error parsing DCS event data: " + e.getMessage());
            }
        }
    }

    private String getJsonValue(final String json, String jsonKey) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject dataObject = jsonObject.optJSONObject(jsonKey);

        if (dataObject == null) {
            throw new JSONException("Error, no data received: " + json);
        } else {
            return dataObject.toString();
        }
    }
}
