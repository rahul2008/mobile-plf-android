/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.communication.events;

import android.support.annotation.Keep;

import com.philips.cdp2.ews.annotations.ConnectionErrorType;

@Keep
public class ApplianceConnectErrorEvent {

    @ConnectionErrorType
    private int errorType;

    public ApplianceConnectErrorEvent(@ConnectionErrorType int errorType) {
        this.errorType = errorType;
    }

    public int getErrorType() {
        return errorType;
    }
}
