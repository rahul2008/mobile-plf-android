/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.troubleshooting.homewifi;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp2.ews.common.callbacks.DialogCallback;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class TroubleshootHomeWiFiViewModel {

    @Nullable private DialogCallback dialogCallback;

    @Inject
    public TroubleshootHomeWiFiViewModel() {
    }

    public void setDialogCallback(@NonNull DialogCallback dialogCallback) {
        this.dialogCallback = dialogCallback;
    }

    public void onCloseButtonClicked() {
        if (dialogCallback != null) {
            dialogCallback.hideDialog();
        }
    }

}