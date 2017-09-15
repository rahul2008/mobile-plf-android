/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class EWSDependencies extends UappDependencies {

    @NonNull
    private final DiscoveryManager<? extends Appliance> discoveryManager;
    @NonNull
    private final Map<String, String> productKeyMap;

    public EWSDependencies(@NonNull final AppInfraInterface appInfra, @NonNull final DiscoveryManager<? extends Appliance> discoveryManager,
                           @NonNull final Map<String, String> productKeyMap) {
        super(appInfra);
        this.discoveryManager = discoveryManager;
        this.productKeyMap = productKeyMap;
    }

    @NonNull
    public DiscoveryManager<? extends Appliance> getDiscoveryManager() {
        return this.discoveryManager;
    }

    @NonNull
    public Map<String, String> getProductKeyMap() {
        return productKeyMap;
    }
}
