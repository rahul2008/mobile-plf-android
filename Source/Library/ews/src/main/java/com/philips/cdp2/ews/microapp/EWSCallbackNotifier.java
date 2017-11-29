/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.support.annotation.NonNull;

public class EWSCallbackNotifier {

    private static EWSCallbackNotifier instance;
    private EWSCallback callback;

    private EWSCallbackNotifier() {
    }

    @Override
    @SuppressWarnings("all")
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public static synchronized EWSCallbackNotifier getInstance() {
        if (instance == null) {
            instance = new EWSCallbackNotifier();
        }
        return instance;
    }

    public void setCallback(@NonNull EWSCallback callback) {
        this.callback = callback;
    }

    public void onSuccess() {
        if (callback != null) {
            callback.onSuccess();
        }
        instance = null;
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
        instance = null;
    }

    public void onBackPressed() {
        if (callback != null) {
            callback.onBackPressed();
        }
        instance = null;
    }
}
