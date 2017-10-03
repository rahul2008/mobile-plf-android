/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.support.annotation.NonNull;

public class EWSCallbackNotifier {

    private static EWSCallbackNotifier callbackNotifier;
    private EWSCallback callback;

    private EWSCallbackNotifier() {
    }

    public static EWSCallbackNotifier getInstance() {
        if (callbackNotifier == null) {
            synchronized (EWSCallbackNotifier.class) {
                if (callbackNotifier == null)
                    callbackNotifier = new EWSCallbackNotifier();
            }
        }
        return callbackNotifier;
    }

    public void setCallback(@NonNull EWSCallback callback) {
        this.callback = callback;
    }

    public void onSuccess() {
        if (callback != null) {
            callback.onSuccess();
        }
        callbackNotifier = null;
    }

    public void onApplianceDiscovered(String cppId) {
        if (callback != null) {
            callback.onApplianceDiscovered(cppId);
        }
    }

    public void onCancel() {
        if (callback != null) {
            callback.onCancel();
        }
        callbackNotifier = null;
    }

    public void onBackPressed() {
        if (callback != null) {
            callback.onBackPressed();
        }
        callbackNotifier = null;
    }
}
