/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({ApplianceRequestType.UNKNOWN, ApplianceRequestType.GET_WIFI_PROPS, ApplianceRequestType.PUT_WIFI_PROPS,
        ApplianceRequestType.GET_DEVICE_PROPS, ApplianceRequestType.PUT_DEVICE_PROPS})
@Retention(RetentionPolicy.SOURCE)
public @interface ApplianceRequestType {
    int UNKNOWN = 0;
    int GET_WIFI_PROPS = 1;
    int PUT_WIFI_PROPS = 2;
    int GET_DEVICE_PROPS = 3;
    int PUT_DEVICE_PROPS = 4;
}
