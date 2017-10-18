/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.communication;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.annotations.NetworkType;
import com.philips.cdp2.ews.communication.events.NetworkConnectEvent;
import com.philips.cdp2.ews.wifi.WiFiConnectivityManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;
import javax.inject.Named;

@SuppressWarnings({"WeakerAccess"})
public class WiFiEventMonitor extends EventMonitor {

    @NonNull
    private final WiFiConnectivityManager wiFiConnectivityManager;

    @Inject
    public WiFiEventMonitor(@NonNull final WiFiConnectivityManager wiFiConnectivityManager,
                            @NonNull final @Named("ews.event.bus") EventBus eventBus) {
        super(eventBus);
        this.wiFiConnectivityManager = wiFiConnectivityManager;
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
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