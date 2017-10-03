/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.support.annotation.NonNull;

public interface EWSCallback {
    void onSuccess();

    void onCancel();

    void onBackPressed();

    void onApplianceDiscovered(@NonNull String cppId);
}
