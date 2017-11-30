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

import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class EWSDependencies extends UappDependencies {

    @NonNull private final Map<String, String> productKeyMap;
    @Nullable private final ContentConfiguration contentConfiguration;

    public EWSDependencies(@NonNull final AppInfraInterface appInfra,
                           @NonNull final Map<String, String> productKeyMap,
                           @Nullable final ContentConfiguration contentConfiguration) {
        super(appInfra);
        this.productKeyMap = productKeyMap;
        this.contentConfiguration = contentConfiguration;
    }

    @NonNull
    Map<String, String> getProductKeyMap() {
        return productKeyMap;
    }

    @Nullable
    ContentConfiguration getContentConfiguration() {
        return contentConfiguration;
    }
}
