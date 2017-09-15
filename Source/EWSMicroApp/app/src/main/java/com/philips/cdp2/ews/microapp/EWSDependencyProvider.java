/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.Map;

public class EWSDependencyProvider {

    private static EWSDependencyProvider instance;

    private static LoggingInterface loggingInterface;
    private static AppTaggingInterface appTaggingInterface;
    private AppInfraInterface appInfraInterface;
    private DiscoveryManager<? extends Appliance> discoveryManager;
    private Map<String, String> productKeyMap;

    private EWSDependencyProvider() {
    }

    public static EWSDependencyProvider getInstance() {
        if (instance == null) {
            synchronized (EWSDependencyProvider.class) {
                if (instance == null)
                    instance = new EWSDependencyProvider();
            }
        }
        return instance;
    }

    void initDependencies(@NonNull final AppInfraInterface appInfraInterface, final DiscoveryManager<? extends Appliance> discoveryManager,
                          @NonNull final Map<String, String> productKeyMap) {
        this.appInfraInterface = appInfraInterface;
        this.discoveryManager = discoveryManager;
        this.productKeyMap = productKeyMap;

        if(productKeyMap == null || !productKeyMap.containsKey(EWSInterface.PRODUCT_NAME)) {
            throw new IllegalArgumentException("productKeyMap does not contain the productName");
        }
    }

    public AppInfraInterface getAppInfra() {
        return appInfraInterface;
    }

    public LoggingInterface getLoggerInterface() {
        if (loggingInterface == null) {
            loggingInterface = getAppInfra().getLogging().createInstanceForComponent("EasyWifiSetupLogger", "1.0.0");
        }

        return loggingInterface;
    }

    public AppTaggingInterface getTaggingInterface() {
        if (appTaggingInterface == null) {
            appTaggingInterface = getAppInfra().getTagging().createInstanceForComponent("EasyWifiSetupTagger", "1.0.0");
        }
        return appTaggingInterface;
    }

    @NonNull
    public String getProductName() {
        if(productKeyMap == null) {
            throw new IllegalStateException("Product keymap not initialized");
        }
        return productKeyMap.get(EWSInterface.PRODUCT_NAME);
    }

    boolean areDependenciesInitialized() {
        return appInfraInterface != null && discoveryManager != null && productKeyMap != null;
    }

    public DiscoveryManager<? extends Appliance> getDiscoveryManager() {
        return discoveryManager;
    }

    public void clear() {
        loggingInterface = null;
        appTaggingInterface = null;
        discoveryManager = null;
        appInfraInterface = null;
        productKeyMap = null;
        instance = null;
    }
}