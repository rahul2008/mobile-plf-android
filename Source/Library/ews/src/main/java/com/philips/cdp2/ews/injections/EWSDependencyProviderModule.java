/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.injections;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.microapp.EWSInterface;
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
    private Map<String, String> productKeyMap;

    public EWSDependencyProviderModule(@NonNull final AppInfraInterface appInfraInterface,
                                       @NonNull final Map<String, String> productKeyMap){
        this.appInfraInterface = appInfraInterface;
        this.productKeyMap = productKeyMap;

        if (productKeyMap == null || !productKeyMap.containsKey(EWSInterface.PRODUCT_NAME)) {
            throw new IllegalArgumentException("productKeyMap does not contain the productName");
        }
    }

    private LoggingInterface provideLoggerInterface() {
        return AppModule.getAppInfraInterface().getLogging().createInstanceForComponent("EasyWifiSetupLogger", "1.0.0");
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
        return productKeyMap.get(EWSInterface.PRODUCT_NAME);
    }

}
