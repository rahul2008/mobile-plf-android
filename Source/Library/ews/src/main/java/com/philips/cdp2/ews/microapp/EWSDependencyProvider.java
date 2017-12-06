/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.Map;

/**
 * EWSDependencyProvider is been used for creating EWSComponent for Activity and Fragment launch.
 * Provide AppInfraInterface, Logging, Tagging and ProductMap as well.
 */
public class EWSDependencyProvider {

    private static LoggingInterface loggingInterface;
    private static AppTaggingInterface appTaggingInterface;
    private AppInfraInterface appInfraInterface;
    private Map<String, String> productKeyMap;

    @VisibleForTesting
    static EWSDependencyProvider instance;

    @VisibleForTesting
    EWSComponent ewsComponent;

    @VisibleForTesting
    @Nullable
    Context context;

    @VisibleForTesting
    EWSDependencyProvider() {
    }

    /**
     * create a static instance of EWSDependencyProvider
     * @return EWSDependencyProvider
     */
    public static EWSDependencyProvider getInstance() {
        if (instance == null) {
            synchronized (EWSDependencyProvider.class) {
                if (instance == null)
                    instance = new EWSDependencyProvider();
            }
        }
        return instance;
    }

    /**
     * Set context for EWSDependencyProvider
     * @param context
     */
    public void setContext(@Nullable Context context) {
        this.context = context;
    }

    /**
     * Return AppInfraInterface.
     * @return  AppInfraInterface
     */
    public AppInfraInterface getAppInfra() {
        return appInfraInterface;
    }

    /**
     * Return LoggingInterface.
     * @return LoggingInterface
     */
    public LoggingInterface getLoggerInterface() {
        if (loggingInterface == null) {
            loggingInterface = getAppInfra().getLogging().createInstanceForComponent("EasyWifiSetupLogger", "1.0.0");
        }

        return loggingInterface;
    }

    /**
     * Return AppTaggingInterface.
     * @return AppTaggingInterface
     */
    public AppTaggingInterface getTaggingInterface() {
        if (appTaggingInterface == null) {
            appTaggingInterface = getAppInfra().getTagging().createInstanceForComponent("EasyWifiSetupTagger", "1.0.0");
        }
        return appTaggingInterface;
    }


    /**
     * Return EWSComponent
     * @return EWSComponent
     */
    public EWSComponent getEwsComponent() {
        return ewsComponent;
    }

    /**
     * Return ProductName
     * @return ProductName
     */
    @NonNull
    public String getProductName() {
        if (productKeyMap == null) {
            throw new IllegalStateException("Product keymap not initialized");
        }
        return productKeyMap.get(EWSInterface.PRODUCT_NAME);
    }

    /**
     * Check and return if appInfraInterface and productKeyMap are null or not
     * @return boolean
     */
    public boolean areDependenciesInitialized() {
        return appInfraInterface != null && productKeyMap != null;
    }

    /**
     *Clear all the object of EWSDependencyProvider.
     */
    public void clear() {
        loggingInterface = null;
        appTaggingInterface = null;
        appInfraInterface = null;
        productKeyMap = null;
        instance = null;
        ewsComponent = null;
    }

}