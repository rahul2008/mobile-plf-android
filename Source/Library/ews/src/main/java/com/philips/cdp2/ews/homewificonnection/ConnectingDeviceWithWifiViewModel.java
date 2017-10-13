/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.homewificonnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.ews.appliance.ApplianceAccessManager;
import com.philips.cdp2.ews.communication.DiscoveryHelper;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.wifi.WiFiConnectivityManager;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import javax.inject.Inject;
import javax.inject.Named;

import static com.philips.cdp2.ews.tagging.Tag.KEY.PRODUCT_NAME;
import static com.philips.cdp2.ews.wifi.WiFiUtil.UNKNOWN_WIFI;

public class ConnectingDeviceWithWifiViewModel {

    public interface ConnectingDeviceToWifiCallback {
        void registerReceiver(@NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter);
        void unregisterReceiver(@NonNull BroadcastReceiver receiver);
        Bundle getBundle();
        void showCancelDialog();
    }

    private static final String TAG = ConnectingDeviceWithWifiViewModel.class.getCanonicalName();
    private static final int WIFI_SET_PROPERTIES_TIME_OUT = 60000;

    @NonNull private final ApplianceAccessManager applianceAccessManager;
    @NonNull private final Navigator navigator;
    @NonNull private final WiFiConnectivityManager wiFiConnectivityManager;
    @NonNull private final WiFiUtil wiFiUtil;
    @NonNull private final Handler handler;
    @NonNull private final DiscoveryHelper discoveryHelper;

    @Nullable private ConnectingDeviceToWifiCallback fragmentCallback;
    @Nullable private String deviceName;

    @NonNull private DiscoveryHelper.DiscoveryCallback discoveryCallback = new DiscoveryHelper.DiscoveryCallback() {
        @Override
        public void onApplianceFound(Appliance appliance) {
            removeTimeoutRunnable();
            discoveryHelper.stopDiscovery();
            onDeviceConnectedToWifi();
        }
    };

    @NonNull private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (netInfo.getState() == NetworkInfo.State.CONNECTED) {
                int currentWifiState = wiFiUtil.getCurrentWifiState();
                if (currentWifiState == WiFiUtil.HOME_WIFI) {
                    unregisterBroadcastReceiver();
                    discoveryHelper.startDiscovery(discoveryCallback);
                } else if (currentWifiState != UNKNOWN_WIFI) {
                    unregisterBroadcastReceiver();
                    removeTimeoutRunnable();
                    navigator.navigateToWIFIConnectionUnsuccessfulTroubleShootingScreen(deviceName);
                }
            }
        }
    };

    @NonNull private final Runnable timeoutRunnable = new Runnable() {
        @Override
        public void run() {
            showConnectionUnsuccessful();
            clear();
        }
    };

    @Inject
    public ConnectingDeviceWithWifiViewModel(@NonNull ApplianceAccessManager applianceAccessManager,
                                             @NonNull Navigator navigator,
                                             @NonNull WiFiConnectivityManager wiFiConnectivityManager,
                                             @NonNull WiFiUtil wiFiUtil,
                                             @NonNull @Named("mainLooperHandler") Handler handler,
                                             @NonNull DiscoveryHelper discoveryHelper) {
        this.applianceAccessManager = applianceAccessManager;
        this.navigator = navigator;
        this.wiFiConnectivityManager = wiFiConnectivityManager;
        this.wiFiUtil = wiFiUtil;
        this.handler = handler;
        this.discoveryHelper = discoveryHelper;
    }

    public void setFragmentCallback(@Nullable ConnectingDeviceToWifiCallback fragmentCallback) {
        this.fragmentCallback = fragmentCallback;
    }

    public void startConnecting(@NonNull final String homeWiFiSSID, @NonNull String homeWiFiPassword, @NonNull String deviceName) {
        this.deviceName = deviceName;
        tagConnectionStart();
        applianceAccessManager.connectApplianceToHomeWiFiEvent(homeWiFiSSID, homeWiFiPassword, new ApplianceAccessManager.SetPropertiesCallback() {
            @Override
            public void onPropertiesSet() {
                connectToHotSpot(homeWiFiSSID);
            }

            @Override
            public void onFailedToSetProperties() {
                removeTimeoutRunnable();
                showConnectionUnsuccessful();
            }
        });
        handler.postDelayed(timeoutRunnable, WIFI_SET_PROPERTIES_TIME_OUT);
    }

    public void clear() {
        removeTimeoutRunnable();
        discoveryHelper.stopDiscovery();
        unregisterBroadcastReceiver();
        fragmentCallback = null;
    }

    public void onCancelButtonClicked() {
        if (fragmentCallback != null) {
            fragmentCallback.showCancelDialog();
        }
    }

    private void onDeviceConnectedToWifi() {
        navigator.navigateToEWSWiFiPairedScreen();
    }

    private void tagConnectionStart() {
        EWSTagger.startTimedAction(Tag.ACTION.TIME_TO_CONNECT);
        EWSTagger.trackAction(Tag.ACTION.CONNECTION_START, PRODUCT_NAME, EWSDependencyProvider.getInstance().getProductName());
    }

    private void showConnectionUnsuccessful() {
        if (fragmentCallback != null) {
            navigator.navigateToWrongWifiNetworkScreen(fragmentCallback.getBundle());
        }
    }

    private void removeTimeoutRunnable() {
        handler.removeCallbacks(timeoutRunnable);
    }

    public void connectToHotSpot(String homeWiFiSSID) {
        if (fragmentCallback != null) {
            fragmentCallback.registerReceiver(broadcastReceiver, createIntentFilter());
        }
        wiFiConnectivityManager.connectToHomeWiFiNetwork(homeWiFiSSID);
    }

    private IntentFilter createIntentFilter() {
        return new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
    }

    private void unregisterBroadcastReceiver() {
        if (fragmentCallback != null) {
            fragmentCallback.unregisterReceiver(broadcastReceiver);
        }
    }
}
