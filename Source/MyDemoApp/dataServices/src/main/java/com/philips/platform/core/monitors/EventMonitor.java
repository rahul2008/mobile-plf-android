/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.monitors;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class EventMonitor {
    protected Eventing eventing;

    @CallSuper
    public void start(@NonNull final Eventing eventing) {
        this.eventing = eventing;
        if (!eventing.isRegistered(this)) {
            eventing.register(this);
        }
    }

    @CallSuper
    public void stop() {
        if (eventing != null) {
            eventing.unregister(this);
            eventing = null;
        }
    }
}
