/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SuppressWarnings("WeakerAccess")
@IntDef({ConnectionErrorType.WRONG_HOME_WIFI, ConnectionErrorType.WRONG_CREDENTIALS})
@Retention(RetentionPolicy.SOURCE)
public @interface ConnectionErrorType {
    int WRONG_HOME_WIFI = 0;
    int WRONG_CREDENTIALS = 1;
}