/*
 * (C) Koninklijke Philips N.V., 2018.
 * All rights reserved.
 */
package com.philips.platform.ews.homewificonnection;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.WiFiNode;
import com.philips.platform.ews.appliance.ApplianceAccessManager;
import com.philips.platform.ews.logger.EWSLogger;
import com.philips.platform.ews.navigation.Navigator;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.tagging.Page;
import com.philips.platform.ews.wifi.WiFiUtil;

import java.util.List;

import javax.inject.Inject;

public class SelectWiFiViewModel implements SelectWiFiAdapter.OnWifiNodeSelectListener{

    @NonNull
    private final EWSTagger ewsTagger;
    @NonNull
    private final EWSLogger ewsLogger;
    @NonNull
    private final WiFiUtil wiFiUtil;
    @NonNull
    private final SelectWiFiAdapter adapter;
    private String selectedSSID;
    @NonNull
    public final ObservableBoolean isRefreshing;
    @NonNull
    public final ObservableBoolean enableNextButton;
    @NonNull
    private final ApplianceAccessManager applianceAccessManager;
    @NonNull
    protected final Navigator navigator;

    @Inject
    SelectWiFiViewModel(@NonNull final Navigator navigator,
                        @NonNull ApplianceAccessManager applianceAccessManager,
                        @NonNull final WiFiUtil wiFiUtil,
                        @NonNull final SelectWiFiAdapter adapter,
                        @NonNull final EWSTagger ewsTagger,
                        @NonNull final EWSLogger ewsLogger) {
        this.navigator = navigator;
        this.applianceAccessManager = applianceAccessManager;
        this.wiFiUtil = wiFiUtil;
        this.adapter = adapter;
        this.ewsTagger = ewsTagger;
        this.ewsLogger = ewsLogger;
        this.isRefreshing = new ObservableBoolean();
        this.enableNextButton = new ObservableBoolean();
        this.adapter.setOnWifiNodeSelectListener(this);
    }

    public void fetchWifiNodes() {
        this.selectedSSID = null;
        isRefreshing.set(true);
        enableNextButton.set(false);
        applianceAccessManager.fetchWiFiNetworks(fetchWiFiNetworksCallBack);
    }

    @Override
    public void onWifiNodeSelected(@NonNull final String selectedSSID) {
        this.selectedSSID = selectedSSID;
        this.enableNextButton.set(true);
    }

    @NonNull
    public SelectWiFiAdapter getAdapter() {
        return adapter;
    }

    public void onContinueButtonClicked() {
        wiFiUtil.setHomeWiFiSSID(selectedSSID);
        navigator.navigateToConnectToDeviceWithPasswordScreen("");
    }

    public void onNetworkNotListedButtonClicked() {
        navigator.navigateToNetworkNotListedTroubleShootingScreen();
    }

    public void cleanUp() {
        adapter.removeOnWifiNodeSelectListener();
    }

    @NonNull
    private final ApplianceAccessManager.FetchWiFiNetworksCallBack fetchWiFiNetworksCallBack = new ApplianceAccessManager.FetchWiFiNetworksCallBack() {
        @Override
        public void onWiFiNetworksReceived(@NonNull List<WiFiNode> wiFiNodeList) {
            adapter.setWifiList(wiFiNodeList);
            isRefreshing.set(false);
        }
    };

    public void trackPageName() {
        ewsTagger.trackPage(Page.SELECT_WIFI_NETWORK);
    }

    @NonNull
    public EWSLogger getEwsLogger() {
        return ewsLogger;
    }
}