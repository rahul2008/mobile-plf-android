/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.homewificonnection;

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
import com.philips.platform.ews.R;
import com.philips.platform.ews.appliance.ApplianceAccessManager;
import com.philips.platform.ews.appliance.ApplianceSessionDetailsInfo;
import com.philips.platform.ews.communication.DiscoveryHelper;
import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.logger.EWSLogger;
import com.philips.platform.ews.navigation.Navigator;
import com.philips.platform.ews.settingdeviceinfo.DeviceFriendlyNameChanger;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.tagging.Page;
import com.philips.platform.ews.tagging.Tag;
import com.philips.platform.ews.util.StringProvider;
import com.philips.platform.ews.wifi.WiFiConnectivityManager;
import com.philips.platform.ews.wifi.WiFiUtil;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;


public class ConnectingDeviceWithWifiViewModel implements DeviceFriendlyNameChanger.Callback {

    interface ConnectingDeviceToWifiCallback {
        void registerReceiver(@NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter);

        void unregisterReceiver(@NonNull BroadcastReceiver receiver);

        Bundle getBundle();

        void showCancelDialog();
    }

    private static final long WIFI_SET_PROPERTIES_TIME_OUT = TimeUnit.SECONDS.toMillis(60);
    private static final String TAG = ConnectingDeviceWithWifiViewModel.class.getCanonicalName();
    @NonNull
    public  ObservableField<String> title;
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
    @NonNull
    private final StringProvider stringProvider;
    @NonNull
    private final ApplianceSessionDetailsInfo applianceSessionDetailsInfo;
    @NonNull
    private final EWSTagger ewsTagger;
    @NonNull
    private final EWSLogger ewsLogger;
    @NonNull
    private final String productNme;

    @VisibleForTesting
    @Nullable
    StartConnectionModel startConnectionModel;
    @Nullable
    private ConnectingDeviceToWifiCallback fragmentCallback;
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
        public void onPropertiesSet(@NonNull WifiPortProperties wifiPortProperties) {
            if (startConnectionModel != null) {
                applianceSessionDetailsInfo.setCppId(wifiPortProperties.getCppid());
                connectToHomeWifiInternal(startConnectionModel.getHomeWiFiSSID());
            } else {
                ewsLogger.e(TAG, "startConnectionModel cannot be null");
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
            //TODO remove cppID from networkNode and replace it with wifiPortProperties.cppID once the api will be finalized within commlib
            if (appliance.getNetworkNode().getCppId().equalsIgnoreCase(applianceSessionDetailsInfo.getCppId())) {
                String appliancePin = applianceSessionDetailsInfo.getAppliancePin();
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
                        discoveryHelper.startDiscovery(discoveryCallback, ewsLogger);
                    } else if (currentWifiState != WiFiUtil.UNKNOWN_WIFI) {
                        unregisterBroadcastReceiver();
                        handleFailureWrongWifiNetwork();
                    }
                }
            }
        }
    };

    private BaseContentConfiguration baseContentConfiguration;
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
                                             @NonNull ApplianceSessionDetailsInfo applianceSessionDetailsInfo,
                                             @NonNull final EWSTagger ewsTagger, @NonNull final EWSLogger ewsLogger,
                                             @NonNull @Named("ProductName") String productName) {
        this.applianceAccessManager = applianceAccessManager;
        this.navigator = navigator;
        this.wiFiConnectivityManager = wiFiConnectivityManager;
        this.wiFiUtil = wiFiUtil;
        this.handler = handler;
        this.deviceFriendlyNameChanger = deviceFriendlyNameChanger;
        this.discoveryHelper = discoveryHelper;
        this.stringProvider = stringProvider;
        this.applianceSessionDetailsInfo = applianceSessionDetailsInfo;
        this.ewsTagger = ewsTagger;
        this.ewsLogger = ewsLogger;
        this.productNme = productName;
        this.baseContentConfiguration = baseContentConfiguration;
    }

    void setFragmentCallback(@Nullable ConnectingDeviceToWifiCallback fragmentCallback) {
        this.fragmentCallback = fragmentCallback;
    }

    void startConnecting(@NonNull final StartConnectionModel startConnectionModel, boolean fromWrongWifiScreen) {
        this.startConnectionModel = startConnectionModel;
        this.title = new ObservableField<>(getTitle(baseContentConfiguration));
        tagConnectionStart();
        if (!fromWrongWifiScreen) {
            deviceFriendlyNameChanger.setNameChangerCallback(this);
            deviceFriendlyNameChanger.changeFriendlyName(startConnectionModel.getDeviceFriendlyName());
            handler.postDelayed(timeoutRunnable, WIFI_SET_PROPERTIES_TIME_OUT);
        } else {
            connectToHomeWifi(startConnectionModel.getHomeWiFiSSID());
        }
    }

    void connectToHomeWifi(@NonNull String homeWiFiSSID) {
        connectToHomeWifiInternal(homeWiFiSSID);
        handler.postDelayed(timeoutRunnable, WIFI_SET_PROPERTIES_TIME_OUT);
    }

    void clear() {
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
        ewsTagger.startTimedAction(Tag.ACTION.TIME_TO_CONNECT);
        ewsTagger.trackAction(Tag.ACTION.CONNECTION_START, Tag.KEY.PRODUCT_NAME,
                productNme);
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
            ewsLogger.e(TAG, "startConnectionModel cannot be null");
        }
    }

    @Override
    public void onFriendlyNameChangingFailed() {
        showConnectionUnsuccessful();
    }

    @NonNull
    String getTitle(@NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(R.string.label_ews_connecting_device_title,
                baseConfig.getDeviceName(), startConnectionModel.getHomeWiFiSSID());
    }

    public void trackPageName() {
        ewsTagger.trackPage(Page.CONNECTING_DEVICE_WITH_WIFI);
    }

    @Nullable
    public String getHomeWiFiSSID() {
        return wiFiUtil.getHomeWiFiSSD();
    }

    @NonNull
    public EWSLogger getEwsLogger() {
        return ewsLogger;
    }

}
