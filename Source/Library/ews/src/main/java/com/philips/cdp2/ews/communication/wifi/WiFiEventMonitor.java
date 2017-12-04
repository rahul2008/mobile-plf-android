/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.communication.wifi;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.annotations.NetworkType;
import com.philips.cdp2.ews.wifi.WiFiConnectivityManager;

import javax.inject.Inject;

@SuppressWarnings({"WeakerAccess"})
public class WiFiEventMonitor {

    @NonNull
    private final WiFiConnectivityManager wiFiConnectivityManager;

    @Inject
    public WiFiEventMonitor(@NonNull final WiFiConnectivityManager wiFiConnectivityManager) {
        this.wiFiConnectivityManager = wiFiConnectivityManager;
    }

    public void connectToNetwork(@NonNull final NetworkConnectEvent event) {
        switch (event.getNetworkType()) {
            case NetworkType.DEVICE_HOTSPOT:
                wiFiConnectivityManager.connectToApplianceHotspotNetwork(event.getNetworkSSID());
                break;
            case NetworkType.HOME_WIFI:
                wiFiConnectivityManager.connectToHomeWiFiNetwork(event.getNetworkSSID());
                break;
        }
    }
}