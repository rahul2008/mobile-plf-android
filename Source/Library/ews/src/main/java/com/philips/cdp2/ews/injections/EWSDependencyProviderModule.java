/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.injections;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.microapp.EWSUapp;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@SuppressWarnings("WeakerAccess")
@Module
public class EWSDependencyProviderModule {

    private AppInfraInterface appInfraInterface;
    @VisibleForTesting
    Map<String, String> productKeyMap;

    public EWSDependencyProviderModule(@NonNull final AppInfraInterface appInfraInterface,
                                       @NonNull final Map<String, String> productKeyMap){
        this.appInfraInterface = appInfraInterface;
        this.productKeyMap = productKeyMap;
        if (productKeyMap == null || !productKeyMap.containsKey(EWSUapp.PRODUCT_NAME)) {
            throw new IllegalArgumentException("productKeyMap does not contain the productName");
        }
    }

    private LoggingInterface provideLoggerInterface() {
        return appInfraInterface.getLogging().createInstanceForComponent("EasyWifiSetupLogger", "1.0.0");
    }

    /**
     * Return AppTaggingInterface.
     * @return AppTaggingInterface
     */
    private AppTaggingInterface provideTaggingInterface() {
        return appInfraInterface.getTagging().createInstanceForComponent("EasyWifiSetupTagger", "1.0.0");
    }

    @Provides
    @Singleton
    @NonNull
    public EWSTagger provideEWSTagger(){
        return new EWSTagger(provideTaggingInterface());
    }

    @Provides
    @Singleton
    @NonNull
    public EWSLogger provideEWSLogger(){
        return new EWSLogger(provideLoggerInterface());
    }

    /**
     * Return ProductName
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
