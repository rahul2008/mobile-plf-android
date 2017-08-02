/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.util;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.util.DICommLog;

public class VerboseRunnable implements Runnable {
    @NonNull
    private final Runnable wrappedRunnable;

    public VerboseRunnable(final @NonNull Runnable wrappedRunnable) {
        this.wrappedRunnable = wrappedRunnable;
    }

    @Override
    public void run() {
        try {
            wrappedRunnable.run();
        } catch (Throwable t) {
            if (DICommLog.isLoggingEnabled()) {
                DICommLog.e(wrappedRunnable.getClass().getSimpleName(), t.getMessage());
            } else {
                t.printStackTrace();
            }
            throw t;
        }
    }
}
