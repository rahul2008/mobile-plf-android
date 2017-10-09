package com.philips.cdp2.ews.hotspotconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.philips.cdp.dicommclient.port.common.WifiPortProperties;
import com.philips.cdp2.ews.appliance.ApplianceAccessManager;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.wifi.WiFiConnectivityManager;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class ConnectingPhoneToHotspotWifiViewModel {

    public interface ConnectingPhoneToHotSpotCallback {

        void registerReceiver(@NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter);
        void unregisterReceiver(@NonNull BroadcastReceiver receiver);
    }
    private static final long DEVICE_CONNECTION_TIMEOUT = TimeUnit.SECONDS.toMillis(30);

    @NonNull private final WiFiConnectivityManager wiFiConnectivityManager;

    @NonNull private final ApplianceAccessManager applianceAccessManager;
    @NonNull private final WiFiUtil wiFiUtil;
    @NonNull private final Navigator navigator;

    @Nullable private ConnectingPhoneToHotSpotCallback callback;

    @NonNull private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (netInfo.getState() == NetworkInfo.State.CONNECTED) {
                Log.d("CONNECT", "Network connected");
                int currentWifiState = wiFiUtil.getCurrentWifiState();
                Log.d("CONNECT", "Wifi state: " + currentWifiState);
                if (currentWifiState == WiFiUtil.DEVICE_HOTSPOT_WIFI) {
                    Log.d("CONNECT", "Hotspot connected");
                    onPhoneConnectedToHotspotWifi();
                }
            } else {
                Log.d("CONNECT", "Network not connected");
            }
        }
    };

    @NonNull private final Runnable timeOutAction = new Runnable() {
        @Override
        public void run() {
            onConnectionAttemptTimedOut();
        }
    };

    @Inject
    ConnectingPhoneToHotspotWifiViewModel(@NonNull WiFiConnectivityManager wiFiConnectivityManager,
                                          @NonNull ApplianceAccessManager applianceAccessManager,
                                          @NonNull WiFiUtil wiFiUtil,
                                          @NonNull Navigator navigator) {
        this.wiFiConnectivityManager = wiFiConnectivityManager;
        this.applianceAccessManager = applianceAccessManager;
        this.wiFiUtil = wiFiUtil;
        this.navigator = navigator;
    }

    public void setCallback(@Nullable ConnectingPhoneToHotSpotCallback callback) {
        this.callback = callback;
    }

    public void connectToHotSpot() {
        // TODO add timeout
        if (callback != null) {
            callback.registerReceiver(broadcastReceiver, createIntentFilter());
        }
        wiFiConnectivityManager.connectToApplianceHotspotNetwork(WiFiUtil.DEVICE_SSID);
    }

    public void handleCancelButtonClicked() {

    }

    public void clear() {
        if (callback != null) {
            callback.unregisterReceiver(broadcastReceiver);
        }
        setCallback(null);
    }

    private void onPhoneConnectedToHotspotWifi() {
        applianceAccessManager.fetchDevicePortProperties(
                new ApplianceAccessManager.FetchCallback() {
                    @Override
                    public void onDeviceInfoReceived(WifiPortProperties properties) {
                        Log.d("CONNECT", "onDeviceInfoReceived: " + properties.getSsid());
                        navigator.navigateToConnectToDeviceWithPasswordScreen();
                    }

                    @Override
                    public void onFailedToFetchDeviceInfo() {
                        Log.d("CONNECT", "onFailedToFetchDeviceInfo");
                    }
                });
    }

    private void onConnectionAttemptTimedOut() {

    }

    private IntentFilter createIntentFilter() {
        return new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
    }
}