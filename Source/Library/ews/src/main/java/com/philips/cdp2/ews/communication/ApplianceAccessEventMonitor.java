/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.communication;

import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.ews.appliance.ApplianceAccessManager;
import com.philips.cdp2.ews.appliance.ApplianceSessionDetailsInfo;
import com.philips.cdp2.ews.communication.events.ConnectApplianceToHomeWiFiEvent;
import com.philips.cdp2.ews.communication.events.DiscoverApplianceEvent;
import com.philips.cdp2.ews.communication.events.FetchDevicePortPropertiesEvent;
import com.philips.cdp2.ews.communication.events.PairingSuccessEvent;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Tag;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import static com.philips.cdp2.ews.EWSActivity.EWS_STEPS;

@SuppressWarnings("WeakerAccess")
public class ApplianceAccessEventMonitor extends EventMonitor implements DiscoveryHelper.DiscoveryCallback {

    @NonNull
    private final ApplianceAccessManager applianceAccessManager;
    @NonNull
    private final ApplianceSessionDetailsInfo sessionDetailsInfo;
    @NonNull
    private final DiscoveryHelper discoveryHelper;

    @Inject
    public ApplianceAccessEventMonitor(@NonNull final ApplianceAccessManager applianceAccessManager,
                                       @NonNull final @Named("ews.event.bus") EventBus eventBus,
                                       @NonNull final ApplianceSessionDetailsInfo sessionDetailsInfo,
                                       @NonNull final DiscoveryHelper discoveryHelper) {
        super(eventBus);
        this.applianceAccessManager = applianceAccessManager;
        this.sessionDetailsInfo = sessionDetailsInfo;
        this.discoveryHelper = discoveryHelper;
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void fetchDevicePortProperties(@SuppressWarnings("UnusedParameters") @NonNull final FetchDevicePortPropertiesEvent event) {
        applianceAccessManager.fetchDevicePortProperties(null);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void connectApplianceToHomeWiFiEvent(ConnectApplianceToHomeWiFiEvent event) {
        applianceAccessManager.connectApplianceToHomeWiFiEvent(event.getHomeWiFiSSID()
                , event.getHomeWiFiPassword()
                , null);
    }

    @SuppressWarnings("UnusedParameters")
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void discoverAppliance(DiscoverApplianceEvent event) {
        EWSLogger.d(EWS_STEPS, "Step 6 : Starting discovery of appliance");
        discoveryHelper.startDiscovery(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        discoveryHelper.stopDiscovery();
    }

    @Override
    public void onApplianceFound(Appliance appliance) {
        EWSLogger.d(EWS_STEPS, "Step 7 : Appliance discovered");
        EWSLogger.d("ApplianceAccessEventMonitor", "EWS Appliance NetworkNode Details: " + appliance.getNetworkNode().toString());
        tagProductConnection(sessionDetailsInfo.getCppId(), appliance.getNetworkNode().getDeviceType());
        discoveryHelper.stopDiscovery();
        eventBus.post(new PairingSuccessEvent());
    }

    private void tagProductConnection(final String cppID, final String deviceType) {
        HashMap<String, String> tagMap = new HashMap<>();
        tagMap.put(Tag.KEY.MACHINE_ID, cppID);
        tagMap.put(Tag.KEY.PRODUCT_MODEL, deviceType);
        tagMap.put(Tag.KEY.PRODUCT_NAME, EWSDependencyProvider.getInstance().getProductName());
        EWSTagger.stopTimedAction(Tag.ACTION.TIME_TO_CONNECT);
        EWSTagger.trackAction(Tag.ACTION.CONNECTION_SUCCESS, tagMap);
    }
}