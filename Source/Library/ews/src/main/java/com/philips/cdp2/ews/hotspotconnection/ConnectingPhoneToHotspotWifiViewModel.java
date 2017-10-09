package com.philips.cdp2.ews.hotspotconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
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
        Fragment fragment();
        int requestCode();
    }
    
    private static final long DEVICE_CONNECTION_TIMEOUT = TimeUnit.SECONDS.toMillis(30);

    @NonNull private final WiFiConnectivityManager wiFiConnectivityManager;

    @NonNull private final ApplianceAccessManager applianceAccessManager;
    @NonNull private final WiFiUtil wiFiUtil;
    @NonNull private final Navigator navigator;

    @Nullable private ConnectingPhoneToHotSpotCallback fragmentCallback;

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
                                          @NonNull WiFiUtil wiFiUtil,
                                          @NonNull Navigator navigator) {
        this.wiFiConnectivityManager = wiFiConnectivityManager;
        this.applianceAccessManager = applianceAccessManager;
        this.wiFiUtil = wiFiUtil;
        this.navigator = navigator;
    }

    public void setFragmentCallback(@Nullable ConnectingPhoneToHotSpotCallback fragmentCallback) {
        this.fragmentCallback = fragmentCallback;
    }

    public void connectToHotSpot() {
        // TODO add timeout
        if (fragmentCallback != null) {
            fragmentCallback.registerReceiver(broadcastReceiver, createIntentFilter());
        }
        wiFiConnectivityManager.connectToApplianceHotspotNetwork(WiFiUtil.DEVICE_SSID);
    }

    public void handleCancelButtonClicked() {

    }

    public void onResultReceived(int result) {
        Log.d("RESULT", String.valueOf(result));
    }

    public void clear() {
        if (fragmentCallback != null) {
            fragmentCallback.unregisterReceiver(broadcastReceiver);
        }
        setFragmentCallback(null);
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
                        showUnsuccessfulDialog();
                    }
                });
    }

    private void showUnsuccessfulDialog() {
        if (fragmentCallback != null) {
            navigator.navigateToUnsuccessfulConnectionDialog(fragmentCallback.fragment(),
                    fragmentCallback.requestCode());
        }
    }

    private void onConnectionAttemptTimedOut() {
        showUnsuccessfulDialog();
    }

    private IntentFilter createIntentFilter() {
        return new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    @Nullable
    ConnectingPhoneToHotSpotCallback getFragmentCallback() {
        return fragmentCallback;
    }
}