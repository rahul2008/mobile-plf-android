/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import retrofit.RetrofitError;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BackendResponse extends Event {
    @Nullable
    private final RetrofitError throwable;

    public BackendResponse(final int referenceId, @NonNull final RetrofitError throwable) {
        super(referenceId);
        this.throwable = throwable;
    }

    @Nullable
    public RetrofitError getCallException() {
        return throwable;
    }
}
