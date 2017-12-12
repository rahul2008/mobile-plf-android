/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.devicepair.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

public class NetworkChangeListener extends BroadcastReceiver {
    protected List<INetworkChangeListener> mListeners;
    protected boolean mIsConnected;

    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getExtras() == null)
            return;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            mIsConnected = true;
        } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            mIsConnected = false;
        }

        notifyAllListener();
    }

    private void notifyAllListener() {
        for (INetworkChangeListener listener : mListeners) {
            notifyState(listener);
        }
    }

    private void notifyState(INetworkChangeListener listener) {
        if (!mIsConnected)
            listener.onConnectionLost();
        else
            listener.onConnectionAvailable();
    }

    public void addListener(INetworkChangeListener networkChangeListener) {
        if (mListeners == null)
            mListeners = new ArrayList<>();
        mListeners.add(networkChangeListener);
    }

    public void removeListener(INetworkChangeListener networkChangeListener) {
        if (mListeners != null)
            mListeners.remove(networkChangeListener);
    }

    public interface INetworkChangeListener {
        void onConnectionLost();

        void onConnectionAvailable();
    }
}