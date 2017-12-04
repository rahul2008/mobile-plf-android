/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.communication.appliance;

import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.ews.appliance.ApplianceAccessManager;
import com.philips.cdp2.ews.appliance.ApplianceSessionDetailsInfo;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Tag;

import java.util.HashMap;

import javax.inject.Inject;

import static com.philips.cdp2.ews.EWSActivity.EWS_STEPS;

@SuppressWarnings("WeakerAccess")
public class ApplianceAccessEventMonitor implements DiscoveryHelper.DiscoveryCallback {

    @NonNull
    private final ApplianceAccessManager applianceAccessManager;
    @NonNull
    private final ApplianceSessionDetailsInfo sessionDetailsInfo;
    @NonNull
    private final DiscoveryHelper discoveryHelper;

    @Inject
    public ApplianceAccessEventMonitor(@NonNull final ApplianceAccessManager applianceAccessManager,
                                       @NonNull final ApplianceSessionDetailsInfo sessionDetailsInfo,
                                       @NonNull final DiscoveryHelper discoveryHelper) {
        this.applianceAccessManager = applianceAccessManager;
        this.sessionDetailsInfo = sessionDetailsInfo;
        this.discoveryHelper = discoveryHelper;
    }

    public void fetchDevicePortProperties() {
        applianceAccessManager.fetchDevicePortProperties(null);
    }

    public void connectApplianceToHomeWiFiEvent(ConnectApplianceToHomeWiFiEvent event) {
        applianceAccessManager.connectApplianceToHomeWiFiEvent(event.getHomeWiFiSSID()
                , event.getHomeWiFiPassword()
                , null);
    }

    public void discoverAppliance() {
        EWSLogger.d(EWS_STEPS, "Step 6 : Starting discovery of appliance");
        discoveryHelper.startDiscovery(this);
    }

    @Override
    public void onApplianceFound(Appliance appliance) {
        EWSLogger.d(EWS_STEPS, "Step 7 : Appliance discovered");
        EWSLogger.d(EWS_STEPS, "EWS Appliance NetworkNode Details: " + appliance.getNetworkNode().toString());
        tagProductConnection(sessionDetailsInfo.getCppId(), appliance.getNetworkNode().getDeviceType());
        discoveryHelper.stopDiscovery();
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