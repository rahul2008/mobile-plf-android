/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.appliance;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

@SuppressWarnings({"WeakerAccess"})
@Singleton
public class ApplianceSessionDetailsInfo {

    private String appliancePin;

    private String cppId;

    @Inject
    public ApplianceSessionDetailsInfo() {
    }

    public String getCppId() {
        return cppId;
    }

    public void setCppId(@Nullable  String cppId) {
        this.cppId = cppId;
    }

    public String getAppliancePin() {
        return appliancePin;
    }

    public void setAppliancePin(@Nullable String appliancePin) {
        this.appliancePin = appliancePin;
    }

    public void clear() {
        this.appliancePin = null;
        this.cppId = null;
    }
}
