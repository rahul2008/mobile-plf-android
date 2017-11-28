/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp2.ews.configuration.ContentConfiguration;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import java.util.Map;

/**
 *This Class create EWSDependencies for initialisation of EWSInterface.
 */
@SuppressWarnings("WeakerAccess")
public class EWSDependencies extends UappDependencies {

    @NonNull private final Map<String, String> productKeyMap;
    @NonNull private ThemeConfiguration themeConfiguration;
    @Nullable private final ContentConfiguration contentConfiguration;

    /**
     * This will create EWSDependency Object
     * @param appInfra
     * @param productKeyMap
     * @param contentConfiguration
     */
    public EWSDependencies(@NonNull final AppInfraInterface appInfra,
                           @NonNull final Map<String, String> productKeyMap,
                           @Nullable final ContentConfiguration contentConfiguration) {
        super(appInfra);
        this.productKeyMap = productKeyMap;
        this.contentConfiguration = contentConfiguration;
    }

    /**
     * This will Provide ProductKeyMap.
     * @return Product Map
     */
    @NonNull
    public Map<String, String> getProductKeyMap() {
        return productKeyMap;
    }

    /**
     * This will provide ContentConfiguration.
     * @return ContentConfiguration
     */
    @Nullable
    public ContentConfiguration getContentConfiguration() {
        return contentConfiguration;
    }
}
