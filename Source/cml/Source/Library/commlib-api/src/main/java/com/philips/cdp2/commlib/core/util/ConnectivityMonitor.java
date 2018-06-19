/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.util.DICommLog;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ConnectivityMonitor implements Availability<ConnectivityMonitor> {

    private static final int WIFI_NETWORK_TIMEOUT_SECONDS = 3;

    protected ConnectivityManager connectivityManager;

    private Set<AvailabilityListener<ConnectivityMonitor>> availabilityListeners = new CopyOnWriteArraySet<>();

    @Nullable
    public NetworkInfo getActiveNetworkInfo() {
        return activeNetworkInfo;
    }

    private NetworkInfo activeNetworkInfo;
    private boolean isConnected;

    private final BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            boolean isCurrentlyConnected = determineIfConnected();

            if (isCurrentlyConnected != isConnected) {
                isConnected = isCurrentlyConnected;

                if (isConnected) {
                    final String networkType = activeNetworkInfo == null ? "N/A" : activeNetworkInfo.getTypeName();
                    DICommLog.i(DICommLog.NETWORKMONITOR, String.format(Locale.US, "Connected to local network [%s]", networkType));
                } else {
                    DICommLog.i(DICommLog.NETWORKMONITOR, "Not connected to local network.");
                }
                notifyConnectivityListeners();
            }
        }
    };

    /**
     * Create a connectivity monitor that notifies network changes based on network types.
     *
     * @param context      the context
     * @param networkTypes the networkCapabilities (see {@link ConnectivityManager})
     * @return the connectivity monitor
     */
    public static ConnectivityMonitor forNetworkTypes(final @NonNull Context context, final int... networkTypes) {
        if (networkTypes.length == 0) {
            throw new IllegalArgumentException("At least one network type must be provided.");
        }

        return new ConnectivityMonitor(context) {
            @Override
            protected boolean determineIfConnected() {
                final NetworkInfo activeNetworkInfo = getActiveNetworkInfo();
                if (activeNetworkInfo == null) {
                    return false;
                }

                for (int networkType : networkTypes) {
                    if (activeNetworkInfo.getType() == networkType && activeNetworkInfo.isConnected()) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    private ConnectivityMonitor(final @NonNull Context context) {
        connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        context.registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = determineIfConnected();
    }

    abstract protected boolean determineIfConnected();

    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public boolean isAvailable() {
        return isConnected;
    }

    @Override
    public void addAvailabilityListener(@NonNull AvailabilityListener<ConnectivityMonitor> listener) {
        availabilityListeners.add(listener);
        listener.onAvailabilityChanged(this);
    }

    @Override
    public void removeAvailabilityListener(@NonNull AvailabilityListener<ConnectivityMonitor> listener) {
        availabilityListeners.remove(listener);
    }

    @Nullable
    public final Network getNetwork() {
        final AtomicReference<Network> result = new AtomicReference<>(null);
        final CountDownLatch latch = new CountDownLatch(1);

        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
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

    private void notifyConnectivityListeners() {
        for (AvailabilityListener<ConnectivityMonitor> listener : availabilityListeners) {
            listener.onAvailabilityChanged(this);
        }
    }
}
