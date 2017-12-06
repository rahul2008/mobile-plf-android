/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.ews.configuration.ContentConfiguration;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

import java.util.Map;

/**
 * EWSDependencies class is for initialisation of EWSInterface.
 * It is keeping productKeyMap and ContentConfiguration.
 */
@SuppressWarnings("WeakerAccess")
public abstract class EWSDependencies extends UappDependencies {

    @NonNull private Map<String, String> productKeyMap;
    @Nullable private final ContentConfiguration contentConfiguration;

    /**
     * This will create EWSDependency Object
     * @param appInfra  AppInfraInterface
     * @param productKeyMap Map<String, String>
     * @param contentConfiguration ContentConfiguration
     */
    public EWSDependencies(@NonNull final AppInfraInterface appInfra,
                           @NonNull final Map<String, String> productKeyMap,
                           @Nullable final ContentConfiguration contentConfiguration) {
        super(appInfra);
        this.productKeyMap = productKeyMap;
        this.contentConfiguration = contentConfiguration;
    }

    /**
     * Return ProductKeyMap.
     * @return Product Map
     */
    @NonNull
    public  Map<String, String> getProductKeyMap() {
        return productKeyMap;
    }

    /**
     * Return ContentConfiguration.
     * @return ContentConfiguration
     */
    @Nullable
    ContentConfiguration getContentConfiguration() {
        return contentConfiguration;
    }

    public abstract CommCentral getCommCentral();
}
