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
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.util.DICommLog;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class ConnectivityMonitor {

    protected ConnectivityManager connectivityManager;

    private Set<ConnectivityListener> connectivityListeners = new CopyOnWriteArraySet<>();

    @Nullable
    public NetworkInfo getActiveNetworkInfo() {
        return activeNetworkInfo;
    }

    public interface ConnectivityListener {
        void onConnectivityChanged(boolean isConnected);
    }

    private NetworkInfo activeNetworkInfo;
    private boolean isConnected;

    private final BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            boolean isCurrentlyConnected = isConnected();

            if (isCurrentlyConnected != isConnected) {
                isConnected = isCurrentlyConnected;
                DICommLog.i(DICommLog.NETWORKMONITOR, "Connected via network: " + getActiveNetworkInfo().getTypeName());

                notifyConnectivityListeners(isConnected);
            }
        }
    };

    /**
     * Create a connectivity monitor that notifies network changes based on network capabilities.
     *
     * @param context             the context
     * @param networkCapabilities the networkCapabilities (see {@link android.net.NetworkCapabilities})
     * @return the connectivity monitor
     */
    public static ConnectivityMonitor forNetworkCapabilities(final @NonNull Context context, final int... networkCapabilities) {
        if (networkCapabilities.length == 0) {
            throw new IllegalArgumentException("At least one capability must be provided.");
        }

        return new ConnectivityMonitor(context) {
            @Override
            protected boolean isConnected() {
                Network network = getActiveNetwork();
                for (int capability : networkCapabilities) {
                    if (connectivityManager.getNetworkCapabilities(network).hasCapability(capability)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

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
            protected boolean isConnected() {
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

    @VisibleForTesting
    ConnectivityMonitor(final @NonNull Context context) {
        connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        context.registerReceiver(connectivityReceiver, createFilter());
    }

    abstract protected boolean isConnected();

    @VisibleForTesting
    IntentFilter createFilter() {
        return new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    @Nullable
    protected Network getActiveNetwork() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return connectivityManager.getActiveNetwork();
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        for (Network network : connectivityManager.getAllNetworks()) {
            if (connectivityManager.getNetworkInfo(network).equals(activeNetworkInfo)) {
                return network;
            }
        }
        return null;
    }

    /**
     * Add connectivity listener.
     *
     * @param connectivityListener the connectivity listener to add. This listener will be notified immediately when added.
     */
    public void addConnectivityListener(final @NonNull ConnectivityListener connectivityListener) {
        connectivityListeners.add(connectivityListener);
        connectivityListener.onConnectivityChanged(isConnected);
    }

    /**
     * Remove connectivity listener.
     *
     * @param connectivityListener the connectivity listener to remove.
     */
    public void removeConnectivityListener(final @NonNull ConnectivityListener connectivityListener) {
        connectivityListeners.remove(connectivityListener);
    }

    private void notifyConnectivityListeners(final boolean isConnected) {
        for (ConnectivityListener listener : connectivityListeners) {
            listener.onConnectivityChanged(isConnected);
        }
    }
}
