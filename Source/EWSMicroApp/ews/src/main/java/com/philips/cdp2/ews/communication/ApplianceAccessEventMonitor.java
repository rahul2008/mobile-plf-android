/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.communication;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.discovery.DiscoveryEventListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
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
import com.philips.cdp2.ews.util.ApplianceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import static com.philips.cdp2.ews.view.EWSActivity.EWS_STEPS;

@SuppressWarnings("WeakerAccess")
public class ApplianceAccessEventMonitor extends EventMonitor implements DiscoveryEventListener {

    private final ApplianceAccessManager applianceAccessManager;
    private final ApplianceSessionDetailsInfo sessionDetailsInfo;
    private final DiscoveryManager<? extends Appliance> discoveryManager;

    @Inject
    public ApplianceAccessEventMonitor(@NonNull final ApplianceAccessManager applianceAccessManager,
                                       @NonNull final @Named("ews.event.bus") EventBus eventBus,
                                       @NonNull final ApplianceSessionDetailsInfo sessionDetailsInfo,
                                       @NonNull final DiscoveryManager<? extends Appliance> discoveryManager) {
        super(eventBus);
        this.applianceAccessManager = applianceAccessManager;
        this.sessionDetailsInfo = sessionDetailsInfo;
        this.discoveryManager = discoveryManager;
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void fetchDevicePortProperties(@SuppressWarnings("UnusedParameters") @NonNull final FetchDevicePortPropertiesEvent event) {
        applianceAccessManager.fetchDevicePortProperties();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void connectApplianceToHomeWiFiEvent(ConnectApplianceToHomeWiFiEvent event) {
        applianceAccessManager.connectApplianceToHomeWiFiEvent(event.getHomeWiFiSSID(), event.getHomeWiFiPassword());
    }

    @SuppressWarnings("UnusedParameters")
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void discoverAppliance(DiscoverApplianceEvent event) {
        EWSLogger.d(EWS_STEPS, "Step 6 : Starting discovery of appliance");
        discoveryManager.addDiscoveryEventListener(this);
        discoveryManager.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        discoveryManager.stop();
    }

    @Override
    public void onDiscoveredAppliancesListChanged() {
        Appliance appliance = discoveryManager.getApplianceByCppId(sessionDetailsInfo.getCppId());
        if (ApplianceUtils.isApplianceOnline(appliance)) {
            EWSLogger.d(EWS_STEPS, "Step 7 : Appliance discovered");
            EWSLogger.d("ApplianceAccessEventMonitor", "EWS Appliance NetworkNode Details: " + appliance.getNetworkNode().toString());
            tagProductConnection(sessionDetailsInfo.getCppId(), appliance.getNetworkNode().getDeviceType());
            discoveryManager.removeDiscoverEventListener(this);
            insertApplianceToDatabase(appliance);
            discoveryManager.updateAddedAppliances();
            discoveryManager.stop();
            eventBus.post(new PairingSuccessEvent());
        }
    }

    private void tagProductConnection(final String cppID, final String deviceType) {
        HashMap<String, String> tagMap = new HashMap<>();
        tagMap.put(Tag.KEY.MACHINE_ID, cppID);
        tagMap.put(Tag.KEY.PRODUCT_MODEL, deviceType);
        tagMap.put(Tag.KEY.PRODUCT_NAME, EWSDependencyProvider.getInstance().getProductName());
        EWSTagger.stopTimedAction(Tag.ACTION.TIME_TO_CONNECT);
        EWSTagger.trackAction(Tag.ACTION.CONNECTION_SUCCESS, tagMap);
    }

    @SuppressWarnings("unchecked")
    private void insertApplianceToDatabase(Appliance appliance) {
        ((DiscoveryManager<Appliance>)discoveryManager).insertApplianceToDatabase(appliance);
    }
}