/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.appliance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

@SuppressWarnings({"WeakerAccess"})
@Singleton
public class ApplianceSessionDetailsInfo {

    @Nullable
    private String appliancePin;

    @Nullable
    private static String cppId;

    @Inject
    public ApplianceSessionDetailsInfo() {
    }

    @Nullable
    public String getCppId() {
        return cppId;
    }

    public void setCppId(@NonNull String cppId) {
        ApplianceSessionDetailsInfo.cppId = cppId;
    }

    @Nullable
    public String getAppliancePin() {
        return appliancePin;
    }

    public void setAppliancePin(@Nullable String appliancePin) {
        this.appliancePin = appliancePin;
    }

    public void clear() {
        this.appliancePin = null;
        cppId = null;
    }
}
