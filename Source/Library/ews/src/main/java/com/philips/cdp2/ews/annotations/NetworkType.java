/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({NetworkType.HOME_WIFI, NetworkType.DEVICE_HOTSPOT})
@Retention(RetentionPolicy.SOURCE)
public @interface NetworkType {
    int HOME_WIFI = 1;
    int DEVICE_HOTSPOT = 2;
}
