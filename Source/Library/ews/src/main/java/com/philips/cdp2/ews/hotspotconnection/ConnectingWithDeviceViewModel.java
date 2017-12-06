package com.philips.cdp2.ews.hotspotconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.settingdeviceinfo.DeviceFriendlyNameFetcher;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.wifi.WiFiConnectivityManager;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

public class ConnectingWithDeviceViewModel implements DeviceFriendlyNameFetcher.Callback {

    interface ConnectingPhoneToHotSpotCallback {
        void registerReceiver(@NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter);

        void unregisterReceiver(@NonNull BroadcastReceiver receiver);

        void showTroubleshootHomeWifiDialog(@NonNull  BaseContentConfiguration baseContentConfiguration);

        Fragment getFragment();
    }

    private static final String TAG = "ConnectWithDeviceVM";
    private static final long DEVICE_CONNECTION_TIMEOUT = TimeUnit.SECONDS.toMillis(30);

    @NonNull
    private final WiFiConnectivityManager wiFiConnectivityManager;

    @NonNull
    private final DeviceFriendlyNameFetcher deviceFriendlyNameFetcher;
    @NonNull
    private final WiFiUtil wiFiUtil;
    @NonNull
    private final Navigator navigator;
    @VisibleForTesting
    @NonNull
    Handler handler;

    @NonNull
    private BaseContentConfiguration baseContentConfiguration;

    @Nullable
    private ConnectingPhoneToHotSpotCallback fragmentCallback;

    @NonNull
    public EWSTagger getEwsTagger() {
        return ewsTagger;
    }

    @NonNull private final EWSTagger ewsTagger;

    @NonNull
    public EWSLogger getEwsLogger() {
        return ewsLogger;
    }

    @NonNull private final EWSLogger ewsLogger;

    @NonNull
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (netInfo.getState() == NetworkInfo.State.CONNECTED) {
                int currentWifiState = wiFiUtil.getCurrentWifiState();
                if (currentWifiState == WiFiUtil.DEVICE_HOTSPOT_WIFI) {
                    onPhoneConnectedToHotspotWifi();
                    unregisterBroadcastReceiver();
                }
            }
        }
    };

    @VisibleForTesting
    @NonNull
    final Runnable timeOutAction = new Runnable() {
        @Override
        public void run() {
            onConnectionAttemptTimedOut();
        }
    };

    @Inject
    ConnectingWithDeviceViewModel(@NonNull WiFiConnectivityManager wiFiConnectivityManager,
                                  @NonNull DeviceFriendlyNameFetcher deviceFriendlyNameFetcher,
                                  @NonNull WiFiUtil wiFiUtil,
                                  @NonNull Navigator navigator,
                                  @NonNull @Named("mainLooperHandler") Handler handler,
                                  @NonNull BaseContentConfiguration baseContentConfiguration,
                                  @NonNull final EWSTagger ewsTagger,
                                  @NonNull final EWSLogger ewsLogger) {
        this.wiFiConnectivityManager = wiFiConnectivityManager;
        this.deviceFriendlyNameFetcher = deviceFriendlyNameFetcher;
        this.wiFiUtil = wiFiUtil;
        this.navigator = navigator;
        this.handler = handler;
        this.baseContentConfiguration = baseContentConfiguration;
        this.ewsTagger = ewsTagger;
        this.ewsLogger = ewsLogger;
    }

    void connectToHotSpot() {
        if (wiFiUtil.isHomeWiFiEnabled()) {
            if (fragmentCallback != null) {
                fragmentCallback.registerReceiver(broadcastReceiver, createIntentFilter());
            } else {
                Log.e(TAG, "FragmentCallback not set in ConnectToHotSpot");
            }
            handler.postDelayed(timeOutAction, DEVICE_CONNECTION_TIMEOUT);
            wiFiConnectivityManager.connectToApplianceHotspotNetwork(WiFiUtil.DEVICE_SSID);
        }else{
            showUnsuccessfulDialog();
        }
    }

    public void handleCancelButtonClicked() {
        wiFiConnectivityManager.stopFindNetwork();
        navigator.navigateBack();
    }

    void onHelpNeeded() {
        ewsTagger.trackActionSendData(Tag.KEY.SPECIAL_EVENTS, Tag.ACTION.USER_NEEDS_HELP);
        navigator.navigateToResetConnectionTroubleShootingScreen();
    }

    void onHelpNotNeeded() {
        navigator.navigateToCompletingDeviceSetupScreen();
    }

    void clear() {
        if (fragmentCallback != null) {
            fragmentCallback.unregisterReceiver(broadcastReceiver);
        }
        setFragmentCallback(null);
    }

    @VisibleForTesting
    void onPhoneConnectedToHotspotWifi() {
        deviceFriendlyNameFetcher.setNameFetcherCallback(this);
        deviceFriendlyNameFetcher.fetchFriendlyName();
    }

    @VisibleForTesting
    void showUnsuccessfulDialog() {
        if (fragmentCallback != null) {
            fragmentCallback.showTroubleshootHomeWifiDialog(baseContentConfiguration);
        } else{
            Log.e(TAG,"FragmentCallback not set in showUnsuccessfulDialog" );
        }
    }

    @VisibleForTesting
    void onConnectionAttemptTimedOut() {
        showUnsuccessfulDialog();
    }

    private IntentFilter createIntentFilter() {
        return new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
    }

    @VisibleForTesting
    void unregisterBroadcastReceiver() {
        if (fragmentCallback != null) {
            fragmentCallback.unregisterReceiver(broadcastReceiver);
        }else{
            Log.e(TAG,"FragmentCallback not set in unregisterBroadcastReceiver" );
        }
    }

    @Override
    public void onFriendlyNameFetchingSuccess(@NonNull String friendlyName) {
        navigator.navigateToConnectToDeviceWithPasswordScreen(friendlyName);
    }

    @Override
    public void onFriendlyNameFetchingFailed() {
        showUnsuccessfulDialog();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    @Nullable
    ConnectingPhoneToHotSpotCallback getFragmentCallback() {
        return fragmentCallback;
    }

    void setFragmentCallback(@Nullable ConnectingPhoneToHotSpotCallback fragmentCallback) {
        this.fragmentCallback = fragmentCallback;
    }

    void trackPageName() {
        ewsTagger.trackPage(Page.CONNECTING_WITH_DEVICE);
    }
}