/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDataModelDebuggingMoonshine;

public class SHNCapabilityDataModelDebuggingMoonshineWrapper implements SHNCapabilityDataModelDebuggingMoonshine {

    private final SHNCapabilityDataModelDebuggingMoonshine capability;
    private final Handler internalHandler;
    private final Handler userHandler;
    private ResultListener<Boolean> listenerInformer;

    public SHNCapabilityDataModelDebuggingMoonshineWrapper(SHNCapabilityDataModelDebuggingMoonshine capability, Handler internalHandler, Handler userHandler) {
        this.capability = capability;
        this.internalHandler = internalHandler;
        this.userHandler = userHandler;
    }

    @Override
    public void setEnabled(final boolean enabled, @NonNull final ResultListener<Boolean> listener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                capability.setEnabled(enabled, new WrappedResultListener<>(userHandler, listener));
            }
        });
    }

    @Override
    public void writeInput(@NonNull final String message, @NonNull final ResultListener<String> listener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                capability.writeInput(message, new WrappedResultListener<>(userHandler, listener));
            }
        });
    }
}

