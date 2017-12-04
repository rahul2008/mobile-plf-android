/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.homewificonnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.ObservableField;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.port.common.WifiPortProperties;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;
import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.appliance.ApplianceAccessManager;
import com.philips.cdp2.ews.appliance.ApplianceSessionDetailsInfo;
import com.philips.cdp2.ews.communication.appliance.DiscoveryHelper;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.settingdeviceinfo.DeviceFriendlyNameChanger;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.util.StringProvider;
import com.philips.cdp2.ews.wifi.WiFiConnectivityManager;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;


public class ConnectingDeviceWithWifiViewModel implements DeviceFriendlyNameChanger.Callback {

    public interface ConnectingDeviceToWifiCallback {
        void registerReceiver(@NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter);

        void unregisterReceiver(@NonNull BroadcastReceiver receiver);

        Bundle getBundle();

        void showCancelDialog();
    }

    private static final long WIFI_SET_PROPERTIES_TIME_OUT = TimeUnit.SECONDS.toMillis(60);
    private static final String TAG = ConnectingDeviceWithWifiViewModel.class.getCanonicalName();
    @NonNull
    private final ApplianceAccessManager applianceAccessManager;
    @NonNull
    private final Navigator navigator;
    @NonNull
    private final WiFiConnectivityManager wiFiConnectivityManager;
    @NonNull
    private final WiFiUtil wiFiUtil;
    @NonNull
    private final Handler handler;
    @NonNull
    private final DiscoveryHelper discoveryHelper;
    @NonNull
    private final DeviceFriendlyNameChanger deviceFriendlyNameChanger;
    @Nullable
    private ConnectingDeviceToWifiCallback fragmentCallback;
    @VisibleForTesting
    @Nullable
    StartConnectionModel startConnectionModel;

    @NonNull
    public final ObservableField<String> title;

    @NonNull
    private final StringProvider stringProvider;

    @NonNull
    private final ApplianceSessionDetailsInfo applianceSessionDetailsInfo;

    @NonNull
    private String cppId;
    
    @NonNull
    private final Runnable timeoutRunnable = new Runnable() {
        @Override
        public void run() {
            if (wiFiUtil.getCurrentWifiState() == WiFiUtil.HOME_WIFI) {
                showConnectionUnsuccessful();
            } else {
                handleFailureWrongWifiNetwork();
            }
            clear();
        }
    };

    @NonNull
    private final ApplianceAccessManager.SetPropertiesCallback sendingNetworkInfoCallback = new ApplianceAccessManager.SetPropertiesCallback() {

        @Override
        public void onPropertiesSet(WifiPortProperties wifiPortProperties) {
            if (startConnectionModel != null) {
                cppId = wifiPortProperties.getCppid();
                connectToHomeWifiInternal(startConnectionModel.getHomeWiFiSSID());
            } else {
                EWSLogger.e(TAG, "startConnectionModel cannot be null");
            }
        }

        @Override
        public void onFailedToSetProperties() {
            showConnectionUnsuccessful();
        }
    };

    @NonNull
    private DiscoveryHelper.DiscoveryCallback discoveryCallback = new DiscoveryHelper.DiscoveryCallback() {
                @Override
                public void onApplianceFound(Appliance appliance) {
                    String appliancePin = applianceSessionDetailsInfo.getAppliancePin();
                    if (appliance.getNetworkNode().getCppId().equalsIgnoreCase(cppId)) {
                        removeTimeoutRunnable();
                        discoveryHelper.stopDiscovery();
                        LanTransportContext.acceptPinFor(appliance, appliancePin);
                        applianceSessionDetailsInfo.clear();
                        onDeviceConnectedToWifi();
                    }
                }

    };

    @NonNull
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isInitialStickyBroadcast()) {
                final NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (netInfo.getState() == NetworkInfo.State.CONNECTED) {
                    int currentWifiState = wiFiUtil.getCurrentWifiState();
                    if (currentWifiState == WiFiUtil.HOME_WIFI) {
                        unregisterBroadcastReceiver();
                        discoveryHelper.startDiscovery(discoveryCallback);
                    } else if (currentWifiState != WiFiUtil.UNKNOWN_WIFI) {
                        unregisterBroadcastReceiver();
                        handleFailureWrongWifiNetwork();
                    }
                }
            }
        }
    };

    @Inject
    public ConnectingDeviceWithWifiViewModel(@NonNull ApplianceAccessManager applianceAccessManager,
                                             @NonNull Navigator navigator,
                                             @NonNull WiFiConnectivityManager wiFiConnectivityManager,
                                             @NonNull WiFiUtil wiFiUtil,
                                             @NonNull DeviceFriendlyNameChanger deviceFriendlyNameChanger,
                                             @NonNull @Named("mainLooperHandler") Handler handler,
                                             @NonNull DiscoveryHelper discoveryHelper,
                                             @NonNull final BaseContentConfiguration baseContentConfiguration,
                                             @NonNull final StringProvider stringProvider,
                                             @NonNull ApplianceSessionDetailsInfo applianceSessionDetailsInfo) {
        this.applianceAccessManager = applianceAccessManager;
        this.navigator = navigator;
        this.wiFiConnectivityManager = wiFiConnectivityManager;
        this.wiFiUtil = wiFiUtil;
        this.handler = handler;
        this.deviceFriendlyNameChanger = deviceFriendlyNameChanger;
        this.discoveryHelper = discoveryHelper;
        this.stringProvider = stringProvider;
        this.title = new ObservableField<>(getTitle(baseContentConfiguration));
        this.applianceSessionDetailsInfo = applianceSessionDetailsInfo;
    }

    public void setFragmentCallback(@Nullable ConnectingDeviceToWifiCallback fragmentCallback) {
        this.fragmentCallback = fragmentCallback;
    }

    public void startConnecting(@NonNull final StartConnectionModel startConnectionModel) {
        this.startConnectionModel = startConnectionModel;
        tagConnectionStart();
        deviceFriendlyNameChanger.setNameChangerCallback(this);
        deviceFriendlyNameChanger.changeFriendlyName(startConnectionModel.getDeviceFriendlyName());
        handler.postDelayed(timeoutRunnable, WIFI_SET_PROPERTIES_TIME_OUT);
    }

    public void connectToHomeWifi(@NonNull String homeWiFiSSID) {
        connectToHomeWifiInternal(homeWiFiSSID);
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
        EWSTagger.trackAction(Tag.ACTION.CONNECTION_START, Tag.KEY.PRODUCT_NAME,
                EWSDependencyProvider.getInstance().getProductName());
    }

    private void removeTimeoutRunnable() {
        handler.removeCallbacks(timeoutRunnable);
    }

    private IntentFilter createIntentFilter() {
        return new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
    }

    private void unregisterBroadcastReceiver() {
        if (fragmentCallback != null) {
            fragmentCallback.unregisterReceiver(broadcastReceiver);
        }
    }

    private void handleFailureWrongWifiNetwork() {
        removeTimeoutRunnable();
        if (fragmentCallback != null) {
            navigator.navigateToWrongWifiNetworkScreen(fragmentCallback.getBundle());
        }
    }

    private void showConnectionUnsuccessful() {
        removeTimeoutRunnable();
        if (startConnectionModel != null) {
            navigator.navigateToWIFIConnectionUnsuccessfulTroubleShootingScreen(startConnectionModel.getDeviceName(), startConnectionModel.getHomeWiFiSSID());
        }
    }

    private void connectToHomeWifiInternal(@NonNull String homeWiFiSSID) {
        if (fragmentCallback != null) {
            fragmentCallback.registerReceiver(broadcastReceiver, createIntentFilter());
        }
        wiFiConnectivityManager.connectToHomeWiFiNetwork(homeWiFiSSID);
    }

    private void sendNetworkInfoToDevice(@NonNull final StartConnectionModel startConnectionModel) {
        applianceAccessManager.connectApplianceToHomeWiFiEvent(
                startConnectionModel.getHomeWiFiSSID(),
                startConnectionModel.getHomeWiFiPassword(),
                sendingNetworkInfoCallback);
    }

    @Override
    public void onFriendlyNameChangingSuccess() {
        if (startConnectionModel != null) {
            sendNetworkInfoToDevice(startConnectionModel);
        } else {
            EWSLogger.e(TAG, "startConnectionModel cannot be null");
        }
    }

    @Override
    public void onFriendlyNameChangingFailed() {
        showConnectionUnsuccessful();
    }


    @NonNull
    String getTitle(@NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(R.string.label_ews_connecting_device_title,
                baseConfig.getDeviceName(), getHomeWiFiSSID());
    }

    public void trackPageName() {
        EWSTagger.trackPage(Page.CONNECTING_DEVICE_WITH_WIFI);
    }

    @Nullable
    public String getHomeWiFiSSID() {
        return wiFiUtil.getHomeWiFiSSD();
    }

}
