
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.utils;

import android.content.*;
import android.net.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtility {

    private Context context;

    private final String TAG = "NetworkUtility";

    public NetworkUtility(Context context) {
        this.context = context;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnected() && activeNetwork.isAvailable());
        RLog.d(TAG, "isNetworkAvailable "+isConnected);
        return isConnected;
    }

    public void registerNetworkListener(BroadcastReceiver broadcastReceiver) {
        context.registerReceiver(broadcastReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void unRegisterNetworkListener(BroadcastReceiver broadcastReceiver) {
        context.unregisterReceiver(broadcastReceiver);
    }

    public boolean isInternetAvailable() {
        try {
            final InetAddress address = InetAddress.getByName("www.philips.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            RLog.d(TAG, "Error in checking internet connection.");
        }
        return false;
    }
}
