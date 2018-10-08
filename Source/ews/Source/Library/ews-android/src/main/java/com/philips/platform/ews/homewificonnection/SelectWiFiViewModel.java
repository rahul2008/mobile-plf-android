/*
 * (C) Koninklijke Philips N.V., 2018.
 * All rights reserved.
 */
package com.philips.platform.ews.homewificonnection;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.philips.cdp.dicommclient.networknode.WiFiNode;
import com.philips.platform.ews.appliance.ApplianceAccessManager;
import com.philips.platform.ews.appliance.ApplianceSessionDetailsInfo;
import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.logger.EWSLogger;
import com.philips.platform.ews.navigation.Navigator;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.tagging.Page;
import com.philips.platform.ews.wifi.WiFiUtil;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class SelectWiFiViewModel implements SelectWiFiAdapter.OnWifiNodeSelectListener{

    interface SelectWiFiViewCallback {
        /*void registerReceiver(@NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter);

        void unregisterReceiver(@NonNull BroadcastReceiver receiver);*/

        void showTroubleshootSelectWifiDialog(@NonNull BaseContentConfiguration baseContentConfiguration);

        Fragment getFragment();
    }

    @NonNull private final EWSTagger ewsTagger;

    @NonNull private final EWSLogger ewsLogger;

    @NonNull
    private final WiFiUtil wiFiUtil;
    /*
    @NonNull
    private final ApplianceSessionDetailsInfo applianceSessionDetailsInfo;*/
    @NonNull
    private final SelectWiFiAdapter adapter;
    @NonNull
    //private final EventBus eventBus;
    private String selectedSSID;
    @NonNull
    public final ObservableBoolean isRefreshing;
    @NonNull
    public final ObservableBoolean enableNextButton;
    @NonNull
    private final ApplianceAccessManager applianceAccessManager;
    @Nullable
    private SelectWiFiViewCallback fragmentCallback;
    @NonNull
    protected final Navigator navigator;

    @Inject
    SelectWiFiViewModel(@NonNull final Navigator navigator,
                        @NonNull ApplianceAccessManager applianceAccessManager,
                        @NonNull final WiFiUtil wiFiUtil,
                        @NonNull final SelectWiFiAdapter adapter,
                        @NonNull final EWSTagger ewsTagger,
                        @NonNull final EWSLogger ewsLogger) {
        //super(screenFlowController);
        this.navigator = navigator;
        this.applianceAccessManager = applianceAccessManager;
        this.wiFiUtil = wiFiUtil;
        this.adapter = adapter;
        this.ewsTagger = ewsTagger;
        this.ewsLogger = ewsLogger;
        //this.eventBus = eventBus;
        this.isRefreshing = new ObservableBoolean();
        this.enableNextButton = new ObservableBoolean();
        //this.eventBus.register(this);
        this.adapter.setOnWifiNodeSelectListener(this);
    }

    public void fetchWifiNodes() {
        this.selectedSSID = null;
        isRefreshing.set(true);
        enableNextButton.set(false);
        applianceAccessManager.fetchWiFiNetworks(fetchWiFiNetworksCallBack);
        //eventBus.post(new FetchWiFiNetworksRequestEvent());
    }

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void onWiFiNetworksResponseReceived(@NonNull final FetchWiFiNetworksResponseEvent responseEvent) {
        adapter.setWifiList(responseEvent.getWifiNetworksList());
        isRefreshing.set(false);
    }*/

    @Override
    public void onWifiNodeSelected(@NonNull final String selectedSSID) {
        this.selectedSSID = selectedSSID;
        this.enableNextButton.set(true);
    }

    @NonNull
    public SelectWiFiAdapter getAdapter() {
        return adapter;
    }

    /*public void onHavingIssuesButtonClicked() {
        showNextScreen(new ConfirmYourWiFiConnectionFragment());
    }*/

    public void onContinueButtonClicked() {
        wiFiUtil.setHomeWiFiSSID(selectedSSID);
        navigator.navigateToConnectToDeviceWithPasswordScreen("");
        //showNextScreen(new EWSWiFiConnectFragment());
    }

    public void onNetworkNotListedButtonClicked() {
        navigator.navigateToNetworkNotListedTroubleShootingScreen();
        }

    public void cleanUp() {
        //eventBus.unregister(this);
        adapter.removeOnWifiNodeSelectListener();
    }

    void setFragmentCallback(@Nullable SelectWiFiViewModel.SelectWiFiViewCallback fragmentCallback) {
        this.fragmentCallback = fragmentCallback;
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
        ewsTagger.trackPage(Page.FETCHING_WIFI_NETWORKS);
    }

    @NonNull
    public EWSLogger getEwsLogger() {
        return ewsLogger;
    }
}

