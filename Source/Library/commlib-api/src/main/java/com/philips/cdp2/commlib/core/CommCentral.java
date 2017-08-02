/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;
import com.philips.cdp2.commlib.core.context.TransportContext;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;

import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public final class CommCentral {
    private static final String TAG = "CommCentral";

    private static String APP_ID;

    private ApplianceManager applianceManager;
    private final DICommApplianceFactory<?> applianceFactory;
    private final Set<DiscoveryStrategy> discoveryStrategies = new CopyOnWriteArraySet<>();

    public CommCentral(@NonNull DICommApplianceFactory applianceFactory, @NonNull final TransportContext... transportContexts) {
        generateTemporaryAppId();
        // Setup transport contexts
        if (transportContexts.length == 0) {
            throw new IllegalArgumentException("This class needs to be constructed with at least one transport context.");
        }

        // Setup ApplianceFactory
        if (applianceFactory == null) {
            throw new IllegalArgumentException("This class needs to be constructed with a non-null appliance factory.");
        }
        this.applianceFactory = applianceFactory;

        // Setup discovery strategies
        for (TransportContext transportContext : transportContexts) {
            DiscoveryStrategy discoveryStrategy = transportContext.getDiscoveryStrategy();
            if (discoveryStrategy != null) {
                discoveryStrategies.add(discoveryStrategy);
            }
        }

        // Setup ApplianceManager
        this.applianceManager = new ApplianceManager(discoveryStrategies, applianceFactory);
    }

    public void startDiscovery() throws MissingPermissionException, TransportUnavailableException {
        startDiscovery(Collections.<String>emptySet());
    }

    public void startDiscovery(@NonNull Set<String> modelIds) throws MissingPermissionException, TransportUnavailableException {
        DICommLog.d(TAG, "Starting discovery for model ids: " + modelIds.toString());

        for (DiscoveryStrategy strategy : this.discoveryStrategies) {
            strategy.start(applianceFactory.getSupportedDeviceTypes(), modelIds);
        }
    }

    public void stopDiscovery() {
        for (DiscoveryStrategy strategy : this.discoveryStrategies) {
            strategy.stop();
        }
    }

    public ApplianceManager getApplianceManager() {
        return applianceManager;
    }

    public static void setAppId(@NonNull final String appId) {
        APP_ID = appId;
    }

    @NonNull
    public static String getAppId() {
        return APP_ID;
    }

    private void generateTemporaryAppId() {
        APP_ID = String.format("deadbeef%08x", new Random().nextInt());
    }
}
