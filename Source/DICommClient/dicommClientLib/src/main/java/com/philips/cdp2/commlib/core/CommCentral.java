/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public final class CommCentral {
    private ApplianceManager applianceManager;
    private final DICommApplianceFactory<?> applianceFactory;
    private Set<DiscoveryStrategy> discoveryStrategies = new CopyOnWriteArraySet<>();

    public CommCentral(@NonNull Set<DiscoveryStrategy> discoveryStrategies, @NonNull DICommApplianceFactory applianceFactory) {
        // Setup discover strategies
        if (discoveryStrategies == null || discoveryStrategies.isEmpty()) {
            throw new IllegalArgumentException("This class needs to be constructed with at least one discovery strategy.");
        }
        this.discoveryStrategies = discoveryStrategies;

        // Setup ApplianceFactory
        if (applianceFactory == null) {
            throw new IllegalArgumentException("This class needs to be constructed with a non-null appliance factory.");
        }
        this.applianceFactory = applianceFactory;

        // Setup ApplianceManager
        this.applianceManager = new ApplianceManager(discoveryStrategies, applianceFactory);
    }

    public void startDiscovery() throws MissingPermissionException {
        for (DiscoveryStrategy strategy : this.discoveryStrategies) {
            strategy.start(applianceFactory.getSupportedModelNames());
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

    public Set<DiscoveryStrategy> getDiscoveryStrategies() {
        return discoveryStrategies;
    }
}
