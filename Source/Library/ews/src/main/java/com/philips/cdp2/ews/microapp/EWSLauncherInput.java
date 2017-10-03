package com.philips.cdp2.ews.microapp;

import android.support.annotation.NonNull;

import com.philips.platform.uappframework.uappinput.UappLaunchInput;

/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

public class EWSLauncherInput extends UappLaunchInput {

    private EWSCallback callback;

    public EWSLauncherInput(@NonNull EWSCallback callback) {
        this.callback = callback;
    }

    public EWSCallback getCallback() {
        return callback;
    }
}
