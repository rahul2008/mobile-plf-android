/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.cpp.listener.DcsEventListener;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;

import org.json.JSONException;
import org.json.JSONObject;

public class RemoteSubscriptionHandler extends SubscriptionHandler implements DcsEventListener {

    private SubscriptionEventListener mSubscriptionEventListener;
    private NetworkNode mNetworkNode;
    private CppController mCppController;

    public RemoteSubscriptionHandler(CppController cppController) {
        mCppController = cppController;
    }

    @Override
    public void enableSubscription(NetworkNode networkNode, SubscriptionEventListener subscriptionEventListener) {
        DICommLog.i(DICommLog.REMOTE_SUBSCRIPTION, "Enabling remote subscription (start dcs)");
        mNetworkNode = networkNode;
        mSubscriptionEventListener = subscriptionEventListener;
        //DI-Comm change. Moved from Constructor
        mCppController.addDCSEventListener(networkNode.getCppId(), this);
    }

    @Override
    public void disableSubscription() {
        DICommLog.i(DICommLog.REMOTE_SUBSCRIPTION, "Disabling remote subscription (stop dcs)");
        mSubscriptionEventListener = null;
        //DI-Comm change. Removing the listener on Disabling remote subscription
        if (mNetworkNode != null) {
            mCppController.removeDCSEventListener(mNetworkNode.getCppId());
        }
    }

    @Override
    public void onDCSEventReceived(String data, String fromEui64, String action) {
        DICommLog.i(DICommLog.REMOTE_SUBSCRIPTION, "onDCSEventReceived: " + data);
        if (data == null || data.isEmpty())
            return;

        if (fromEui64 == null || fromEui64.isEmpty())
            return;

        if (!mNetworkNode.getCppId().equals(fromEui64)) {
            DICommLog.d(DICommLog.REMOTE_SUBSCRIPTION, "Ignoring event, not from associated network node (" + fromEui64 + ")");
            return;
        }

        DICommLog.i(DICommLog.REMOTE_SUBSCRIPTION, "DCS event received from " + fromEui64);
        DICommLog.i(DICommLog.REMOTE_SUBSCRIPTION, data);

        if (mSubscriptionEventListener != null) {
            postSubscriptionEventOnUIThread(extractData(data), mSubscriptionEventListener);
        }
    }

    private String extractData(final String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject dataObject = jsonObject.optJSONObject("data");

            if (dataObject == null) {
                return "Error, no data received: " + data;
            } else {
                return dataObject.toString();
            }
        } catch (JSONException e) {
            DICommLog.i(DICommLog.REMOTEREQUEST, "JSONException: " + e.getMessage());
            return "Error, JSONException:" + e.getMessage();
        }
    }
}