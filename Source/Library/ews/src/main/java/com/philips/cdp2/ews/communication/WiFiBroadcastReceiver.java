/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp2.ews.annotations.ConnectionErrorType;
import com.philips.cdp2.ews.appliance.ApplianceSessionDetailsInfo;
import com.philips.cdp2.ews.communication.events.ApplianceConnectErrorEvent;
import com.philips.cdp2.ews.communication.events.DiscoverApplianceEvent;
import com.philips.cdp2.ews.communication.events.FetchDevicePortPropertiesEvent;
import com.philips.cdp2.ews.communication.events.ForgetApplianceNetworkEvent;
import com.philips.cdp2.ews.communication.events.FoundHomeNetworkEvent;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Named;

import static com.philips.cdp2.ews.view.EWSActivity.EWS_STEPS;

public class WiFiBroadcastReceiver extends BroadcastReceiver implements EventingChannel.ChannelCallback {

    @NonNull
    private final Context appContext;
    @NonNull
    private final WiFiUtil wiFiUtil;
    @NonNull
    private final EventBus eventBus;
    @NonNull
    private final ApplianceSessionDetailsInfo sessionDetailsInfo;
    private boolean isRegistered;

    @Inject
    public WiFiBroadcastReceiver(@NonNull final Context appContext,
                                 @NonNull final @Named("ews.event.bus") EventBus eventBus,
                                 @NonNull final WiFiUtil wiFiUtil,
                                 @NonNull final ApplianceSessionDetailsInfo sessionDetailsInfo) {
        this.appContext = appContext;
        this.wiFiUtil = wiFiUtil;
        this.eventBus = eventBus;
        this.sessionDetailsInfo = sessionDetailsInfo;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {

            final NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if ((NetworkInfo.State.CONNECTED == netInfo.getState())) {
                EWSLogger.d(EWS_STEPS, "Wifi broadcast event received, current wifi state : " + wiFiUtil.getCurrentWifiState());
                final int currentWifiState = wiFiUtil.getCurrentWifiState();
                if (currentWifiState == WiFiUtil.DEVICE_HOTSPOT_WIFI) {
                    eventBus.post(new FetchDevicePortPropertiesEvent());
                } else if (currentWifiState == WiFiUtil.HOME_WIFI) {
                    if(!sessionDetailsInfo.hasSessionProperties()) {
                        eventBus.post(new ApplianceConnectErrorEvent(ConnectionErrorType.WRONG_HOME_WIFI));
                        return;
                    }
                    eventBus.post(new ForgetApplianceNetworkEvent());
                    eventBus.post(new DiscoverApplianceEvent());
                } else if (currentWifiState == WiFiUtil.WRONG_WIFI) {
                    eventBus.post(new ApplianceConnectErrorEvent(ConnectionErrorType.WRONG_HOME_WIFI));
                } else {
                    eventBus.post(new FoundHomeNetworkEvent());
                }
            }
        }
    }

    @Override
    public void onStart() {
//        // TODO consider changes for removing
//        if (!isRegistered) {
//            appContext.registerReceiver(this, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
//            isRegistered = true;
//        }
    }

    @Override
    public void onStop() {
//        // TODO consider changes for removing
//        if (isRegistered) {
//            appContext.unregisterReceiver(this);
//            isRegistered = false;
//        }
    }

    @VisibleForTesting
    public boolean isRegistered() {
        return isRegistered;
    }
}
