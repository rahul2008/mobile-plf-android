/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.ews.injections;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.ews.logger.EWSLogger;
import com.philips.platform.ews.microapp.EWSUapp;
import com.philips.platform.ews.tagging.EWSTagger;

import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@SuppressWarnings("WeakerAccess")
@Module
public class EWSDependencyProviderModule {

    @NonNull private final EWSLogger ewsLogger;
    @NonNull private final  EWSTagger ewsTagger;
    @VisibleForTesting
    Map<String, String> productKeyMap;

    public EWSDependencyProviderModule(@NonNull final AppInfraInterface appInfraInterface,
                                       @NonNull final Map<String, String> productKeyMap) {
        this.productKeyMap = productKeyMap;
        this.ewsTagger = new EWSTagger(appInfraInterface.getTagging().createInstanceForComponent("EasyWifiSetupTagger", "1.0.0"));
        this.ewsLogger = new EWSLogger(appInfraInterface.getLogging().createInstanceForComponent("EasyWifiSetupLogger", "1.0.0"));
        if (!productKeyMap.containsKey(EWSUapp.PRODUCT_NAME)) {
            throw new IllegalArgumentException("productKeyMap does not contain the productName");
        }
    }


    @Provides
    @Singleton
    @NonNull
    public EWSTagger provideEWSTagger() {
        return ewsTagger;
    }

    @Provides
    @Singleton
    @NonNull
    public EWSLogger provideEWSLogger() {
        return ewsLogger;
    }

    /**
     * Return ProductName
     *
     * @return ProductName
     */
    @Provides
    @Singleton
    @Named("ProductName")
    @NonNull
    public String provideProductName() {
        if (productKeyMap == null) {
            throw new IllegalStateException("Product keymap not initialized");
        }
        return productKeyMap.get(EWSUapp.PRODUCT_NAME);
    }

}
