package com.philips.cdp2.ews.hotspotconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.port.common.WifiPortProperties;
import com.philips.cdp2.ews.appliance.ApplianceAccessManager;
import com.philips.cdp2.ews.wifi.WiFiConnectivityManager;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class ConnectingPhoneToHotspotWifiViewModel implements ApplianceAccessManager.FetchCallback {

    private static final long DEVICE_CONNECTION_TIMEOUT = TimeUnit.SECONDS.toMillis(30);

    @NonNull private final WiFiConnectivityManager wiFiConnectivityManager;
    @NonNull private final ApplianceAccessManager applianceAccessManager;
    @NonNull private final WiFiUtil wiFiUtil;

    @NonNull private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (netInfo.getState() == NetworkInfo.State.CONNECTED) {
                int currentWifiState = wiFiUtil.getCurrentWifiState();
                if (currentWifiState == WiFiUtil.DEVICE_HOTSPOT_WIFI) {
                    onPhoneConnectedToHotspotWifi();
                }
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
                                          @NonNull WiFiUtil wiFiUtil) {
        this.wiFiConnectivityManager = wiFiConnectivityManager;
        this.applianceAccessManager = applianceAccessManager;
        this.wiFiUtil = wiFiUtil;
    }

    public void connectToHotSpot() {
        // TODO add timeout
        // TODO register receiver and figure out when to unregister it
        wiFiConnectivityManager.connectToApplianceHotspotNetwork(WiFiUtil.DEVICE_SSID);
    }

    public void handleCancelButtonClicked() {

    }

    private void onPhoneConnectedToHotspotWifi() {
        applianceAccessManager.fetchDevicePortProperties(this);
    }

    @Override
    public void onDeviceInfoReceived(WifiPortProperties properties) {

    }

    @Override
    public void onFailedToFetchDeviceInfo() {

    }

    private void onConnectionAttemptTimedOut() {

    }
}