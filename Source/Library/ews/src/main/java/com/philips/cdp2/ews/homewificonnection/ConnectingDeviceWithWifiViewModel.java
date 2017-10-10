package com.philips.cdp2.ews.homewificonnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp2.ews.appliance.ApplianceAccessManager;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.wifi.WiFiConnectivityManager;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import javax.inject.Inject;

import static com.philips.cdp2.ews.tagging.Tag.KEY.PRODUCT_NAME;

/**
 * Created by salvatorelafiura on 10/10/2017.
 */

public class ConnectingDeviceWithWifiViewModel {

    public interface ConnectingDeviceToWifiCallback {
        void registerReceiver(@NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter);

        void unregisterReceiver(@NonNull BroadcastReceiver receiver);
    }

    static final int WIFI_SET_PROPERTIES_TIME_OUT = 60000;

    @NonNull
    private final ApplianceAccessManager applianceAccessManager;
    @NonNull
    private final Navigator navigator;
    @NonNull
    private final WiFiConnectivityManager wiFiConnectivityManager;
    @Nullable
    private ConnectingDeviceToWifiCallback fragmentCallback;
    @NonNull
    private final WiFiUtil wiFiUtil;


    @NonNull
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (netInfo.getState() == NetworkInfo.State.CONNECTED) {
                int currentWifiState = wiFiUtil.getCurrentWifiState();
                if (currentWifiState == WiFiUtil.DEVICE_HOTSPOT_WIFI) {
                    onDeviceConnectedToWifi();
                }
            }
        }
    };

    private Handler handler;
    Runnable timeoutRunnable = new Runnable() {
        @Override
        public void run() {
            showConnectionUnsuccessful();
        }
    };

    @Inject
    public ConnectingDeviceWithWifiViewModel(@NonNull ApplianceAccessManager applianceAccessManager, @NonNull Navigator navigator, @NonNull WiFiConnectivityManager wiFiConnectivityManager, @NonNull WiFiUtil wiFiUtil) {
        this.applianceAccessManager = applianceAccessManager;
        this.navigator = navigator;
        this.wiFiConnectivityManager = wiFiConnectivityManager;
        this.wiFiUtil = wiFiUtil;
    }


    public void setFragmentCallback(@Nullable ConnectingDeviceToWifiCallback fragmentCallback) {
        this.fragmentCallback = fragmentCallback;
    }

    public void startConnecting(@NonNull final String homeWiFiSSID, @NonNull String homeWiFiPassword) {
        tagConnectionStart();
        applianceAccessManager.connectApplianceToHomeWiFiEvent(homeWiFiSSID, homeWiFiPassword, new ApplianceAccessManager.SetPropertiesCallback() {
            @Override
            public void onPropertiesSet() {
                connectToHotSpot(homeWiFiSSID);
            }

            @Override
            public void onFailedToSetProperties() {
                //TODO implement logic to switch between the two errors and navigate to the proper fragment according to the design.
                showConnectionUnsuccessful();
            }
        });
        handler.postDelayed(timeoutRunnable, WIFI_SET_PROPERTIES_TIME_OUT);
    }

    private void onDeviceConnectedToWifi() {
        navigator.navigateToEWSWiFiPairedScreen();
    }

    private void tagConnectionStart() {
        EWSTagger.startTimedAction(Tag.ACTION.TIME_TO_CONNECT);
        EWSTagger.trackAction(Tag.ACTION.CONNECTION_START, PRODUCT_NAME, EWSDependencyProvider.getInstance().getProductName());
    }

    private void showConnectionUnsuccessful() {
        removeTimeoutRunnable();
        navigator.navigateToConnectionUnsuccessfulTroubleShootingScreen();
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

}
