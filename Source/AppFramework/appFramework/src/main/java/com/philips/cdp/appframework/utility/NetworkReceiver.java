/**
 * Network status Receiver class. it responsible for getting the status of the connectivity across the device.
 *
 * @author naveen@philips.com
 * <p/>
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.appframework.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkReceiver extends BroadcastReceiver {

    private final String TAG = NetworkReceiver.class.getSimpleName();
    private NetworkStateListener mNetworkCallback = null;

    public NetworkReceiver() {
    }

    public NetworkReceiver(NetworkStateListener callback) {
        this();
        mNetworkCallback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager mConnectionManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectionManager != null) {
            NetworkInfo mActiveNetwork = mConnectionManager
                    .getActiveNetworkInfo();
            if (mActiveNetwork != null) {
                Logger.v(TAG, "Connection Available");
                if (mNetworkCallback != null)
                    mNetworkCallback.onNetworkStateChanged(true);
            } else {
                Logger.v(TAG, "Connection Not Available");

                if (mNetworkCallback != null)
                    mNetworkCallback.onNetworkStateChanged(false);
            }
        }
    }
}
