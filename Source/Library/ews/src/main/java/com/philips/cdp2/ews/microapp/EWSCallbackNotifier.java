/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.support.annotation.NonNull;

/**
 * EWSCallbackNotifier class provide callback to EWSCallBack
 */
public class EWSCallbackNotifier {

    private static EWSCallbackNotifier callbackNotifier;
    private EWSCallback callback;

    private EWSCallbackNotifier() {
    }

    /**
     * Return static instance of EWSCallbackNotifier
     */
    public static EWSCallbackNotifier getInstance() {
        if (callbackNotifier == null) {
            synchronized (EWSCallbackNotifier.class) {
                if (callbackNotifier == null)
                    callbackNotifier = new EWSCallbackNotifier();
            }
        }
        return callbackNotifier;
    }

    /**
     * Setter for EWSCallback instance
     * @param callback EWSCallback
     */
    public void setCallback(@NonNull EWSCallback callback) {
        this.callback = callback;
    }

    /**
     *CallBack for Success
     */
    public void onSuccess() {
        if (callback != null) {
            callback.onSuccess();
        }
        callbackNotifier = null;
    }

    /**
     * Callback on ApplianceDiscovery
     * @param cppId String cppId
     */
    public void onApplianceDiscovered(String cppId) {
        if (callback != null) {
            callback.onApplianceDiscovered(cppId);
        }
    }

    /**
     * CallBack for Cancel
     */
    public void onCancel() {
        if (callback != null) {
            callback.onCancel();
        }
        callbackNotifier = null;
    }

    /**
     * CallBack for Back press
     */
    public void onBackPressed() {
        if (callback != null) {
            callback.onBackPressed();
        }
        callbackNotifier = null;
    }
}
