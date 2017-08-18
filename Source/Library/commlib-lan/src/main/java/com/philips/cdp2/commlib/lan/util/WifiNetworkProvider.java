/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.util.DICommLog;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class WifiNetworkProvider {

    private static final int WIFI_NETWORK_TIMEOUT_SECONDS = 3;

    private final ConnectivityManager connectivityManager;
    private static WifiNetworkProvider instance;
    private final WifiManager wifiManager;

    public static WifiNetworkProvider get(final @NonNull Context context) {
        if (instance == null) {
            instance = new WifiNetworkProvider(context);
        }
        return instance;
    }

    private WifiNetworkProvider(final @NonNull Context context) {
        this.connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    @Nullable
    public final Network getNetwork() {
        final AtomicReference<Network> result = new AtomicReference<>(null);
        final CountDownLatch latch = new CountDownLatch(1);

        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);

        NetworkCallback networkCallback = new NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                final NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);

                if (networkInfo.isConnected()) {
                    DICommLog.i(DICommLog.WIFI, "Wifi network available.");
                    result.set(network);
                }
                latch.countDown();
            }

            @Override
            public void onLost(Network network) {
                DICommLog.i(DICommLog.WIFI, "Wifi network lost.");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager.bindProcessToNetwork(null);
                } else {
                    ConnectivityManager.setProcessDefaultNetwork(null);
                }
            }
        };
        connectivityManager.registerNetworkCallback(builder.build(), networkCallback);

        try {
            DICommLog.i(DICommLog.WIFI, "Waiting max 3 seconds for Wifi network to become available.");
            latch.await(WIFI_NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            DICommLog.e(DICommLog.WIFI, "Interrupted while waiting for Wifi network to become available.");
        } finally {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
        return result.get();
    }

    public WifiInfo getWifiInfo() {
        return wifiManager.getConnectionInfo();
    }
}
