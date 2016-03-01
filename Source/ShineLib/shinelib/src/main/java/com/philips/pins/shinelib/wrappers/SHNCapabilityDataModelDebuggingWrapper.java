/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDataModelDebugging;

public class SHNCapabilityDataModelDebuggingWrapper implements SHNCapabilityDataModelDebugging {

    private final SHNCapabilityDataModelDebugging capability;
    private final Handler internalHandler;
    private final Handler userHandler;

    public SHNCapabilityDataModelDebuggingWrapper(SHNCapabilityDataModelDebugging capability, Handler internalHandler, Handler userHandler) {
        this.capability = capability;
        this.internalHandler = internalHandler;
        this.userHandler = userHandler;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                capability.setEnabled(enabled);
            }
        });
    }

    @Override
    public void writeInput(@NonNull final String message) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                capability.writeInput(message);
            }
        });
    }

    @Override
    public void setOutputListener(@Nullable final ResultListener<String> listener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                WrappedResultListener<String> wrappedResultListener = null;
                if (listener != null) {
                    wrappedResultListener = new WrappedResultListener<>(userHandler, listener);
                }
                capability.setOutputListener(wrappedResultListener);
            }
        });
    }
}

