/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class EWSDependencies extends UappDependencies {

    private final Map<String, String> productKeyMap;

    public EWSDependencies(@NonNull final AppInfraInterface appInfra,
                           @NonNull final Map<String, String> productKeyMap) {
        super(appInfra);
        this.productKeyMap = productKeyMap;
    }

    @NonNull
    public Map<String, String> getProductKeyMap() {
        return productKeyMap;
    }
}
