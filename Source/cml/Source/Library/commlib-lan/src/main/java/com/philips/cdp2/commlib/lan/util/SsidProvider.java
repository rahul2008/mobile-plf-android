/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class SsidProvider {

    public interface NetworkChangeListener {
        void onNetworkChanged();
    }

    private final WifiManager wifiManager;
    private String currentSsid;

    private Set<NetworkChangeListener> networkChangeListeners = new CopyOnWriteArraySet<>();

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String newSsid = getCurrentSsid();

            if (!Objects.equals(newSsid, currentSsid)) {
                currentSsid = newSsid;
                notifyListeners();
            }
        }
    };

    public SsidProvider(final @NonNull Context context) {
        this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        this.currentSsid = getCurrentSsid();
        context.registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Nullable
    public String getCurrentSsid() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if (wifiInfo == null) {
            return null;
        } else if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            return wifiInfo.getSSID();
        }
        return null;
    }

    public void addNetworkChangeListener(@NonNull NetworkChangeListener listener) {
        networkChangeListeners.add(listener);
        listener.onNetworkChanged();
    }

    public void removeNetworkChangeListener(@NonNull NetworkChangeListener listener) {
        networkChangeListeners.remove(listener);
    }

    private void notifyListeners() {
        for (NetworkChangeListener listener : networkChangeListeners) {
            listener.onNetworkChanged();
        }
    }
}