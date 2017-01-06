/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BackendResponse extends Event {
    @Nullable
    private final Exception throwable;

    //TODO: Spoorti: Can this constructor be removed - Action item on Spoorti
    public BackendResponse(final int referenceId) {
        super(referenceId);
        throwable = null;
    }

    public BackendResponse(final int referenceId, @NonNull final Exception throwable) {
        super(referenceId);
        this.throwable = throwable;
    }

    //TODO: Spoorti: can succed be removed - Action on Spoorti
    public boolean succeed() {
        return throwable == null;
    }

    @Nullable
    public Exception getCallException() {
        return throwable;
    }
}
