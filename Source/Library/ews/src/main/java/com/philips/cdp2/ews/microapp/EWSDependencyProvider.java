/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;
import com.philips.cdp2.ews.appliance.BEApplianceFactory;
import com.philips.cdp2.ews.configuration.ContentConfiguration;
import com.philips.cdp2.ews.injections.DaggerEWSComponent;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.injections.EWSConfigurationModule;
import com.philips.cdp2.ews.injections.EWSModule;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import java.util.Map;

public class EWSDependencyProvider {

    private static LoggingInterface loggingInterface;
    private static AppTaggingInterface appTaggingInterface;
    private AppInfraInterface appInfraInterface;
    private Map<String, String> productKeyMap;

    @VisibleForTesting
    static EWSDependencyProvider instance;

    @VisibleForTesting
    static CommCentral commCentral;

    @VisibleForTesting
    EWSComponent ewsComponent;

    @Nullable
    private ThemeConfiguration themeConfiguration;

    @VisibleForTesting
    @Nullable
    Context context;

    @VisibleForTesting
    EWSDependencyProvider() {
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

    void setContext(@Nullable Context context) {
        this.context = context;
    }

    void initDependencies(@NonNull final AppInfraInterface appInfraInterface,
                                 @NonNull final Map<String, String> productKeyMap) {
        this.appInfraInterface = appInfraInterface;
        this.productKeyMap = productKeyMap;

        if (productKeyMap == null || !productKeyMap.containsKey(EWSInterface.PRODUCT_NAME)) {
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

    void createEWSComponent(@NonNull FragmentLauncher fragmentLauncher, @NonNull ContentConfiguration contentConfiguration) {
        createEWSComponent(fragmentLauncher.getFragmentActivity(), fragmentLauncher.getParentContainerResourceID(), contentConfiguration);
    }

    public void createEWSComponent(@NonNull FragmentActivity fragmentActivity, @IdRes int parentContainerResourceID, @NonNull ContentConfiguration contentConfiguration) {
        ewsComponent = DaggerEWSComponent.builder()
                .eWSModule(new EWSModule(fragmentActivity
                        , fragmentActivity.getSupportFragmentManager()
                        , parentContainerResourceID, getCommCentral()))
                .eWSConfigurationModule(new EWSConfigurationModule(fragmentActivity, contentConfiguration))
                .build();
    }

    public EWSComponent getEwsComponent() {
        return ewsComponent;
    }

    @NonNull
    public String getProductName() {
        if (productKeyMap == null) {
            throw new IllegalStateException("Product keymap not initialized");
        }
        return productKeyMap.get(EWSInterface.PRODUCT_NAME);
    }

    boolean areDependenciesInitialized() {
        return appInfraInterface != null && productKeyMap != null;
    }

    public void clear() {
        loggingInterface = null;
        appTaggingInterface = null;
        appInfraInterface = null;
        productKeyMap = null;
        instance = null;
        ewsComponent = null;
    }

    @VisibleForTesting
    CommCentral getCommCentral() {
        if (commCentral == null) {
            commCentral = createCommCentral();
        }
        return commCentral;
    }

    @VisibleForTesting
    @NonNull
    CommCentral createCommCentral() {
        LanTransportContext lanTransportContext = new LanTransportContext(
                new RuntimeConfiguration(context, appInfraInterface));
        BEApplianceFactory factory = new BEApplianceFactory(lanTransportContext);
        return new CommCentral(factory, lanTransportContext);
    }

    void setThemeConfiguration(@Nullable ThemeConfiguration themeConfiguration) {
        this.themeConfiguration = themeConfiguration;
    }

    public ThemeConfiguration getThemeConfiguration() {
        return themeConfiguration;
    }
}