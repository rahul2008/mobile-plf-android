/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNMapResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceDiagnostics;

import java.util.Map;

public class SHNCapabilityDeviceDiagnosticsWrapper implements SHNCapabilityDeviceDiagnostics {

    @NonNull
    private final SHNCapabilityDeviceDiagnostics shnCapability;

    @NonNull
    private final Handler internalHandler;

    @NonNull
    private final Handler userHandler;

    public SHNCapabilityDeviceDiagnosticsWrapper(@NonNull final SHNCapabilityDeviceDiagnostics shnCapability, @NonNull final Handler internalHandler, @NonNull final Handler userHandler) {
        this.shnCapability = shnCapability;
        this.internalHandler = internalHandler;
        this.userHandler = userHandler;
    }

    @Override
    public void readDeviceDiagnostics(@NonNull final SHNMapResultListener<String, String> listener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                shnCapability.readDeviceDiagnostics(new SHNMapResultListener<String, String>() {
                    @Override
                    public void onActionCompleted(@NonNull final Map<String, String> value, @NonNull final SHNResult result) {
                        postActionComplete(value, result, listener);
                    }
                });
            }
        });
    }

    private void postActionComplete(@NonNull final Map<String, String> value, @NonNull final SHNResult result, @NonNull final SHNMapResultListener<String, String> listener) {
        userHandler.post(new Runnable() {
            @Override
            public void run() {
                listener.onActionCompleted(value, result);
            }
        });
    }
}
